package org.prodoelmit.handlers

import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.utility.kotlin.extension.request.removeInlineKeyboard
import org.prodoelmit.Items
import org.prodoelmit.allowedUserIds
import org.prodoelmit.bot
import org.prodoelmit.states.DeleteItemState
import org.prodoelmit.states.EditingLocationState
import org.prodoelmit.states.EditingNameState
import org.prodoelmit.states.EditingPhotoState
import org.prodoelmit.states.FetchingItemState
import org.prodoelmit.states.States
import org.prodoelmit.states.EditingState
import org.prodoelmit.states.ShowInsideState
import org.prodoelmit.states.UndeleteItemState

object CallbackQueryHandler : IUpdateHandler {
    override fun handleUpdate(update: Update) {
        val callbackQuery = update.callbackQuery()
        println("Callback query: $callbackQuery")
        val data = callbackQuery.data()

        val parts = data.trim().split("#")
        if (parts.size > 2) {
            println("Invalid callback query")
        }

        val method = parts[0]
        val itemId = parts.getOrNull(1)?.toInt()

        val userId = callbackQuery.from().id()
        bot.removeInlineKeyboard(userId, callbackQuery.maybeInaccessibleMessage().messageId())

        when (method) {
            "editName" -> {
                checkNotNull(itemId)
                States.setState(userId, EditingNameState(itemId))
            }
            "editLocation" -> {
                checkNotNull(itemId)
                States.setState(userId, EditingLocationState(itemId))
            }
            "editPhoto" -> {
                checkNotNull(itemId)
                States.setState(userId, EditingPhotoState(itemId))
            }
            "showItem" -> {
                checkNotNull(itemId)
                val item = Items.getItem(itemId)
                States.setState(userId, FetchingItemState("$itemId"))
            }
            "edit" -> {
                checkNotNull(itemId)
                States.setState(userId, EditingState(itemId))
            }
            "showInside" -> {
                checkNotNull(itemId)
                States.setState(userId, ShowInsideState(itemId))
            }
            "delete" -> {
                checkNotNull(itemId)
                States.setState(userId, DeleteItemState(itemId))
            }
            "undelete" -> {
                checkNotNull(itemId)
                States.setState(userId, UndeleteItemState(itemId))
            }
        }
    }

    override fun checkUpdate(update: Update): Boolean {
        if (update.callbackQuery() == null) return false
        if (update.callbackQuery().from().id() !in allowedUserIds) return false
        return true
    }
}