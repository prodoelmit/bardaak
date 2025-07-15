package org.prodoelmit.states

interface IState {
    fun onEnter(userId: Long)
    fun onLeave(userId: Long)
}