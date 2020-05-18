package com.caiozed.gotoplay.models

import java.io.Serializable

data class Company (var id: Long,
                    var name: String,
                    var logo: Image? = null) : Serializable