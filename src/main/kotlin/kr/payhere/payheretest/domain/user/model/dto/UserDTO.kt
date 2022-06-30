package kr.payhere.payheretest.domain.user.model.dto

import kr.payhere.payheretest.domain.user.model.entity.User

class UserDTO (
    val id: Long,
    val email: String,
) {
    constructor(user: User) : this(
        id = user.id,
        email = user.email,
    )
}