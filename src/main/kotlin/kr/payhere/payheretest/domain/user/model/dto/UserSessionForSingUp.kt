package kr.payhere.payheretest.domain.user.model.dto

import java.io.Serializable

class UserSessionForSingUp (
    val email: String,
    val key: String,
): Serializable