package com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.database.GamesDbHelper
import com.caiozed.gotoplay.databinding.BacklogLayoutBinding
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.GameStatus
import com.caiozed.gotoplay.utils.GridLoadAsyncTask
import com.caiozed.gotoplay.utils.doAsyncMain

class BacklogViewModel(var view: BacklogLayoutBinding): BaseObservable(){
    var page: Int = 0
    @get:Bindable
    var searchString: String = ""
        set (value){
            field = value
            notifyPropertyChanged(BR.searchString)
            startSearch()
        }

    fun searchDatabase(): MutableList<Game>{
        var gamesDbHelper = GamesDbHelper(view.root.context)
        var games = gamesDbHelper.findGames(GameStatus.Backlog.value, gamesDbHelper.readableDatabase,  page, searchString)
        page++
        return games
    }

    fun startSearch(name: String = "") {
        var grid = view.root!!.findViewById<RecyclerView>(R.id.backlog_grid);
        page = 0

        GridLoadAsyncTask(grid, GridLayoutManager(view.root!!.context, 2)
        ) { return@GridLoadAsyncTask searchDatabase() }
    }
}

