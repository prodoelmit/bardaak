package org.prodoelmit.states


object States {
    val stateByUserId = mutableMapOf<Long, IState>()

    fun setState(userId: Long, newState: IState) {
        getState(userId)?.apply {
            onLeave(userId)
        }
        stateByUserId[userId] = newState
        newState.onEnter(userId)

        println("Current state for $userId: $newState")
    }


    fun getState(userId: Long): IState? {
        return stateByUserId.getOrDefault(userId, StartState)
    }

}