package com.example.getactivetask.DI.Modules

import android.app.Application
import android.content.Context
import com.example.getactivetask.Base.MyApplication

import dagger.Binds
import dagger.Module

@Module
abstract class ContextModule {

    @Binds
    internal abstract fun provideContext(application: MyApplication): Context
}
