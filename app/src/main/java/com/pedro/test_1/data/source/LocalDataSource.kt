package com.pedro.test_1.data.source

import com.pedro.test_1.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    val isLoggedIn: Flow<Boolean>
    val username: Flow<String?>

    suspend fun saveUser(user: User)
    suspend fun clear()
}
