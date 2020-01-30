package com.example.getactivetask.DI.VMFactory

import androidx.lifecycle.ViewModelProvider
import com.example.getactivetask.DI.VMFactory.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}