package wf.ada.utils

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder

fun String.hashToArgon2id(): String {
    val arg2SpringSecurity = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
    return arg2SpringSecurity.encode(this)
}

fun String.verifyHash(hash: String): Boolean {
    val arg2SpringSecurity = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
    return arg2SpringSecurity.matches(this, hash)
}
