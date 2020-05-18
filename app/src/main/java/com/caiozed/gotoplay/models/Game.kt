package com.caiozed.gotoplay.models

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.time.format.DateTimeFormatter
import java.util.*

data class Game(var id: Long,
                var name: String,
                var category: Int = 0,
                var created_at: Int = 0,
                var slug: String = "",
                var summary: String = "",
                var updated_at: Int = 0,
                var url: String = "",
                var rating: Float = 0f,
                var first_release_date: Long = 0,
                var storyline: String = "",
                var involved_companies: Array<Company>? = null,
                var genres: Array<Genre>? = null,
                var platforms: Array<Platform>? = null,
                var screenshots: Array<Image>? = null,
                var videos: Array<Video>? = null,
                var cover: Image? = null
                ) : Serializable {

    var base64Image: String? = null
    var status: Int? = null

    var releaseDate: String? = ""
}