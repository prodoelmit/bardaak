package org.prodoelmit.handlers

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

object LogAnythingHandler: IUpdateHandler {
    override fun handleUpdate(update: Update) {
        println("LogAnythingHandler: ${update}")
    }

    override fun checkUpdate(update: Update): Boolean {
        return true
    }
}