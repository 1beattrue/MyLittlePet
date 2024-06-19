package edu.mirea.onebeattrue.mylittlepet.data.mapper

import com.google.firebase.auth.FirebaseUser
import edu.mirea.onebeattrue.mylittlepet.data.network.dto.UserDto

fun FirebaseUser.mapEntityToDto() = UserDto(
    token = uid
)