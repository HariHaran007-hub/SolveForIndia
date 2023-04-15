package com.zero.golgol.model

class Challenge(
    val challengeLogo: String,
    val challengeTitle: String,
    val challengeDescription: String,
    val challengeProgress: Int
){
    constructor() : this("","","",0){

    }
}
