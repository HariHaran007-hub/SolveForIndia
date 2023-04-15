package com.zero.golgol.model

import java.io.Serializable

class Reward(
    val rewardLogo: String,
    val rewardTitle: String,
    val rewardProgress: Int
) : Serializable{
    constructor() : this("","",0){

    }
}
