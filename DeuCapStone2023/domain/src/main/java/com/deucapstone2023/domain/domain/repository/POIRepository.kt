package com.deucapstone2023.domain.domain.repository

import com.deucapstone2023.domain.domain.model.POIModel

interface POIRepository {
    suspend fun getPOISearch(
        poiModel: POIModel,
        appKey: String
    ) : List<POIModel>
}