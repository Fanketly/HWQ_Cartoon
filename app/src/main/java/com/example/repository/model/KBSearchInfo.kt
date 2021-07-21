package com.example.repository.model


import com.google.gson.annotations.SerializedName

data class KBSearchInfo(
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
        val list: List<ListDTO>,
        @SerializedName("offset")
        val offset: Int,
        @SerializedName("total")
        val total: Int
    ) {
        data class ListDTO(
            @SerializedName("alias")
            val alias: String,
            @SerializedName("author")
            val author: List<Author>,
            @SerializedName("cover")
            val cover: String,
            @SerializedName("females")
            val females: List<Any>,
            @SerializedName("males")
            val males: List<Any>,
            @SerializedName("name")
            val name: String,
            @SerializedName("parodies")
            val parodies: List<Any>,
            @SerializedName("path_word")
            val pathWord: String,
            @SerializedName("popular")
            val popular: Int,
            @SerializedName("theme")
            val theme: List<Any>
        ) {
            data class Author(
                @SerializedName("alias")
                val alias: Any,
                @SerializedName("name")
                val name: String,
                @SerializedName("path_word")
                val pathWord: String
            )
        }
    }
}