package furhatos.app.complimentbot.flow

import furhatos.app.complimentbot.flow.main.Idle
import furhatos.app.complimentbot.setting.activate
import furhatos.app.complimentbot.setting.mainPersona
import furhatos.app.complimentbot.setting.maxNumberOfUsers
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.users
import furhatos.app.complimentbot.setting.LslManager
import furhatos.app.complimentbot.setting.Markers


val Init = state {
    onEntry {
        /** Set our default interaction parameters */
        users.setSimpleEngagementPolicy(0.5, 1.2, 1.2, 1.7, maxNumberOfUsers)

        /** Set our main character - defined in personas */
        activate(mainPersona)

        /** Initialize LSL stream (dummy marker)
        GlobalScope.launch {
            repeat(5) {
                LslManager.send(Markers.INIT_STREAM)
                delay(200)  // total 1 second
            }
        } */
        LslManager.send(Markers.INIT_STREAM)

        /** start the interaction */
        goto(Idle)

    }
}