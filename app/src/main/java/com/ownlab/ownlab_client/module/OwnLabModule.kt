package com.ownlab.ownlab_client.module

import com.ownlab.ownlab_client.repository.AuthRepository
import com.ownlab.ownlab_client.service.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class OwnLabModule {
    @Provides
    fun provideAuthRepository(authApi: AuthApi) = AuthRepository(authApi)
}