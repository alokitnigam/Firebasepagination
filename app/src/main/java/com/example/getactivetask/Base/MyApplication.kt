package com.example.getactivetask.Base

import com.example.getactivetask.DI.ApplicationComponent
import com.example.getactivetask.DI.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class  MyApplication : DaggerApplication() {

//    @Inject
//    lateinit var myWorkerFactory: MyWorkerFactory
//    @Inject
//    lateinit var dbHelper: DbHelper

    lateinit var component: ApplicationComponent


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerApplicationComponent.builder().application(this).build()
        component.inject( this)
        return component
    }



}
