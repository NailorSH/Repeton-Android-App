package com.nailorsh.repeton.di

import com.nailorsh.repeton.data.repositories.AuthServiceImpl
import com.nailorsh.repeton.data.repositories.FakeRepetonRepository
import com.nailorsh.repeton.domain.repositories.AuthService
import com.nailorsh.repeton.domain.repositories.RepetonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun bindAccountService(
        accountServiceImpl: AuthServiceImpl
    ): AuthService

    @Binds
    abstract fun bindRepetonRepository(
        repetonRepository: FakeRepetonRepository
    ): RepetonRepository
}