package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("adminDongCode")
    val adminDongCode: String?,
    @SerializedName("collectionType")
    val collectionType: String?,
    @SerializedName("dataKind")
    val dataKind: String?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("detailAddrname")
    val detailAddrname: String?,
    @SerializedName("detailBizName")
    val detailBizName: String?,
    @SerializedName("detailInfoFlag")
    val detailInfoFlag: String?,
    @SerializedName("evChargers")
    val evChargers: EvChargers?,
    @SerializedName("firstBuildNo")
    val firstBuildNo: String?,
    @SerializedName("firstNo")
    val firstNo: String?,
    @SerializedName("frontLat")
    val frontLat: String?,
    @SerializedName("frontLon")
    val frontLon: String?,
    @SerializedName("groupSubLists")
    val groupSubLists: GroupSubLists?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("legalDongCode")
    val legalDongCode: String?,
    @SerializedName("lowerAddrName")
    val lowerAddrName: String?,
    @SerializedName("bizName")
    val bizName: String?,
    @SerializedName("lowerBizName")
    val lowerBizName: String?,
    @SerializedName("middleAddrName")
    val middleAddrName: String?,
    @SerializedName("middleBizName")
    val middleBizName: String?,
    @SerializedName("mlClass")
    val mlClass: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("navSeq")
    val navSeq: String?,
    @SerializedName("newAddressList")
    val newAddressList: NewAddressList,
    @SerializedName("noorLat")
    val noorLat: String?,
    @SerializedName("noorLon")
    val noorLon: String?,
    @SerializedName("parkFlag")
    val parkFlag: String?,
    @SerializedName("pkey")
    val pkey: String?,
    @SerializedName("radius")
    val radius: String?,
    @SerializedName("roadName")
    val roadName: String?,
    @SerializedName("rpFlag")
    val rpFlag: String?,
    @SerializedName("secondBuildNo")
    val secondBuildNo: String?,
    @SerializedName("secondNo")
    val secondNo: String?,
    @SerializedName("telNo")
    val telNo: String?,
    @SerializedName("upperAddrName")
    val upperAddrName: String?,
    @SerializedName("upperBizName")
    val upperBizName: String?,
    @SerializedName("zipCode")
    val zipCode: String?
)