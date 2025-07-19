package org.prodoelmit

import com.pengrad.telegrambot.model.PhotoSize
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendPhoto
import com.pengrad.telegrambot.utility.kotlin.extension.request.getFile
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.Path

fun Update.userId(): Long = message().from().id()

val LOCATION_REGEX = Regex("\\d+")

val bullet = '•'

fun sendMarkdown(userId: Long,
                 markdownText: String,
                 keyboard: InlineKeyboardMarkup? = null,
                 ): Unit {
    val result = bot.sendMessage(
        userId,
        markdownText,
        {
            parseMode(ParseMode.MarkdownV2)
            if (keyboard != null) {
                replyMarkup(keyboard)
            }
        }
    )

    if (!result.isOk) {
        println("Error sending message: ${result.description()} ")
    }
}


fun sendPhoto(
    userId: Long,
    markdownText: String,
    photoFile: File,
    keyboard: InlineKeyboardMarkup? = null
) {

    val request = SendPhoto(userId, photoFile)
        .caption(
            markdownText,
        )
        .parseMode(ParseMode.MarkdownV2 )
        .showCaptionAboveMedia(false)
        .replyMarkup(keyboard)

    val result = bot.execute(request)

    if (!result.isOk) {
        println("Error sending photo: ${result.description()} ")
    }
}