package kr.payhere.payheretest.domain.account_book.cateogory.service

import kr.payhere.payheretest.config.BeanConfig.Companion.LOCAL_30_MIN
import kr.payhere.payheretest.config.BeanConfig.Companion.REDIS_30_MIN
import kr.payhere.payheretest.domain.account_book.cateogory.model.dto.AccountBookCategoryDTO
import kr.payhere.payheretest.domain.account_book.cateogory.model.entity.AccountBookCategory
import kr.payhere.payheretest.domain.account_book.cateogory.model.error_code.AccountBookCategoryErrorCode
import kr.payhere.payheretest.domain.account_book.cateogory.repository.AccountBookCategoryRepository
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.exception.NotFoundException
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class AccountBookCategoryService (
    private val accountBookCategoryRepository: AccountBookCategoryRepository,
) {
    @Cacheable(value = [REDIS_30_MIN], key = "'ACCOUNT:BOOK:CATEGORY:PARENT'")
    fun getParentCategories(): List<AccountBookCategoryDTO> {
        return accountBookCategoryRepository.findByDeletableFalseAndParentIdIsNull()
            .map { AccountBookCategoryDTO(it) }
    }

    @Cacheable(value = [LOCAL_30_MIN], key = "'ACCOUNT:BOOK:CATEGORY:BASE'")
    fun getBaseCategories(): List<AccountBookCategoryDTO> {
        return accountBookCategoryRepository.findByDeletableFalse()
            .map { AccountBookCategoryDTO(it) }
    }

    fun getUserCategories(userId: Long): MutableList<AccountBookCategoryDTO> {
        return accountBookCategoryRepository.findByUserIdAndDeletedAtIsNotNull(userId)
            .map { AccountBookCategoryDTO(it) }
            .toMutableList()
    }

    @Transactional
    fun save(category: AccountBookCategory) {
        val duplicateNames = accountBookCategoryRepository.findByNameAndUserIdAndDeletedAtIsNull(category.name, category.userId!!)

        if (duplicateNames.isNotEmpty()) {
            throw BadRequestException(AccountBookCategoryErrorCode.NameAlreadyUse)
        }

        accountBookCategoryRepository.save(category)
    }

    @Transactional
    fun delete(id: Long, userId: Long) {
        val category = accountBookCategoryRepository.findByIdOrNull(id)
            ?: throw NotFoundException(AccountBookCategoryErrorCode.NotFound)

        if (!category.deletable) {
            throw BadRequestException(AccountBookCategoryErrorCode.BaseCategoryDelete)
        }

        if (category.userId != userId) {
            throw BadRequestException(AccountBookCategoryErrorCode.OtherUserCategoryDelete)
        }

        category.deletedAt = LocalDateTime.now()
    }
}