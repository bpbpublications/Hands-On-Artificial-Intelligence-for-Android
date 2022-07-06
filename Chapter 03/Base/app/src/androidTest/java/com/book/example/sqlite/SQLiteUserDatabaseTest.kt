package com.book.example.sqlite

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.contains
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SQLiteUserDatabaseTest {

    @Test
    fun userOperations() {
        /* get an in-memory database for our tests and close it when we're done */
        SQLiteUserDatabase(ApplicationProvider.getApplicationContext(), null)
            .use { database ->

                val inserted = database.insertUser(User(1, "test"))
                assertTrue(inserted)

                val duplicateInserted = database.insertUser(User(1, "test"))
                assertFalse(duplicateInserted)

                val updated = database.updateUser(User(1, "updated"))
                assertTrue(updated)

                val users = database.listUsers()
                assertThat(users, contains(User(1, "updated")))

                val removed = database.removeUser(1)
                assertEquals(1, removed)

            }
    }

}