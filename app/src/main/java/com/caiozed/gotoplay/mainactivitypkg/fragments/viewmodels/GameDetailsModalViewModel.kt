package com.caiozed.gotoplay.mainactivitypkg.fragments.viewmodels


import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.caiozed.gotoplay.BR
import com.caiozed.gotoplay.mainactivitypkg.GameDetailsViewModel
import com.caiozed.gotoplay.models.Game


class GameDetailsModalViewModel(var view: View, var parentViewModel: GameDetailsViewModel) : BaseObservable() {
    @get:Bindable
    var game: Game? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.game)
        }



}
