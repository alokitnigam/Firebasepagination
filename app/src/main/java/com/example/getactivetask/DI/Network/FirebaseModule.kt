package com.example.getactivetask.DI.Network

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class FirebaseModule {


    @Provides
    @Singleton
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()


    @Provides
    @Singleton
    fun providesFirebaseRepo(firebaseRepoImpl: FirebaseRepoImpl): FirebaseRepo {
        return firebaseRepoImpl
    }

}
