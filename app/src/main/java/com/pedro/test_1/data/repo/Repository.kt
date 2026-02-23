package com.pedro.test_1.data.repo

import com.pedro.test_1.domain.entities.AuthResult
import com.pedro.test_1.domain.entities.ListItem
import com.pedro.test_1.domain.entities.RepoItems
import com.pedro.test_1.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun fetchItems(page: Int, pageSize: Int, query: String? = null): RepoItems
    suspend fun getItemById(id: String): ListItem?
    suspend fun login(username: String, password: String): AuthResult

    // Session-related
    fun isLoggedIn(): Flow<Boolean>
    fun username(): Flow<String?>
    suspend fun saveUser(user: User)
    suspend fun logout()
}
