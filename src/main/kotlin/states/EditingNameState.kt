package org.prodoelmit.states

import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot

class EditingNameState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        bot.sendMessage(userId, "Choose a new name. Old name was: \n${item.name}")
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