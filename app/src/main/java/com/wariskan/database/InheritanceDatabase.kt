package com.kenji.waris.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wariskan.util.MIGRATION_to_1_2

@Database(entities = [Inheritance::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class InheritanceDatabase : RoomDatabase() {
    abstract val dao: InheritanceDao

    companion object {

        @Volatile
        private var INSTANCE: InheritanceDatabase? = null

        fun getInstance(context: Context): InheritanceDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InheritanceDatabase::class.java,
                        "inheritance_database"
                    )
                        .addMigrations(MIGRATION_to_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}