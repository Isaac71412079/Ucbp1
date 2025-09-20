package com.example.ucbp1.features.github.domain.model

import org.junit.Test

class UrlPathTest {
    @Test(expected = Exception::class)
    fun `test UrlPath`() {
        UrlPath("myurl")
        UrlPath("")
    }
}