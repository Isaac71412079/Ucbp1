package com.example.ucbp1.features.profile.domain.value

import com.example.ucbp1.features.profile.domain.model.value.ProfileEmail
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals

class ProfileEmailTest {
    @Test()
    fun `ProfileEmail acepta un correo válido`() {
        val email = ProfileEmail("isaactest@ucb.edu.bo")
        Assert.assertEquals("isaactest@ucb.edu.bo", email.value)
    }
}