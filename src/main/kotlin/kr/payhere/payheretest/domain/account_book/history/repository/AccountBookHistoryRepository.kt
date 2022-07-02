package kr.payhere.payheretest.domain.account_book.history.repository

import kr.payhere.payheretest.config.entity.fetchPage
import kr.payhere.payheretest.domain.account_book.history.model.entity.AccountBookHistory
import kr.payhere.payheretest.domain.account_book.history.model.entity.QAccountBookHistory.accountBookHistory
import kr.payhere.payheretest.domain.account_book.model.entity.QAccountBook.accountBook
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

interface AccountBookHistoryRepository: JpaRepository<AccountBookHistory, Long>, AccountBookHistoryCustom {

}

interface AccountBookHistoryCustom {
    fun findByAccountBookId(accountBookId: Long): List<AccountBookHistory>
    fun findDeletedLists(userId: Long, pageable: Pageable): Page<AccountBookHistory>
}

class AccountBookHistoryRepositoryImpl: QuerydslRepositorySupport(AccountBookHistory::class.java), AccountBookHistoryCustom {
    override fun findByAccountBookId(accountBookId: Long): List<AccountBookHistory> {
        return from(accountBookHistory)
            .join(accountBookHistory.accountBook).fetchJoin()
            .join(accountBookHistory.accountBookCategory).fetchJoin()
            .where(accountBookHistory.accountBookId.eq(accountBookId))
            .where(accountBookHistory.deletedAt.isNull)
            .fetch()
    }

    override fun findDeletedLists(userId: Long, pageable: Pageable): Page<AccountBookHistory> {
        return from(accountBookHistory)
            .join(accountBookHistory.accountBook).fetchJoin()
            .join(accountBookHistory.accountBookCategory).fetchJoin()
            .where(accountBookHistory.deletedAt.isNotNull)
            .where(accountBookHistory.accountBook.userId.eq(userId))
            .select(accountBookHistory)
            .fetchPage(pageable)

    }
}