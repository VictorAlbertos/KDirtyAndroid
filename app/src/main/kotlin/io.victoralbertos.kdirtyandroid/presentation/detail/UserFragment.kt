package io.victoralbertos.kdirtyandroid.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.skydoves.landscapist.glide.GlideImage
import io.victoralbertos.kdirtyandroid.presentation.AppScaffold

class UserFragment : Fragment() {
    private val navArgs by navArgs<UserFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppScaffold(
                    showBack = true,
                    titleToolBar = navArgs.user.name,
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .size(
                                        width = 200.dp,
                                        height = 200.dp,
                                    )
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Black, CircleShape),
                                imageModel = navArgs.user.avatar,
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                )
            }
        }
    }
}

