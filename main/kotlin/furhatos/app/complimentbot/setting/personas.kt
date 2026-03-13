package furhatos.app.complimentbot.setting

import furhatos.flow.kotlin.FlowControlRunner
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.Voice



class Persona(val name: String, val mask: String = "adult", val face: List<String>, val voice: List<Voice>)

fun FlowControlRunner.activate(persona: Persona) {
    for (voice in persona.voice) {
        if (voice.isAvailable) {
            furhat.voice = voice
            break
        }
    }

    for (face in persona.face) {
        if (furhat.faces.get(persona.mask)?.contains(face)!!) {
            furhat.character = face
            break
        }
    }
}

val mainPersona = Persona(
    name = "Android-2",
    face = listOf("Android-2", "Alex"),
    voice = listOf(Voice("Rachel22k_HQ(en-GB) - Acapela"))
)
