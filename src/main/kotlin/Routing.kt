package com.plottwist.backend.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

fun Application.configureRouting() {
    routing {
        // RUTA PRINCIPAL (Para probar que el servidor sirva en el navegador)
        get("/") {
            call.respondText("¡Servidor de Historia de México Activo, Jefe!")
        }

        // END-POINT ÚNICO: Datos de las Etapas (Servicio Global para el Front-end)
        get("/etapas") {
            val jsonEtapas = """
                [
                  {
                    "id": 1,
                    "titulo": "Época Prehispánica: Tenochtitlán",
                    "introduccion": "¡Hola! Soy Quetzal el ajolote. Bienvenidos a la gran Tenochtitlán, una ciudad flotante increíble.",
                    "datoCurioso": "Sabías que los mexicas construían islas artificiales llamadas chinampas para cultivar su comida?",
                    "preguntaQuiz": "¿Sobre qué lago se fundó Tenochtitlán?",
                    "opciones": ["Lago de Texcoco", "Lago de Pátzcuaro", "Lago de Chapala"],
                    "correcta": 0
                  },
                  {
                    "id": 2,
                    "titulo": "La Conquista: Llegada de Cortés",
                    "introduccion": "En 1519, extraños hombres con armaduras llegaron en barcos gigantescos desde el mar.",
                    "datoCurioso": "Moctezuma pensó al principio que Hernán Cortés era el dios Quetzalcóatl que regresaba.",
                    "preguntaQuiz": "¿En qué año llegaron los españoles a Tenochtitlán?",
                    "opciones": ["1510", "1519", "1521"],
                    "correcta": 1
                  },
                  {
                    "id": 3,
                    "titulo": "La Independencia: El Grito de Dolores (Rama Ali)",
                    "introduccion": "Espacio reservado para la introducción de Ali...",
                    "datoCurioso": "Espacio reservado para el dato curioso de Ali...",
                    "preguntaQuiz": "Pregunta de Ali pendiente",
                    "opciones": ["Opción A", "Opción B", "Opción C"],
                    "correcta": 0
                  },
                  {
                    "id": 4,
                    "titulo": "La Revolución Mexicana (Rama2)",
                    "introduccion": "Espacio reservado para la introducción de Ali...",
                    "datoCurioso": "Espacio reservado para el dato curioso de Ali...",
                    "preguntaQuiz": "Pregunta de Ali pendiente",
                    "opciones": ["Opción A", "Opción B", "Opción C"],
                    "correcta": 0
                  },
                  {
                    "id": 5,
                    "titulo": "México Moderno (Rama1)",
                    "introduccion": "Espacio reservado para la introducción de Dayana...",
                    "datoCurioso": "Espacio reservado para el dato curioso de Dayana...",
                    "preguntaQuiz": "Pregunta de Dayana pendiente",
                    "opciones": ["Opción A", "Opción B", "Opción C"],
                    "correcta": 0
                  }
                ]
            """.trimIndent()
            call.respondText(jsonEtapas, contentType = ContentType.Application.Json)
        }
    }
}