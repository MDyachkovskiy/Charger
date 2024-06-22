package com.test.application.feature_location.di

import com.test.application.feature_location.domain.usecase.FindLocationUseCase
import com.test.application.feature_location.domain.usecase.FindLocationUseCaseImpl
import dagger.Binds
import dagger.Module

@Module(includes = [RepositoryModule::class])
internal abstract class UseCaseModule {

    @LocationScope
    @Binds
    abstract fun bindFindLocationUseCase(
        findLocationUseCaseImpl: FindLocationUseCaseImpl
    ): FindLocationUseCase
}