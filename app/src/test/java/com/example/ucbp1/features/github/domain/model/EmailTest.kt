package com.example.ucbp1.features.github.domain.model

import org.junit.Test
import org.junit.Assert.assertEquals

class EmailTest {
    @Test()
    fun `test input Data Email` (){
        val inputData = "Isaac.Rivero@ucb.com"
        val expected = "isaac.rivero@ucb.com"

        val emailValueObject = Email.create(inputData)
        assertEquals(expected, emailValueObject.value)
    }
}