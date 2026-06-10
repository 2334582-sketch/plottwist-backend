package com.plottwist.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.configureRouting() {
    routing {
        // RUTA PRINCIPAL
        get("/") {
            call.respondText("¡Servidor de Historia de México Activo, Jefe!")
        }

        // ENDPOINT 1: RAMA MAIN (Paco)
        get("/prehispanico") {
            val jsonMain = """
                {
                  "status": "success",
                  "titulo": "Época Prehispánica: Tenochtitlán",
                  "introduccion": "¡Hola! Soy Quetzal el ajolote. Bienvenidos a la gran Tenochtitlán, una ciudad flotante increíble.",
                  "datoCurioso": "Los mexicas construían islas artificiales llamadas chinampas para cultivar su comida sobre el agua.",
                  "preguntaQuiz": "¿Sobre qué lago se fundó Tenochtitlán?",
                  "opciones": ["Lago de Texcoco", "Lago de Chapala"],
                  "correcta": 0
                }
            """.trimIndent()
            call.respondText(jsonMain, contentType = ContentType.Application.Json)
        }

        // ENDPOINT 2: RAMA 1 (Ali)
        get("/independencia") {
            val jsonAli = """
                {
                  "status": "success",
                  "titulo": "La Independencia y Revolución",
                  "introduccion": "¡Viva México! Bienvenidos a la época de la lucha por la libertad y la soberanía nacional.",
                  "datoCurioso": "El campaneo de Miguel Hidalgo en Dolores no fue en la mañana, sino en la noche del 15 de septiembre.",
                  "preguntaQuiz": "¿Quién es conocido como el Padre de la Patria?",
                  "opciones": ["Miguel Hidalgo", "Porfirio Díaz"],
                  "correcta": 0
                }
            """.trimIndent()
            call.respondText(jsonAli, contentType = ContentType.Application.Json)
        }

        // ENDPOINT 3: RAMA 2 (Dayana)
        get("/moderno") {
            val jsonDayana = """
                {
                  "status": "success",
                  "titulo": "México Moderno",
                  "introduccion": "¡Bienvenidos al siglo XXI! Analizando los cambios políticos y sociales del México actual.",
                  "datoCurioso": "La Constitución actual que rige a todo México se promulgó en la ciudad de Querétaro en el año 1917.",
                  "preguntaQuiz": "¿En qué año se promulgó la Constitución Mexicana vigente?",
                  "opciones": ["1917", "1810"],
                  "correcta": 0
                }
            """.trimIndent()
            call.respondText(jsonDayana, contentType = ContentType.Application.Json)
        }
    }
}