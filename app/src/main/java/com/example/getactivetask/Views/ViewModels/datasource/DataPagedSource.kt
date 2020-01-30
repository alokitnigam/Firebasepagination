package com.example.getactivetask.Views.ViewModels.datasource

import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.paging.PageKeyedDataSource
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.DI.Network.FirebaseRepo
import com.google.firebase.database.DataSnapshot
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class DataPagedSource(val firebaseRepo: FirebaseRepo) : PageKeyedDataSource<String, DataSnapshot>() {
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, DataSnapshot>
    ) {

        firebaseRepo.getInitialPosts(5)
            .observeOn(Schedulers.io())
            .subscribe({
                callback.onResult(it,it[it.size -1].key,it[it.size -1].key)
            },{
                Log.i("","")
            })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, DataSnapshot>) {
//        firebaseRepo.getNextPosts(5,params.key)
//            .observeOn(Schedulers.io())
//            .subscribe({
//                callback.onResult(it,it[it.size -1].key)
//            }, {
//                Log.i("","")
//
//            }
//            )
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, DataSnapshot>) {

    }


}