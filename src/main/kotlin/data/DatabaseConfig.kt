package com.example.modules.data

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    private val hikari: HikariDataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = System.getenv("DB_URL") ?: throw(IllegalArgumentException("DB URL is required."))
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DB_USER") ?: throw(IllegalArgumentException("DB user is required."))
            password = System.getenv("DB_PASSWORD") ?: throw(IllegalArgumentException("DB password is required."))
            maximumPoolSize = 10
            isAutoCommit = true
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        HikariDataSource(config)
    }

    val db: Database by lazy {
        Database.connect(hikari)
    }
}