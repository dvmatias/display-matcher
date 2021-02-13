package com.cmdv.data.mappers

import com.cmdv.data.helpers.DateHelper
import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.CameraType
import com.cmdv.domain.models.DeviceModel
import com.cmdv.domain.models.ReleaseStatus
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.*


private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_LONG_VALUE = -1L
private const val DEFAULT_DOUBLE_VALUE = 0.0
private const val DEFAULT_BOOLEAN_VALUE = false

private const val FIELD_MANUFACTURER_ID = "manufacturer_id"
private const val FIELD_RESUME = "resume"
private const val FIELD_DISPLAY = "display"
private const val FIELD_RESOLUTION = "resolution"
private const val FIELD_CAMERA = "camera"
private const val FIELD_VIDEO = "video"
private const val FIELD_RAM = "ram"
private const val FIELD_CHIPSET = "chipset"
private const val FIELD_CAPACITY = "capacity"
private const val FIELD_TECHNOLOGY = "technology"
private const val FIELD_MODEL = "model"
private const val FIELD_NAME = "name"
private const val FIELD_VARIANT = "variant"
private const val FIELD_VERSION = "version"
private const val FIELD_THUMBNAIL = "thumbnail"
private const val FIELD_IMAGES = "images"
private const val FIELD_BODY = "body"
private const val FIELD_DIMENSIONS = "dimensions"
private const val FIELD_WIDTH = "width"
private const val FIELD_HEIGHT = "height"
private const val FIELD_THICKNESS = "thickness"
private const val FIELD_WEIGHT = "weight"
private const val FIELD_BUILD = "build"
private const val FIELD_SIM = "sim"
private const val FIELD_CAMERA_REAR = "camera_rear"
private const val FIELD_CAMERA_FRONT = "camera_front"
private const val FIELD_CAMERA_TYPE = "type"
private const val FIELD_SPECS = "specs"
private const val FIELD_FEATURES = "features"
private const val FIELD_DISPLAY_TYPE = "type"
private const val FIELD_DISPLAY_SIZE = "size"
private const val FIELD_DIAGONAL = "diagonal"
private const val FIELD_RATIO = "ratio"
private const val FIELD_AREA = "area"
private const val FIELD_SCREEN_TO_BODY = "screen_to_body"
private const val FIELD_DENSITY = "density"
private const val FIELD_LAUNCH = "launch"
private const val FIELD_RELEASE = "release"
private const val FIELD_ANNOUNCED = "announced"
private const val FIELD_EXPECTED = "expected"
private const val FIELD_RELEASED = "released"
private const val FIELD_STATUS = "status"
private const val FIELD_NETWORK = "network"
private const val FIELD_2G = "2g"
private const val FIELD_3G = "3g"
private const val FIELD_4G = "4g"
private const val FIELD_5G = "5g"
private const val FIELD_SPEED = "speed"
private const val FIELD_PLATFORM = "platform"
private const val FIELD_OS = "os"
private const val FIELD_CPU = "cpu"
private const val FIELD_GPU = "gpu"

object DeviceMapper : BaseMapper<DocumentSnapshot, DeviceModel>() {

    @Suppress("UNCHECKED_CAST")
    override fun transformEntityToModel(e: DocumentSnapshot): DeviceModel {
        val id: String = e.id
        val manufacturerId: String = e.getStringValue(FIELD_MANUFACTURER_ID)
        val resume: DeviceModel.ResumeModel = getResume(e)
        val model: DeviceModel.ModelModel = getModel(e)
        val thumbnail: String = e.getStringValue(FIELD_THUMBNAIL)
        val images: ArrayList<String> = e.get(FIELD_IMAGES) as ArrayList<String>
        val body: DeviceModel.BodyModel = getBody(e)
        val cameraRear: DeviceModel.CameraModel = getCamera(e, FIELD_CAMERA_REAR)
        val cameraFront: DeviceModel.CameraModel = getCamera(e, FIELD_CAMERA_FRONT)
        val display: DeviceModel.DisplayModel = getDisplay(e)
        val launch: DeviceModel.LaunchModel = getLaunch(e)
        val network: DeviceModel.NetworkModel = getNetwork(e)
        val platform: DeviceModel.PlatformModel = getPlatform(e)

        return DeviceModel(
            id,
            manufacturerId,
            resume,
            model,
            thumbnail,
            images,
            body,
            cameraRear,
            cameraFront,
            display,
            launch,
            network,
            platform
        )
    }

