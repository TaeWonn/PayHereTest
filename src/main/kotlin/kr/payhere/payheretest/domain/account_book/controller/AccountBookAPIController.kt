package kr.payhere.payheretest.domain.account_book.controller

import kr.payhere.payheretest.config.auth.Restricted
import kr.payhere.payheretest.domain.account_book.history.model.dto.AccountBookHistoryDTO
import kr.payhere.payheretest.domain.account_book.history.service.AccountBookHistoryService
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookCreateParam
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookListDTO
import kr.payhere.payheretest.domain.account_book.model.param.AccountBookUpdateParam
import kr.payhere.payheretest.domain.account_book.service.AccountBookService
import kr.payhere.payheretest.domain.user.controller.UserAPIController.Companion.UserSessionContextKey
import kr.payhere.payheretest.domain.user.model.dto.SessionContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.SessionAttribute
import javax.validation.Valid

@Restricted
@RestController
@RequestMapping("/api/account-books")
class AccountBookAPIController (
    private val accountBookService: AccountBookService,
    private val accountBookHistoryService: AccountBookHistoryService,
) {
    // a. 가계부에 오늘 사용한 돈의 금액과 관련된 메모를 남길 수 있습니다.
    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun save(
        @RequestBody @Valid param: AccountBookCreateParam,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ) {
        accountBookService.save(param, context.userId)
    }

    // b. 가계부에서 수정을 원하는 내역은 금액과 메모를 수정 할 수 있습니다.
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun modify(
        @RequestBody @Valid param: AccountBookUpdateParam,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ) {
        accountBookHistoryService.updateMemoMoney(param, context.userId)
    }

    // c. 가계부에서 삭제를 원하는 내역은 삭제 할 수 있습니다.
    @DeleteMapping("/{historyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable historyId: Long,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ) {
        accountBookHistoryService.delete(historyId, context.userId)
    }

    // d. 삭제한 내역은 언제든지 다시 복구 할 수 있어야 한다.
    @PutMapping("/restore/{historyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun restore(
        @PathVariable historyId: Long,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ) {
        accountBookHistoryService.restore(historyId, context.userId)
    }

    // e. 가계부에서 이제까지 기록한 가계부 리스트를 볼 수 있습니다.
    @GetMapping
    fun myAccountBooks(
        @PageableDefault(size = 10) pageable: Pageable,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ): Page<AccountBookListDTO> {
        return accountBookService.myList(context.userId, pageable)
    }

    // f. 가계부에서 상세한 세부 내역을 볼 수 있습니다.
    @GetMapping("/{accountBookId}")
    fun getHistories(
        @PathVariable accountBookId: Long,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ): List<AccountBookHistoryDTO> {
        return accountBookHistoryService.findByAccountBookId(accountBookId, context.userId)
    }

    @GetMapping("/deleted")
    fun deletedAccountBooks(
        @PageableDefault(size = 10) pageable: Pageable,
        @SessionAttribute(UserSessionContextKey) context: SessionContext,
    ): Page<AccountBookHistoryDTO> {
        return accountBookHistoryService.myDeletedHistories(context.userId, pageable)
    }
}