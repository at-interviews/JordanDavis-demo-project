package com.example.sample.di

import com.example.sample.network.Api
import com.example.sample.network.RetrofitClient
import com.example.sample.repository.RestaurantRepository
import com.example.sample.repository.RestaurantRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRestaurantRepository(): RestaurantRepository {
        return RestaurantRepositoryImpl(provideApi())
    }

    @Singleton
    @Provides
    fun provideApi(): Api {
        return RetrofitClient.api
    }
}