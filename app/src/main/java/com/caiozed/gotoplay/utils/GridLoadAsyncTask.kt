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
                                var backgroundFunc: () -> MutableList<Game>?): AsyncTask<String, String, String>() {
//    var dialog = openLoading(activity)
    private var games: MutableList<Game>? = mutableListOf()
    private var adapter: GameAdapter? = null
    override fun onPreExecute() {
        super.onPreExecute();
        adapter = GameAdapter(games as MutableList<Game?>)
        grid.layoutManager = layoutManager
        grid.adapter = adapter
//        dialog.show()
    }

    override fun doInBackground(vararg params: String?): String?{
        return run()
    }

    private fun run(): String?{
        runCallBack()

        var scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?
            ) {
                //Run request
                doAsyncSecondary {
                    runCallBack()
                }.execute()
            }
        }

        grid.addOnScrollListener(scrollListener)
        return  null
    }

    private fun runCallBack(){
        adapter!!.addNullData()

        games = backgroundFunc()


        if(games?.count()!! > 0){
            adapter!!.removeNull()
            adapter?.addItems(games!!)
        }
    }
}
