package com.zero.golgol.model

class ActiveChallenges(var challengeId: String,
                       val challengeLogo: String,
                       val challengeTitle: String,
                       val challengeDescription: String,
                       var progress: Int) {

    constructor() : this("","","","",0){

    }
}
