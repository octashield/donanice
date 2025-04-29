package br.com.octashield.donanice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Leitura::class], version = 1)
abstract class DonaNiceDatabase : RoomDatabase() {
    abstract fun leituraDao(): LeituraDao

    companion object {
        @Volatile private var INSTANCE: DonaNiceDatabase? = null

        fun getInstance(context: Context): DonaNiceDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    DonaNiceDatabase::class.java,
                    "dona_nice_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
