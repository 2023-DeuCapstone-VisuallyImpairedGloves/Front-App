package com.deucapstone2023.data.data.repositoryimpl

import com.deucapstone2023.data.data.datasource.remote.FetchPOIDatasource
import javax.inject.Inject
import com.deucapstone2023.domain.domain.repository.POIRepository

class POIRepositoryImpl @Inject constructor(
    private val poiDatasource: FetchPOIDatasource
) : POIRepository {
    override suspend fun getPOISearch(poiModel: com.deucapstone2023.domain.domain.model.POIModel, appKey: String): List<com.deucapstone2023.domain.domain.model.POIModel> =
        poiDatasource.getPOISearch(poiModel = poiModel, appKey = appKey)

}

