package furhatos.app.complimentbot.flow.main

import furhatos.flow.kotlin.*
import furhatos.records.User
import furhatos.app.complimentbot.setting.LslManager
import furhatos.app.complimentbot.setting.Markers


val Idle: State = state {

    var currentUser: User? = null

    init {
        if (furhat.isVirtual() && users.hasAny() == false) {
            furhat.say("Add a Virtual User to start the interaction.")
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        currentUser = it
        furhat.attend(it)
        LslManager.send(Markers.START_EXPERIMENT)
        furhat.say("During the following game you will roll the die in front of you and I will ask you for the outcome. " +
                "Remember that the face with the smiley face is worth 5 points. The face with the red cross is worth 0 points.")
        furhat.ask("Tell me when you're ready to start.")
    }

    onResponse {
        val text = it.text.lowercase()
        if (text.contains("ready") || text.contains("yes") || text.contains("start") || text.contains("go")) {
            furhat.say("Great, let's begin.")
            goto(startReading(currentUser!!))
        } else {
            furhat.ask("Just say 'ready' when you want to start.")
        }
    }
}
