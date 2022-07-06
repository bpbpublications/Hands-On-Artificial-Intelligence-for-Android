package com.book.example.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.logging.Level

@Database(
    entities = [ User::class, AuditEntry::class ],
    views = [ FullAuditEntry::class ],
    version = 2)
@TypeConverters(InstantConverters::class, LevelConverters::class)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun auditDao(): AuditDao

    companion object {

        private const val DATABASE_NAME = "app.db"

        @Volatile private var instance: ApplicationDatabase? = null

        /**
         * Retrieves the current database instance for the application.
         * @param context A reference to any {@link Context} object in the application.
         *                It will be used once to obtain the global application context.
         */
        fun getDatabase(context: Context): ApplicationDatabase =
            instance ?: synchronized(this) {
                instance ?:
                    Room.databaseBuilder(
                            context.applicationContext,
                            ApplicationDatabase::class.java,
                            DATABASE_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build()
                    .also { instance = it }
            }

        internal val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE user ADD COLUMN pwd_hash TEXT NOT NULL DEFAULT ''")
            }
        }
    }

}
