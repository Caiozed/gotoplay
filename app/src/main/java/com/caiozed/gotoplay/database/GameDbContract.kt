package com.caiozed.gotoplay.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.caiozed.gotoplay.models.Game
import com.caiozed.gotoplay.models.GameDbModel

object GameDbContract {
    object GameEntry : BaseColumns {
        const val TABLE_NAME = "games"
        const val NAME = "name"
        const val IMAGE = "image"
        const val STATUS = "status"
    }

    const val SQL_CREATE_ENTRIES =
        """CREATE TABLE ${GameEntry.TABLE_NAME} 
                (${BaseColumns._ID} INTEGER PRIMARY KEY,
                ${GameEntry.IMAGE} TEXT,                
                ${GameEntry.NAME} TEXT,
                ${GameEntry.STATUS} INTEGER);
                """

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${GameEntry.TABLE_NAME}"
}

class GamesDbHelper(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(GameDbContract.SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(GameDbContract.SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Games.db"
    }

    fun upsert(gameDB: Game, db: SQLiteDatabase){
        db?.execSQL("INSERT OR REPLACE INTO ${GameDbContract.GameEntry.TABLE_NAME} "+
                "(${BaseColumns._ID}, ${GameDbContract.GameEntry.IMAGE}, ${GameDbContract.GameEntry.NAME}, ${GameDbContract.GameEntry.STATUS}) "+
                "VALUES(${gameDB.id}, \"${gameDB.base64Image}\", \"${gameDB.name}\", ${gameDB.status})")
    }

    fun delete(gameDB: Game, db: SQLiteDatabase){
        db?.delete(GameDbContract.GameEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf("${gameDB.id}"))
    }

    fun findGames(status: Int, db: SQLiteDatabase, page: Int, name: String): MutableList<Game>{
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            GameDbContract.GameEntry.IMAGE,
            GameDbContract.GameEntry.STATUS,
            BaseColumns._ID,
            GameDbContract.GameEntry.NAME)

        // Filter results WHERE "title" = 'My Title'
        var selection = "${GameDbContract.GameEntry.STATUS} = ?"
        val selectionArgs = mutableListOf<String>("$status")
        if(!name.isNullOrEmpty()){
            selection = "$selection AND ${GameDbContract.GameEntry.NAME} LIKE ?"
            selectionArgs.add("%${name}%")
        }

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${BaseColumns._ID} DESC "

        val cursor = db.query(
            GameDbContract.GameEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs.toTypedArray(),          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder,
            "${page * 10},10"// The sort order
        )

        val games = mutableListOf<Game>()
        with(cursor) {
            while (moveToNext()) {
                val image = getString(getColumnIndexOrThrow(GameDbContract.GameEntry.IMAGE))
                val status = getInt(getColumnIndexOrThrow(GameDbContract.GameEntry.STATUS))
                val id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val name = getString(getColumnIndexOrThrow(GameDbContract.GameEntry.NAME))
                var game = Game(id = id, name = name)
                game.base64Image = image
                game.status = status
                games.add(game)
            }
        }

        return games
    }
}
