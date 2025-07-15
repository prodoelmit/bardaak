package org.prodoelmit.handlers

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

interface IUpdateHandler {
    fun tryHandleUpdate(update: Update) {
        if (checkUpdate(update)) {
            handleUpdate(update)
        }
    }
    fun handleUpdate(update: Update)
    fun checkUpdate(update: Update): Boolean
}