package com.example.musicapp.mvvm

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SongRepo(var context: Context) {

    private val _songList = MutableStateFlow(listOf<SongModel>())
    val songList: StateFlow<List<SongModel>> get() = _songList
    var list = mutableListOf<SongModel>()

    suspend fun getSong(){
        val contentResolver: ContentResolver = context.contentResolver
        var songUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        var cursor: Cursor? = contentResolver.query(songUri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {

            val songId = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val songTittle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songData = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val date = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED)
            val albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val songSize = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
            val artistName = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val currentId = cursor.getLong(songId)
                val song_tittle = cursor.getString(songTittle)
                val song_data = cursor.getString(songData)
                val song_date = cursor.getLong(date)
                val albumId = cursor.getLong(albumColumn)
                val song_size=cursor.getInt(songSize)
                val duration=cursor.getInt(duration)
                val artist_name=cursor.getString(artistName)

                val Image_Uri = Uri.parse("content://media/external/audio/albumart")
                val album_uri = ContentUris.withAppendedId(Image_Uri, albumId)

                if (!songTittle.equals("<unknown>")) {
                    list.add(SongModel(currentId, song_tittle, song_data, album_uri, song_date,song_size,artist_name,duration))
                }
            }
        }
        _songList.emit(list)
    }
}