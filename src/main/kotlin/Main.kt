package org.prodoelmit

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.MenuButton
import com.pengrad.telegrambot.model.MenuButtonCommands
import com.pengrad.telegrambot.request.SetChatMenuButton
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.prodoelmit.handlers.CallbackQueryHandler
import org.prodoelmit.handlers.CommandHandler
import org.prodoelmit.handlers.IUpdateHandler
import org.prodoelmit.handlers.LogAnythingHandler
import org.prodoelmit.handlers.MessageHandler
import java.sql.Connection


val botToken = System.getenv("TELEGRAM_BOT_TOKEN")
val allowedUserIds = Environment.ALLOWED_USER_IDS.split(",").map { it.trim().toLong()}

val bot = TelegramBot(botToken)
fun main() {

    initDatabase()
    initBot()


}

fun initDatabase() {
    DatabaseManager.initializeDatabaseWithValidation()
}

fun initBot() {

    val handlers = listOf(
        MessageHandler,
        CommandHandler,
        CallbackQueryHandler,
        LogAnythingHandler,
    )

    bot.setUpdatesListener { updates ->
        updates.forEach {  update ->
            handlers.forEach { handler -> handler.tryHandleUpdate(update) }
        }
        UpdatesListener.CONFIRMED_UPDATES_ALL
    }
}
