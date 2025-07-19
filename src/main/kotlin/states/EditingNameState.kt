package org.prodoelmit.states

import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class EditingNameState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        val text = buildString {
            appendLine("Choose a new name. Old name was:".escapeForMarkdown())
            appendLine(item.markdownSafeName)
        }
        sendMarkdown(userId, text)
    }

    var userId: Long? = null


    fun setNewName(newName: String) {
        val item = Items.getItem(itemId)!!
        item.name = newName
        Items.updateItem(item)

        States.setState(userId!!, ShowItemState(item, ShowItemState.Reason.Edited))
    }

    override fun onLeave(userId: Long) {
    }
}