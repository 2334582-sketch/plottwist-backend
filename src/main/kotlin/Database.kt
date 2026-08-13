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

    fun getConnection(): Connection? {
        return try {
            var rawUrl = System.getenv("DATABASE_URL") ?: return null
            if (rawUrl.startsWith("postgres://")) {
                rawUrl = rawUrl.replace("postgres://", "jdbc:postgresql://")
            } else if (rawUrl.startsWith("postgresql://")) {
                rawUrl = rawUrl.replace("postgresql://", "jdbc:postgresql://")
            }
            DriverManager.getConnection(rawUrl)
        } catch (e: Exception) {
            null
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
                stmt.executeUpdate()
                usuariosMemoria[usuario] = pass
                return true
            }
        } catch (e: Exception) {
            // Si la tabla no existe o falla la conexión, usa el respaldo
        } finally {
            conn?.close()
        }

        // 2. Respaldo en memoria local
        if (usuariosMemoria.containsKey(usuario)) {
            return false
        }
        usuariosMemoria[usuario] = pass
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
                }
            }
        } catch (e: Exception) {
            // Si la base falla, pasa a verificar la memoria local
        } finally {
            conn?.close()
        }

        // 2. Respaldo en memoria local
        return usuariosMemoria[usuario] == pass
    }
}