package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        routing {
            // FN-06: Endpoint de Login (POST)
            post("/login") {
                val body = call.receiveText()
                println("Intento de login recibido: $body")
                call.respondText(
                    """{"status": "success", "message": "Autenticación correcta en Railway"}""",
                    ContentType.Application.Json
                )
            }

            // FN-07: Endpoints GET para Módulos de Trivia
            get("/prehispanico") {
                call.respondText("✅ [Railway Backend] Módulo Prehispánico conectado correctamente.")
            }

            get("/independencia") {
                call.respondText("✅ [Railway Backend] Módulo Independencia y Revolución conectado correctamente.")
            }

            get("/moderno") {
                call.respondText("✅ [Railway Backend] Módulo México Moderno conectado correctamente.")
            }
        }
    }.start(wait = true)
}