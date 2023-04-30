package com.example.deucapstone2023.domain.usecase.impl

import com.example.deucapstone2023.domain.repository.POIRepository
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.POIUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class POIUsecaseImpl @Inject constructor(
    private val poiRepository: POIRepository
) : POIUsecase {
    override fun getPOISearch(poiModel: POIModel, appKey: String): Flow<List<POIModel>> = flow {
        emit(poiRepository.getPOISearch(
            poiModel = poiModel,
            appKey = appKey
        ))
    }

}
