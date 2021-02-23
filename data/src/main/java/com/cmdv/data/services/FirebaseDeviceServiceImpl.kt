package com.cmdv.data.services

import android.util.Log
import com.cmdv.common.utils.FirebaseConstants.COLLECTION_DEVICES_PATH
import com.cmdv.data.mappers.DeviceMapper
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.services.FirebaseDeviceService
import com.cmdv.domain.utils.LiveDataStatusWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.regex.Pattern

@Suppress("SpellCheckingInspection")
@ExperimentalCoroutinesApi
object FirebaseDeviceServiceImpl : FirebaseDeviceService {
    private val TAG = FirebaseDeviceService::class.java.simpleName
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getDevicesByManufacturer(
        manufacturerId: String
    ): Flow<LiveDataStatusWrapper<List<DeviceModel>>> =
        callbackFlow {
            offer(LiveDataStatusWrapper.loading(null))
            val collection = db.collection(COLLECTION_DEVICES_PATH)
            val query = collection.whereEqualTo("manufacturer_id", manufacturerId)

            query.get().addOnSuccessListener { task ->
                val list =
                    task?.documents?.mapNotNull { snapShot ->
                        DeviceMapper.transformEntityToModel(snapShot)
                    }
                offer(LiveDataStatusWrapper.success(list))
            }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
            }
        }

    override suspend fun getDeviceById(
        id: String
    ): Flow<LiveDataStatusWrapper<DeviceModel>> =
        callbackFlow {
            val docRef = db.collection(COLLECTION_DEVICES_PATH).document(id)

            docRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val deviceModel =
                        if (documentSnapshot != null) {
                            DeviceMapper.transformEntityToModel(documentSnapshot)
                        } else null
                    offer(LiveDataStatusWrapper.success(deviceModel))
                }
            awaitClose {
                Log.d(TAG, "")
            }
        }

    override suspend fun getDevicesByName(
        searchTerm: String,
        manufacturerId: String
    ): Flow<LiveDataStatusWrapper<List<DeviceModel>>> =
        callbackFlow {
            offer(LiveDataStatusWrapper.loading(null))
            val collection = db.collection(COLLECTION_DEVICES_PATH)
            val query = collection.whereEqualTo("manufacturer_id", manufacturerId)

            query.get().addOnSuccessListener { task ->
                val fullList: List<DeviceModel>? =
                    task?.documents?.mapNotNull { snapShot ->
                        DeviceMapper.transformEntityToModel(snapShot)
                    }
                val filteredList = fullList?.mapNotNull {
                    it.takeIf { device -> device.resume.name.contains(searchTerm, true) }
                }
                offer(LiveDataStatusWrapper.success(filteredList))
            }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
            }
        }
}