package com.book.example.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    private lateinit var db: ApplicationDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java)
                .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testUserDao() {
        val dao = db.userDao()

        dao.insert(
            User(1, "regular", UserLevel.NORMAL),
            User(2, "super", UserLevel.SUPERUSER)
        )

        val users = dao.findAll()
        assertThat(users, containsInAnyOrder(
                User(1, "regular", UserLevel.NORMAL),
                User(2, "super", UserLevel.SUPERUSER)
        ))

        val updated = dao.update(User(1, "new name", UserLevel.NORMAL))
        assertThat(updated, equalTo(1))

        val names = dao.findAllUserNames()
        assertThat(names, containsInAnyOrder(
            UserName("new name"),
            UserName("super")
        ))

        val normalUsers = dao.findAllByLevel(UserLevel.NORMAL)
        assertThat(normalUsers, contains(
            User(1, "new name", UserLevel.NORMAL))
        )

        val superUsers = dao.findAllByLevel(UserLevel.SUPERUSER)
        assertThat(superUsers, contains(
            User(2, "super", UserLevel.SUPERUSER))
        )

        val deleted = dao.delete(normalUsers[0])
        assertThat(deleted, equalTo(1))

    }

    @Test
    fun testAuditDao() {
        val users = db.userDao()
        val audit = db.auditDao()

        users.insert(
            User(1, "regular", UserLevel.NORMAL),
            User(2, "super", UserLevel.SUPERUSER)
        )

        val currentTimestamp = Instant.now()
        audit.insert(
            AuditEntry(1, currentTimestamp),
            AuditEntry(2, currentTimestamp)
        )

        val details = audit.findAllWithUserDetails()
        assertThat(details, containsInAnyOrder(
            FullAuditEntry(1, currentTimestamp, "regular", UserLevel.NORMAL),
            FullAuditEntry(2, currentTimestamp, "super", UserLevel.SUPERUSER)
        ))
    }
}