package org.prodoelmit

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.concurrent.atomic.AtomicBoolean

object DatabaseManager {
    private var isInitialized = false
    val databasePath = Environment.DB_PATH

    fun initializeDatabaseWithValidation() {
        if (isInitialized) return

        Database.connect(
            url = "jdbc:sqlite:$databasePath",
            driver = "org.sqlite.JDBC"
        )

        transaction {
            // Check if tables exist, create if they don't
            if (!SchemaUtils.checkCycle(ItemsTable)) {
                SchemaUtils.create(ItemsTable)
                println("Created new tables")
            } else {
                println("Tables already exist")
            }
        }

        isInitialized = true
        println("Database initialized and validated at: $databasePath")
    }
}