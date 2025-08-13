package org.prodoelmit.states

import org.prodoelmit.DELETED_PREFIX
import org.prodoelmit.Items
import org.prodoelmit.bullet
import org.prodoelmit.escapeForMarkdown
import org.prodoelmit.sendMarkdown

class DeleteItemState(val itemId: Int) : IState {

    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        setNewName(DELETED_PREFIX + item.name?.removePrefix(DELETED_PREFIX))
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

    companion object {
    }
}