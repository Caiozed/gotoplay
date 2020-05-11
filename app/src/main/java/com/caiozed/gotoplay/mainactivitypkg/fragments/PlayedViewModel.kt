package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.database.GamesDbHelper
import com.caiozed.gotoplay.databinding.BacklogLayoutBinding
import com.caiozed.gotoplay.databinding.PlayedLayoutBinding
import com.caiozed.gotoplay.databinding.PlayingLayoutBinding
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.GameStatus
import com.caiozed.gotoplay.utils.GridLoadAsyncTask

class PlayedViewModel(var view: PlayedLayoutBinding){
    var page: Int = 0

    fun searchDatabase(): MutableList<Game>{
        var gamesDbHelper = GamesDbHelper(view.root.context)
        var games = gamesDbHelper.findGames(GameStatus.Played.value, gamesDbHelper.readableDatabase, page)
        page++
        return games
    }

    fun startSearch() {
        var grid = view.root!!.findViewById<RecyclerView>(R.id.played_grid);

        GridLoadAsyncTask(grid, GridLayoutManager(view.root!!.context, 2)
        ) { return@GridLoadAsyncTask searchDatabase() }.execute()
    }
}

