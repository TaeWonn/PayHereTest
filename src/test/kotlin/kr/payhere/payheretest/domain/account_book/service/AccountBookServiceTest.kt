package kr.payhere.payheretest.domain.account_book.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.slot
import kr.payhere.payheretest.assertionsErrorCode
import kr.payhere.payheretest.domain.account_book.history.model.entity.AccountBookHistory
import kr.payhere.payheretest.domain.account_book.history.repository.AccountBookHistoryRepository
import kr.payhere.payheretest.domain.account_book.model.entity.AccountBook
import kr.payhere.payheretest.domain.account_book.model.error_code.AccountBookErrorCode
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookCreateParam
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookHistoryCreateParam
import kr.payhere.payheretest.domain.account_book.repository.AccountBookRepository
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.isEqual
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
internal class AccountBookServiceTest (
    @MockK val accountBookRepository: AccountBookRepository,
    @MockK val accountBookHistoryRepository: AccountBookHistoryRepository,
) {
    @SpyK
    private var accountBookService = AccountBookService(accountBookRepository, accountBookHistoryRepository)

    @Nested
    @DisplayName("가계부 저장")
    inner class Save {
        @Test
        fun `오늘 날짜로 등록된 가계부가 있다면 예외를 반환한다`() {
            //given
            val param = AccountBookCreateParam(memo = null, emptyList())
            val userId = anyLong()

            every { accountBookRepository.findByPayDateAndUserId(any(), userId) } returns mockk()

            //when
            val throws = assertThatThrownBy { accountBookService.save(param, userId) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookErrorCode.TodayAlreadySave)
        }

        @Test
        fun `가계부 저장시 내역도 같이 저장한다`() {
            //given
            val histories = listOf(
                AccountBookHistoryCreateParam(BigDecimal(100), BigDecimal(200), categoryId = anyLong())
            )
            val param = AccountBookCreateParam(memo = null, histories)
            val userId = anyLong()
            val accountBookSlot = slot<AccountBook>()
            val saveAccountBook = mockk<AccountBook>() {
                every { id } returns anyLong()
            }
            val historiesSlot = slot<List<AccountBookHistory>>()

            every { accountBookRepository.findByPayDateAndUserId(any(), userId) } returns null
            every { accountBookRepository.save(capture(accountBookSlot)) } returns saveAccountBook
            every { accountBookHistoryRepository.saveAll(capture(historiesSlot)) } returns mockk()

            //when
            accountBookService.save(param, userId)

            //then
            val accountCapture = accountBookSlot.captured
            val historiesCapture = historiesSlot.captured
            assertThat(accountCapture.memo).isNull()
            historiesCapture.size isEqual 1
            historiesCapture.first().cash isEqual BigDecimal(100)
            historiesCapture.first().card isEqual BigDecimal(200)
        }
    }

}