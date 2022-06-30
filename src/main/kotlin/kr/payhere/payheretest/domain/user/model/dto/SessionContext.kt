package kr.payhere.payheretest.domain.user.model.dto

import java.io.Serializable

class SessionContext(
    val userId: Long
): Serializable {
    constructor(userDTO: UserDTO): this(userId = userDTO.id)
}