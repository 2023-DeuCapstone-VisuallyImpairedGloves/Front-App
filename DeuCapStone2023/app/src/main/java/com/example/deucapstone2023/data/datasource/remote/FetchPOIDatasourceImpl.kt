package com.example.deucapstone2023.data.datasource.remote

import com.example.deucapstone2023.data.datasource.remote.api.TmapApi
import com.example.deucapstone2023.data.mapper.toPOIModel
import com.example.deucapstone2023.data.mapper.toRequestDTO
import com.example.deucapstone2023.domain.model.POIModel
import javax.inject.Inject

class FetchPOIDatasourceImpl @Inject constructor(
    private val tmapApi: TmapApi
) : FetchPOIDatasource {

    override suspend fun getPOISearch(poiModel: POIModel, appKey: String): List<POIModel> =
        tmapApi.getPOISearch(poiModel.toRequestDTO(appKey = appKey))?.toPOIModel() ?: emptyList()
}