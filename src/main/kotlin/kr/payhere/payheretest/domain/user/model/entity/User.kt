package kr.payhere.payheretest.domain.user.model.entity

import kr.payhere.payheretest.config.entity.AES128Converter
import kr.payhere.payheretest.config.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity

@Entity
class User (
    @Column(unique = true, nullable = false)
    var email: String,
    @Convert(converter = AES128Converter::class)
    var password: String,
    var mailAuthentication: Boolean = false,
) : BaseEntity() {

    fun checkPassword(password: String): Boolean {
        return this.password == password
    }
}