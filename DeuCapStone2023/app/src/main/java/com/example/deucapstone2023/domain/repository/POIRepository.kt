package com.example.deucapstone2023.domain.repository

import com.example.deucapstone2023.domain.model.POIModel
import kotlinx.coroutines.flow.Flow

interface POIRepository {
    suspend fun getPOISearch(
        poiModel: POIModel,
        appKey: String
    ) : List<POIModel>
}