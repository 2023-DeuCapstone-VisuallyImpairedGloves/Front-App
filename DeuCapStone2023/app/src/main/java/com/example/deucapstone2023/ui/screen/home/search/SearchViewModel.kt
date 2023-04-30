package com.example.deucapstone2023.ui.screen.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deucapstone2023.domain.model.POIModel
import com.example.deucapstone2023.domain.usecase.POIUsecase
import com.example.deucapstone2023.ui.screen.home.search.state.POIState
import com.example.deucapstone2023.utils.catchFetching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SearchUiState(
    val poiList: List<POIState>,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val searchType: String
) {
    companion object {
        fun getInitValues() = SearchUiState(
            poiList = emptyList(),
            latitude = .0,
            longitude = .0,
            searchType = "",
            name = ""
        )
    }
}

fun SearchUiState.toPOIModel() = POIModel(
    name = name,
    centerLat = latitude,
    centerLon = longitude,
    radius = "",
    searchtypCd = searchType,
    bizName = "",
    address = ""
)

fun POIModel.toPOIState() = POIState(
    poiName = name,
    poiAddress = address,
    poiDistance = radius,
    poiBiz = bizName
)

fun List<POIModel>.toPOIListState() = this.map { it.toPOIState() }

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val poiUsecase: POIUsecase
) : ViewModel() {

    private val _searchUiState = MutableStateFlow(SearchUiState.getInitValues())
    val searchUiState get() = _searchUiState.asStateFlow()

    fun searchPlace(appKey: String) =
        poiUsecase.getPOISearch(
            poiModel = searchUiState.value.toPOIModel(),
            appKey = appKey
        ).onEach { poiList ->
            _searchUiState.update { state ->
                state.copy(poiList = poiList.toPOIListState())
            }
        }.catchFetching(
            onFailedHttpException = {},
            onFailedElseException = {}
        ).launchIn(viewModelScope)

}

