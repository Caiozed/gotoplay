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
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.utils.IGDBService
import com.caiozed.gotoplay.utils.doAsyncSecondary
import kotlinx.android.synthetic.main.api_key_fragment.view.*
import java.io.File

class ApiKeyFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var _view: View = activity!!.layoutInflater.inflate(R.layout.api_key_fragment, null)

        var alert = AlertDialog.Builder(activity!!)
        alert.setView(_view)
        _view.findViewById<ProgressBar>(R.id.progress_bar_indefinite).visibility = View.INVISIBLE;

        _view.signup.setOnClickListener {
            val uris = Uri.parse("https://api.igdb.com/signup")
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window", true)
            intents.putExtras(b)
            context?.startActivity(intents)
        }
        _view.fab_api_ok.setOnClickListener {
            var games: MutableList<Game> = mutableListOf()
            IGDBService.userKey = _view.api_text.text.trim().toString()
            doAsyncSecondary({
                games = IGDBService.getGames("fields id; where id = 1; limit 1;")
            }, {
                _view.findViewById<ProgressBar>(R.id.progress_bar_indefinite).visibility = View.VISIBLE;
            }, {
                _view.findViewById<ProgressBar>(R.id.progress_bar_indefinite).visibility = View.INVISIBLE;
                if(games.count() > 0){
                    dismiss()
                    var created = File(MainActivity.instance.configPath).createNewFile()
                    if(created){
                        File(MainActivity.instance.configPath).writeText(IGDBService.userKey)
                    }
                }else{
                    Toast.makeText(_view.context, "Something went wrong while validating the key! :(", Toast.LENGTH_LONG).show()
                }
            }).execute()
        }
        return alert.create()
    }

}
