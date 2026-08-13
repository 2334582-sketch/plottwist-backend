package com.plottwist.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import com.plottwist.backend.*

// Clases DTO para recibir el JSON del Login/Registro desde Android
@Serializable
data class UserRequest(val usuario: String, val password: String)

fun Application.configureRouting() {
    // Forzamos la inicialización de la Base de Datos
    LocalDatabase

    routing {
        // RUTA PRINCIPAL
        get("/") {
            call.respondText("¡Servidor de Historia de México Activo, Jefe!", status = HttpStatusCode.OK)
        }

        // ==========================================
        // 🔐 ENDPOINTS DE AUTENTICACIÓN (BASE DE DATOS)
        // ==========================================

        // 1. REGISTRO DE USUARIO
        post("/registro") {
            try {
                val req = call.receive<UserRequest>()
                val exito = LocalDatabase.registrarUsuario(req.usuario, req.password)

                if (exito) {
                    call.respondText("""{"status":"success","mensaje":"Usuario registrado correctamente"}""", ContentType.Application.Json, HttpStatusCode.Created)
                } else {
                    call.respondText("""{"status":"error","mensaje":"El usuario ya existe o datos inválidos"}""", ContentType.Application.Json, HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("""{"status":"error","mensaje":"Error en el formato JSON"}""", ContentType.Application.Json, HttpStatusCode.BadRequest)
            }
        }

        // 2. INICIO DE SESIÓN
        post("/login") {
            try {
                val req = call.receive<UserRequest>()
                val esValido = LocalDatabase.validarLogin(req.usuario, req.password)

                if (esValido) {
                    call.respondText("""{"status":"success","usuario":"${req.usuario}"}""", ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respondText("""{"status":"error","mensaje":"Usuario o contraseña incorrectos"}""", ContentType.Application.Json, HttpStatusCode.Unauthorized)
                }
            } catch (e: Exception) {
                call.respondText("""{"status":"error","mensaje":"Formato inválido"}""", ContentType.Application.Json, HttpStatusCode.BadRequest)
            }
        }

        // ==========================================
        // 📚 ENDPOINTS DE LA TRIVIA
        // ==========================================

        // ENDPOINT 1: RAMA MAIN (Prehispánico)
        get("/prehispanico") {
            val jsonMain = """
            {
                "status": "success",
                "titulo": "Época Prehispánica: Tenochtitlán",
                "introduccion": "¡Hola! Soy Quetzal el ajolote. Bienvenidos a Tenochtitlán.",
                "preguntaQuiz": "¿Sobre qué lago se fundó Tenochtitlán?",
                "opciones": ["Lago de Texcoco", "Lago de Chapala"]
            }
            """.trimIndent()
            call.respondText(jsonMain, ContentType.Application.Json)
        }

        // ENDPOINT 2: RAMA ALI (Independencia)
        get("/independencia") {
            val jsonAli = """
            {
                "status": "success",
                "titulo": "Independencia y Revolución",
                "preguntaQuiz": "¿Quién es considerado el Padre de la Patria?",
                "opciones": ["Miguel Hidalgo", "Porfirio Díaz"]
            }
            """.trimIndent()
            call.respondText(jsonAli, ContentType.Application.Json)
        }

        // ENDPOINT 3: RAMA DAYANA (México Moderno)
        get("/moderno") {
            val jsonDayana = """
            {
                "status": "success",
                "titulo": "México Moderno",
                "preguntaQuiz": "¿En qué año se promulgó la Constitución actual?",
                "opciones": ["1917", "1810"]
            }
            """.trimIndent()
            call.respondText(jsonDayana, ContentType.Application.Json)
        }
    }
}