package com.example.repository.model
import com.google.gson.annotations.SerializedName


/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2021/7/19
 * Time: 17:38
 */
data class KBCartoonInfo(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("results")
    val results: Results
) {
    data class Results(
        @SerializedName("comic")
        val comic: Comic,
        @SerializedName("groups")
        val groups: Groups,
        @SerializedName("is_lock")
        val isLock: Boolean,
        @SerializedName("is_login")
        val isLogin: Boolean,
        @SerializedName("is_mobile_bind")
        val isMobileBind: Boolean,
        @SerializedName("is_vip")
        val isVip: Boolean,
        @SerializedName("popular")
        val popular: Int
    ) {
        data class Comic(
            @SerializedName("alias")
            val alias: String,
            @SerializedName("author")
            val author: List<Author>,
            @SerializedName("brief")
            val brief: String,
            @SerializedName("clubs")
            val clubs: List<Any>,
            @SerializedName("cover")
            val cover: String,
            @SerializedName("datetime_updated")
            val datetimeUpdated: String,
            @SerializedName("females")
            val females: List<Any>,
            @SerializedName("free_type")
            val freeType: FreeType,
            @SerializedName("img_type")
            val imgType: Int,
            @SerializedName("last_chapter")
            val lastChapter: LastChapter,
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
            @SerializedName("reclass")
            val reclass: Reclass,
            @SerializedName("region")
            val region: Region,
            @SerializedName("restrict")
            val restrict: Restrict,
            @SerializedName("seo_baidu")
            val seoBaidu: String,
            @SerializedName("status")
            val status: Status,
            @SerializedName("theme")
            val theme: List<Theme>,
            @SerializedName("uuid")
            val uuid: String
        ) {
            data class Author(
                @SerializedName("name")
                val name: String,
                @SerializedName("path_word")
                val pathWord: String
            )

            data class FreeType(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )

            data class LastChapter(
                @SerializedName("name")
                val name: String,
                @SerializedName("uuid")
                val uuid: String
            )

            data class Reclass(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )

            data class Region(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )

            data class Restrict(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )

            data class Status(
                @SerializedName("display")
                val display: String,
                @SerializedName("value")
                val value: Int
            )

            data class Theme(
                @SerializedName("name")
                val name: String,
                @SerializedName("path_word")
                val pathWord: String
            )
        }

        data class Groups(
            @SerializedName("default")
            val default: Default
        ) {
            data class Default(
                @SerializedName("count")
                val count: Int,
                @SerializedName("name")
                val name: String,
                @SerializedName("path_word")
                val pathWord: String
            )
        }
    }
}