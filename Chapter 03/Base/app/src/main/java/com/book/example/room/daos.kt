package com.book.example.room

import androidx.room.*

@Dao
interface UserDao {

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User): Int

    @Update
    fun update(vararg users: User): Int

    @Query("SELECT * FROM user")
    fun findAll(): List<User>

    @Query("SELECT * FROM user WHERE level = :level")
    fun findAllByLevel(level: UserLevel): List<User>

    @Query("SELECT user_name FROM user")
    fun findAllUserNames(): List<UserName>
}

@Dao
interface AuditDao {

    @Insert
    fun insert(vararg entries: AuditEntry)

    @Query("SELECT * FROM fullauditentry")
    fun findAllWithUserDetails(): List<FullAuditEntry>

}