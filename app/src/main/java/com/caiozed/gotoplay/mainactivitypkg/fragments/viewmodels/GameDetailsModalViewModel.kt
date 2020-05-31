package com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels


import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.LinearLayoutManager
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.adapters.ImageAdapter
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsViewModel
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.Image
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.convertTimestampToString
import com.caiozed.gotoplay.utils.doAsyncSecondary
import kotlinx.android.synthetic.main.game_details_layout.*
import kotlinx.android.synthetic.main.game_details_layout.view.*
import kotlinx.android.synthetic.main.game_details_modal.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*
import kotlinx.android.synthetic.main.tag_layout.view.*


class GameDetailsModalViewModel(var view: View, var parentViewModel: GameDetailsViewModel) : BaseObservable() {
    @get:Bindable
    var game: Game? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.game)
        }



}
