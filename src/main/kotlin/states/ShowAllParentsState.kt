package org.prodoelmit.states

import org.prodoelmit.Item
import org.prodoelmit.Items
import org.prodoelmit.bullet
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class ShowAllParentsState() : IState {

    var allItemsWithoutParents: Collection<Item> = emptyList()

    override fun onEnter(userId: Long) {

        allItemsWithoutParents = Items.getItemsWithoutParent()

        val text = buildString {
            appendLine("All items without parent:")
            appendLine()

            allItemsWithoutParents.forEach { item ->
                val linkInParens = "(/show_${item.id})".escapeForMarkdown()
                appendLine("${bullet} *${item.markdownSafeName}* $linkInParens")
            }
        }


        sendMarkdown(userId, text)
    }

    override fun onLeave(userId: Long) {
    }

}