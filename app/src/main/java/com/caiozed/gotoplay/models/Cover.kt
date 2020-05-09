package com.caiozed.gotoplay.models


import java.io.Serializable

data class Cover (var game: Int,
                  var height: Int,
                  var image_id: String,
                  var url: String,
                  var width: Int): Serializable{
}