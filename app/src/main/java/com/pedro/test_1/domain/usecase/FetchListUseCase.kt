package com.pedro.test_1.domain.usecase

import com.pedro.test_1.domain.entities.ListItem
import com.pedro.test_1.data.repo.Repository
import com.pedro.test_1.domain.entities.RepoItems

class FetchListUseCase(private val repository: Repository) {
    suspend operator fun invoke(page: Int, pageSize: Int, query: String? = null): RepoItems {
        return repository.fetchItems(page, pageSize, query)
    }
}
