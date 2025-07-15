package org.prodoelmit

import java.io.File

data class Item(
    val id: Int,
    var name: String? = null,
    var location: Int? = null,
    var filename: String? = null
) {
    val imageFile: File?
        get() = filename?.let {Images.resolve(it)}?.toFile()
}