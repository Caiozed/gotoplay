package com.caiozed.gotoplay.mainactivitypkg

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.BR

class GameDetailsViewModel(view: GameDetailsActivity) : BaseObservable() {

    @get:Bindable
    var game: Game? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.game)
        }

}

