package com.nicholaspark.controller

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.nicholaspark.data.LadderHttpMethod

import com.amazonaws.services.lambda.runtime.Context

interface RouteController {

    fun handleAllRoutes(context: Context, input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent
}

/*
    The BaseController should have the `@ApiResource(resource="")` annotation
    to help route the url to the proper controller
 */
abstract class BaseController() : RouteController {

    abstract fun setup()

    override fun handleAllRoutes(context: Context, input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        setup()
        return when (input.httpMethod) {
            LadderHttpMethod.GET.toString() -> handleGetMethod(input)
            LadderHttpMethod.POST.toString() -> handlePostMethod(input)
            LadderHttpMethod.PUT.toString() -> handlePutMethod(input)
            LadderHttpMethod.PATCH.toString() -> handlePatchMethod(input)
            LadderHttpMethod.DELETE.toString() -> handleDeleteMethod(input)
            else -> handleUnknownHttpMethod(input)
        }
    }

    abstract fun handleGetMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

    abstract fun handlePostMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

    abstract fun handleDeleteMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

    abstract fun handlePutMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

    abstract fun handlePatchMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

    fun handleUnknownHttpMethod(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent {
        return APIGatewayProxyResponseEvent().apply {
            statusCode = 500
            body = "Could not find url method: $input"
        }
    }
}