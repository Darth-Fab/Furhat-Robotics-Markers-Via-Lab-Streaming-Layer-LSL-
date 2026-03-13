package furhatos.app.complimentbot.flow.main

import furhatos.flow.kotlin.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.io.File
import furhatos.app.complimentbot.setting.LslManager
import furhatos.app.complimentbot.setting.Markers

val EndReading = state {
    onEntry {
        LslManager.send(Markers.END_EXPERIMENT)
        furhat.say("Thank you for playing!")

        // Only save if user completed all rolls
        if (userScores.isNotEmpty()) {

            // Print scores to console
            println("Participant scores: $userScores")

            // Create timestamp
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            val timestamp = now.format(formatter)

            // CSV file path
            val filePath = "C:/Users/LabUser/AICROSS-Experiment/Furhat/data/participant_scores.csv"
            val file = File(filePath)

            // Determine next participant ID
            var participantID = 1
            if (file.exists()) {
                // Count existing lines minus header
                val lines = file.readLines()
                participantID = if (lines.size > 1) lines.size else 1
            }

            // Prepare CSV line: participant ID, timestamp, then scores
            val csvLine = "$participantID,$timestamp," + userScores.joinToString(",")

            // Write header if file doesn’t exist
            if (!file.exists()) {
                file.writeText("ParticipantID,DateTime," +
                        userScores.indices.joinToString(",") { "Score${it + 1}" } + "\n")
            }

            // Append the participant data
            file.appendText(csvLine + "\n")
        }

        // Clear scores for next participant
        userScores.clear()

        // Continue to Idle or next user
        goto(Idle)
    }
}
