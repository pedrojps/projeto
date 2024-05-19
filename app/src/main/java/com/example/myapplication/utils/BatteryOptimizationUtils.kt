package com.example.myapplication.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.*
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log

object BatteryOptimizationUtils {

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return pm.isIgnoringBatteryOptimizations(context.packageName)
        }
        return true
    }

    fun requestIgnoreBatteryOptimizations(context: Context) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = context.packageName
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                context.startActivity(intent)
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = context.packageName
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Log.e("BatteryOptimizationUtils", "Intent cannot be resolved")
                }
            }
        }
    }

    fun showBatteryOptimizationDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Otimização de Bateria")
            .setMessage("Para garantir que as notificações do seu hábito sejam entregues pontualmente, por favor, remova nosso aplicativo das otimizações de bateria.")
            .setPositiveButton("Remover") { _, _ ->
                requestIgnoreBatteryOptimizations(context)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

}