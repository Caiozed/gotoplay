package com.caiozed.gotoplay.mainactivitypkg.fragments

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.caiozed.gotoplay.R
import com.caiozed.gotoplay.mainactivitypkg.MainActivity
import com.caiozed.gotoplay.models.ApiAuthorization
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.doAsyncSecondary
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.api_key_fragment.view.*
import java.io.File

class ApiKeyFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var _view: View = activity!!.layoutInflater.inflate(R.layout.api_key_fragment, null)

        var alert = AlertDialog.Builder(activity!!)
        alert.setView(_view)
        _view.findViewById<ProgressBar>(R.id.progress_bar_indefinite).visibility = View.INVISIBLE;
        _view.findViewById<FloatingActionButton>(R.id.fab_api_ok).visibility = View.INVISIBLE;

        //Test token validity
        validateToken(_view)

        _view.fab_api_ok.setOnClickListener {
            validateToken(_view)
        }

        return alert.create()
    }

    fun validateToken(_view: View){
        var games: MutableList<Game> = mutableListOf()
        var progressBar = _view.findViewById<ProgressBar>(R.id.progress_bar_indefinite)
        var retryBtn = _view.findViewById<FloatingActionButton>(R.id.fab_api_ok)

        doAsyncSecondary({
            games = IGDBService.getGames("fields id; where id = 1; limit 1;")
        }, {
            progressBar.visibility = View.VISIBLE;
            retryBtn.visibility = View.INVISIBLE
        }, {
            progressBar.visibility = View.INVISIBLE;
            if(games.count() > 0){
                var created = File(MainActivity.instance.configPath).createNewFile()
                if(created){
                    File(MainActivity.instance.configPath).writeText(IGDBService.token)
                }
                dismiss()
            }else{
                var authorization = ApiAuthorization("", 0)
                doAsyncSecondary({
                    authorization = IGDBService.validate();
                }, {}, {
                    File(MainActivity.instance.configPath).writeText(authorization.access_token)
                    IGDBService.token = authorization.access_token
                    if(authorization.access_token.isNullOrEmpty()){
                        retryBtn.visibility = View.VISIBLE
                        Toast.makeText(_view.context, "Something went wrong while validating the key! :(", Toast.LENGTH_LONG).show()
                    }else{
                        dismiss()
                    }
                }).execute()
            }
        }).execute()
    }
}
