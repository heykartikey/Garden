package com.example.garden.di

import com.example.garden.data.repository.AuthRepository
import com.example.garden.data.repository.FeedRepository
import com.example.garden.data.repository.FirebaseEmailAuthRepository
import com.example.garden.data.repository.FirebaseFeedRepository
import com.example.garden.view.main.FirebaseLikesRepository
import com.example.garden.view.main.LikesRepository
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class FeedViewModelModule {
    @Provides
    fun providesFirebaseFeed(
        @Named("feed_ref") ref: DatabaseReference,
        storageRef: StorageReference,
    ): FeedRepository = FirebaseFeedRepository(ref = ref, storageRef = storageRef)

    @Provides
    fun providesFirebaseLikes(
        @Named("likes_ref") ref: DatabaseReference,
    ): LikesRepository = FirebaseLikesRepository(ref = ref)

    @Provides
    @Named("likes_ref")
    fun providesLikedRef() = Firebase.database.getReference("likes")

    @Provides
    @Named("feed_ref")
    fun providesFeedRef() = Firebase.database.getReference("feed")

    @Provides
    fun providesStorageRef() = Firebase.storage.getReference("images")

    @Provides
    fun providesAuthRepository(): AuthRepository = FirebaseEmailAuthRepository()
}