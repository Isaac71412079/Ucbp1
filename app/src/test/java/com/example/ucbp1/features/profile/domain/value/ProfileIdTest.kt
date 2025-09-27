package com.example.ucbp1.features.profile.domain.value

import com.example.ucbp1.features.profile.domain.model.value.ProfileId
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals

class ProfileIdTest {
    @Test
    fun `ProfileId acepta un string válido`() {
        val id = ProfileId("user01")
        Assert.assertEquals("user01", id.value)
    }
}