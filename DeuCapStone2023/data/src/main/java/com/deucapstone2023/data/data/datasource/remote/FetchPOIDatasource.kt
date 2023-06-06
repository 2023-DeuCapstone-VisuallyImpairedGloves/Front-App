package com.deucapstone2023.data.data.datasource.remote

import com.deucapstone2023.domain.domain.model.POIModel

interface FetchPOIDatasource {

    suspend fun getPOISearch(
        poiModel: POIModel,
        appKey: String
    ): List<POIModel>
}