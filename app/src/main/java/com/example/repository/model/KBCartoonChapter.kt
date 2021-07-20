package com.example.repository.model


import com.google.gson.annotations.SerializedName

data class KBCartoonChapter(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results
) {
    data class Results(
        @SerializedName("chapter")
        val chapter: Chapter,
        @SerializedName("comic")
        val comic: Comic,
        @SerializedName("is_lock")
        val isLock: Boolean,
        @SerializedName("is_login")
        val isLogin: Boolean,
        @SerializedName("is_mobile_bind")
        val isMobileBind: Boolean,
        @SerializedName("is_vip")
        val isVip: Boolean,
        @SerializedName("show_app")
        val showApp: Boolean
    ) {
        data class Chapter(
            @SerializedName("comic_id")
            val comicId: String,
            @SerializedName("comic_path_word")
            val comicPathWord: String,
            @SerializedName("contents")
            val contents: List<Content>,
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
            @SerializedName("is_long")
            val isLong: Boolean,
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
            val uuid: String,
            @SerializedName("words")
            val words: List<Int>
        ) {
            data class Content(
                @SerializedName("url")
                val url: String,
                @SerializedName("uuid")
                val uuid: String
            )
        }

        data class Comic(
            @SerializedName("name")
            val name: String,
            @SerializedName("path_word")
            val pathWord: String,
            @SerializedName("restrict")
            val restrict: Restrict,
            @SerializedName("uuid")
            val uuid: String
        ) {
            data class Restrict(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )
        }
    }
}