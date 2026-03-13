package furhatos.app.complimentbot.flow

import furhatos.app.complimentbot.flow.main.EndReading
// this is new:
import furhatos.app.complimentbot.flow.main.ResumeReading
// this is new:
import furhatos.app.complimentbot.flow.main.experimentActive
import furhatos.flow.kotlin.*

// this is different:
val Parent: State = state {

    onUserEnter(instant = true) {
        furhat.glance(it)

        if (experimentActive) {
            goto(ResumeReading)
        }
    }

    onUserLeave {
        if (it == users.current) {
            furhat.say("I lost track of you. Please make sure that I can see your face.")
            goto(UserAbsentGrace)
        }
    }
}


// this is new:
val UserAbsentGrace: State = state(Parent) {

    onEntry {
        furhat.attendNobody()

        delay(30000)

        if (users.count == 0) {
            experimentActive = false
            goto(EndReading)
        } else {
            goto(ResumeReading)
        }
    }

    onUserEnter {
        goto(ResumeReading)
    }
}
