package com.example.musicapp.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SongViewModel(private val songRepo: SongRepo):ViewModel() {

    init {
        viewModelScope.launch (Dispatchers.IO){
            songRepo.getSong()

        }
    }

    fun get()=songRepo.songList

}