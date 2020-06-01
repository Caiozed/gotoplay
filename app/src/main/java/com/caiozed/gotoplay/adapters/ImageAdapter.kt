package com.caiozed.gotoplay.adapters

import android.app.ActionBar
import android.app.Activity
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
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.database.GamesDbHelper
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsActivity
import com.caiozed.gotoplay.mainactivitypkg.ImageViewActivity
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.mainactivitypkg.fragments.BacklogFragment
import com.caiozed.gotoplay.mainactivitypkg.fragments.HomeFragment
import com.caiozed.gotoplay.mainactivitypkg.fragments.PlayedFragment
import com.caiozed.gotoplay.mainactivitypkg.fragments.PlayingFragment
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.Image
import com.caiozed.gotoplay.utils.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.game_layout.view.*
import kotlinx.android.synthetic.main.image_layout.view.*
import java.io.Serializable
import android.R as AndroidR

class ImageAdapter(private var images: MutableList<Image?>?) :
    RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    private var VIEW_TYPE_ITEM = 0
    private var VIEW_TYPE_LOADING = 1
    public var view: View? = null
    public var viewParent: RecyclerView? = null

    open class ImageHolder(private val textView: View) : RecyclerView.ViewHolder(textView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ImageHolder {
        var root: View? = null
        viewParent = parent as RecyclerView

        return if (viewType == VIEW_TYPE_ITEM) {
            root = LayoutInflater.from(parent.context).inflate(R.layout.image_layout, parent, false) as View
            DataViewHolder(root)
        } else {
            root = LayoutInflater.from(parent.context).inflate(R.layout.progress_bar, parent, false) as View
            ProgressViewHolder(root)
        }

        return ImageHolder(root!!)
    }
    internal inner class DataViewHolder(itemView: View?) :
        GameAdapterViewHolder(itemView)

    internal inner class ProgressViewHolder(itemView: View?) :
        GameAdapterViewHolder(itemView)

    open inner class GameAdapterViewHolder(itemView: View?) :
        ImageAdapter.ImageHolder(itemView!!)

    override fun getItemViewType(position: Int): Int {
        return if (images!![position] != null) VIEW_TYPE_ITEM else VIEW_TYPE_LOADING
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        view = holder.itemView

        if (holder is ImageAdapter.DataViewHolder) {
            val image = this.images!![position]

            //Add image to grid
            if (image != null) {
                processImage(Game(0, "", cover = image), holder.itemView.game_image)
            }

            var imageView = view!!.findViewById<ImageView>(R.id.game_image)
            view!!.game_image?.setOnClickListener {
                var intent = Intent(view!!.context, ImageViewActivity::class.java)
                var containerStr = view!!.context.getString(R.string.screenshot_transition)

                var options = ActivityOptions.makeSceneTransitionAnimation(view!!.context as Activity,
                    Pair.create<View, String>(imageView, containerStr))

                intent.putExtra("image", image as Serializable)
                view!!.context.startActivity(intent, options.toBundle())
            }
        } else { //Do whatever you want. Or nothing !!
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = this.images?.size ?: 0

    public fun addItems(images: MutableList<Image>) {
        var oldSize  = this.images?.size ?: 0

        this.images?.addAll(images)
        notifyItemRangeInserted(oldSize,images.size)
    }

    public fun addNullData() {
        try{
            if(images?.size == 0 || images?.last() != null){
                images!!.add(null)
                if(images?.size!! > 0) {
                    notifyItemInserted(images!!.size)
                }
            }
        }
        catch (e: Exception) {
            d("Exception", e.toString())
        }
    }

    public fun removeNull() {
        try {
            if(images?.size!! > 0 && images?.last() == null){
                images?.removeAt(images?.size!! - 1)
                notifyItemRemoved(images?.size!! - 1)
            }
        }
        catch (e: Exception) {
            d("Exception", e.toString())
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