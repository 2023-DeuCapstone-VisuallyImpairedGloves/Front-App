package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.datasource.remote.FetchPOIDatasource
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.repository.POIRepository
import javax.inject.Inject

class POIRepositoryImpl @Inject constructor(
    private val poiDatasource: FetchPOIDatasource
) : POIRepository{
    override suspend fun getPOISearch(poiModel: POIModel, appKey: String): List<POIModel> =
        poiDatasource.getPOISearch(poiModel = poiModel, appKey = appKey)

}

