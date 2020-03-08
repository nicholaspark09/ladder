package com.nicholaspark.utils

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.nicholaspark.controller.BaseController

class TestControllerWithoutResource : BaseController() {

    override fun setup() {
    }

    override fun handleGetMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent()

    override fun handlePostMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent()

    override fun handleDeleteMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent()

    override fun handlePutMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent()

    override fun handlePatchMethod(input: APIGatewayProxyRequestEvent) = APIGatewayProxyResponseEvent()

}