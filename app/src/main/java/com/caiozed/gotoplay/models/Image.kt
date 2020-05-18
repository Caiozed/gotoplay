package com.caiozed.gotoplay.models


import java.io.Serializable

data class Image (var id: Long,
                  var image_id: String,
                  var url: String): Serializable{
}