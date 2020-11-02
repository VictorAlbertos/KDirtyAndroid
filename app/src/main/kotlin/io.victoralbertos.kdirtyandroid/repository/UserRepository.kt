package io.victoralbertos.kdirtyandroid.repository

import io.victoralbertos.kdirtyandroid.entities.User
import io.victoralbertos.kdirtyandroid.network.api.GithubUsersApi
import io.victoralbertos.kdirtyandroid.repository.mappers.UserMapper
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val githubUsersApi: GithubUsersApi,
    private val userMapper: UserMapper
) {
    suspend fun getUsers(lastIdQueried: Int?): List<User> {
        val id = lastIdQueried ?: FIRST_DEFAULT_ID
        val usersDto = githubUsersApi.getUsers(id, USERS_PER_PAGE)
        return usersDto.map { userMapper.asEntity(it) }
    }

    companion object {
        private const val FIRST_DEFAULT_ID = 0
        const val USERS_PER_PAGE = 50
    }
}
