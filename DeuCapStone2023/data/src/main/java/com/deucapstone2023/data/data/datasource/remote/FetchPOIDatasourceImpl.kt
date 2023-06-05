package com.deucapstone2023.data.data.datasource.remote

import com.deucapstone2023.data.data.datasource.remote.api.TmapApi
import com.deucapstone2023.data.data.datasource.remote.mapper.toPOIModel
import com.deucapstone2023.data.data.datasource.remote.mapper.toRequestDTO
import com.deucapstone2023.domain.domain.model.POIModel
import javax.inject.Inject

class FetchPOIDatasourceImpl @Inject constructor(
    private val tmapApi: TmapApi
) : FetchPOIDatasource {

    override suspend fun getPOISearch(poiModel: POIModel, appKey: String): List<POIModel> =
        tmapApi.getPOISearch(poiModel.toRequestDTO(appKey = appKey))?.toPOIModel() ?: emptyList()
}