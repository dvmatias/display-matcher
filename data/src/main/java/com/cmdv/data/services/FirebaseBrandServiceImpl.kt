package com.cmdv.data.services

import android.util.Log
import com.cmdv.data.mappers.BrandMapper
import com.cmdv.domain.models.BrandModel
import com.cmdv.domain.services.FirebaseBrandService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val COLLECTION_BRANDS_PATH = "brands"

@Suppress("SpellCheckingInspection")
@ExperimentalCoroutinesApi
object FirebaseBrandServiceImpl : FirebaseBrandService {
    private val TAG = FirebaseBrandServiceImpl::class.java.simpleName
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getBrands(): Flow<List<BrandModel>?> {
        return callbackFlow {
            val listenerRegistration =
                db.collection(COLLECTION_BRANDS_PATH)
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching brands",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot?.documents?.mapNotNull { BrandMapper().transformEntityToModel(it) }
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }

}