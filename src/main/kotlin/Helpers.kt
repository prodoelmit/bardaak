package org.prodoelmit

import com.pengrad.telegrambot.model.PhotoSize
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.utility.kotlin.extension.request.getFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.Path

fun Update.userId(): Long = message().from().id()

val LOCATION_REGEX = Regex("\\d+")
