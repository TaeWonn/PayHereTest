package kr.payhere.payheretest.domain.account_book.cateogory.controller

import kr.payhere.payheretest.config.auth.Restricted
import kr.payhere.payheretest.domain.account_book.cateogory.model.dto.AccountBookCategoryDTO
import kr.payhere.payheretest.domain.account_book.cateogory.model.dto.AccountBookCategoryListDTO
import kr.payhere.payheretest.domain.account_book.cateogory.model.param.AccountBookCategoryCreateParam
import kr.payhere.payheretest.domain.account_book.cateogory.service.AccountBookCategoryService
import kr.payhere.payheretest.domain.user.controller.UserAPIController
import kr.payhere.payheretest.domain.user.model.dto.SessionContext
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttribute
import javax.validation.Valid

@Restricted
@RestController
@RequestMapping("/api/account-books/categories")
class AccountBookCategoryAPIController (
    private val accountBookCategoryService: AccountBookCategoryService,
) {

    @GetMapping
    fun list(@SessionAttribute(UserAPIController.UserSessionContextKey) context: SessionContext): Map<Long?, List<AccountBookCategoryListDTO>> {
        val myCategories = accountBookCategoryService.getUserCategories(context.userId)
        val baseCategories = accountBookCategoryService.getBaseCategories()

        myCategories.addAll(baseCategories)

        return baseCategories
            .map { AccountBookCategoryListDTO(it) }
            .groupBy { it.parentId }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun save(
        @RequestBody @Valid param: AccountBookCategoryCreateParam,
        @SessionAttribute(UserAPIController.UserSessionContextKey) context: SessionContext,
    ) {
        val category = param.toEntity(context.userId)
        accountBookCategoryService.save(category)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable id: Long,
        @SessionAttribute(UserAPIController.UserSessionContextKey) context: SessionContext,
    ) {
        accountBookCategoryService.delete(id, context.userId)
    }

    @GetMapping("/parents")
    fun parents(): List<AccountBookCategoryListDTO> {
        return accountBookCategoryService.getParentCategories()
            .map { AccountBookCategoryListDTO(it) }
    }
}