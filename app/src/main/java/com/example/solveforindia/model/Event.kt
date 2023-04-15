package com.zero.golgol.model

import java.io.Serializable

data class Event(
    val eventImage: String,
    val eventOrganizer: String,
    val eventName: String,
    val eventDistance: String,
    val eventTime: String,
    val eventAvailability: String,
    val eventCost: String
) : Serializable{
    constructor() : this ("","", "" , "" , "", "","")
}