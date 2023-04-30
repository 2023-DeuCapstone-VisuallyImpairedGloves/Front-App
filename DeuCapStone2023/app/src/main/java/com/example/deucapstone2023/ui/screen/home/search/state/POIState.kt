package com.example.deucapstone2023.ui.screen.home.search.state

data class POIState(
    val poiName: String,
    val poiAddress: String,
    val poiDistance: String,
    val poiBiz: String
) {
    companion object {
        fun getInitValues() = POIState(
            poiName = "",
            poiAddress = "",
            poiDistance = "",
            poiBiz = ""
        )
    }
}
