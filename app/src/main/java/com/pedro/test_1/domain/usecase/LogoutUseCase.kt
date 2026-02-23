package com.pedro.test_1.domain.usecase

import com.pedro.test_1.data.repo.Repository

class LogoutUseCase(private val repository: Repository) {
    suspend operator fun invoke() {
        repository.logout()
    }
}
