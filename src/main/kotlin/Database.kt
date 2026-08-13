package com.plottwist.backend

import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.ConcurrentHashMap

object LocalDatabase {
    // Memoria local de respaldo por si PostgreSQL no responde
    private val usuariosMemoria = ConcurrentHashMap<String, String>().apply {
        put("admin", "1234")
        put("estudiante", "mexico2026")
    }

    init {
        // 🚀 Crea la tabla automáticamente en PostgreSQL al iniciar el servidor
        crearTablaSiNoExiste()
    }

    fun getConnection(): Connection? {
        return try {
            // Asegura cargar el driver JDBC de PostgreSQL
            Class.forName("org.postgresql.Driver")
            var rawUrl = System.getenv("DATABASE_URL") ?: return null

            if (rawUrl.startsWith("postgres://")) {
                rawUrl = rawUrl.replace("postgres://", "jdbc:postgresql://")
            } else if (rawUrl.startsWith("postgresql://")) {
                rawUrl = rawUrl.replace("postgresql://", "jdbc:postgresql://")
            }
            DriverManager.getConnection(rawUrl)
        } catch (e: Exception) {
            println("❌ Error al conectar a PostgreSQL: ${e.message}")
            null
        }
    }

    private fun crearTablaSiNoExiste() {
        var conn: Connection? = null
        try {
            conn = getConnection()
            if (conn != null) {
                val stmt = conn.createStatement()
                stmt.executeUpdate(
                    """
                    CREATE TABLE IF NOT EXISTS "Usuarios" (
                        usuario VARCHAR(100) PRIMARY KEY,
                        password VARCHAR(100) NOT NULL
                    );
                    """.trimIndent()
                )
                println("✅ Tabla 'Usuarios' verificada / creada exitosamente en PostgreSQL.")
            }
        } catch (e: Exception) {
            println("⚠️ No se pudo verificar la tabla en PostgreSQL: ${e.message}")
        } finally {
            conn?.close()
        }
    }

    fun registrarUsuario(usuario: String, pass: String): Boolean {
        if (usuario.isBlank() || pass.isBlank()) return false

        // 1. Intenta guardar en PostgreSQL
        var conn: Connection? = null
        try {
            conn = getConnection()
            if (conn != null) {
                val stmt = conn.prepareStatement("INSERT INTO \"Usuarios\" (usuario, password) VALUES (?, ?)")
                stmt.setString(1, usuario)
                stmt.setString(2, pass)
                val filasAfectadas = stmt.executeUpdate()
                if (filasAfectadas > 0) {
                    usuariosMemoria[usuario] = pass
                    println("💾 Usuario registrado en PostgreSQL: $usuario")
                    return true
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error en Insert PostgreSQL: ${e.message}")
            // Si el usuario ya existe en PostgreSQL, rechazamos el registro
            if (e.message?.contains("duplicate key") == true || e.message?.contains("already exists") == true) {
                return false
            }
        } finally {
            conn?.close()
        }

        // 2. Respaldo en memoria local (solo si la BD Postgres no está disponible)
        if (usuariosMemoria.containsKey(usuario)) {
            return false
        }
        usuariosMemoria[usuario] = pass
        println("📝 Usuario registrado en memoria local: $usuario")
        return true
    }

    fun validarLogin(usuario: String, pass: String): Boolean {
        if (usuario.isBlank() || pass.isBlank()) return false

        // 1. Intenta validar contra PostgreSQL
        var conn: Connection? = null
        try {
            conn = getConnection()
            if (conn != null) {
                val stmt = conn.prepareStatement("SELECT password FROM \"Usuarios\" WHERE usuario = ?")
                stmt.setString(1, usuario)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val passDb = rs.getString("password")
                    return passDb == pass
                } else {
                    // Si consultó Postgres exitosamente y el usuario NO existe
                    return false
                }
            }
        } catch (e: Exception) {
            println("⚠️ Error al consultar PostgreSQL, pasando a verificación local: ${e.message}")
        } finally {
            conn?.close()
        }

        // 2. Respaldo en memoria local
        return usuariosMemoria[usuario] == pass
    }
}