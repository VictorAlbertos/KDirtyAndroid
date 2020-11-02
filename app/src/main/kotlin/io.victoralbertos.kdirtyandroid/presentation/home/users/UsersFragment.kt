package io.victoralbertos.kdirtyandroid.presentation.home.users

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.victor.kdirtyandroid.R
import io.victor.kdirtyandroid.databinding.UsersFragmentBinding
import io.victoralbertos.kdirtyandroid.paging.DefaultLoadStateAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment(R.layout.users_fragment) {
    private val usersViewModel: UsersViewModel by viewModels()

    private val usersAdapter = UsersAdapter(this) { user ->
        findNavController().navigate(UsersFragmentDirections.actionUserFragment(user))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        UsersFragmentBinding.bind(view).apply {
            toolbar.setupWithNavController(findNavController(), AppBarConfiguration(findNavController().graph))
            toolbar.setTitle(R.string.list_of_users)

            rvUsers.apply {
                adapter = usersAdapter
                    .withLoadStateFooter(footer = DefaultLoadStateAdapter { usersAdapter.retry() })
                layoutManager = GridLayoutManager(context, 3)

                setHasFixedSize(true)

                addItemDecoration(DividerItemDecoration(context, LinearLayout.HORIZONTAL))
                addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            }

            swipeRefreshLayout.setOnRefreshListener {
                usersAdapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            usersViewModel.pagingData.collectLatest {
                usersAdapter.submitData(it)
            }
        }
    }
}
