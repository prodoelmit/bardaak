package org.prodoelmit.states

import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.sendMarkdown

class FetchingItemState(val idString: String): IState {
    override fun onEnter(userId: Long) {
        val itemId = idString.trim().toInt()
        val item = Items.getItem(itemId)

        if (item == null) {
            sendMarkdown(userId, "Item with id $itemId not found")
            States.restart(userId)
            return
        }

        States.setState(userId, ShowItemState(item))
    }

    override fun onLeave(userId: Long) {
    }
}