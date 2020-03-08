package com.nicholaspark.router

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.nicholaspark.annotation.ApiResource
import com.nicholaspark.controller.BaseController
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

interface RequestRouterContract {
    fun handleAllRoutes(input: APIGatewayProxyRequestEvent?, context: Context): APIGatewayProxyResponseEvent
}

class RequestRouter(internal val controllers: List<KClass<out BaseController>> = emptyList()) : RequestRouterContract {

    internal val activeControllers: HashMap<String, BaseController> = hashMapOf()
    lateinit var input: APIGatewayProxyRequestEvent
    lateinit var context: Context

    override fun handleAllRoutes(input: APIGatewayProxyRequestEvent?, context: Context): APIGatewayProxyResponseEvent {
        return when (input) {
            null -> APIGatewayProxyResponseEvent().apply { statusCode = 500 }
            else -> {
                this.input = input
                this.context = context
                val resource = input.resource
                routeResourceToController(resource)
            }
        }
    }

    internal fun routeResourceToController(
        resource: String
    ): APIGatewayProxyResponseEvent {
        return try {
            when (val controllerClass = getKClassMatchingApiResource(resource)) {
                null -> createErrorForNoControllerWithMethodDefined(resource)
                else -> getOrInitializeController(controllerClass).handleAllRoutes(context, input)
            }
        } catch (e: java.lang.IllegalStateException) {
            createErrorForNoControllerWithMethodDefined(resource)
        }
    }

    private fun createErrorForNoControllerWithMethodDefined(apiGatewayResource: String) =
        APIGatewayProxyResponseEvent().apply {
            statusCode = 500
            body = "A Controller with @ApiMethod(\"$apiGatewayResource\") must be defined"
        }

    internal fun getKClassMatchingApiResource(apiGatewayResource: String) = controllers.firstOrNull {
        getApiResource(it) == apiGatewayResource
    }

    internal fun getOrInitializeController(
        controllerClass: KClass<out BaseController>
    ): BaseController {
        val resource = getApiResource(controllerClass)
        return when {
            activeControllers.containsKey(resource) -> activeControllers[resource]!!
            else -> {
                return controllerClass.createInstance().apply {
                    activeControllers[resource] = this
                }
            }
        }
    }

    internal fun getApiResource(genericClass: KClass<out BaseController>): String {
        return genericClass.findAnnotation<ApiResource>()?.resource
            ?: throw IllegalStateException("All BaseControllers must be annotated with @ApiResource")
    }
}