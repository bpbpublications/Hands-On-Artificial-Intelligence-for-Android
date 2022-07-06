package com.book.example.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.book.example.room.ApplicationDatabase
import com.book.example.room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UsersViewModel(application: Application)
        : AndroidViewModel(application) {

    private val database = ApplicationDatabase.getDatabase(
        getApplication<Application>().applicationContext)

    fun findUsers(consumer: (List<User>) -> Unit) {
        viewModelScope.launch {
            val users = withContext(Dispatchers.IO) {
                database.userDao().findAll()
            }
            consumer(users)
        }
    }
}