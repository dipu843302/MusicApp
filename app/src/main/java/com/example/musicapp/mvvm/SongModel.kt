package com.example.musicapp.mvvm

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.Duration

@Parcelize
data class SongModel
    (
    var song_id: Long,
    var song_title: String,
    var song_data: String,
    var image: Uri,
    var date: Long,
    var song_size:Int,
    var artist_name:String,
    var duration:Int
):Parcelable
