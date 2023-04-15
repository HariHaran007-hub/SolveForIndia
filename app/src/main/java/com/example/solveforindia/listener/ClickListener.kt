package com.zero.golgol.listener

interface ClickListener {

    fun buttonClick()

    fun buttonClick(position: Int, type: String)

    fun buttonClick(position: Int)

}