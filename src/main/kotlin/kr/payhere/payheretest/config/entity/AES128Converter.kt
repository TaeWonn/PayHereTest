package kr.payhere.payheretest.config.entity

import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class AES128Converter: AttributeConverter<String, String> {

    @Value("\${encrypt.key}")
    private lateinit var key: String

    private val algorithm: String = "AES/CBC/PKCS5Padding"

    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute == null) return null
        if (attribute.isEmpty()) return ""
        val attribute = attribute.trim()

        if (key.length != 16)
            throw RuntimeException("AES-128 encrypting fail - invalid secret key's length. required=16, input=${key.length}")

        val byteKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, byteKey, IvParameterSpec(key.toByteArray()))
        return Base64.encodeBase64String(cipher.doFinal(attribute.toByteArray()))
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData == null) return null
        if (dbData.isEmpty()) return ""
        if (key.length != 16)
            throw RuntimeException("AES-128 decr ypting fail - invalid secret key's length. required=16, input=${key.length}")

        val byteKey = SecretKeySpec(key.toByteArray(), "AES")
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, byteKey, IvParameterSpec(key.toByteArray()))
        return String(cipher.doFinal(Base64.decodeBase64(dbData)))
    }
}