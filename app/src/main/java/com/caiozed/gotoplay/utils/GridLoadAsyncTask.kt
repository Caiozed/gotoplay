package com.caiozed.gotoplay.utils

import android.app.Activity
import android.os.AsyncTask
import android.widget.BaseAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.adapters.GameAdapter
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.models.Game
import kotlinx.coroutines.runBlocking


public class GridLoadAsyncTask (var grid: RecyclerView,
                                var layoutManager: RecyclerView.LayoutManager,
                                var backgroundFunc: () -> MutableList<Game>?) {
//    var dialog = openLoading(activity)
    private var games: MutableList<Game>? = mutableListOf()
    private var adapter: GameAdapter? = null

    init{
        initialize()
    }

    fun initialize() {
        adapter = GameAdapter(games as MutableList<Game?>)
        grid.layoutManager = layoutManager
        grid.swapAdapter(adapter, true)
        adapter!!.addNullData()

        var scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                var games: MutableList<Game>? = null
                doAsyncSecondary({
                    games = backgroundFunc()
                },{
                    adapter!!.addNullData()
                },{
                    adapter!!.removeNull()
                    if(games?.count()!! > 0){
                        adapter?.addItems(games!!)
                    }
                }).execute()

            }
        }

        grid.addOnScrollListener(scrollListener)
//        dialog.show()
    }
}
