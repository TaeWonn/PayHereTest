package kr.payhere.payheretest.domain.account_book.cateogory.model.entity

import kr.payhere.payheretest.config.entity.BaseEntity
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class AccountBookCategory(
    var name: String,
    var depth: Int = 1,
    var userId: Long? = null,
    var parentId: Long? = null,
    var deletable: Boolean = true,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
    var deletedAt: LocalDateTime? = null,
): BaseEntity() {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parentId", updatable = false, insertable = false)
    var parent: AccountBookCategory? = null
}