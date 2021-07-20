package com.example.repository.model

import com.google.gson.annotations.SerializedName


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/7/19
 * Time: 18:01
 */
data class KBCartoonChapters(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results
) {
    data class Results(
        @SerializedName("limit")
        val limit: Int,
        @SerializedName("list")
        val list: List<Chapter>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class Chapter(
            @SerializedName("comic_id")
            val comicId: String,
            @SerializedName("comic_path_word")
            val comicPathWord: String,
            @SerializedName("count")
            val count: Int,
            @SerializedName("datetime_created")
            val datetimeCreated: String,
            @SerializedName("group_id")
            val groupId: Any,
            @SerializedName("group_path_word")
            val groupPathWord: String,
            @SerializedName("img_type")
            val imgType: Int,
            @SerializedName("index")
            val index: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("next")
            val next: String,
            @SerializedName("ordered")
            val ordered: Int,
            @SerializedName("prev")
            val prev: Any,
            @SerializedName("size")
            val size: Int,
            @SerializedName("type")
            val type: Int,
            @SerializedName("uuid")
            val uuid: String
        )
    }
}