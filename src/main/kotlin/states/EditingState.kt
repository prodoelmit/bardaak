package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot

class EditingState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        val item = Items.getItem(itemId)!!

        val keyboard = InlineKeyboardMarkup()

        keyboard.addRow(
            InlineKeyboardButton(text = "Name", callbackData = "editName#${item.id}"),
            InlineKeyboardButton(text = "Location", callbackData = "editLocation#${item.id}"),
            InlineKeyboardButton(text = "Photo", callbackData = "editPhoto#${item.id}"),
        )

        bot.sendMessage(userId, "What would you like to edit?", {
            replyMarkup(keyboard)
        })
    }
    override fun onLeave(userId: Long) {
    }
}