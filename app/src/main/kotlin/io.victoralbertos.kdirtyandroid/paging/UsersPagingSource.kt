package io.victoralbertos.kdirtyandroid.paging

import androidx.paging.PagingSource
import io.victoralbertos.kdirtyandroid.entities.User
import io.victoralbertos.kdirtyandroid.repository.UserRepository
import javax.inject.Inject

class UsersPagingSource @Inject constructor(
    private val usersRepository: UserRepository
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> = try {
        val users = usersRepository.getUsers(params.key)
        LoadResult.Page(
            data = users,
            prevKey = null, // Only paging forward
            nextKey = users.last().id
        )
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}
