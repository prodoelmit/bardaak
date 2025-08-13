package org.prodoelmit.states

import org.prodoelmit.DELETED_PREFIX
import org.prodoelmit.Items

class UndeleteItemState(val itemId: Int) : IState {

    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        val newName = item.name?.removePrefix(DELETED_PREFIX) ?: ""
        setNewName(newName)
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