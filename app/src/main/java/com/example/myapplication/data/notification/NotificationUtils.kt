package com.example.myapplication.data.notification

import android.content.Context
import androidx.work.*
import com.example.myapplication.common.time.LocalDate
import com.example.myapplication.common.time.LocalDateFormat
import com.example.myapplication.common.time.LocalTime
import com.example.myapplication.data.source.local.database.AppDatabase
import com.example.myapplication.data.source.local.projection.HabitAlertProject
import com.example.myapplication.ui.habitCategori.HabitCategoriViewItem
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.apply
import kotlin.let

object NotificationUtils {

    fun getDayWeek(mItem: String?):ArrayList<Int?> {
        var dayOfWeek: ArrayList<Int?> = ArrayList()
        try {
            val dayOfWeekStringList =
                mItem?.substring(1, (mItem?.length?:1) - 1)
                    ?.replace(" ".toRegex(), "")?.split(",")?.toTypedArray()

            dayOfWeekStringList?.forEach { s: String ->
                dayOfWeek.add(
                    s.toInt()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dayOfWeek
    }

    fun sortItens(items:List<HabitCategoriViewItem> ):List<HabitCategoriViewItem> {
        val now = LocalTime.now()
        val sortedItems = items.sortedWith { item1, item2 ->
            when {
                item1.model is HabitAlertProject && item2.model is HabitAlertProject -> {
                    val isBefore1 = (item1.model as HabitAlertProject).time?.before(now)
                    val isBefore2 = (item2.model as HabitAlertProject).time?.before(now)
                    when {
                        isBefore1 == true && isBefore2 != true -> 1
                        isBefore1 != true && isBefore2 == true -> -1
                        else -> (item1.model as HabitAlertProject).time?.compareTo((item2.model as HabitAlertProject).time) ?: 0
                    }
                }
                item1.model is HabitAlertProject-> -1
                item2.model is HabitAlertProject -> 1
                else -> 0
            }
        }

        return sortedItems
    }

    fun calculateInitialDelay(notificationSchedule: HabitAlertProject): Long {
        val now = Calendar.getInstance()
        val notificationTime = getAlertData(notificationSchedule.time)

        // Se a notificação já passou hoje, avança para o próximo dia de notificação
        if (now.after(notificationTime)) {
            notificationTime.add(Calendar.DATE, 1)
        }

        val repeatDays = getDayWeek(notificationSchedule.week)
        // Adiciona os dias de repetição necessários
        while (!repeatDays.contains(notificationTime.get(Calendar.DAY_OF_WEEK))) {
            notificationTime.add(Calendar.DATE, 1)
        }

        return notificationTime.timeInMillis - now.timeInMillis
    }

    private fun getAlertData(time: LocalTime?): Calendar {
        val localDateTime =  try {
            val dateString = "${LocalDate().toString(LocalDateFormat.SQLITE_DATE)} ${time.toString()}"
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            LocalDateTime.parse(dateString, formatter)
        }catch (e:Exception){
            LocalDateTime.now()
        }

        val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

        // Configura o Calendar com a data e hora
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar
    }

    fun scheduleNotifications(context: Context?) {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            context?.let {
                val notificationDao = AppDatabase.getInstance(it).habitCategoriDao()
                val workManager = WorkManager.getInstance(it)

                // Cancela todas as notificações agendadas anteriormente
                workManager.cancelAllWork()

                val notificationScheduleList = notificationDao.listByHabitAlertIsAtive(true)

                for (schedule in notificationScheduleList) {
                    val initialDelayMillis = calculateInitialDelay(schedule)
                    val initialDelayDuration = Duration.ofMillis(initialDelayMillis)

                    val inputData = Data.Builder()
                        .putInt("notificationId", schedule.alertId.toInt())
                        .putString("title", "Lembrete do Hábito: ${schedule.nome}")
                        .putString("text", "Hora de registrar seu hábito de ${schedule.nome}!")
                        .putLong("scheduleId", schedule.id)
                        .build()

                    val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInitialDelay(initialDelayDuration)
                        .setInputData(inputData)
                        .build()

                    workManager.enqueue(notificationRequest)
                }
            }
        }
    }

}