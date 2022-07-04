package kr.payhere.payheretest.domain.account_book.cateogory.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kr.payhere.payheretest.assertionsErrorCode
import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import kr.payhere.payheretest.domain.account_book.cateogory.model.error_code.AccountBookCategoryErrorCode
import kr.payhere.payheretest.domain.account_book.cateogory.repository.AccountBookCategoryRepository
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.isEqual
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
internal class AccountBookCategoryServiceTest (
    @MockK val accountBookCategoryRepository: AccountBookCategoryRepository,
) {
    @SpyK
    private var accountBookCategoryService = AccountBookCategoryService(accountBookCategoryRepository)


    @Nested
    @DisplayName("카테고리 저장")
    inner class Save {
        @Test
        fun `같은 이름으로 등록된 카테고리가 있다면 예외를 반환한다`() {
            //given
            val categoryParam = AccountBookCategory(
                name = anyString(),
                userId = anyLong(),
            )

            every {
                accountBookCategoryRepository.findByNameAndUserIdAndDeletedAtIsNull(categoryParam.name, categoryParam.userId!!)
            } returns listOf(
                mockk()
            )
            every { accountBookCategoryRepository.save(categoryParam) } returns categoryParam

            //when
            val throws = assertThatThrownBy { accountBookCategoryService.save(categoryParam) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookCategoryErrorCode.NameAlreadyUse)
        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    inner class Delete {
        @Test
        fun `기본 카테고리를 삭제하려하면 예외를 반환한다`() {
            //given
            val id = anyLong()
            val userId = anyLong()
            val category = AccountBookCategory(
                name = anyString(),
                deletable = false,
            )

            every { accountBookCategoryRepository.findByIdOrNull(id) } returns category

            //when
            val throws = assertThatThrownBy { accountBookCategoryService.delete(id, userId) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookCategoryErrorCode.BaseCategoryDelete)
        }

        @Test
        fun `내가 만든 카테고리 아니라면 예외를 반환한다`() {
            //given
            val id = anyLong()
            val userId = anyLong()
            val category = AccountBookCategory(name = anyString(), userId = 1)

            every { accountBookCategoryRepository.findByIdOrNull(id) } returns category

            //when
            val throws = assertThatThrownBy { accountBookCategoryService.delete(id, userId) }

            //then
            throws.isInstanceOf(BadRequestException::class.java)
                .assertionsErrorCode()
                .isEqualTo(AccountBookCategoryErrorCode.OtherUserCategoryDelete)
        }

        @Test
        fun `삭제되면 deletedAt 이 현재 시간으로 주입된다`() {
            //given
            val id = anyLong()
            val userId = anyLong()
            val category = AccountBookCategory(name = anyString(), userId = userId)
            val now = LocalDateTime.now()

            every { accountBookCategoryRepository.findByIdOrNull(id) } returns category
            mockkStatic(LocalDateTime::class)
            every { LocalDateTime.now() } returns now

            //when
            accountBookCategoryService.delete(id, userId)

            //then
            category.deletedAt isEqual now
        }
    }
}