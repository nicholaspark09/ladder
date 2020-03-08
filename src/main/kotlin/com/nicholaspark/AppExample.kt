package com.nicholaspark

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent

class AppExample : RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private val ladder: Ladder by lazy { Ladder.initialize(emptyList()) }

    override fun handleRequest(input: APIGatewayProxyRequestEvent?, context: Context) =
        ladder.handleRequest(input, context)
}