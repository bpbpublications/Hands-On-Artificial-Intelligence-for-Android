package com.book.example.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.isEmptyString
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomMigrationTest {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ApplicationDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate_1_to_2() {
        val databaseName = "migration-test.db"
        helper.createDatabase(databaseName, 1)
            .apply {
                insert("user", SQLiteDatabase.CONFLICT_FAIL,
                    ContentValues().apply {
                        put("id", 1)
                        put("user_name", "previous")
                        put("level", "NORMAL")
                    }
                )
                close()
            }

        val db = helper.runMigrationsAndValidate(databaseName, 2, true,
            ApplicationDatabase.MIGRATION_1_2)

        db.query("SELECT * FROM user").use { cursor ->
            cursor.moveToFirst()
            assertThat(
                cursor.getInt(cursor.getColumnIndex("id")),
                equalTo(1))
            assertThat(
                cursor.getString(cursor.getColumnIndex("user_name")),
                equalTo("previous"))
            assertThat(
                cursor.getString(cursor.getColumnIndex("level")),
                equalTo("NORMAL"))
            assertThat(
                cursor.getString(cursor.getColumnIndex("pwd_hash")),
                isEmptyString())
        }
    }

}