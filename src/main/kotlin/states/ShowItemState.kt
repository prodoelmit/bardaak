package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendPhoto
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Environment
import org.prodoelmit.Item
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown
import org.prodoelmit.sendPhoto
import kotlin.collections.isNotEmpty

class ShowItemState(val item: Item, val reason: Reason = Reason.None) : IState {

    val storeRegex = Regex(Environment.STORE_ARTICLE_REGEX)

    var itemsInside : Collection<Item> = emptyList()

    override fun onEnter(userId: Long) {

        itemsInside = Items.getItemsInLocation(item.id)

        val keyboard = makeKeyboard()

        val text = makeText()



        if (item.filename != null) {
            sendPhoto(userId, text, item.imageFile!!, keyboard)
        } else {
            sendMarkdown(userId, text)
        }
    }

    override fun onLeave(userId: Long) {
    }

    enum class Reason {
        Edited,
        Created,
        None
    }

    private fun makeText(): String {
        val locationString = item.location?.let { locationItemId ->
            val locationItem = Items.getItem(locationItemId)
            locationItem?.let {
                val linkInParens = "(/show_${it.id})".escapeForMarkdown()
                "${it.markdownSafeName} $linkInParens"
            }
        } ?: "Unknown"

        val firstLine = when (reason) {
            Reason.Created -> "Item created:"
            Reason.Edited -> "Item edited:"
            Reason.None -> ""
        }


        val text = buildString {
            if (firstLine.isNotEmpty()) {
                appendLine(firstLine)
            }

            val linkInParens = "(/show_${item.id})".escapeForMarkdown()
            appendLine("*Id*: ${item.id} $linkInParens")
            appendLine("*Name*: ${item.markdownSafeName}")
            appendLine("*Location*: ${locationString}")
            if (itemsInside.isNotEmpty()) {
                val insideLinkInParens = "(/inside_${item.id})".escapeForMarkdown()
                appendLine("*Items inside*: ${itemsInside.count()} $insideLinkInParens")
            }
        }
        return text
    }

    fun makeKeyboard(): InlineKeyboardMarkup {
        val keyboard = InlineKeyboardMarkup()


        if (item.location != null) {
            keyboard.addRow(
                InlineKeyboardButton(text = "Show location", callbackData = "showItem#${item.location}"),
            )
        }
        if (itemsInside.isNotEmpty()) {
            keyboard.addRow(
                InlineKeyboardButton(text = "What's inside", callbackData = "showInside#${item.id}"),
            )
        }

        // Provide a button for mapping article number to store
        // (by default - ikea.com.cy)
        if (item.name != null) {
            val name = item.name!!
            val matchedPart = storeRegex.find(name)?.groupValues?.first()
            if (matchedPart != null) {
                keyboard.addRow(
                    InlineKeyboardButton(text = Environment.STORE_BUTTON_TITLE, url = Environment.STORE_BUTTON_URL.replace("{{article}}", matchedPart)),
                )
            }
        }

        keyboard.addRow(
            InlineKeyboardButton(text = "Edit", callbackData = "edit#${item.id}"),
        )

        return keyboard
    }
}