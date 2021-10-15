package io.victoralbertos.kdirtyandroid.presentation.home.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import io.victor.kdirtyandroid.R
import io.victoralbertos.kdirtyandroid.entities.User
import io.victoralbertos.kdirtyandroid.presentation.AppScaffold

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val usersViewModel: UsersViewModel by viewModels()

    @ExperimentalFoundationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppScaffold(
                    showBack = false,
                    titleToolBar = stringResource(id = R.string.list_of_users),
                    content = {
                        val items: LazyPagingItems<User> = usersViewModel.pagingData.collectAsLazyPagingItems()
                        LazyVerticalGrid(cells = GridCells.Fixed(3)) {
                            items(items.itemCount) { index ->
                                UserItem(items[index]!!)
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun UserItem(user: User) {
        Row(
            modifier = Modifier
                .clickable {
                    findNavController().navigate(UsersFragmentDirections.actionUserFragment(user))
                }
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            user.avatar?.let {
                GlideImage(
                    modifier = Modifier
                        .height(125.dp),
                    imageModel = user.avatar,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    @Composable
    fun UserImage(
        imageUrl: String,
        modifier: Modifier = Modifier
    ) {
        GlideImage(
            modifier = modifier,
            imageModel = imageUrl,
            contentScale = ContentScale.Crop
        )
    }
}
