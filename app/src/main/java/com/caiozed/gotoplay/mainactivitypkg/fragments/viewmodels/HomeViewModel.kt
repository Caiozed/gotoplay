package com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.databinding.HomeFragmentBinding
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.mainactivitypkg.fragments.ApiKeyFragment
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.GridLoadAsyncTask
import com.caiozed.gotoplay.utils.IGDBService


class HomeViewModel(
    var homeFragment: HomeFragmentBinding
) : BaseObservable() {

    var popularPage: Int = 0
    var upComingPage: Int = 0
    var latestPage: Int = 0

    @get:Bindable
    var games: MutableList<Game>? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.games)
        }


    private fun searchForTopRatedGames(): MutableList<Game>?{
        games = IGDBService.getGames(
            """fields name, id, cover.url, cover.image_id, rating;
                where rating != null;
                sort rating desc;
                limit 10;
                offset ${popularPage * 10};"""
        )

        popularPage++
        return games
    }


    private fun searchForUpcomingReleases(): MutableList<Game>?{
        games = IGDBService.getGames(
            """fields name, id, cover.url, cover.image_id, rating;
                where first_release_date >= ${  System.currentTimeMillis() / 1000 };
                sort first_release_date desc;
                limit 10;
                offset ${upComingPage * 10};
                """
        )

        upComingPage++
        return games
    }


    private fun searchForLatestReleases(): MutableList<Game>?{
        games = IGDBService.getGames(
            """fields name, id, cover.url, cover.image_id, rating;
                where first_release_date <= ${  System.currentTimeMillis() / 1000 };
                sort first_release_date desc;
                limit 10;
                offset ${latestPage * 10};
                """
        )

        latestPage++
        return games
    }

    fun startSearch(context: Context) {
        if(IGDBService.token.isNullOrEmpty()){
            var dialog = ApiKeyFragment();
            dialog.positiveAction = {
                search(context)
            }
            MainActivity.instance.openDialog(dialog,{}, cancelable = false);
        }else{
            search(context)
        }
    }

    private fun search(context: Context){
        var gridPopular = homeFragment.root!!.findViewById<RecyclerView>(R.id.popularGridView);
        var gridUpcoming = homeFragment.root!!.findViewById<RecyclerView>(R.id.upcomingReleasesGridView);
        var gridLatest = homeFragment.root!!.findViewById<RecyclerView>(R.id.latestReleasesGridView);

        GridLoadAsyncTask(gridPopular, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        ) { return@GridLoadAsyncTask searchForTopRatedGames() }

        GridLoadAsyncTask(gridUpcoming, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        ) { return@GridLoadAsyncTask searchForUpcomingReleases() }

        GridLoadAsyncTask(gridLatest, LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL ,false)
        ) { return@GridLoadAsyncTask searchForLatestReleases() }
    }

    fun validate(){
        IGDBService.validate();
    }
}

