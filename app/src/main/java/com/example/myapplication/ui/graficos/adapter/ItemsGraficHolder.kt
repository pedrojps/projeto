package com.example.myapplication.ui.graficos.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.common.time.LocalDate
import com.example.myapplication.common.time.LocalDateFormat
import com.example.myapplication.databinding.ItemGraficBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class ItemsGraficHolder(val binding: ItemGraficBinding): RecyclerView.ViewHolder(binding.root) {

    var mItem: ItemsGrafic? = null
    private var mDayOfWeek: ArrayList<Int?> = ArrayList()
    var startDate = LocalDate()
    var endDate = LocalDate()
    var total: Double = 0.0

    fun onBind(item: ItemsGrafic,startDate:LocalDate,endDate:LocalDate) {
        this.startDate = startDate
        this.endDate = endDate
        mItem = item
        val i = setData() ?: return
        val d = LineDataSet(i, "")
        val data = LineData(d)

        binding.title.text = item.category.nome
        binding.media.text = itemView.context.getString(R.string.media_diaria_item, cacularByDay(total))
        binding.lineChart.data = data
        binding.lineChart.notifyDataSetChanged() // let the chart know it's data changed

        binding.lineChart.invalidate() // refresh chart

    }

    fun setData(): List<Entry>? {
        val byDay = HashMap<String, Double>()
        if ( mItem?.list == null ) return null
        total = 0.0
        for (enty in ArrayList(mItem?.list)) {
            val day = enty.data.toString(LocalDateFormat.DATE2)
            var cont: Double = try {
                byDay[day]!!
            } catch (e: Exception) {
                0.0
            }

            val foo = cont + enty.valor.toDouble()
            byDay[day] = foo
            total += foo
        }
        val lda: LocalDateTime = convertToLocalDateViaInstant(startDate)
        val ldb: LocalDateTime = convertToLocalDateViaInstant(endDate)
        val dates: MutableList<String> = ArrayList()
        var ld = lda
        while (!ld.isAfter(ldb)) {
            dates.add(
                ld.format(DateTimeFormatter.ofPattern(LocalDateFormat.DATE2))
            )
            ld = ld.plusDays(1)
        }
        val valsComp1: MutableList<Entry> = ArrayList()
        for (date in dates) {
            var cont = 0.0
            cont = try {
                byDay[date]!!
            } catch (e: Exception) {
                0.0
            }
            valsComp1.add(
                Entry(
                    dates.indexOf(date).toFloat(),
                    cont.toFloat()
                )
            )
        }
        return valsComp1
    }

    private fun cacularByDay(valor: Double): Double {
        val data1 = Calendar.getInstance()
        val data2 = Calendar.getInstance()
        try {
            data1.time = startDate
            data2.time = endDate
        } catch (e: java.lang.Exception) {
        }
        var dias = data2[Calendar.DAY_OF_YEAR] -
                data1[Calendar.DAY_OF_YEAR]
        if (dias == 0) dias = 1
        return valor / dias
    }

    fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDateTime {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}