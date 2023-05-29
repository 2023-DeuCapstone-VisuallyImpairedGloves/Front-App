package com.example.deucapstone2023.data.datasource.remote

import com.example.deucapstone2023.domain.model.POIModel

interface FetchPOIDatasource {

    suspend fun getPOISearch(
        poiModel: POIModel,
        appKey: String
    ) : List<POIModel>
}