package com.plottwist.backend

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.http.*
import java.util.concurrent.ConcurrentHashMap

val usuariosDatabase = ConcurrentHashMap<String, String>().apply {
    put("admin", "1234")
    put("estudiante", "mexico2026")
}

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT")?.toInt() ?: 8080) {
        routing {

            post("/register") {
                try {
                    val body = call.receiveText()
                    val usuario = extraerValorJson(body, "usuario")
                    val password = extraerValorJson(body, "password")

                    if (usuario.isEmpty() || password.isEmpty()) {
                        call.respondText("""{"status": "error", "message": "Campos vacíos."}""", ContentType.Application.Json, HttpStatusCode.BadRequest)
                        return@post
                    }

                    if (usuariosDatabase.containsKey(usuario)) {
                        call.respondText("""{"status": "error", "message": "El usuario ya existe."}""", ContentType.Application.Json, HttpStatusCode.Conflict)
                    } else {
                        usuariosDatabase[usuario] = password
                        call.respondText("""{"status": "success", "message": "Usuario registrado."}""", ContentType.Application.Json, HttpStatusCode.Created)
                    }
                } catch (e: Exception) {
                    call.respondText("""{"status": "error"}""", ContentType.Application.Json, HttpStatusCode.BadRequest)
                }
            }

            post("/login") {
                try {
                    val body = call.receiveText()
                    val usuario = extraerValorJson(body, "usuario")
                    val password = extraerValorJson(body, "password")

                    if (usuariosDatabase[usuario] == password) {
                        call.respondText("""{"status": "success", "usuario": "$usuario"}""", ContentType.Application.Json, HttpStatusCode.OK)
                    } else {
                        call.respondText("""{"status": "error", "message": "Credenciales inválidas."}""", ContentType.Application.Json, HttpStatusCode.Unauthorized)
                    }
                } catch (e: Exception) {
                    call.respondText("""{"status": "error"}""", ContentType.Application.Json, HttpStatusCode.InternalServerError)
                }
            }

            get("/usuarios") {
                val lista = usuariosDatabase.keys().toList().joinToString(prefix = "[", postfix = "]", separator = ", ") { "\"$it\"" }
                call.respondText(lista, ContentType.Application.Json)
            }

            // 🎮 Nivel 1: Prehispánico
            get("/prehispanico") {
                val json = """
                [
                  {
                    "id": 1,
                    "historia": "¡Hola! Soy Ajolotito 🦎. Hace cientos de años, los Mexicas llegaron al Lago de Texcoco buscando una señal divina: un águila devorando una serpiente sobre un nopal. ¡Ahí fundaron su gran imperio!",
                    "pregunta": "¿Cuál fue la gran capital del imperio Mexica construida sobre el lago?",
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

            // 🎮 Nivel 2: La Conquista
            get("/conquista") {
                val json = """
                [
                  {
                    "id": 2,
                    "historia": "¡Viajemos a 1519! ⛵ Hernán Cortés desembarcó en las costas de Veracruz. Moctezuma II lo recibió pensando que podría ser la profecía del regreso del dios Quetzalcóatl.",
                    "pregunta": "¿Quién era el tlatoani Mexica que gobernaba Tenochtitlan a la llegada de Cortés?",
                    "opciones": ["Cuauhtémoc", "Moctezuma II", "Cuitláhuac", "Nezahualcóyotl"],
                    "correcta": 1,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿Cómo se le conoce a la batalla donde Cortés lloró bajo un ahuehuete tras ser derrotado?",
                    "plotTwistOpciones": ["La Noche Triste", "La Batalla de Otumba", "La Caída de Tenochtitlan", "La Noche Victoriosa"],
                    "plotTwistCorrecta": 0
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }

            // 🎮 Nivel 3: Independencia
            get("/independencia") {
                val json = """
                [
                  {
                    "id": 3,
                    "historia": "¡Corre la madrugada del 16 de septiembre de 1810! 🔔 El cura Miguel Hidalgo tocó las campanas del templo de Dolores para convocar al pueblo a levantarse en armas.",
                    "pregunta": "¿En qué año dio Miguel Hidalgo el famoso 'Grito de Dolores'?",
                    "opciones": ["1810", "1821", "1910", "1857"],
                    "correcta": 0,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿Qué imagen tomó Hidalgo como primer estandarte del movimiento?",
                    "plotTwistOpciones": ["La Virgen de Soledad", "La Virgen de Guadalupe", "El Escudo Nacional", "La Bandera Trigarante"],
                    "plotTwistCorrecta": 1
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }

            // 🎮 Nivel 4: Revolución Mexicana
            get("/revolucion") {
                val json = """
                [
                  {
                    "id": 4,
                    "historia": "¡Tierra y Libertad! 🤠 Tras más de 30 años del gobierno de Porfirio Díaz, Francisco I. Madero llamó al pueblo a tomar las armas el 20 de noviembre de 1910.",
                    "pregunta": "¿Qué caudillo del sur acuñó el lema 'Tierra y Libertad'?",
                    "opciones": ["Pancho Villa", "Emiliano Zapata", "Venustiano Carranza", "Álvaro Obregón"],
                    "correcta": 1,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿Cómo se llamaba el plan redactado por Madero para llamar a la Revolución?",
                    "plotTwistOpciones": ["Plan de San Luis", "Plan de Ayala", "Plan de Guadalupe", "Plan de Iguala"],
                    "plotTwistCorrecta": 0
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }

            // 🎮 Nivel 5: México Moderno
            get("/moderno") {
                val json = """
                [
                  {
                    "id": 5,
                    "historia": "¡Llegamos al México Contemporáneo! 📜 En 1917, en Querétaro, se redactó la carta magna que rige los derechos sociales, la educación y la tierra en nuestro país.",
                    "pregunta": "¿En qué año se promulgó la Constitución Política actual de México?",
                    "opciones": ["1857", "1917", "1921", "1985"],
                    "correcta": 1,
                    "plotTwistPregunta": "¡PLOT TWIST! ¿En qué histórico recinto de Querétaro se firmó?",
                    "plotTwistOpciones": ["Teatro de la República", "Teatro Juárez", "Palacio de Bellas Artes", "Catedral de Querétaro"],
                    "plotTwistCorrecta": 0
                  }
                ]
                """.trimIndent()
                call.respondText(json, ContentType.Application.Json)
            }
        }
    }.start(wait = true)
}

fun extraerValorJson(json: String, clave: String): String {
    val regex = "\"$clave\"\\s*:\\s*\"([^\"]*)\"".toRegex()
    return regex.find(json)?.groupValues?.get(1)?.trim() ?: ""
}