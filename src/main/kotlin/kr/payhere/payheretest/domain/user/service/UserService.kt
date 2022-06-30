package kr.payhere.payheretest.domain.user.service

import kr.payhere.payheretest.domain.user.model.dto.UserDTO
import kr.payhere.payheretest.domain.user.model.entity.User
import kr.payhere.payheretest.domain.user.model.error_code.UserErrorCode
import kr.payhere.payheretest.domain.user.model.param.UserSignUpParam
import kr.payhere.payheretest.domain.user.respository.UserRepository
import kr.payhere.payheretest.exception.BadRequestException
import kr.payhere.payheretest.exception.ConflictException
import kr.payhere.payheretest.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    private val userRepository: UserRepository,

) {
    fun login(email: String, password: String): UserDTO {
        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException(UserErrorCode.UserEmailNotFound)

        if (!user.checkPassword(password)) {
            throw NotFoundException(UserErrorCode.PassWordNotEqual)
        }

        if (!user.mailAuthentication) {
            throw BadRequestException(UserErrorCode.EmailNotAuthentication)
        }

        return UserDTO(user)
    }

    @Transactional
    fun signUp(email: String, password: String) {
        val duplicateEmailUser = userRepository.findByEmail(email)

        if (duplicateEmailUser != null) {
            throw ConflictException(UserErrorCode.ExitsEmail)
        }
        val user = User(email, password)

        userRepository.save(user)
    }

    @Transactional
    fun emailCertification(email: String, key: String) {
        val user = userRepository.findByEmail(email)
            ?: throw NotFoundException(UserErrorCode.UserEmailNotFound)

        if (user.mailAuthentication) {
            throw BadRequestException(UserErrorCode.ExitsEmailAuthentication)
        }

        user.mailAuthentication = true
    }
}