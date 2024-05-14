package com.example.myapplication.data.notification

import android.content.Context
import androidx.work.*
import com.example.myapplication.common.time.LocalDate
import com.example.myapplication.common.time.LocalDateFormat
import com.example.myapplication.common.time.LocalTimeFormat
import com.example.myapplication.data.source.local.database.AppDatabase
import com.example.myapplication.data.source.local.projection.HabitAlertProject
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.*
import java.util.concurrent.Executors
import com.example.myapplication.common.time.LocalTime as Time

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

    private val FORMATO_HORAS: DateTimeFormatter = DateTimeFormatter
        .ofPattern("HH:mm")
        .withResolverStyle(ResolverStyle.STRICT)

    private fun faltando(agora: LocalTime, desejada: LocalTime): LocalTime {
        return desejada.minusHours(agora.hour.toLong()).minusMinutes(agora.minute.toLong())
    }

    private fun mostrar(horario: LocalTime, objetivo: String) {
        val desejada = LocalTime.parse(objetivo, FORMATO_HORAS)
        val falta = faltando(horario, desejada)
        System.out.println(
            "Entre " + horario.format(FORMATO_HORAS)
                .toString() + " e " + desejada.format(FORMATO_HORAS)
                .toString() + ", a diferença é de " + falta.format(FORMATO_HORAS)
                .toString() + "."
        )
    }

    fun calculateInitialDelay(notificationSchedule: HabitAlertProject): Long {
        val now = Calendar.getInstance()
        val date = LocalDate.parse(LocalDate().toString(LocalDateFormat.SQLITE_DATE),LocalDateFormat.SQLITE_DATE)
        val notificationTime = Calendar.getInstance().apply {
            timeInMillis = (date.time + (notificationSchedule.time?.time?: 0))
         }

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
                        .putString("title", "Lembrete de Hábito de " + schedule.nome)
                        .putString("text", "Hora de registrar seu hábito!")
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