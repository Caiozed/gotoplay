package com.caiozed.gotoplay.models

import java.io.Serializable

data class ApiAuthorization (var access_token: String,
                             var expires_in: Long):Serializable