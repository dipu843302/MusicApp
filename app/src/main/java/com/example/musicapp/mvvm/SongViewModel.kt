package com.example.musicapp.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongViewModel(private val songRepo: SongRepo):ViewModel() {
    private val _songList = MutableStateFlow(listOf<SongModel>())
    val songList: StateFlow<List<SongModel>> get() = _songList

    init {
        viewModelScope.launch (Dispatchers.IO){
            songRepo.getSong()
           _songList.value= songRepo.songList.value
        }
    }


}