package com.caiozed.gotoplay.utils

import com.caiozed.gotoplay.models.Cover
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.GameSearch
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result;

class IGDBService {
    companion object {
        lateinit var instance: IGDBService

        var userKey = "319ac2d1e830a8bb29d2a449a4a141d8"
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

         fun getCovers(coverIds: List<Int>?): MutableList<Cover>{
            val(_, _, result ) = Fuel.post(covers)
                .header("user-key", userKey)
                .body("fields *; where id = (${coverIds?.joinToString(",")});"
                )
                .responseObject<Array<Cover>>()

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

        /// Get covers for selected games if exists
         fun findCovers(games: MutableList<Game>):  MutableList<Game>{
            var covers = getCovers(games?.map { it.cover })
            covers.forEach { it ->
                var gameId = it.game
                var game = games?.filter { it.id == gameId }?.get(0)
                if (game != null) {
                    game!!.coverData = it
                }
            }
            return games
        }
    }

    init {
        instance = this
    }


}