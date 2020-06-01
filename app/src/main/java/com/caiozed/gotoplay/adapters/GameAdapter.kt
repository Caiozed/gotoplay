package com.caiozed.gotoplay.adapters

import android.app.ActionBar
import android.app.ActivityOptions
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log.d
import android.util.Pair
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.database.GamesDbHelper
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsActivity
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.mainactivitypkg.fragments.*
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.game_layout.view.*
import java.io.Serializable
import android.R as AndroidR

class GameAdapter(private var games: MutableList<Game?>?) :
    RecyclerView.Adapter<GameAdapter.GameHolder>() {
    private var VIEW_TYPE_ITEM = 0
    private var VIEW_TYPE_LOADING = 1
    public var view: View? = null
    public var viewParent: RecyclerView? = null

    open class GameHolder(private val textView: View) : RecyclerView.ViewHolder(textView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapter.GameHolder {
        var root: View? = null
        viewParent = parent as RecyclerView

        return if (viewType == VIEW_TYPE_ITEM) {
            root = LayoutInflater.from(parent.context).inflate(R.layout.game_layout, parent, false) as View
            DataViewHolder(root)
        } else {
            root = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false) as View
            ProgressViewHolder(root)
        }
        return GameHolder(root!!)
    }
    internal inner class DataViewHolder(itemView: View?) :
        GameAdapterViewHolder(itemView)

    internal inner class ProgressViewHolder(itemView: View?) :
        GameAdapterViewHolder(itemView)

    open inner class GameAdapterViewHolder(itemView: View?) :
        GameAdapter.GameHolder(itemView!!)

    override fun getItemViewType(position: Int): Int {
        return if (games!![position] != null) VIEW_TYPE_ITEM else VIEW_TYPE_LOADING
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: GameHolder, position: Int) {
        view = holder.itemView

        if (holder is GameAdapter.DataViewHolder) {
            val game = this.games!![position]
            holder.itemView.game_text.background = null
            holder.itemView.game_text.text = game?.name

            //Add image to grid
            if (game != null) {
                processImage(game, holder.itemView.game_text)
            }

            if (game != null) {
                setClickEvents(holder.itemView, game)
            }

        } else { //Do whatever you want. Or nothing !!
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = this.games?.size ?: 0

    public fun addItems(games: MutableList<Game>) {
        var oldSize  = this.games?.size ?: 0

        this.games?.addAll(games)
        notifyItemRangeInserted(oldSize,games.size)
    }

    public fun addNullData() {
        try{
            if(games?.size == 0 || games?.last() != null){
                games!!.add(null)
                if(games?.size!! > 0) {
                    notifyItemInserted(games!!.size)
                }
            }
        }
        catch (e: Exception) {
            d("Exception", e.toString())
        }
    }

    public fun removeNull() {
        try {
            if(games?.size!! > 0 && games?.last() == null){
                games?.removeAt(games?.size!! - 1)
                notifyItemRemoved(games?.size!! - 1)
            }
        }
        catch (e: Exception) {
            d("Exception", e.toString())
        }
    }

    private fun removeItem (view: View, game: Game){

        val anim: Animation = AnimationUtils.loadAnimation(
            view.context,
            AndroidR.anim.fade_out
        )
        anim.duration = 300
        view.startAnimation(anim)

        view.postDelayed(Runnable {
            var position = this.games!!.indexOf(game)
            games?.remove(game)
            notifyItemRemoved(position) //Refresh list
        }, anim.duration)
    }


    private fun setClickEvents(view: View, game: Game){
        viewParent!!.setHasFixedSize(true);//Fix for data not updating
        var container = view.findViewById<CardView>(R.id.game_container)

        //Add click event to image container
        view.game_text.setOnClickListener {
            var intent = Intent(view.context, GameDetailsActivity::class.java)
            var containerStr = view.context.getString(R.string.game_transition)

            var options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.instance,
                Pair.create<View, String>(container, containerStr))

            intent.putExtra("game", game as Serializable)
            view.context.startActivity(intent, options.toBundle())
        };

        when(MainActivity.instance.currentFragment){
            is HomeFragment,
            is SearchFragment -> {
                viewParent!!.setHasFixedSize(false);//Remove Fix for horizontal lists
                view.fab_add_to_backlog.setOnClickListener{
                    insertAnimation(R.id.navigation_backlog)
                    updateGame(game, view, GameStatus.Backlog)
                }

                view.fab_add_to_play.setOnClickListener{
                    insertAnimation(R.id.navigation_playing)
                    updateGame(game, view, GameStatus.Playing)
                }

                view.fab_add_to_played.setOnClickListener{
                    insertAnimation(R.id.navigation_played)
                    updateGame(game, view, GameStatus.Played)
                }
            }

            is BacklogFragment -> {
                view.fab_add_to_backlog.setImageDrawable(view.context.getDrawable(R.drawable.ic_delete))
                view.fab_add_to_backlog.setOnClickListener{
                    deleteGame(game, view)
                }

                view.fab_add_to_play.setOnClickListener{
                    insertAnimation(R.id.navigation_playing)
                    updateGame(game, view, GameStatus.Playing)
                    removeItem(view, game)
                }

                view.fab_add_to_played.setOnClickListener{
                    insertAnimation(R.id.navigation_played)
                    updateGame(game, view, GameStatus.Played)
                    removeItem(view, game)
                }
            }

            is PlayingFragment -> {
                view.fab_add_to_play.setImageDrawable(view.context.getDrawable(R.drawable.ic_delete))
                view.fab_add_to_play.setOnClickListener{
                    deleteGame(game, view)
                }

                view.fab_add_to_backlog.setOnClickListener{
                    insertAnimation(R.id.navigation_backlog)
                    updateGame(game, view, GameStatus.Backlog)
                    removeItem(view, game)
                }

                view.fab_add_to_played.setOnClickListener{
                    insertAnimation(R.id.navigation_played)
                    updateGame(game, view, GameStatus.Played)
                    removeItem(view, game)
                }
            }

            is PlayedFragment -> {
                view.fab_add_to_played.setImageDrawable(view.context.getDrawable(R.drawable.ic_delete))
                view.fab_add_to_played.setOnClickListener{
                    deleteGame(game, view)
                }

                view.fab_add_to_backlog.setOnClickListener{
                    insertAnimation(R.id.navigation_backlog)
                    updateGame(game, view, GameStatus.Backlog)
                    removeItem(view, game)
                }

                view.fab_add_to_play.setOnClickListener{
                    insertAnimation(R.id.navigation_playing)
                    updateGame(game, view, GameStatus.Playing)
                    removeItem(view, game)
                }
            }
        }
    }

    private fun deleteGame(game: Game?, view: View){
        if (game != null) {
            var dbContext = GamesDbHelper(view.context)
            dbContext.delete(game, dbContext.writableDatabase)
            removeItem(view, game)
        }
    }

    private fun updateGame(game: Game?, view: View, status: GameStatus){
        if (game != null) {
            game.status = status.value
            var dbContext = GamesDbHelper(view.context)
            dbContext.upsert(game, dbContext.writableDatabase)
        }
    }

    private fun insertAnimation(resourceId : Int){
        var view = MainActivity.instance.findViewById<View>(resourceId)

        val anim: Animation = AnimationUtils.loadAnimation(
            view.context,
            R.anim.bounce
        )
        view.startAnimation(anim)
    }
}