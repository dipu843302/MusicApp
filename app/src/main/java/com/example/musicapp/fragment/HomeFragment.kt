package com.example.musicapp.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.mvvm.SongModel
import com.example.musicapp.mvvm.SongRepo
import com.example.musicapp.mvvm.SongViewModel
import com.example.musicapp.mvvm.SongViewModelFactory
import com.example.musicapp.ui.theme.MusicAppTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var viewModel: SongViewModel
    private lateinit var repository: SongRepo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        repository = SongRepo(requireContext())
        val viewModelFactory = SongViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SongViewModel::class.java]

        return ComposeView(requireContext()).apply {
            setContent {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ExternalStoragePermission()
                }
            }
        }
    }

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    fun setRecyclerView() {
        val songList = viewModel.songList.value

        LazyColumn {
            items(songList.size) {
                DataLayout(songList[it])
                Log.d("list",songList[it].toString())
            }
        }
    }

    @Composable
    fun DataLayout(songModel: SongModel) {

        Surface(
            modifier = Modifier
                .background(Color.White)
                .padding(10.dp)
                .fillMaxSize()
                .clickable {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToSongPlayFragment(songModel)
                    findNavController().navigate(action)
                }
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Card(
                    shape = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp),
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = "",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(.9f),
                    horizontalAlignment = Alignment.Start
                )
                {
                    Text(
                        text = "${songModel.song_title}",
                        fontSize = 15.sp,
                        color = Color.Black,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    var size = songModel.song_size / 1024
                    var songUnit = "Kb"
                    if (size >= 1024) {
                        size /= 1024
                        songUnit = "Mb"
                    }

                    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy");
                    val dateTime = simpleDateFormat.format(songModel.date).toString();
                  var duration=  formatTime(songModel.duration)
                    Text(
                        text = "$size $songUnit  $duration",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
    @Composable
    fun formatTime(t: Int) : String{
        var hours = t / 3600
        var minutes = (t % 3600) / 60
        var seconds = t % 60

        return "$hours:$minutes:$seconds"
    }

    @SuppressLint("PermissionLaunchedDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun ExternalStoragePermission() {
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner, effect = {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        permissionState.launchPermissionRequest()
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        })

        when {
            permissionState.hasPermission -> {
                setRecyclerView()
            }
            permissionState.shouldShowRationale -> {
                Column {
                    Text(text = "Reading external permission is required by this app")
                }
            }
            !permissionState.hasPermission && !permissionState.shouldShowRationale -> {
                Text(text = "Permission fully denied. Go to settings to enable")
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MusicAppTheme {
            //DataLayout()
        }
    }


}