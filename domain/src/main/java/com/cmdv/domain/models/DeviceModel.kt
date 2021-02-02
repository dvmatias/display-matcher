package com.cmdv.domain.models

data class DeviceModel(
    val id: String,
    val manufacturer: String,
    val manufacturerId: String,
    val name: String,
    val variant: String,
    val version: String,
    val displayHeight: Long,
    val displayWidth: Long,
    val displaySize: Double,
    val imageUrl: String,
    val dateRelease: String,
    val isReleased: Boolean,
    val releaseStatus: ReleaseStatus,
    val camera: String,
    val video: String,
    val ram: ArrayList<String>,
    val chipset: String
)

enum class ReleaseStatus {
    RELEASED,
    NOT_RELEASED,
    DELAYED
}