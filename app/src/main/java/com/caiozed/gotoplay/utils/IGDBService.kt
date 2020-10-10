package com.caiozed.gotoplay.utils

import com.caiozed.gotoplay.models.ApiAuthorization
import com.caiozed.gotoplay.models.Game
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Headers
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

class IGDBService {
    companion object {
        lateinit var instance: IGDBService

        var userID = "6g5h5upbxikf5qnwud8wycatn5t4id"
        var token = ""
        var games = "https://api.igdb.com/v4/games/"
        var autorize = "https://id.twitch.tv/oauth2/token?client_id=${userID}&client_secret=07fg4jdvhtlole2p8jwlb9jeu210by&grant_type=client_credentials"

         fun getGames(query: String): MutableList<Game>{
            val(_, _, result ) = Fuel.post(games)
                .header("Client-ID", userID)
                .authentication()
                .bearer(token)
                .body(query)
                .responseObject<Array<Game>>()

            return when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                    mutableListOf()
                }
                is Result.Success -> {
                    val data = result.component1()
                    data!!.toMutableList()
                }
            }
        }

        fun validate(): ApiAuthorization {
            val(_, _, result ) = Fuel.post(autorize)
                .responseObject<ApiAuthorization>()

            return when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                    ApiAuthorization("", 0)
                }
                is Result.Success -> {
                    val data = result.component1()
                    data!!
                }
            }
        }
    }

    init {
        instance = this
    }


}