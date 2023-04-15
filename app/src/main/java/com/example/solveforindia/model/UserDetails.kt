package com.zero.golgol.model

class UserDetails {
    var userId: String? = null
    var userName: String? = null
    var userAge: String? = null
    var userGender: String? = null
    var userIdCardNumber: String? = null
    var userPhoneNumber: String? = null
    var loginType: String? = null
    var avatar: Int? = null
    var gols : Int? = null
    var activeChallenges: HashMap<String, ActiveChallenges>? = null
    var badges : Int? = null
    var golPals : HashMap<String, String>? = null


    constructor() {}
    constructor(
        userId: String?,
        userName: String?,
        userAge: String?,
        userGender: String?,
        userIdCardNumber: String?,
        userPhoneNumber: String?,
        loginType: String?,
        avatar: Int?,
        gols: Int?,
        activeChallenges: HashMap<String, ActiveChallenges>,
        badges: Int?,
        golPals : HashMap<String , String>
    ) {
        this.userId = userId
        this.userName = userName
        this.userAge = userAge
        this.userGender = userGender
        this.userIdCardNumber = userIdCardNumber
        this.userPhoneNumber = userPhoneNumber
        this.loginType = loginType
        this.avatar = avatar
        this.gols = gols
        this.activeChallenges = activeChallenges
        this.badges = badges
        this.golPals = golPals
    }

//    override fun toString(): String {
//        return "UserDetails(userId=$userId, userName=$userName, userAge=$userAge, userGender=$userGender, userIdCardNumber=$userIdCardNumber, userPhoneNumber=$userPhoneNumber, loginType=$loginType, avatar=$avatar, activeChallenge=$activeChallenges)"
//    }


}