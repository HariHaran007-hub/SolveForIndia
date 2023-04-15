package com.zero.golgol.model

import java.io.Serializable

class FunPlay(
    val activityImage: Int,
    val activityOrganizer: String,
    val activityName: String,
    val activityDistance: String,
    val activityTime: String,
    val activityAvailability: String,
    var sessionAvenue: SessionAvenue?
) : Serializable{
    constructor() : this(0 , "" , "" ,""
        ,"","",null)
}