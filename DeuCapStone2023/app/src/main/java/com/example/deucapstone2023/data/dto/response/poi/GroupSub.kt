package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class GroupSub(
    @SerializedName("subCenterX")
    val subCenterX: String?,
    @SerializedName("subCenterY")
    val subCenterY: String?,
    @SerializedName("subClassCd")
    val subClassCd: String?,
    @SerializedName("subClassNmA")
    val subClassNmA: String?,
    @SerializedName("subClassNmB")
    val subClassNmB: String?,
    @SerializedName("subClassNmC")
    val subClassNmC: String?,
    @SerializedName("subClassNmD")
    val subClassNmD: String?,
    @SerializedName("subName")
    val subName: String?,
    @SerializedName("subNavSeq")
    val subNavSeq: String?,
    @SerializedName("subNavX")
    val subNavX: String?,
    @SerializedName("subNavY")
    val subNavY: String?,
    @SerializedName("subParkYn")
    val subParkYn: String?,
    @SerializedName("subPkey")
    val subPkey: String?,
    @SerializedName("subPoiId")
    val subPoiId: String?,
    @SerializedName("subRpFlag")
    val subRpFlag: String?,
    @SerializedName("subSeq")
    val subSeq: String?
)