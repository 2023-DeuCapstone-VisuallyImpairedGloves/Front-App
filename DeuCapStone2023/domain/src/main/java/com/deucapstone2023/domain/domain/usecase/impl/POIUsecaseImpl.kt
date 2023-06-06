package com.deucapstone2023.domain.domain.usecase.impl

import com.deucapstone2023.domain.domain.repository.POIRepository
import com.deucapstone2023.domain.domain.model.POIModel
import com.deucapstone2023.domain.domain.usecase.POIUsecase
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
