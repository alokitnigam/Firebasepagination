package com.example.getactivetask.Views.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.getactivetask.DI.Network.FirebaseRepo
import com.example.getactivetask.DI.Network.NetworkState
import com.google.firebase.database.DataSnapshot
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityViewModel @Inject
constructor( val firebaseRepo: FirebaseRepo) : ViewModel()
{
    var posts:MutableLiveData<List<DataSnapshot>> = MutableLiveData()
    var nextposts:MutableLiveData<List<DataSnapshot>> = MutableLiveData()
    var datachaged:MutableLiveData<Pair<DataSnapshot,String>> = MutableLiveData()
    var netWorkResponse: MutableLiveData<NetworkState> = MutableLiveData()

    fun getInitialPosts(){
        firebaseRepo.getInitialPosts(5)
            .observeOn(Schedulers.io())
            .subscribe({
                netWorkResponse.postValue( NetworkState(NetworkState.SUCESS,"Success"))
                posts.postValue(it)
            },{
                netWorkResponse.postValue( NetworkState(NetworkState.FAILED,"Failure"))
            })
    }
    fun getNextPosts(key:String,previousTimestamp:String){
        firebaseRepo.getNextPosts(5,key,previousTimestamp)
            .observeOn(Schedulers.io())
            .subscribe({
                netWorkResponse.postValue( NetworkState(NetworkState.SUCESS,"Success"))
                nextposts.postValue(it)

            },{
                netWorkResponse.postValue( NetworkState(NetworkState.FAILED,"Failure"))
            })
    }

    fun getNotifiedOfDataChanges(){
        firebaseRepo.notifyDataChanged()
            .observeOn(Schedulers.io())
            .subscribe({
                datachaged.postValue(it)
            },{

            })
    }




}