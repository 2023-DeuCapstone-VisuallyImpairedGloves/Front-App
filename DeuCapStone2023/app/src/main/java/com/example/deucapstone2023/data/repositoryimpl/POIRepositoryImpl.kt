package com.example.deucapstone2023.data.repositoryimpl

import com.example.deucapstone2023.data.ApiService
import com.example.deucapstone2023.data.mapper.toPOIModel
import com.example.deucapstone2023.data.mapper.toRequestDTO
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.repository.POIRepository
import javax.inject.Inject

class POIRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : POIRepository{
    override suspend fun getPOISearch(poiModel: POIModel, appKey: String): List<POIModel> =
        apiService.getPOISearch(poiModel.toRequestDTO(appKey = appKey))?.toPOIModel() ?: emptyList()

}

