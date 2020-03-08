package com.nicholaspark.controller

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.nicholaspark.data.LadderHttpMethod
import com.nicholaspark.utils.BaseControllerMocked
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BaseControllerTest {

    @MockK
    lateinit var context: Context
    @MockK
    lateinit var input: APIGatewayProxyRequestEvent

    private var controller = BaseControllerMocked()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        controller.reset()
    }

    @Test
    fun handleUnknownMethod() {
        // Given
        val expectedOutput = "details"
        every { input.toString() } returns expectedOutput

        // When
        val result = controller.handleUnknownHttpMethod(input)

        // Then
        assertEquals(500, result.statusCode)
        assertEquals("Could not find url method: $expectedOutput", result.body)
    }

    @Test
    fun `test unknown method in handleAllRoutes`() {
        // Given
        val expectedOutput = "details"
        every { input.toString() } returns expectedOutput
        every { input.httpMethod } returns "not here"

        // When
        val result = controller.handleAllRoutes(context, input)

        // Then
        assertEquals(500, result.statusCode)
        assertEquals("Could not find url method: $expectedOutput", result.body)
    }

    @Test
    fun `test handleGetRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.GET)
    }

    @Test
    fun `test handlePostRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.POST)
    }

    @Test
    fun `test handlePutRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.PUT)
    }

    @Test
    fun `test handlePOSTRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.POST)
    }

    @Test
    fun `test handlePatchRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.PATCH)
    }

    @Test
    fun `test handleDeleteRoutes gets proper methods`() {
        testGivenHttpMethodRoutesAndIsCalled(LadderHttpMethod.DELETE)
    }

    private fun testGivenHttpMethodRoutesAndIsCalled(method: LadderHttpMethod) {
        // Given
        val methodName = method.toString()
        every { input.httpMethod } returns methodName

        // When
        val result = controller.handleAllRoutes(context, input)

        // Then
        assertEquals(methodName, controller.methodCalled)
    }
}