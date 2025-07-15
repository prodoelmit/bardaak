package org.prodoelmit.handlers

import com.pengrad.telegrambot.model.PhotoSize
import com.pengrad.telegrambot.model.Update
import org.prodoelmit.Items
import org.prodoelmit.LOCATION_REGEX
import org.prodoelmit.allowedUserIds
import org.prodoelmit.states.CreatingItemState
import org.prodoelmit.states.EditingLocationState
import org.prodoelmit.states.EditingNameState
import org.prodoelmit.states.EditingPhotoState
import org.prodoelmit.states.FetchingItemState
import org.prodoelmit.states.ShowItemState
import org.prodoelmit.states.StartState
import org.prodoelmit.states.States
import org.prodoelmit.states.states.SearchState
import org.prodoelmit.userId

object MessageHandler: IUpdateHandler {
    override fun handleUpdate(update: Update) {
        val message = update.message()
        val userId = update.userId()

        if (!message.photo().isNullOrEmpty()) {
            handlePhoto(userId, message.photo().maxBy {photo -> photo.height()}, message.caption())
        } else {
            handleText(userId, message.text())
        }

        println("MessageHandler:")
        println(update.message())
        update.message().text()
    }


    fun handlePhoto(userId: Long, photo: PhotoSize, caption: String?) {
        val state = States.getState(userId)
        when (state) {
            is EditingPhotoState -> {
                state.setNewPicture(photo)
            }
            else -> {
                States.setState(userId, CreatingItemState(photo, caption))
            }
        }
    }

    fun handleText(userId: Long, text: String) {
        val state = States.getState(userId)
        when (state) {
            is EditingNameState -> {
                state.setNewName(text)
            }
            is EditingLocationState -> {
                state.setNewLocation(text)
            }
            is EditingPhotoState -> {
                state.onIncorrectType()
            }
            else -> {
                if (text.matches(LOCATION_REGEX)) {
                    States.setState(userId, FetchingItemState(text))
                } else {
                    States.setState(userId, SearchState(text))
                }
            }
        }

    }

    override fun checkUpdate(update: Update): Boolean {
        if (update.message() == null) return false
        if (update.message().from().id() !in allowedUserIds) return false

        // Commands are handled in CommandHandler
        if (update.message().entities() != null) return false

        return true
    }
}