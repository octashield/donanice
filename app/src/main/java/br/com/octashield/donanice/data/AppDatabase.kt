package br.com.octashield.donanice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Leitura::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun leituraDao(): LeituraDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //fun getDatabase(context: Context): AppDatabase {
        //    return INSTANCE ?: synchronized(this) {
        //        Room.databaseBuilder(
        //            context.applicationContext,
        //            AppDatabase::class.java,
        //            "donanice.db"
        //        ).build().also {
        //            INSTANCE = it
        //        }
        //    }
        //}
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "donanice_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}