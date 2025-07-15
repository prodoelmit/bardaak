package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot

class ShowInsideState(val itemId: Int) : IState {

    override fun onEnter(userId: Long) {

        val item = Items.getItem(itemId) ?: return
        val itemsInside = Items.getItemsInLocation(itemId)

        if (itemsInside.isEmpty()) {
            bot.sendMessage(userId, "Nothing inside of (${item.id}) - ${item.name}", { parseMode(ParseMode.Markdown) })
            States.restart(userId)
            return
        }

        val text = buildString {
            appendLine("Inside of (${item.id}) - ${item.name}:")
            appendLine()
            itemsInside.forEach {
                appendLine("*${it.name}* (/show\\_${it.id})")
            }
        }

        val result = bot.sendMessage(
            userId,
            text,
            {
                parseMode(ParseMode.Markdown)
            }
        )
        println(result)
    }

    override fun onLeave(userId: Long) {
    }

    enum class Reason {
        Edited,
        Created,
        None
    }
}