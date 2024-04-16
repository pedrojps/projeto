package com.example.myapplication.utils

import com.activeandroid.Model
import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type
import kotlin.collections.ArrayList

@Suppress("unused")
object Globals {

    @JvmStatic
    @Deprecated("Use INSTANCE instead", ReplaceWith("INSTANCE"))
    fun sharedInstance() : Globals {
        return this
    }

    operator fun set(key: c, value: Serializable?) {
        delete(key)

        val cache = Cache()

        cache.key = key.name
        cache.value = Gson().toJson(value)

        cache.save()
    }

    fun setRaw(name: String, value: String?) {
        deleteRaw(name)

        val cache = Cache()

        cache.key = name
        cache.value = value

        cache.save()
    }

    inline operator fun <reified T: Any> get(key: c): T?{
        return get(key, T::class.java)
    }

    fun <T> get(key: c, type: Class<T>): T? {
        val value = getRaw(key.name)

        return if (value != null) {
            Gson().fromJson(value, type)
        }
        else null
    }

    fun <T> get(key: c, type: Type): T? {
        val value = getRaw(key.name)

        return if (value != null) {
            Gson().fromJson(value, type)
        }
        else null
    }

    fun getRaw(key: c): String? {
        return getRaw(key.name)
    }

    fun getRaw(key: String): String? {
        return try {
            return Select().from(Cache::class.java)
                .where("key = ?", key)
                .executeSingle<Cache>()
                ?.value

        } catch (t: Throwable) {
            null
        }
    }

    fun getBoolean(key: c): Boolean {
        return get<Boolean>(key) == true
    }

    inline fun <reified T: Any>getTypedToken(key: c): T?{
        return get<T>(key, object : TypeToken<T>(){}.type)
    }

    fun clear() {
        val names = getNames(
            c.INSTALLATION_ID
        )

        deleteEverythingExcept(names)
    }

    private fun getNames(vararg keys: c): ArrayList<String> {
        val result = ArrayList<String>()

        for (key in keys)
            result.add(key.name)

        return result
    }

    private fun deleteEverythingExcept(exceptions: ArrayList<String>) {
        val query = Delete().from(Cache::class.java)

        for (key in exceptions)
            query.where("key <> ?", key)

        query.execute<Model>()
    }

    private fun delete(vararg keys: c) {
        val query = Delete().from(Cache::class.java)

        for (key in keys)
            query.where("key = ?", key)

        query.execute<Model>()
    }

    private fun deleteRaw(vararg names: String) {
        val query = Delete().from(Cache::class.java)

        for (name in names)
            query.where("key = ?", name)

        query.execute<Model>()
    }

    enum class c {
        INSTALLATION_ID, SHOW_DIALOG_MAIN, SHOW_DIALOG_ADD_HABIT, SHOW_DIALOG_DETAIL, SHOW_DIALOG_GRAFIC
    }
}