package com.caiozed.gotoplay.models

import java.io.Serializable

data class Game(var id: Long,
                var category: Int,
                var cover: Long,
                var created_at: Int,
                var name: String,
                var slug: String,
                var summary: String,
                var updated_at: Int,
                var url: String,
                var rating: Float) : Serializable {

    var coverData: Cover? = null
    var base64Image: String? = null
    var status: Int? = null
}