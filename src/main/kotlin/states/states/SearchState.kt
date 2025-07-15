package org.prodoelmit.states.states

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.states.IState


class SearchState(val query: String) : IState {
    override fun onEnter(userId: Long) {
        val items = Items.searchByName(query)

        val text = buildString {
            appendLine("Search results:")
            appendLine()

            items.forEach { item ->
                appendLine("- *${item.name}* (/show\\_${item.id})")
            }
        }

        bot.sendMessage(
            userId,
            text,
            {
                parseMode(ParseMode.Markdown)
            }
        )

    }

    override fun onLeave(userId: Long) {
    }
}