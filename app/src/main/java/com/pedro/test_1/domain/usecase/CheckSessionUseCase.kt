package com.pedro.test_1.domain.usecase

import com.pedro.test_1.data.repo.Repository
import kotlinx.coroutines.flow.first

class CheckSessionUseCase(private val repository: Repository) {
    suspend operator fun invoke(): Boolean {
        return repository.isLoggedIn().first()
    }
}
