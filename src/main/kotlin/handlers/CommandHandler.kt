package org.prodoelmit.handlers

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.MessageEntity
import com.pengrad.telegrambot.model.Update
import org.prodoelmit.allowedUserIds
import org.prodoelmit.states.FetchingItemState
import org.prodoelmit.states.ShowAllParentsState
import org.prodoelmit.states.ShowInsideState
import org.prodoelmit.states.States
import org.prodoelmit.states.restart
import org.prodoelmit.userId

object CommandHandler : IUpdateHandler {
    override fun handleUpdate(update: Update) {
        println("CommandHandler: ${update.message()}")

        val message = update.message().text()
        val fullCommand = message.removePrefix("/").substringBefore(" ")

        val command = fullCommand.substringBefore("_")
        val itemId = fullCommand.substringAfter("_")

        when (command) {
            "restart" -> States.restart(update.userId())
            "show" -> {
                check(itemId.isNotEmpty()) { "Item id can't be empty" }
                States.setState(update.userId(), FetchingItemState(itemId))
            }
            "inside" -> {
                checkNotNull(itemId)
                States.setState(update.userId(), ShowInsideState(itemId.toInt()))
            }
            "allparents" -> {
                States.setState(update.userId(), ShowAllParentsState())
            }
        }
    }

    override fun checkUpdate(update: Update): Boolean {
        if (update.message() == null) return false
        if (update.message().from().id() !in allowedUserIds) return false

        val entities = update.message().entities()

        if (entities == null) return false
        if (entities.none {
                it.type() == MessageEntity.Type.bot_command
            }) return false


        return true
    }
}