    private fun getResume(e: DocumentSnapshot): DeviceModel.ResumeModel {
        val mapModel = e.getMap<String, String>(FIELD_RESUME)

        return DeviceModel.ResumeModel(
            mapModel.getString(FIELD_DISPLAY),
            mapModel.getString(FIELD_RESOLUTION),
            mapModel.getString(FIELD_CAMERA),
            mapModel.getString(FIELD_VIDEO),
            mapModel.getString(FIELD_RAM),
            mapModel.getString(FIELD_CHIPSET),
            mapModel.getString(FIELD_CAPACITY),
            mapModel.getString(FIELD_TECHNOLOGY)
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getModel(e: DocumentSnapshot): DeviceModel.ModelModel {
        val mapModel = e.getMap<String, String>(FIELD_MODEL)
        return DeviceModel.ModelModel(
            name = mapModel[FIELD_NAME] ?: DEFAULT_STRING_VALUE,
            variant = mapModel[FIELD_VARIANT] ?: DEFAULT_STRING_VALUE,
            version = mapModel[FIELD_VERSION] ?: DEFAULT_STRING_VALUE
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getBody(e: DocumentSnapshot): DeviceModel.BodyModel {
        val mapBody = e.getMap<String, Any>(FIELD_BODY)
        val mapDimensions: Map<String, String> = mapBody[FIELD_DIMENSIONS] as Map<String, String>
        val dimensions = DeviceModel.BodyModel.DimensionsModel(
            mapDimensions[FIELD_WIDTH]?.toDouble() ?: DEFAULT_DOUBLE_VALUE,
            mapDimensions[FIELD_HEIGHT]?.toDouble() ?: DEFAULT_DOUBLE_VALUE,
            mapDimensions[FIELD_THICKNESS]?.toDouble() ?: DEFAULT_DOUBLE_VALUE
        )
        return DeviceModel.BodyModel(
            dimensions = dimensions,
            weight = (mapBody[FIELD_WEIGHT] as String).toDouble(),
            build = mapBody[FIELD_BUILD] as String,
            sim = mapBody[FIELD_SIM] as ArrayList<String>
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getCamera(e: DocumentSnapshot, field: String): DeviceModel.CameraModel {
        val cameraMap = e.getMap<String, Any>(field)

        fun getCameraType(): CameraType =
            when (cameraMap[FIELD_CAMERA_TYPE]) {
                CameraType.SINGLE.type -> CameraType.SINGLE
                CameraType.DUAL.type -> CameraType.DUAL
                CameraType.TRIPLE.type -> CameraType.TRIPLE
                CameraType.QUAD.type -> CameraType.QUAD
                else -> CameraType.DEFAULT
            }

        return DeviceModel.CameraModel(
            getCameraType(),
            cameraMap[FIELD_SPECS] as ArrayList<String>,
            cameraMap.getString(FIELD_FEATURES),
            cameraMap.getString(FIELD_VIDEO)
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDisplay(e: DocumentSnapshot): DeviceModel.DisplayModel {
        val displayMap = e.getMap<String, Any>(FIELD_DISPLAY)
        val sizeMap: Map<String, Any> = displayMap[FIELD_DISPLAY_SIZE] as Map<String, Any>
        val size = DeviceModel.DisplayModel.SizeModel(
            sizeMap.getString(FIELD_DIAGONAL).toDouble(),
            sizeMap[FIELD_WIDTH] as Long,
            sizeMap[FIELD_HEIGHT] as Long,
            sizeMap[FIELD_RATIO] as String,
            sizeMap.getString(FIELD_AREA).toDouble(),
            sizeMap.getString(FIELD_SCREEN_TO_BODY).toDouble()
        )
        return DeviceModel.DisplayModel(
            displayMap[FIELD_DISPLAY_TYPE] as String,
            size,
            displayMap[FIELD_DENSITY] as Long
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getLaunch(e: DocumentSnapshot): DeviceModel.LaunchModel {
        val launchMap = e.getMap<String, Any>(FIELD_LAUNCH)
        val releaseMap: Map<String, Any> = launchMap[FIELD_RELEASE] as Map<String, Any>

        fun getReleaseStatus(): ReleaseStatus =
            when (releaseMap[FIELD_STATUS]) {
                ReleaseStatus.RUMORED.status -> ReleaseStatus.RUMORED
                ReleaseStatus.COMING_SOON.status -> ReleaseStatus.COMING_SOON
                ReleaseStatus.CANCELLED.status -> ReleaseStatus.CANCELLED
                ReleaseStatus.AVAILABLE.status -> ReleaseStatus.AVAILABLE
                ReleaseStatus.DISCONTINUED.status -> ReleaseStatus.DISCONTINUED
                else -> ReleaseStatus.DISCONTINUED
            }

        return DeviceModel.LaunchModel(
            announced = DateHelper.getDateFromTimestamp(launchMap[FIELD_ANNOUNCED] as Timestamp) ?: Date(),
            DeviceModel.LaunchModel.ReleaseModel(
                expected = DateHelper.getDateFromTimestamp(releaseMap[FIELD_EXPECTED] as Timestamp),
                released = DateHelper.getDateFromTimestamp(releaseMap[FIELD_RELEASED] as Timestamp),
                getReleaseStatus()
            )
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun getNetwork(e: DocumentSnapshot): DeviceModel.NetworkModel {
        val networkMap = e.getMap<String, Any>(FIELD_NETWORK)

        return DeviceModel.NetworkModel(
            technology = networkMap.getString(FIELD_TECHNOLOGY),
            twoG = networkMap[FIELD_2G] as ArrayList<String>,
            threeG = networkMap[FIELD_3G] as ArrayList<String>,
            fourG = networkMap[FIELD_4G] as ArrayList<String>,
            fiveG = networkMap[FIELD_5G] as ArrayList<String>,
            speed = networkMap.getString(FIELD_SPEED)
        )
    }

    private fun getPlatform(e: DocumentSnapshot): DeviceModel.PlatformModel {
        val platformMap = e.getMap<String, String>(FIELD_PLATFORM)

        return DeviceModel.PlatformModel(
            os = platformMap.getString(FIELD_OS),
            cpu = platformMap.getString(FIELD_CPU).getStringArrayFromString("|"),
            gpu = platformMap.getString(FIELD_GPU).getStringArrayFromString("|"),
            chipset = platformMap.getString(FIELD_CHIPSET).getStringArrayFromString("|"),
        )
    }
}

fun <K, V> Map<K, V>.getString(key: K): String = this[key].toString()

@Suppress("UNCHECKED_CAST")
private fun <K, V> DocumentSnapshot.getMap(field: String): Map<K, V> =
    this.get(field) as Map<K, V>

private fun DocumentSnapshot.getStringValue(field: String): String =
    try {
        this.get(field) as String
    } catch (e: Exception) {
        DEFAULT_STRING_VALUE
    }

private fun DocumentSnapshot.getLongValue(filed: String): Long =
    try {
        this.get(filed) as Long
    } catch (e: Exception) {
        DEFAULT_LONG_VALUE
    }

private fun DocumentSnapshot.getDoubleValue(filed: String): Double =
    try {
        this.get(filed) as Double
    } catch (e: Exception) {
        DEFAULT_DOUBLE_VALUE
    }

private fun DocumentSnapshot.getTimeStampValue(filed: String): String =
    DateHelper.getFormattedDateFromSeconds(this.getTimestamp(filed)?.seconds, DateHelper.PATTERN_MMMM_D_YYYY)

private fun Timestamp.getStringValue(): String =
    DateHelper.getFormattedDateFromSeconds(this.seconds, DateHelper.PATTERN_MMMM_D_YYYY)

private fun DocumentSnapshot.getBooleanValue(filed: String): Boolean =
    try {
        this.get(filed) as Boolean
    } catch (e: Exception) {
        DEFAULT_BOOLEAN_VALUE
    }

private fun String.getStringArrayFromString(separator: String): ArrayList<String> =
    this.let { source ->
        val result = arrayListOf<String>()
        if (source.isNotBlank()) {
            source.split(separator).forEach { result.add(it) }
        }
        result
    }

/*
{
    "body": {
      "build": "",
      "dimensions": {
        "depth": 0.38,
        "height": 6.63,
        "width": 2.91
      },
      "sim": "Dual SIM (Nano-SIM, dual stand-by)|IP52, splash resistant",
      "weight": 7.58
    },
    "camera_rare": {
      "features": "Dual-LED flash, panorama, HDR",
      "specs": "64 MP, f/1.7, (wide), 1/1.72\", 0.8µm, PDAF|16 MP, 121˚ (ultrawide), 1.0µm, PDAF|2 MP, (depth)|TOF 3D",
      "type": "QUAD",
      "video": "6K@30fps, 4K@30/60fps, 1080p@30/60fps, gyro-EIS"
    },
    "camera_front": {
      "features": "HDR",
      "specs": "16 MP, (wide), 1.0µm|8 MP, 100˚ (ultrawide), 1.12µm",
      "type": "DUAL",
      "video": "1080p@30fps"
    },
    "display": {
      "density": 409,
      "size": {
        "area": 104.9,
        "diagonal": 6.7,
        "height": 2520,
        "ratio": "21:9",
        "screen_to_body": 84.1,
        "width": 1080
      },
      "type": "IPS LCD, 90Hz, HDR10, 560 nits (typ)"
    },
    "id": "",
    "images": [
      "https://fdn2.gsmarena.com/vv/pics/motorola/motorola-moto-edge-s-1.jpg",
      "https://fdn2.gsmarena.com/vv/pics/motorola/motorola-moto-edge-s-2.jpg"
    ],
    "launch": {
      "announced": "2021-01-26T00:00:00",
      "released": "2021-02-03T00:00:00"
    },
    "manufacturer_id": "eyJuYW1lIjoibW90b3JvbGEiLCAic2hvcnRfbmFtZSI6Im1vdG8ifQ==",,
    "network": {
      "2g": "GSM 850 / 900 / 1800 / 1900 - SIM 1 & SIM 2",
      "3g": "HSDPA 850 / 900 / 1700(AWS) / 1900 / 2100",
      "4g": "1, 2, 3, 4, 5, 7, 8, 12, 13, 17, 20, 26, 28, 32, 34, 38, 39, 40, 41, 42, 43, 66",
      "5g": "1, 3, 5, 7, 8, 28, 38, 41, 66, 77, 78 SA/NSA",
      "speed": "HSPA 42.2/5.76 Mbps, LTE-A, 5G",
      "technology": "GSM / CDMA / HSPA / CDMA2000 / LTE / 5G"
    },
    "platform": {
      "chipset": "Qualcomm SM8250-AC Snapdragon 870 5G (7 nm)",
      "cpu": "Octa-core (1x3.2 GHz Kryo 585 & 3x2.42 GHz Kryo 585 & 4x1.80 GHz Kryo 585)",
      "gpu": "Adreno 650",
      "os": "Android 11"
    },
    "thumbnail": "https://fdn2.gsmarena.com/vv/pics/motorola/motorola-moto-edge-s-1.jpg"
  }
 */