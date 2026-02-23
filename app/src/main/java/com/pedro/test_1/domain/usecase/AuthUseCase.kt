package com.pedro.test_1.domain.usecase

import com.pedro.test_1.domain.entities.AuthResult
import com.pedro.test_1.data.repo.Repository

class AuthUseCase(private val repository: Repository) {
    suspend operator fun invoke(username: String, password: String): AuthResult {
        return repository.login(username, password)
    }
}
