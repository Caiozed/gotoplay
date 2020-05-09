package com.caiozed.gotoplay.adapters

import com.caiozed.gotoplay.R
import android.app.ActivityOptions
import android.content.Intent
import android.util.Log.d
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsActivity
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.doAsyncMain
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_layout.view.*
import java.io.Serializable

class GameAdapter(private var games: MutableList<Game?>?) :
    RecyclerView.Adapter<GameAdapter.GameHolder>() {
    private var VIEW_TYPE_ITEM = 0
    private var VIEW_TYPE_LOADING = 1

    open class GameHolder(private val textView: View) : RecyclerView.ViewHolder(textView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapter.GameHolder {
        var root: View? = null
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
        if (holder is GameAdapter.DataViewHolder) {
            val game = this.games!![position]
            holder.itemView.game_text.background = null
            holder.itemView.game_text.text = game?.name
            var img = ImageView(holder.itemView.context)

            //Add image to grid
            doAsyncMain{
                Picasso.get()
                    .load("https://images.igdb.com/igdb/image/upload/t_720p/${game?.coverData?.image_id}.jpg")
                    //.placeholder(TextDrawable(game.name))
                    //.error(TextDrawable(game.name))
                    .into(img,
                        object: Callback {
                            override fun onSuccess() {
                                //set animations here
                                holder.itemView.game_text.text = "";
                                holder.itemView.game_text.background = img.drawable
                            }

                            override fun onError(e: Exception?) {
                                holder.itemView.game_text.background = null
                                d("Image Not Found", "Image for game ${game?.name} not found")
                            }
                        })
            }.execute()

            holder.itemView.setOnClickListener {
                var intent = Intent(holder.itemView.context, GameDetailsActivity::class.java)
                var containerStr = holder.itemView.context.getString(R.string.game_transition)

                var options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.instance,
                    Pair.create<View, String>(holder.itemView.game_container, containerStr))

                intent.putExtra("game", game as Serializable)
                MainActivity.instance.startActivity(intent, options.toBundle())
            };
        } else { //Do whatever you want. Or nothing !!
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = this.games?.size ?: 0

    public fun addItems(games: MutableList<Game>) {
        this.games?.addAll(games)
        MainActivity.instance.runOnUiThread {
            notifyDataSetChanged()
        }
    }

    public fun addNullData() {
        if(games?.size == 0 || games?.last() != null){
            games!!.add(null)
            MainActivity.instance.runOnUiThread {
                if(games?.size!! > 0) {
                    notifyItemInserted(games!!.size)
                }
            }
        }
    }

    public fun removeNull() {
        if(games?.size!! > 0 && games?.last() == null){
            games?.removeAt(games?.size!! - 1)

            MainActivity.instance.runOnUiThread {
                notifyItemRemoved(games!!.size)
            }
        }
    }
}