package com.example.pensiaapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pensiadb.db"
        private const val DATABASE_VERSION = 1
        private const val DATABASE_PATH = "/databases/"
    }

    private val dbPath: String = context.applicationInfo.dataDir + DATABASE_PATH + DATABASE_NAME

    init {
        copyDatabase(context)
    }

    override fun onCreate(db: SQLiteDatabase?) {
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    // Fungsi untuk membuka database
    fun openDatabase(): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
    }

    // Fungsi untuk menyalin database dari assets ke direktori database aplikasi
    private fun copyDatabase(context: Context) {
        val dbFile = context.getDatabasePath(DATABASE_NAME)

        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()

            try {
                context.assets.open(DATABASE_NAME).use { inputStream ->
                    FileOutputStream(dbFile).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException("Error copying database", e)
            }
        }
    }
}
