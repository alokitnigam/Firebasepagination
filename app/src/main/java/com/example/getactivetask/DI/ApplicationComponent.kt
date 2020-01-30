package com.example.getactivetask.DI

import android.app.Application
import com.example.getactivetask.Base.MyApplication
import com.example.getactivetask.DI.Modules.ContextModule
import com.example.getactivetask.DI.Modules.ActivityBindingModule
import com.example.getactivetask.DI.Network.FirebaseModule
import com.example.getactivetask.DI.VMFactory.MyViewModelModule
import com.example.getactivetask.DI.VMFactory.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,AndroidSupportInjectionModule::class,
    ActivityBindingModule::class, FirebaseModule::class,
    ViewModelFactoryModule::class, MyViewModelModule::class])
interface ApplicationComponent : AndroidInjector<MyApplication> {


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }



}