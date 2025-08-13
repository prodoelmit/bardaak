package org.prodoelmit.states

import org.prodoelmit.Items
import org.prodoelmit.bullet
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class SearchState(val query: String) : IState {
    override fun onEnter(userId: Long) {
        val items = Items.searchByName(query)

        val text = buildString {
            appendLine("Search results:")
            appendLine()

            items.forEach { item ->
                val linkInParens = "(/show_${item.id})".escapeForMarkdown()
                appendLine("${bullet} *${item.markdownSafeName}* $linkInParens")
            }
        }

        sendMarkdown(userId, text)
    }

    override fun onLeave(userId: Long) {
    }
}