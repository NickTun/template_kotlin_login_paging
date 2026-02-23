package com.pedro.test_1.data.source

import com.pedro.test_1.domain.entities.AuthResult
import com.pedro.test_1.domain.entities.ListItem

interface NetworkDataSource {
    suspend fun fetchItems(page: Int, pageSize: Int): List<ListItem>
    suspend fun fetchItemById(id: String): ListItem?
    suspend fun authenticate(username: String, password: String): AuthResult
}
