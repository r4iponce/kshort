package wf.ada.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

object Database {
    fun migrate() {
        Flyway
            .configure()
            .dataSource(HikariDataSource(hikari()))
            .load()
            .migrate()
    }

    fun connect() {
        Database.connect(hikari())
    }

    private fun hikari(): HikariDataSource {
        val config =
            HikariConfig().apply {
                driverClassName = Config.driver
                jdbcUrl = Config.dbUrl
                password = Config.password
                username = Config.user
                maximumPoolSize = 3
                isAutoCommit = false
                transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                validate()
            }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }
}
