package io.victoralbertos.kdirtyandroid.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.compose.LazyPagingItems
import io.victor.kdirtyandroid.R

@Composable
fun Fragment.AppScaffold(
    content: @Composable () -> Unit,
    titleToolBar: String,
    showBack: Boolean = false
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = titleToolBar
                    )
                },
                navigationIcon = if (showBack) {
                    {
                        IconButton(onClick = {
                            findNavController().popBackStack()
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                } else {
                    null
                },
                backgroundColor = colorResource(R.color.white),
                contentColor = colorResource(R.color.black)
            )
        }, content = { content() })
}


@ExperimentalFoundationApi
fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}
