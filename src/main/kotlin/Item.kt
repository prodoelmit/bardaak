package org.prodoelmit

import java.io.File

data class Item(
    val id: Int,
    var name: String? = null,
    var location: Int? = null,
    var filename: String? = null
) {
    val imageFile: File?
        get() = filename?.let { Images.resolve(it) }?.toFile()

    val markdownSafeName: String? get() = name?.escapeForMarkdown()

}

/* In all other places characters '_', '*', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!' must be escaped with the preceding character '\'.
 */
fun String.escapeForMarkdown(): String {
    val charactersToEscape = listOf(
        '_',
        '*',
        '[',
        ']',
        '(',
        ')',
        '~',
        '`',
        '>',
        '#',
        '+',
        '-',
        '=',
        '|',
        '{',
        '}',
        '.',
    )

    return this.map { c ->  if (c in charactersToEscape) "\\$c" else c }.joinToString(separator = "")

}

val DELETED_PREFIX = "[D] "