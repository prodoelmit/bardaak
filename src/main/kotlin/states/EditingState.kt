package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.DELETED_PREFIX
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class EditingState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        val item = Items.getItem(itemId)!!

        val keyboard = InlineKeyboardMarkup()

        keyboard.addRow(
            InlineKeyboardButton(text = "Name", callbackData = "editName#${item.id}"),
            InlineKeyboardButton(text = "Location", callbackData = "editLocation#${item.id}"),
            InlineKeyboardButton(text = "Photo", callbackData = "editPhoto#${item.id}"),
        )
        if (item.name != null) {
            keyboard.addRow(
                if (!item.name!!.startsWith(DELETED_PREFIX)) {
                    InlineKeyboardButton(text = "Mark deleted", callbackData = "delete#${item.id}")
                } else {
                    InlineKeyboardButton(text = "Unmark deleted", callbackData = "undelete#${item.id}")
                }
            )
        }
        val text = buildString {
            append("You're editing ".escapeForMarkdown())
            append("*${item.markdownSafeName}*")
            append(". What would you like to edit?".escapeForMarkdown())
        }
        sendMarkdown(userId, text, keyboard)
    }
    override fun onLeave(userId: Long) {
    }
}