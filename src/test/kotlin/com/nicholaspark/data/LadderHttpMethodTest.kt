package com.nicholaspark.data

import junit.framework.Assert.assertTrue
import org.junit.Test

class LadderHttpMethodTest {

    val methods = LadderHttpMethod.values()

    @Test
    fun `test that toString uppercases the names`() {
        // When
        val results = methods.map {
            it.toString()
        }

        // Then
        results.forEach {
            it.toCharArray().forEach { char ->
                assertTrue(char.isUpperCase())
            }
        }
    }
}