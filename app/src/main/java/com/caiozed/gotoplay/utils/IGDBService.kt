package com.caiozed.gotoplay.utils

import com.caiozed.gotoplay.models.Game
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

class IGDBService {
    companion object {
        lateinit var instance: IGDBService

        var userKey = ""
        var games = "https://api-v3.igdb.com/games/"
        var covers = "https://api-v3.igdb.com/covers/"
        var search = "https://api-v3.igdb.com/search/"

         fun getGames(query: String): MutableList<Game>{
            val(_, _, result ) = Fuel.post(games)
                .header("user-key", userKey)
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
    }

    init {
        instance = this
    }


}