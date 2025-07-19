package org.prodoelmit.states

import com.pengrad.telegrambot.model.PhotoSize
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Images
import org.prodoelmit.Items
import org.prodoelmit.bot
import org.prodoelmit.sendMarkdown

class EditingPhotoState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        sendMarkdown(userId, "Send me a new picture")
    }

    var userId: Long? = null

    fun setNewPicture(photo: PhotoSize) {
        val filename = Images.download(photo)

        val item = Items.getItem(itemId)!!
        item.filename = filename
        Items.updateItem(item)

        States.setState(userId!!, ShowItemState(item, ShowItemState.Reason.Edited))
    }

    fun onIncorrectType() {
        sendMarkdown(userId!!, "Please send me an image")
    }

    override fun onLeave(userId: Long) {
    }
}