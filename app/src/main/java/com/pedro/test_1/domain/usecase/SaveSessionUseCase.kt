package com.pedro.test_1.domain.usecase

import com.pedro.test_1.domain.entities.User
import com.pedro.test_1.data.repo.Repository

class SaveSessionUseCase(private val repository: Repository) {
    suspend operator fun invoke(user: User) {
        repository.saveUser(user)
    }
}
