package com.example.ucbp1.features.profile.domain

import com.example.ucbp1.features.profile.domain.model.ProfileModel
import com.example.ucbp1.features.profile.domain.model.value.*
import org.junit.Assert.*
import org.junit.Test

class ProfileModelTest {

    @Test
    fun `Profile se construye correctamente con value objects válidos`() {
        val profile = ProfileModel(
            id = ProfileId("user01"),
            name = ProfileName("Isaac Rivero"),
            email = ProfileEmail("isaac.rivero@ucb.edu.bo"),
            avatarUrl = ProfilePathUrl("https://example.com/default-avatar.png")
        )

        assertEquals("user01", profile.id.value)
        assertEquals("Isaac Rivero", profile.name.value)
        assertEquals("isaac.rivero@ucb.edu.bo", profile.email.value)
        assertEquals("https://example.com/default-avatar.png", profile.avatarUrl.value)
    }
}