package com.zero.golgol.model

import java.io.Serializable

data class InActiveChallenge(
    val challengeLogo: String,
    val challengeTitle: String,
    val challengeId : String,
    val challengeDescription: String
) : Serializable{
    constructor() : this("","","",""){

    }

    constructor(activeChallengeId: String, progress: Int) : this()
}
