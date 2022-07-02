package kr.payhere.payheretest.domain.account_book.service

import kr.payhere.payheretest.domain.account_book.history.repository.AccountBookHistoryRepository
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookCreateParam
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookListDTO
import kr.payhere.payheretest.domain.account_book.repository.AccountBookRepository
import kr.payhere.payheretest.exception.BadRequestException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountBookService (
    private val accountBookRepository: AccountBookRepository,
    private val accountBookHistoryRepository: AccountBookHistoryRepository,
) {
    @Transactional
    fun save(param: AccountBookCreateParam, userId: Long) {
        val accountBook = param.toEntity(userId)
        val find = accountBookRepository.findByPayDateAndUserId(accountBook.payDate, userId)

        if (find != null) {
            throw BadRequestException(AccountBookErrorCode.TodayAlreadySave)
        }

        val save = accountBookRepository.save(accountBook)

        val histories = param.toHistoryEntity(save.id)
        accountBookHistoryRepository.saveAll(histories)
    }

    fun myList(userId: Long, pageable: Pageable): Page<AccountBookListDTO> {
        return accountBookRepository.findByUserId(userId, pageable)
            .map { AccountBookListDTO(it) }
    }

}