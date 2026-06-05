package com.plottwist.backend

import com.plottwist.configureRouting
import com.plottwist.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val envPort = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(Netty, port = envPort, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}