package com.zero.golgol.model

import java.io.Serializable

class ActivityDetails(
    val activityImage: String,
    val activityOrganizer: String,
    val activityName: String,

    val activityTime: String,
    val activityAvailability: String,
    var sessionAvenue: SessionAvenue?,
    var activityDate : String
): Serializable{
    constructor() : this("" , "" , ""
        ,"","",null,"")
}