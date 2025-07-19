package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.bullet
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class ShowInsideState(val itemId: Int) : IState {

    override fun onEnter(userId: Long) {

        val item = Items.getItem(itemId) ?: return
        val itemsInside = Items.getItemsInLocation(itemId)

        if (itemsInside.isEmpty()) {
            val idInParens = "(${item.id})".escapeForMarkdown()

            sendMarkdown(userId, "Nothing inside of $idInParens - ${item.markdownSafeName}")
            States.restart(userId)
            return
        }

        val text = buildString {
            appendLine("Inside of (${item.id}) - ${item.name}:".escapeForMarkdown())
            appendLine()
            itemsInside.forEach {
                val linkInParens = "(/show_${it.id})".escapeForMarkdown()
                appendLine("$bullet ${it.markdownSafeName} $linkInParens")
            }
        }

        sendMarkdown(userId, text)

    }

    override fun onLeave(userId: Long) {
    }

    enum class Reason {
        Edited,
        Created,
        None
    }
}