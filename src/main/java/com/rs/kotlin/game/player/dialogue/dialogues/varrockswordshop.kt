package com.rs.kotlin.game.player.dialogue.dialogues

import com.rs.kotlin.game.player.queue.QueueTask

suspend fun QueueTask.varrockswordshop(npcId: Int) {
    npc("Hello, bold adventurer! Can I interest you in some swords?", npcId)

    when (
        options(
            "Yes, please!",
            "No, I'm okay for swords right now.",
        )
    ) {
        1 -> {
            player("Yes, please!")
        }

        2 -> {
            player("No, I'm okay for swords right now.")
            npc("Come back if you need any.", npcId)
        }
    }
}