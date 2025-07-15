package org.prodoelmit.states

import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot

class FetchingItemState(val idString: String): IState {
    override fun onEnter(userId: Long) {
        val itemId = idString.trim().toInt()
        val item = Items.getItem(itemId)

        if (item == null) {
            bot.sendMessage(userId, "Item with id $itemId not found")
            States.restart(userId)
            return
        }

        States.setState(userId, ShowItemState(item))
    }

    override fun onLeave(userId: Long) {
    }
}