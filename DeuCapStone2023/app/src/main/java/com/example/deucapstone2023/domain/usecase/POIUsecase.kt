package com.example.deucapstone2023.domain.usecase

import com.example.deucapstone2023.domain.model.POIModel
import kotlinx.coroutines.flow.Flow

interface POIUsecase {
    fun getPOISearch(poiModel: POIModel, appKey: String) : Flow<List<POIModel>>
}