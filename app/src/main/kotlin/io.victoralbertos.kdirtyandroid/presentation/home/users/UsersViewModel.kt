package io.victoralbertos.kdirtyandroid.presentation.home.users

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.victoralbertos.kdirtyandroid.entities.User
import io.victoralbertos.kdirtyandroid.paging.UsersPagingSource
import io.victoralbertos.kdirtyandroid.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class UsersViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val pagingData: Flow<PagingData<User>> = Pager(PagingConfig(pageSize = 20)) { UsersPagingSource(userRepository) }
        .flow
        .cachedIn(viewModelScope)
}
