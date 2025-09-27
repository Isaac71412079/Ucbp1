package com.example.ucbp1.features.profile.domain.value

import com.example.ucbp1.features.profile.domain.model.value.ProfilePathUrl
import org.junit.Assert
import org.junit.Test
import org.junit.Assert.assertEquals

class ProfilePathUrlTest {
    @Test
    fun `ProfileAvatarUrl devuelve valor por defecto si es null`() {
        val avatar = ProfilePathUrl(null)
        Assert.assertEquals("https://example.com/default-avatar.png", avatar.orDefault())
    }
}