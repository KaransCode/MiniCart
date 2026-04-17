package com.example.minicart.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single user for this demo
    val name: String,
    val email: String,
    val profileImageUri: String? = null,
    val receiveNotifications: Boolean = true,
    val preferredCategory: String = "Electronics"
)
