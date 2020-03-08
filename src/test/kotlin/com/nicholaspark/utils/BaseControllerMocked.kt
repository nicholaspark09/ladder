package com.nicholaspark.utils

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.nicholaspark.controller.BaseController

class BaseControllerMocked : BaseController() {
    var isSetup = false
    var methodCalled = ""

    override fun setup() {
        isSetup = true
    }

    override fun handleGetMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent().also {
        methodCalled = "GET"
    }

    override fun handlePostMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent().also {
        methodCalled = "POST"
    }

    override fun handleDeleteMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent().also {
        methodCalled = "DELETE"
    }

    override fun handlePutMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent().also {
        methodCalled = "PUT"
    }

    override fun handlePatchMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent().also {
        methodCalled = "PATCH"
    }

    fun reset() {
        isSetup = false
        methodCalled = ""
    }
}