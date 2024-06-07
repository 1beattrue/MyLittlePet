package edu.mirea.onebeattrue.mylittlepet.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class UserDto {
    @SerializedName("token") String token;

    public UserDto(String token) {
        this.token = token;
    }
}
