package com.example.deucapstone2023.data.dto.response.poi
import com.google.gson.annotations.SerializedName

data class GroupSubLists(
    @SerializedName("groupSub")
    val groupSub: List<GroupSub?>?
)