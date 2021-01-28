package com.cmdv.data.mappers

import com.cmdv.domain.mappers.BaseMapper
import com.cmdv.domain.models.BrandModel
import com.google.firebase.firestore.DocumentSnapshot
import java.lang.Exception

private const val DEFAULT_VALUE = ""
private const val FIELD_NAME = "name"
private const val FIELD_SHORT_NAME = "short"
private const val FIELD_IMAGE_URL = "image_url"

class BrandMapper : BaseMapper<DocumentSnapshot, BrandModel>() {

    override fun transformEntityToModel(e: DocumentSnapshot): BrandModel {
        val id: String = e.id
        val name: String = e.getStringValue(FIELD_NAME)
        val shortName: String = e.getStringValue(FIELD_SHORT_NAME)
        val imageUrl: String = e.getStringValue(FIELD_IMAGE_URL)
        return BrandModel(id, name, shortName, imageUrl)
    }

}

private fun DocumentSnapshot.getStringValue(field: String): String =
    try { this.get(field) as String } catch (e: Exception) { DEFAULT_VALUE }