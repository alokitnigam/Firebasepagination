package com.example.getactivetask.DI.Network

import com.example.getactivetask.DI.Models.DataModel
import com.google.firebase.database.DataSnapshot
import io.reactivex.Completable
import io.reactivex.Observable

interface FirebaseRepo {

    fun createPost(dataModel: DataModel): Completable

    fun updatePost(dataModel: DataModel, key: String): Completable

    fun getInitialPosts(count: Int):Observable<List<DataSnapshot>>

    fun notifyDataChanged():Observable<Pair<DataSnapshot,String>>

    fun getNextPosts(count: Int,key:String,previousTimestamp:String):Observable<List<DataSnapshot>>

    fun deletePost(key: String):Completable


}