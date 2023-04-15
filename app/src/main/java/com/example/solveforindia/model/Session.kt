package com.zero.golgol.model

import java.io.Serializable


class Session(
    var sessionImage: Int,
    var sessionAvenue: SessionAvenue?,
    var sessionName: String,
    var sessionDistance: String,
    var sessionTime: String,
    var isEquipmentsAvailable: Boolean?,
    var isSafe: Boolean?,
    var isPlaymatesAvailable: Boolean?,
    var players: HashMap<String, String>?,
    var createdBy: String,
    var sessionDate: String,
    var maxUsers: Int?,
    var sessionId : String
): Serializable{
    
    constructor() : this(0,null,"","",
        "",null,null,null,null,"","",0,"")

    override fun toString(): String {
        return "Session(sessionImage=$sessionImage, sessionAvenue=$sessionAvenue, sessionName='$sessionName', sessionDistance='$sessionDistance', sessionTime='$sessionTime', isEquipmentsAvailable=$isEquipmentsAvailable, isSafe=$isSafe, isPlaymatesAvailable=$isPlaymatesAvailable, players=$players, createdBy='$createdBy', sessionDate='$sessionDate', maxUsers=$maxUsers, sessionId='$sessionId')"
    }

}