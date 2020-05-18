package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.os.AsyncTask
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.SearchFragmentBinding
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.GridLoadAsyncTask
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.doAsyncMain

class SearchViewModel(var searchFragment: SearchFragmentBinding) : BaseObservable() {
    private var currentRequest: AsyncTask<Void,Void,String>? = null
    var page: Int = 0

    @get:Bindable
    var searchString: String? = null
    set (value){
        field = value
        notifyPropertyChanged(BR.searchString)
        if(value?.count()!! > 3){
            currentRequest?.cancel(true)

            currentRequest = doAsyncMain{
                searchGames()
                currentRequest = null;
            }.execute()
        }
    }

    private fun search(): MutableList<Game>? {
        var games: MutableList<Game>? = null
            games = IGDBService.getGames(
                """fields name, id, cover.url, cover.image_id, rating; search "$searchString"; limit 10; offset ${page * 10};"""
            )
        page++
        return games
    }

    private fun searchGames() {
        page = 0
        var layoutManager = LinearLayoutManager(searchFragment.root.context, LinearLayoutManager.HORIZONTAL ,false)
        var grid = searchFragment.root!!.findViewById<RecyclerView>(R.id.resultsGridView);

        GridLoadAsyncTask(
            grid, layoutManager
        ) { return@GridLoadAsyncTask search() }
    }
}
