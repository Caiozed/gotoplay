package com.caiozed.gotoplay.utils

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper


class doAsyncMain(val handler: () -> Unit) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        Handler(Looper.getMainLooper()).post(Runnable {
            handler()
        })
        return null;
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}

class doAsyncSecondary(val handler: () -> Unit) : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        handler()
        return null;
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ...
    }
}