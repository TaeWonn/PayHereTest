package kr.payhere.payheretest.config.entity

import com.querydsl.jpa.JPQLQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

fun <T : Any> JPQLQuery<T>.fetchPage(pageable: Pageable, fetchCount: Long? = null): Page<T> {
    val fetchCount = fetchCount ?: this.fetchCount()
    val fetch = this.applyPageable(pageable).fetch()

    return PageImpl(fetch, pageable, fetchCount)
}

fun <T : Any> JPQLQuery<T>.applyPageable(pageable: Pageable): JPQLQuery<T> {
    return this.offset(pageable.offset).limit(pageable.pageSize.toLong())
}