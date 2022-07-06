package com.book.example.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Closeable

/**
 * Our data model. This demo database only contains simple user records.
 */
data class User(val id: Int, val name: String)

class SQLiteUserDatabase(context: Context, databaseName: String? = DATABASE_NAME)
        : SQLiteOpenHelper(context, databaseName, null, DATABASE_VERSION), Closeable {

    override fun onCreate(db: SQLiteDatabase) {
        /*
            Called when the database does not exist.
            We need to create all tables.
         */
        db.execSQL("""
            CREATE TABLE ${UserTable.TABLE_NAME} (
                ${UserTable.COLUMN_ID} INTEGER PRIMARY KEY,
                ${UserTable.COLUMN_NAME} TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        /*
            In this small sample, there is only one database version.
            We would have to migrate the database schema manually in this function
            whenever there was a mismatch on the version numbers.
         */
        db.execSQL("DROP TABLE IF EXISTS ${UserTable.TABLE_NAME}")
        onCreate(db)
    }

    /**
     * Inserts a new user in the database.
     * @param user An instance of the User class with the values we want to use.
     * @return True if the insertion was successful.
     */
    fun insertUser(user: User): Boolean {
        val userValues = ContentValues().apply {
            put(UserTable.COLUMN_ID, user.id)
            put(UserTable.COLUMN_NAME, user.name)
        }

        return writableDatabase.let {
            it.insert(UserTable.TABLE_NAME, null, userValues) != -1L
        }
    }

    /**
     * Updates an existing user in the database.
     * @param user An instance of the User class with the values we want to use.
     * @return True if the update was successful.
     */
    fun updateUser(user: User): Boolean {
        val userValues = ContentValues().apply {
            put(UserTable.COLUMN_NAME, user.name)
        }

        return writableDatabase.update(
                UserTable.TABLE_NAME, userValues,
                "${UserTable.COLUMN_ID}==?",
                arrayOf(user.id.toString())
            ) > 0
    }

    /**
     * Deletes a user from the database.
     * @param userId The identifier of the user to remove.
     * @return The number of records that were removed.
     */
    fun removeUser(userId: Int): Int =
        writableDatabase
            .delete(UserTable.TABLE_NAME, "${UserTable.COLUMN_ID}==?",
                arrayOf(userId.toString()))

    /**
     * Queries the database to find out which users have been registered.
     * @return A list of User objects.
     */
    fun listUsers(): List<User> =
        readableDatabase.let { db ->
            db.query(UserTable.TABLE_NAME, arrayOf(UserTable.COLUMN_ID, UserTable.COLUMN_NAME), null, null, null, null, null)
                .use { cursor ->
                    List(cursor.count) { index ->
                        cursor.moveToPosition(index)
                        User(
                            cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_NAME))
                        )
                    }
                }
        }

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "users.db"

        private object UserTable {
            const val TABLE_NAME = "USER"
            const val COLUMN_ID = "ID"
            const val COLUMN_NAME = "NAME"
        }

    }
}