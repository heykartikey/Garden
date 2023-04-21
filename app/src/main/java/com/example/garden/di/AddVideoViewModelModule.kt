package com.example.garden.di

import com.example.garden.data.repository.FeedRepository
import com.example.garden.view.add_video.AddVideoViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AddVideoViewModelModule {

    @Provides
    fun providesAddVideoViewModel(repo: FeedRepository) = AddVideoViewModel(repo)
}