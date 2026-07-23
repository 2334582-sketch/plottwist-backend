package com.plottwist.backend.plugins

import java.sql.DriverManager

object LocalDatabase {
    private const val DB_URL = "jdbc:sqlite:plottwist.db"

    init {
        // Crear la tabla de usuarios si no existe al iniciar el servidor
        try {
            DriverManager.getConnection(DB_URL).use { conn ->
                val statement = conn.createStatement()
                statement.execute("""
                    CREATE TABLE IF NOT EXISTS usuarios (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        usuario TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                """.trimIndent())
            }
            println("✅ Base de Datos SQLite inicializada correctamente.")
        } catch (e: Exception) {
            println("❌ Error al inicializar BD: ${e.message}")
        }
    }

    // Función para registrar un usuario nuevo
    fun registrarUsuario(u: String, p: String): Boolean {
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                val stmt = conn.prepareStatement("INSERT INTO usuarios (usuario, password) VALUES (?, ?)")
                stmt.setString(1, u)
                stmt.setString(2, p)
                stmt.executeUpdate() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    // Función para validar login
    fun validarLogin(u: String, p: String): Boolean {
        return try {
            DriverManager.getConnection(DB_URL).use { conn ->
                val stmt = conn.prepareStatement("SELECT id FROM usuarios WHERE usuario = ? AND password = ?")
                stmt.setString(1, u)
                stmt.setString(2, p)
                val resultSet = stmt.executeQuery()
                resultSet.next() // Devuelve true si encontró al usuario
            }
        } catch (e: Exception) {
            false
        }
    }
}