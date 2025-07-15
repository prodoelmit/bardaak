package org.prodoelmit

import com.pengrad.telegrambot.model.PhotoSize
import com.pengrad.telegrambot.utility.kotlin.extension.request.getFile
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

object Images {
    val imagesDir = Path(Environment.IMAGES_DIR)

    init {
        imagesDir.createDirectories()
    }

    fun download(photo: PhotoSize): String {
        val uuid = UUID.randomUUID().toString()
        val filename = "${uuid}.png"
        val fileId = photo.fileId()
        val file = bot.getFile(fileId).file()
        bot.getFileContent(file).apply {
            Files.createDirectories(imagesDir)
            val filePath = imagesDir.resolve(filename)
            Files.write(filePath, this )
        }

        return filename
    }


    fun resolve(filename: String): Path = imagesDir.resolve(filename)
}