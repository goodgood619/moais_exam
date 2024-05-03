package com.moais.assignment.util

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object Utils {
}

fun encrypt(input: String, key: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedBytes = cipher.doFinal(input.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}

fun decrypt(input: String, key: String): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input))
    return String(decryptedBytes)
}

fun getChosungPattern(input: String): String {
    val result = StringBuilder()
    for (char in input) {
        if (char.isHangul()) {
            val unicode = char.toInt()
            val chosungIndex = ((unicode - CHOSUNG_START_UNICODE) / (CHOSUNG_COUNT * JONGSUNG_COUNT)) % CHOSUNG_LIST.size
            result.append(CHOSUNG_LIST[chosungIndex])
        } else if (char == ' ') {
            result.append(char)
        }
    }
    return result.toString()
}

private fun Char.isHangul(): Boolean {
    val unicode = this.toInt()
    return unicode in CHOSUNG_START_UNICODE..CHOSUNG_END_UNICODE
}

private const val CHOSUNG_START_UNICODE = 44032 // '가'의 유니코드 포인트
private const val CHOSUNG_END_UNICODE = 55203   // '힣'의 유니코드 포인트
private const val CHOSUNG_COUNT = 28            // 초성 개수
private const val JONGSUNG_COUNT = 21      // 종성 개수

private val CHOSUNG_LIST = arrayOf(
    'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
    'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
)