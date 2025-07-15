package org.prodoelmit.states

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.Items
import org.prodoelmit.bot

class EditingLocationState(val itemId: Int): IState {
    override fun onEnter(userId: Long) {
        this.userId = userId
        val item = Items.getItem(itemId)!!
        val oldLocation = item.location?.let { locationItemId ->
            val locationItem = Items.getItem(locationItemId)
            locationItem?.let {
                "(${it.id}) ${it.name}"
            } ?: "undefined"
        }
        bot.sendMessage(userId, "Choose a new location id. Old location was:\n*$oldLocation*", {
            parseMode(ParseMode.Markdown)
        })
    }

    var userId: Long? = null


    fun setNewLocation(locationText: String) {
        val item = Items.getItem(itemId)!!

        val locationId = locationText.trim().toIntOrNull() ?: run {
            bot.sendMessage(userId!!, "Incorrect location ID. Please enter a valid ID consisting only of digits. Use 0 if you want to clear location")
            return
        }


        if (locationId == 0) {
            item.location = null
            Items.updateItem(item)
        } else {
            val locationItem = Items.getItem(locationId) ?: run {
                bot.sendMessage(userId!!, "There is no location with this ID. Please enter an existing ID")
                return
            }

            item.location = locationItem.id

            Items.updateItem(item)
        }

        States.setState(userId!!, ShowItemState(item, ShowItemState.Reason.Edited))
    }

    override fun onLeave(userId: Long) {
    }
}