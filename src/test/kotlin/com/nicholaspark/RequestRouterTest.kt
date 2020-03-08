package com.nicholaspark

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.nicholaspark.utils.TestController
import com.nicholaspark.utils.TestControllerWithoutResource
import com.nicholaspark.data.LadderHttpMethod
import com.nicholaspark.router.RequestRouter
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame
import kotlin.test.assertTrue

class RequestRouterTest {

    lateinit var requestRouter: RequestRouter
    val testController = TestController::class
    val controllers = listOf(TestController::class)
    @MockK
    lateinit var context: Context

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        requestRouter = RequestRouter(controllers)
        requestRouter.activeControllers.clear()
    }

    @Test
    fun `test null input results in 500`() {
        // Given
        val input = null

        // When
        val result = requestRouter.handleAllRoutes(input, context)

        // Then
        assertEquals(500, result)
    }

    @Test
    fun `test input and context get set`() {
        // Given
        val input = givenApplesRequestEvent()

        // When
        val result = requestRouter.handleAllRoutes(input, context)

        // Then
        assertEquals(input, requestRouter.input)
        assertEquals(context, requestRouter.context)
    }

    @Test
    fun `test No Matching Controller results in ApiGatewayResponse of 500`() {
        // Given
        val resource = "/oranges"

        // When
        val result = requestRouter.routeResourceToController("/oranges")

        // Then
        assertEquals(500, result.statusCode)
        assertEquals("A Controller with @ApiMethod(\"$resource\") must be defined", result.body)
    }

    @Test
    fun `test routeResourceToController exception gets converted to Error Response Event`() {
        // Given
        val resource = "/oranges"

        // When
        val result = requestRouter.routeResourceToController(resource)

        // Then
        assertEquals(500, result.statusCode)
        assertEquals("A Controller with @ApiMethod(\"$resource\") must be defined", result.body)
    }

    @Test
    fun `test controllerHandlesRoute from routeResourceToController`() {
        // Given
        val requestEvent = givenApplesRequestEvent()

        // When
        val result = requestRouter.handleAllRoutes(requestEvent, context)

        // Then
        assertEquals(200, result.statusCode)
    }

    @Test
    fun `test getClassMatchingApiResource is first controller with resource`() {
        // Given
        val resource = "/apples"

        // When
        val result = requestRouter.getKClassMatchingApiResource(resource)

        // Then
        assertEquals(testController, result)
    }

    @Test
    fun `test getOrInitializeController initializes controller`() {
        // Given
        val expectedResource = "/apples"
        assertEquals(0, requestRouter.activeControllers.size)

        // When
        val result = requestRouter.getOrInitializeController(testController)

        // Then
        assertTrue(result is TestController)
        assertTrue(requestRouter.activeControllers.contains(expectedResource))
    }

    @Test
    fun `test getOrInitializeController returns controller`() {
        // Given
        assertEquals(0, requestRouter.activeControllers.size)
        val expectedController = requestRouter.getOrInitializeController(testController)
        assertEquals(1, requestRouter.activeControllers.size)

        // When
        val result = requestRouter.getOrInitializeController(testController)

        // Then
        assertSame(expectedController, result)
    }

    @Test
    fun getApiResource_testAnnotationFound() {
        // Given
        val expected = "/apples"
        val controller = controllers.first()

        // When
        val result = requestRouter.getApiResource(controller)

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `calling getApiResource on controller without annotation will return an exception`() {
        // Given
        val controllerWithoutResource = TestControllerWithoutResource::class

        // When/Then
        assertFailsWith<IllegalStateException> {
            requestRouter.getApiResource(controllerWithoutResource)
        }
    }

    private fun givenApplesRequestEvent() = APIGatewayProxyRequestEvent().apply {
        httpMethod = LadderHttpMethod.GET.toString()
        resource = "/apples"
    }
}