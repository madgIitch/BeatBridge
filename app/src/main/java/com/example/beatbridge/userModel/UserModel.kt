package com.example.beatbridge.userModel

import org.mindrot.jbcrypt.BCrypt

data class UserModel(
    val username: String,
    private var passwordHash: String,
    val email: String,
    val city: String,
    val country: String,
    val biography: String,
    val roles: Set<Role>
) {
    enum class Role {
        SINGER, PRODUCER, DJ, SOUND_ENGINEER, MUSICIAN, EVENT_ORGANIZER, MUSIC_LOVER
    }

    companion object {
        fun create(username: String, password: String, email: String, city: String, country: String, biography: String, roles: Set<Role>): UserModel {
            return UserModel(username, hashPassword(password), email, city, country, biography, roles)
        }

        private fun hashPassword(password: String): String {
            return BCrypt.hashpw(password, BCrypt.gensalt())
        }
    }

    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun checkPassword(password: String): Boolean {
        return BCrypt.checkpw(password, this.passwordHash)
    }


}

