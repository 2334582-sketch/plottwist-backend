package com.plottwist.backend

import com.plottwist.backend.plugins.configureRouting
import com.plottwist.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val envPort = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(Netty, port = envPort, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}