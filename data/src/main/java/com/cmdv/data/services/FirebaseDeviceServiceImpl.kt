package com.cmdv.data.services

import android.util.Log
import com.cmdv.common.utils.FirebaseConstants.COLLECTION_DEVICES_PATH
import com.cmdv.common.utils.FirebaseConstants.COLLECTION_MANUFACTURERS_PATH
import com.cmdv.data.mappers.DeviceMapper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.services.FirebaseDeviceService
import com.cmdv.domain.utils.LiveDataStatusWrapper
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
object FirebaseDeviceServiceImpl : FirebaseDeviceService {
    private val TAG = FirebaseDeviceService::class.java.simpleName
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getDevices(manufacturerId: String): Flow<LiveDataStatusWrapper<List<DeviceModel>>> =
        callbackFlow {
            offer(LiveDataStatusWrapper.loading(null))
            val listenerRegistration =
                db.collection(COLLECTION_MANUFACTURERS_PATH)
                    .document(manufacturerId)
                    .collection(COLLECTION_DEVICES_PATH)
                    .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                        if (firebaseFirestoreException != null) {
                            cancel(
                                message = "Error fetching devices of manufacter with ID=$manufacturerId",
                                cause = firebaseFirestoreException
                            )
                            return@addSnapshotListener
                        }
                        val map = querySnapshot?.documents?.mapNotNull { snapShot ->
                            DeviceMapper.transformEntityToModel(snapShot)
                        }
                        offer(LiveDataStatusWrapper.success(map))
                    }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }

    override suspend fun getDevice(id: String, manufacturerId: String): Flow<LiveDataStatusWrapper<DeviceModel>> =
        callbackFlow {
            val docRef = db.collection(COLLECTION_MANUFACTURERS_PATH)
                .document(manufacturerId)
                .collection(COLLECTION_DEVICES_PATH)
                .document(id)
            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val deviceModel =
                        if (documentSnapshot != null) { DeviceMapper.transformEntityToModel(documentSnapshot) } else null
                    offer(LiveDataStatusWrapper.success(deviceModel))
                }
            awaitClose {
                Log.d(TAG, "")
            }
        }
}