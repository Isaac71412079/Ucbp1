package com.example.ucbp1.features.profile.domain.value

import com.example.ucbp1.features.profile.domain.model.value.ProfileName
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals

class ProfileNameTest {
    @Test(expected = IllegalArgumentException::class)
    fun `ProfileName no acepta nombre vacío`() {
        ProfileName("")
    }
}