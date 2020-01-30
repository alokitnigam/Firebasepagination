package com.example.getactivetask.Views.ViewModels.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.DI.Network.FirebaseRepo
import com.google.firebase.database.DataSnapshot

class OnlineDataSource(val firebaseRepo: FirebaseRepo) : DataSource.Factory<String, DataSnapshot>() {
    private lateinit var latestSource: DataPagedSource
    private val sourceLiveData = MutableLiveData<DataPagedSource>()

    override fun create(): DataSource<String, DataSnapshot> {
        latestSource = DataPagedSource(firebaseRepo)
        sourceLiveData.postValue(latestSource)
        return latestSource
    }


}