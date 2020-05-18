package com.caiozed.gotoplay.models

import java.io.Serializable

data class Platform (var id: Long,
                     var abbreviation: String,
                     var platform_logo: Image? = null) :Serializable