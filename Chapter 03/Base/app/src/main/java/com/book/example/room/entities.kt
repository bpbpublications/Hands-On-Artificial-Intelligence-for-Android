package com.book.example.room

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "user")
data class User (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(index = true) val level: UserLevel,
    @ColumnInfo(name = "pwd_hash") val passwordHash: String = ""
)

@Entity(tableName = "audit", primaryKeys = [ "userId", "timestamp" ])
data class AuditEntry (
    val userId: Int,
    val timestamp: Instant
)

data class UserName (
    @ColumnInfo(name = "user_name") val name: String,
)

@DatabaseView(
    "SELECT audit.userId, audit.timestamp, user.user_name, user.level" +
    " FROM audit " +
    "INNER JOIN user ON audit.userId = user.id")
data class FullAuditEntry(
    val userId: Int,
    val timestamp: Instant,
    @ColumnInfo(name = "user_name") val userName: String,
    @ColumnInfo(name = "level") val userLevel: UserLevel
)

enum class UserLevel {
    NORMAL,
    SUPERUSER
}