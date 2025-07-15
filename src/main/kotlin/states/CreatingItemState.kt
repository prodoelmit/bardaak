package org.prodoelmit.states

import com.pengrad.telegrambot.model.PhotoSize
import org.prodoelmit.Images
import org.prodoelmit.Items
import org.prodoelmit.LOCATION_REGEX

class CreatingItemState(
    val photo: PhotoSize? = null,
    val text: String? = null,
): IState {
    override fun onEnter(userId: Long) {
        val filename = photo?.let { Images.download(it) }
        var name: String? = null
        var location: Int? = null
        text?.split("\n")?.forEach { line ->
            when {
                line.trim().matches(LOCATION_REGEX) -> {location = line.trim().toIntOrNull()}
                else -> {name = line.trim()}
            }
        }

        val item = Items.createItem(
            name,
            filename,
            location
        )
        States.setState(userId, ShowItemState(item, ShowItemState.Reason.Created))

    }

    override fun onLeave(userId: Long) {
    }
}