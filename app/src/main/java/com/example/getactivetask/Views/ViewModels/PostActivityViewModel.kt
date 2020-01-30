package com.example.getactive.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getactivetask.DI.Models.DataModel
import com.example.getactivetask.DI.Network.FirebaseRepo
import com.example.getactivetask.DI.Network.NetworkState
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PostActivityViewModel @Inject
constructor(val firebaseRepo: FirebaseRepo) : ViewModel()
{
    var netWorkResponse: MutableLiveData<NetworkState> = MutableLiveData()


    fun postData(dataModel: DataModel){
        firebaseRepo.createPost(dataModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Success") },
                {  netWorkResponse.value = NetworkState(NetworkState.FAILED,"Failure")}
            )

    }

    fun updateData(dataModel: DataModel,key:String){
        firebaseRepo.updatePost(dataModel,key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Success") },
                {  netWorkResponse.value = NetworkState(NetworkState.FAILED,"Failure")}
            )

    }

    fun deletePost(key:String){
        firebaseRepo.deletePost(key)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { netWorkResponse.value = NetworkState(NetworkState.SUCESS,"Success") },
                {  netWorkResponse.value = NetworkState(NetworkState.FAILED,"Failure")}
            )
    }

}