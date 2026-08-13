package com.plottwist.backend

import java.sql.Connection
import java.sql.DriverManager

object LocalDataBase {
    fun getConnection(): Connection? {
        return try {
            var rawUrl = System.getenv("DATABASE_URL") ?: return null

            // Railway entrega la URL como 'postgres://', pero JDBC requiere 'jdbc:postgresql://'
            if (rawUrl.startsWith("postgres://")) {
                rawUrl = rawUrl.replace("postgres://", "jdbc:postgresql://")
            } else if (rawUrl.startsWith("postgresql://")) {
                rawUrl = rawUrl.replace("postgresql://", "jdbc:postgresql://")
            }

            DriverManager.getConnection(rawUrl)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}