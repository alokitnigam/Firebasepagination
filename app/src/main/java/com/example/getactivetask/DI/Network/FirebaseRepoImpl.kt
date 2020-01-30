package com.example.getactivetask.DI.Network

import com.example.getactivetask.DI.Models.DataModel
import com.google.firebase.database.*
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FirebaseRepoImpl @Inject constructor(private val db:FirebaseDatabase) : FirebaseRepo {
    override fun createPost(dataModel: DataModel): Completable {
        return Completable.create { emitter ->
            db.getReference("posts")
                .push()
                .setValue(dataModel)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }.subscribeOn(Schedulers.io())
    }

    override fun updatePost(
        dataModel: DataModel,
        key: String
    ): Completable {
        return Completable.create { emitter ->
            db.getReference("posts")
                .child(key)
                .child("title")
                .setValue(dataModel.title)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }.subscribeOn(Schedulers.io())
    }

    override fun getInitialPosts(count: Int): Observable<List<DataSnapshot>> {
        return Observable.create(ObservableOnSubscribe<List<DataSnapshot>> { emitter ->
            db.getReference("posts").limitToLast(count)
                .orderByChild("time")
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList = arrayListOf<DataSnapshot>()
                        for(childDataSnapshot  in dataSnapshot.children){
                            dataList.add(childDataSnapshot)
                        }
                        emitter.onNext(dataList)
                    }
                })

        }).subscribeOn(Schedulers.io())      }

    override fun notifyDataChanged(): Observable<Pair<DataSnapshot,String>> {
        return Observable.create(ObservableOnSubscribe<Pair<DataSnapshot,String>> { emitter ->

            db.getReference("posts")
                .addChildEventListener(object :ChildEventListener{
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                    }

                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                        emitter.onNext(Pair(p0,"Changed"))

                    }

                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        emitter.onNext(Pair(p0,"Added"))
                    }

                    override fun onChildRemoved(p0: DataSnapshot) {
                        emitter.onNext(Pair(p0,"Deleted"))

                    }
                })
        })
    }

    override fun getNextPosts(count: Int,key:String,previousTimestamp:String): Observable<List<DataSnapshot>> {
        return Observable.create(ObservableOnSubscribe<List<DataSnapshot>> { emitter ->
            db.getReference("posts")
                .orderByChild("time")
                .endAt(previousTimestamp,key)
                .limitToLast(5)

                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val dataList = arrayListOf<DataSnapshot>()
                        for(childDataSnapshot  in dataSnapshot.children){
                            dataList.add(childDataSnapshot)
                        }
                        emitter.onNext(dataList)
                    }
                })

        }).subscribeOn(Schedulers.io())
    }



    override fun deletePost(key: String): Completable {
        return Completable.create { emitter ->
            db.getReference("posts")
                .child(key).removeValue()
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
        }.subscribeOn(Schedulers.io())
    }


}