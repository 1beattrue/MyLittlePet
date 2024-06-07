package edu.mirea.onebeattrue.mylittlepet.data.mapper

import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.data.remote.dto.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() {
    fun mapUserEntityToDto(user: FirebaseUser): UserDto =
        UserDto(user.uid)



    companion object {
        private const val UNDEFINED_ID = 0L
    }
}