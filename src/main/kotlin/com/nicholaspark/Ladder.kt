/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.nicholaspark

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.nicholaspark.controller.BaseController
import com.nicholaspark.router.RequestRouter
import com.nicholaspark.router.RequestRouterContract
import kotlin.reflect.KClass

class Ladder(
    val controllers: List<KClass<out BaseController>>,
    val requestRouter: RequestRouterContract = RequestRouter(controllers)
) {

    fun handleRequest(input: APIGatewayProxyRequestEvent?, context: Context) =  requestRouter.handleAllRoutes(input, context)

    companion object {

        @Volatile
        var INSTANCE: Ladder? = null

        @JvmStatic
        @Synchronized
        fun initialize(controllers: List<KClass<out BaseController>>): Ladder {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Ladder(controllers)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}
