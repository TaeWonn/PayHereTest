package kr.payhere.payheretest.domain.account_book.history.service

import kr.payhere.payheretest.domain.account_book.history.model.dto.AccountBookHistoryDTO
import kr.payhere.payheretest.domain.account_book.history.repository.AccountBookHistoryRepository
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookUpdateParam
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.exception.ForbiddenException
import kr.payhere.payheretest.exception.NotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountBookHistoryService(
    private val accountBookHistoryRepository: AccountBookHistoryRepository,
) {

    @Transactional
    fun delete(id: Long, userId: Long) {
        val history = accountBookHistoryRepository.findByIdOrNull(id)
            ?: throw NotFoundException(AccountBookErrorCode.HistoryNotFound)

        history.delete(userId)
    }

    @Transactional
    fun restore(id: Long, userId: Long) {
        val history = accountBookHistoryRepository.findByIdOrNull(id)
            ?: throw NotFoundException(AccountBookErrorCode.HistoryNotFound)

        history.restore(userId)
    }

    @Transactional
    fun updateMemoMoney(param: AccountBookUpdateParam, userId: Long) {
        val ids = param.histories.map { it.historyId }
        val histories = accountBookHistoryRepository.findAllById(ids)

        val distinctById = histories.distinctBy { it.accountBookId }
        when {
            distinctById.isEmpty() -> throw BadRequestException(AccountBookErrorCode.HistoryNotFound)
            distinctById.size > 1 -> throw BadRequestException(AccountBookErrorCode.ModifyCanSameDateOnly)
        }

        val accountBook = distinctById.first().accountBook
        accountBook.updateMemo(param.memo, userId)

        histories.forEach { history ->
            val historyParam = param.histories.firstOrNull() { it.historyId == history.id }
                ?: throw BadRequestException(AccountBookErrorCode.HistoryNotFound)
            history.updateCashAndCard(historyParam.cash, historyParam.card)
        }

    }

    fun findByAccountBookId(accountBookId: Long, userId: Long): List<AccountBookHistoryDTO> {
        val histories = accountBookHistoryRepository.findByAccountBookId(accountBookId)

        val accountBook = histories.firstOrNull()?.accountBook
            ?: return emptyList()

        if (accountBook.userId != userId) {
            throw ForbiddenException(AccountBookErrorCode.ReadOnlyWriter)
        }

        return histories.map { AccountBookHistoryDTO(it) }
    }

    fun myDeletedHistories(userId: Long, pageable: Pageable): Page<AccountBookHistoryDTO> {
        return accountBookHistoryRepository.findDeletedLists(userId, pageable)
            .map { AccountBookHistoryDTO(it) }
    }

}