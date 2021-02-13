package com.cmdv.domain.models

import java.util.*

data class DeviceModel(
    val id: String,
    val manufacturerId: String,
    val resume: ResumeModel,
    val model: ModelModel,
    val thumbnail: String,
    val images: ArrayList<String>,
    val body: BodyModel,
    val cameraRear: CameraModel,
    val cameraFront: CameraModel,
    val display: DisplayModel,
    val launch: LaunchModel,
    val network: NetworkModel,
    val platform: PlatformModel?
) {
    data class ResumeModel(
        val display: String,
        val resolution: String,
        val camera: String,
        val video: String,
        val ram: String,
        val chipset: String,
        val capacity: String,
        val technology: String,
    )

    data class ModelModel(
        val name: String,
        val variant: String,
        val version: String
    )

    data class BodyModel(
        val dimensions: DimensionsModel,
        val weight: Double,
        val build: String,
        val sim: ArrayList<String>
    ) {
        data class DimensionsModel(
            val width: Double,
            val height: Double,
            val thickness: Double
        )
    }

    data class CameraModel(
        val type: CameraType,
        val specs: ArrayList<String>,
        val features: String,
        val video: String
    )

    data class DisplayModel(
        val type: String,
        val size: SizeModel,
        val density: Long
    ) {
        data class SizeModel(
            val diagonal: Double,
            val width: Long,
            val height: Long,
            val ratio: String,
            val area: Double,
            val screenToBody: Double
        )
    }

    data class LaunchModel(
        val announced: Date,
        val release: ReleaseModel
    ) {
        data class ReleaseModel(
            val expected: Date?,
            val released: Date?,
            val status: ReleaseStatus
        )
    }

    data class NetworkModel(
        val technology: String,
        val twoG: ArrayList<String>,
        val threeG: ArrayList<String>,
        val fourG: ArrayList<String>,
        val fiveG: ArrayList<String>,
        val speed: String
    )

    data class PlatformModel(
        val os: String,
        val cpu: ArrayList<String>,
        val gpu: ArrayList<String>,
        val chipset: ArrayList<String>
    )
}

enum class CameraType(val type: String) {
    DEFAULT(""),
    SINGLE("SINGLE"),
    DUAL("DUAL"),
    TRIPLE("TRIPLE"),
    QUAD("QUAD")
}

enum class ReleaseStatus(val status: String) {
    RUMORED("RUMORED"),
    COMING_SOON("COMING_SOON"),
    CANCELLED("CANCELLED"),
    AVAILABLE("AVAILABLE"),
    DISCONTINUED("DISCONTINUED")
}

/*

Rumored
Not yet officially announced or even confirmed for market release yet. You'll notice that we're very enthusiastic on reporting on rumors for upcoming phone releases. If you've got a hot story to share with us, contact us here.

Coming Soon
Officially announced, but not yet available to buy in stores or online. However, it may be available for pre-order. We include the announcement date within our Phone Specifications page

Cancelled
Officially announced, then officially cancelled. Rarely happens.

Available
In production, can be found new in stores and online. Availability could vary by region though.

Discontinued
No longer in production, can usually be found pre-owned online.


 */