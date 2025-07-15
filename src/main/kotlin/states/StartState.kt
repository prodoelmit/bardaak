package org.prodoelmit.states

import com.pengrad.telegrambot.utility.kotlin.extension.request.sendMessage
import org.prodoelmit.bot

object StartState: IState {
    override fun onEnter(userId: Long) {
        bot.sendMessage(userId, "Hi there!")
    }

    override fun onLeave(userId: Long) {
    }
}

fun States.restart(userId: Long) {
    setState(userId, StartState)
}
