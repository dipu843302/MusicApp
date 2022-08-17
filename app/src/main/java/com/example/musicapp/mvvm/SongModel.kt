package com.example.musicapp.mvvm

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongModel
    (
    var song_id: Long,
    var song_title: String,
    var song_data: String,
    var image: Uri,
    var date: Long,
    var song_size:String,
    var artist_name:String
):Parcelable
