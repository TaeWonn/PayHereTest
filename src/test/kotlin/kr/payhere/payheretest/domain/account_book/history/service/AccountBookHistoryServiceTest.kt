package kr.payhere.payheretest.domain.account_book.history.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kr.payhere.payheretest.assertionsErrorCode
import kr.payhere.payheretest.domain.account_book.history.model.entity.AccountBookHistory
import kr.payhere.payheretest.domain.account_book.history.repository.AccountBookHistoryRepository
import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookHistoryUpdateParam
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookUpdateParam
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.exception.ForbiddenException
import kr.payhere.payheretest.isEqual
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class AccountBookHistoryServiceTest (
    @MockK val accountBookHistoryRepository: AccountBookHistoryRepository,
) {
    @SpyK
    private var accountBookHistoryService = AccountBookHistoryService(accountBookHistoryRepository)

    @Nested
    @DisplayName("가계부 내역 삭제")
    inner class Delete {
        @Test
        fun `다른 사람이 작성한 내역을 삭제하려하면 예외를 반환한다`() {
            //given
            val id = anyLong()
            val userId = 1L
            val history = generateHistory().apply {
                accountBook = AccountBook(memo = null, userId = 2)
            }

            every { accountBookHistoryRepository.findByIdOrNull(id) } returns history

            //when
            val throws = assertThatThrownBy { accountBookHistoryService.delete(id, userId) }

            //then
            throws.isInstanceOf(ForbiddenException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.ModifyOnlyWriter)
        }

        @Test
        fun `내역을 삭제하면 deletedAt 에 현재 시간을 주입한다`() {
            val now = LocalDateTime.now()
            val id = anyLong()
            val userId = 1L
            val history = generateHistory().apply {
                accountBook = AccountBook(memo = null, userId = 1)
            }

            every { accountBookHistoryRepository.findByIdOrNull(id) } returns history
            mockkStatic(LocalDateTime::class)
            every { LocalDateTime.now() } returns now

            //when
            accountBookHistoryService.delete(id, userId)

            //then
            history.deletedAt isEqual now
        }
    }

    @Nested
    @DisplayName("가계부 내역 복구")
    inner class Restore {
        @Test
        fun `다른 사람이 작성한 내역을 복구하려하면 예외를 반환한다`() {
            //given
            val id = anyLong()
            val userId = 1L
            val history = generateHistory().apply {
                accountBook = AccountBook(memo = null, userId = 2)
            }

            every { accountBookHistoryRepository.findByIdOrNull(id) } returns history

            //when
            val throws = assertThatThrownBy { accountBookHistoryService.restore(id, userId) }

            //then
            throws.isInstanceOf(ForbiddenException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.ModifyOnlyWriter)
        }

        @Test
        fun `내역을 복구하면 deletedAt 의 null 을 주입한다`() {
            val id = anyLong()
            val userId = 1L
            val history = generateHistory().apply {
                accountBook = AccountBook(memo = null, userId = 1)
            }

            every { accountBookHistoryRepository.findByIdOrNull(id) } returns history

            //when
            accountBookHistoryService.restore(id, userId)

            //then
            assertThat(history.deletedAt).isNull()
        }
    }

    @Nested
    @DisplayName("메모 및 금액 수정")
    inner class UpdateMemoMoney {
        @Test
        fun `다른 날짜의 내역이 포함되어있으면 예외를 반환한다`() {
            //given
            val historiesParam = generateHistoriesParam()
            val param = generateParam(histories = historiesParam)
            val histories = generateHistoriesEntity()
            val userId = anyLong()

            histories.forEachIndexed { index, it -> it.accountBookId = index.toLong() }

            every { accountBookHistoryRepository.findAllById(any()) } returns histories

            //when
            val throws = assertThatThrownBy { accountBookHistoryService.updateMemoMoney(param, userId) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.ModifyCanSameDateOnly)
        }

        @Test
        fun `내가 작성한 가계부가 아니라면 예외를 반환한다`() {
            //given
            val historiesParam = generateHistoriesParam()
            val param = generateParam(histories = historiesParam)
            val userId = 1L
            val accountBook = AccountBook(memo = null, userId = userId + 1)
            val histories = generateHistoriesEntity()
            histories.forEach {
                it.accountBookId = 1
                it.accountBook = accountBook
            }

            every { accountBookHistoryRepository.findAllById(any()) } returns histories

            //when
            val throws = assertThatThrownBy { accountBookHistoryService.updateMemoMoney(param, userId) }

            //then
            throws.isInstanceOf(ForbiddenException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.ModifyOnlyWriter)
        }

        @Test
        fun `내역이 없다면 예외를 반환한다`() {
            //given
            val historiesParam = generateHistoriesParam()
            val param = generateParam(histories = historiesParam)
            val userId = 1L
            val histories = emptyList<AccountBookHistory>()

            every { accountBookHistoryRepository.findAllById(any()) } returns histories

            //when
            val throws = assertThatThrownBy { accountBookHistoryService.updateMemoMoney(param, userId) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.HistoryNotFound)
        }

        @Test
        fun `가계부의 메모와 내역의 금액을 수정`() {
            //given
            val historiesParam = generateHistoriesParam()
            val param = generateParam(memo = "memoooooooo", histories = historiesParam)
            val userId = 1L
            val accountBook = AccountBook(memo = null, userId = userId)
            val histories = generateHistoriesEntity()
            histories.forEach {
                it.accountBookId = 1
                it.accountBook = accountBook
            }

            every { accountBookHistoryRepository.findAllById(any()) } returns histories

            //when
            accountBookHistoryService.updateMemoMoney(param, userId)

            //then
            accountBook.memo isEqual param.memo

            histories.forEachIndexed { index, history ->
                history.card isEqual historiesParam[index].card
                history.cash isEqual historiesParam[index].cash
            }
        }

        private fun generateParam(memo: String? = null, histories: List<AccountBookHistoryUpdateParam>)
            = AccountBookUpdateParam(
                memo = memo,
                histories = histories
            )

        private fun generateHistoriesParam() = listOf(
            AccountBookHistoryUpdateParam(1, BigDecimal(100), BigDecimal(200)),
            AccountBookHistoryUpdateParam(2, BigDecimal(100), BigDecimal(300)),
            AccountBookHistoryUpdateParam(3, BigDecimal(200), BigDecimal(400)),
        )

        private fun generateHistoriesEntity() = listOf(
            generateHistory().apply { id = 1 },
            generateHistory().apply { id = 2 },
            generateHistory().apply { id = 3 },
        )
    }

    private fun generateHistory() = AccountBookHistory(
        cash = BigDecimal(0),
        card = BigDecimal(0),
        accountBookId = anyLong(),
        accountBookCategoryId = anyLong(),
    )

}