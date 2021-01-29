package com.cmdv.data.services

import android.util.Log
import com.cmdv.common.utils.FirebaseConstants.COLLECTION_MANUFACTURERS_PATH
import com.cmdv.data.mappers.ManufacturerMapper
import com.cmdv.domain.models.ManufacturerModel
import com.cmdv.domain.services.FirebaseManufacturerService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@Suppress("SpellCheckingInspection")
@ExperimentalCoroutinesApi
object FirebaseManufacturerServiceImpl : FirebaseManufacturerService {
    private val TAG = FirebaseManufacturerServiceImpl::class.java.simpleName
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getManufacturers(): Flow<List<ManufacturerModel>?> =
        callbackFlow {
            val listenerRegistration =
                db.collection(COLLECTION_MANUFACTURERS_PATH)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                        if (firebaseFirestoreException != null) {
                            cancel(
                                message = "Error fetching manufacturers",
                                cause = firebaseFirestoreException
                            )
                            return@addSnapshotListener
                        }
                        val map = querySnapshot?.documents?.mapNotNull {
                            ManufacturerMapper.transformEntityToModel(it)
                        }
                        offer(map)
                    }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }

}