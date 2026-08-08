package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.util.concurrent.ConcurrentHashMap

// 🗄️ BASE DE DATOS DE USUARIOS (En memoria)
val usuariosDatabase = ConcurrentHashMap<String, String>().apply {
    put("admin", "1234")
    put("estudiante", "mexico2026")
}

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        routing {

            // 📝 1. REGISTRO DE NUEVOS USUARIOS (POST /register)
            post("/register") {
                try {
                    val body = call.receiveText()
                    val usuario = extraerValorJson(body, "usuario")
                    val password = extraerValorJson(body, "password")

                    if (usuario.isEmpty() || password.isEmpty()) {
                        call.respondText(
                            """{"status": "error", "message": "El usuario y la contraseña no pueden estar vacíos."}""",
                            ContentType.Application.Json,
                            HttpStatusCode.BadRequest
                        )
                        return@post
                    }

                    if (usuariosDatabase.containsKey(usuario)) {
                        call.respondText(
                            """{"status": "error", "message": "El nombre de usuario ya está registrado."}""",
                            ContentType.Application.Json,
                            HttpStatusCode.Conflict
                        )
                    } else {
                        usuariosDatabase[usuario] = password
                        println("✅ Nuevo usuario registrado en Ktor: $usuario")

                        call.respondText(
                            """{"status": "success", "message": "Usuario registrado exitosamente."}""",
                            ContentType.Application.Json,
                            HttpStatusCode.Created
                        )
                    }
                } catch (e: Exception) {
                    call.respondText(
                        """{"status": "error", "message": "Error procesando el registro."}""",
                        ContentType.Application.Json,
                        HttpStatusCode.BadRequest
                    )
                }
            }

            // 🔑 2. INICIO DE SESIÓN (POST /login)
            post("/login") {
                try {
                    val body = call.receiveText()
                    val usuario = extraerValorJson(body, "usuario")
                    val password = extraerValorJson(body, "password")

                    val passwordRegistrada = usuariosDatabase[usuario]

                    if (passwordRegistrada != null && passwordRegistrada == password) {
                        call.respondText(
                            """{"status": "success", "usuario": "$usuario", "message": "Autenticación correcta."}""",
                            ContentType.Application.Json,
                            HttpStatusCode.OK
                        )
                    } else {
                        call.respondText(
                            """{"status": "error", "message": "Usuario o contraseña incorrectos."}""",
                            ContentType.Application.Json,
                            HttpStatusCode.Unauthorized
                        )
                    }
                } catch (e: Exception) {
                    call.respondText(
                        """{"status": "error", "message": "Error al procesar el inicio de sesión."}""",
                        ContentType.Application.Json,
                        HttpStatusCode.InternalServerError
                    )
                }
            }

            // 🎮 3. ENDPOINTS DE TRIVIA
            get("/prehispanico") {
                val json = """
                [
                  {
                    "id": 1,
                    "pregunta": "¿Cuál fue la gran capital del imperio Mexica construida sobre el lago de Texcoco?",
                    "opciones": ["Teotihuacán", "Tenochtitlan", "Chichén Itzá", "Tula"],
                    "correcta": 1,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿En qué año exacto se fundó Tenochtitlan según la tradición?",
                    "plotTwistOpciones": ["1325", "1521", "1492", "1810"],
                    "plotTwistCorrecta": 0
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }

            get("/independencia") {
                val json = """
                [
                  {
                    "id": 1,
                    "pregunta": "¿En qué año dio Miguel Hidalgo el famoso 'Grito de Dolores'?",
                    "opciones": ["1810", "1821", "1910", "1857"],
                    "correcta": 0,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿Cómo se llamaba la estandarte que tomó Hidalgo como bandera?",
                    "plotTwistOpciones": ["Virgen de la Soledad", "Virgen de Guadalupe", "Escudo Imperial", "Bandera de las Tres Garantías"],
                    "plotTwistCorrecta": 1
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }

            get("/moderno") {
                val json = """
                [
                  {
                    "id": 1,
                    "pregunta": "¿En qué año se promulgó la Constitución Política actual de México?",
                    "opciones": ["1857", "1917", "1921", "1985"],
                    "correcta": 1,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿En qué teatro se juró dicha Constitución?",
                    "plotTwistOpciones": ["Teatro de la República", "Teatro Juárez", "Palacio de Bellas Artes", "Teatro Degollado"],
                    "plotTwistCorrecta": 0
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}

// 🛠️ Función auxiliar nativa para leer llaves de JSON sin librerías externas
fun extraerValorJson(json: String, clave: String): String {
    val regex = "\"$clave\"\\s*:\\s*\"([^\"]*)\"".toRegex()
    return regex.find(json)?.groupValues?.get(1)?.trim() ?: ""
}