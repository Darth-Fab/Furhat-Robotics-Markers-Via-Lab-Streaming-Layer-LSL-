package furhatos.app.complimentbot.flow.main

import java.awt.Color
import furhatos.app.complimentbot.flow.Parent
import furhatos.app.complimentbot.flow.served
import furhatos.app.complimentbot.gestures.TripleBlink
import furhatos.app.complimentbot.setting.lookForward
import furhatos.flow.kotlin.*
import furhatos.records.User
import furhatos.app.complimentbot.setting.LslManager
import furhatos.app.complimentbot.setting.Markers

val userScores = mutableListOf<Int>()
var scoreCount = 0
val totalScores = 2
var experimentActive = false

fun parseScore(text: String): Int? {
    return when (text.lowercase().trim()) {
        "zero", "oh", "zeero", "null", "0", "searoo", "ceero", "seero",
        "cheero", "jeero", "ziero", "tseero", "zehro", "zeroh",
        "nool", "nuhll", "nüll", "newl" -> 0

        "one", "won", "ain", "1" -> 1

        "two", "to", "2", "choo", "tooo", "tyoo",
        "tü", "tuhoo", "tue", "teuoo" -> 2

        "three", "tree", "tee", "ee", "3" -> 3
        "four", "for", "fo", "4" -> 4

        "five", "fife", "fi", "fai", "i", "5",
        "faive", "fahive", "feive", "vife",
        "foive", "fivuh" -> 5

        else -> null
    }
}

/** Markers (moved outside state) */
fun FlowControlRunner.promptRoll() {
    LslManager.send(Markers.START_TRIAL)
    delay(1500)

    LslManager.send(Markers.ROLL_DICE)
    furhat.say("Please roll your die now.")
    delay(5000)

    LslManager.send(Markers.LOOK_DICE)
    furhat.ask("What is the total score of your roll?")
}

fun startReading(user: User): State = state(Parent) {

    onEntry {
        userScores.clear()
        scoreCount = 0
        experimentActive = true

        furhat.attend(lookForward)
        furhat.gesture(TripleBlink, priority = 10)
        delay(200)

        user.served = true
        furhat.attend(user)
        furhat.ledStrip.solid(Color(0, 120, 0))

        promptRoll()
    }

    onResponse {
        val number = parseScore(it.text)

        if (number != null) {
            LslManager.send(Markers.REPORT_RESULTS)

            userScores.add(number)
            scoreCount++

            if (scoreCount < totalScores) {
                furhat.say("Thank you. You can now report your outcome on the tablet.")
                LslManager.send(Markers.REPORT_TABLET)

                delay(3000)
                LslManager.send(Markers.END_TRIAL)

                delay(1500)
                promptRoll()
            } else {
                furhat.say("Thank you. You can now report your outcome on the tablet. The task is now complete.")
                experimentActive = false
                goto(EndReading)
            }

        } else {
            furhat.ask("Please give a number between zero and five.")
        }
    }

    onNoResponse {
        LslManager.send(Markers.REPORT_FAILED)
        furhat.ask("Sorry, I did not hear you. What was your result?")
    }

    onResponseFailed {
        LslManager.send(Markers.REPORT_FAILED)
        furhat.ask("Sorry, I did not understand. What was your result?")
    }
}

/** ResumeReading also uses PromptRoll function */
val ResumeReading: State = state(Parent) {

    onEntry {
        furhat.say("Let's continue where we left off.")
        LslManager.send(Markers.LOOK_DICE)
        furhat.ask("What is the total score of your roll?")
    }

    onResponse {
        val number = parseScore(it.text)

        if (number != null) {
            LslManager.send(Markers.REPORT_RESULTS)

            userScores.add(number)
            scoreCount++

            if (scoreCount < totalScores) {
                furhat.say("Thank you. Please roll your die again.")
                LslManager.send(Markers.REPORT_TABLET)

                delay(3000)
                LslManager.send(Markers.END_TRIAL)

                delay(1500)
                promptRoll()
            } else {
                furhat.say("Thank you. The task is now complete.")
                experimentActive = false
                goto(EndReading)
            }

        } else {
            furhat.ask("Please give a number between zero and five.")
        }
    }

    onNoResponse {
        LslManager.send(Markers.REPORT_FAILED)
        furhat.ask("Sorry, I did not hear you. What was your result?")
    }

    onResponseFailed {
        LslManager.send(Markers.REPORT_FAILED)
        furhat.ask("Sorry, I did not understand. What was your result?")
    }
}