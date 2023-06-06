package com.deucapstone2023.domain.domain.usecase

import com.deucapstone2023.domain.domain.model.POIModel
import kotlinx.coroutines.flow.Flow

interface POIUsecase {
    fun getPOISearch(poiModel: POIModel, appKey: String) : Flow<List<POIModel>>
}