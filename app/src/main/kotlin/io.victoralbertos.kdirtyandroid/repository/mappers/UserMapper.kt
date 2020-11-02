package io.victoralbertos.kdirtyandroid.repository.mappers

import io.victoralbertos.kdirtyandroid.entities.User
import io.victoralbertos.kdirtyandroid.network.dtos.UserDTO
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun asEntity(userDTO: UserDTO) = User(
        id = userDTO.id,
        name = userDTO.login,
        avatar = userDTO.avatarUrl
    )
}
