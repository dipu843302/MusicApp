package com.example.musicapp.fragment

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.musicapp.R
import com.example.musicapp.mvvm.SongModel
import com.example.musicapp.ui.theme.BackgroundOne
import com.example.musicapp.ui.theme.BackgroundThree
import com.example.musicapp.ui.theme.BackgroundTwo
import com.example.musicapp.ui.theme.MusicAppTheme
import java.time.Duration

class SongPlayFragment : Fragment() {

    private val args: SongPlayFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val song = args.song

        return ComposeView(requireContext()).apply {
            setContent {
                PlayerScreen(song)
            }
        }
    }

    @Composable
    fun TopAppBar() {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                findNavController().navigate(R.id.action_songPlayFragment_to_homeFragment)
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Arrow",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))
//
//        IconButton(onClick = { /*TODO*/ }) {
//            Icon(
//                imageVector = Icons.Default.PlaylistAdd,
//                contentDescription = "Add list",
//                tint = Color.White
//            )
//        }

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = Color.White
                )
            }
        }
    }

    @Composable
    fun SongDescription(songModel: SongModel) {
        Text(
            text = songModel.song_title,
            style = MaterialTheme.typography.h5,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = songModel.artist_name,
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                color = Color.White
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun PlayerSlider(ofHours: Duration?) {
        if (ofHours != null) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = 0f,
                    onValueChange = {},
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "0s", color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${ofHours.seconds}s", color = Color.White)
                }
            }
        }
    }

    @Composable
    fun PlayerButtons(songModel: SongModel,
        modifier: Modifier = Modifier,
        playerButtonSize: Dp = 72.dp,
        sideButtonSize: Dp = 42.dp
    )
    {
        var mMediaPlayer: MediaPlayer? = null

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val buttonModifier = Modifier
                .size(sideButtonSize)
                .semantics { role = Role.Button }

            Image(
                imageVector = Icons.Filled.SkipPrevious,
                contentDescription = "Skip Previous",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = buttonModifier
            )

//            Image(
//                imageVector = Icons.Filled.Replay10,
//                contentDescription = "Reply 10 Second",
//                contentScale = ContentScale.Fit,
//                colorFilter = ColorFilter.tint(Color.White),
//                modifier = buttonModifier
//            )

            Image(
                imageVector = Icons.Filled.PlayCircleFilled,
                contentDescription = "Play",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(playerButtonSize)
                    .semantics { role = Role.Button }
                    .clickable {

                        val songPlay = Uri.parse(songModel.song_data)

                        if (mMediaPlayer == null) {
                            mMediaPlayer = MediaPlayer.create(requireContext(), songPlay)
                            mMediaPlayer!!.isLooping = true
                            mMediaPlayer!!.start()
                        } else mMediaPlayer!!.start()

                    }
            )

//            Image(
//                imageVector = Icons.Filled.Forward10,
//                contentDescription = "Forward 10 Seconds",
//                contentScale = ContentScale.Fit,
//                colorFilter = ColorFilter.tint(Color.White),
//                modifier = buttonModifier
//            )

            Image(
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "Skip Next",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = buttonModifier
            )
        }
    }

    /**
     * PlayerScreen
     * */

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun PlayerScreen(songModel:SongModel) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            BackgroundOne,
                            BackgroundTwo,
                            BackgroundThree
                        )
                    )
                )
                .padding(horizontal = 10.dp)
        ) {
            TopAppBar()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.height(30.dp))
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Song banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .sizeIn(maxWidth = 500.dp, maxHeight = 500.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .weight(10f)
                )
                Spacer(modifier = Modifier.height(30.dp))
                SongDescription(songModel)
                Spacer(modifier = Modifier.height(35.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(10f)
                ) {
                    PlayerSlider(Duration.ofHours(2))
                    Spacer(modifier = Modifier.height(40.dp))
                    PlayerButtons(songModel,modifier = Modifier.padding(vertical = 8.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MusicAppTheme {
         //  PlayerScreen()
        }
    }
}