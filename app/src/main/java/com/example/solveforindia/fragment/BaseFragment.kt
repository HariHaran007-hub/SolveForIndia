package com.zero.golgol.fragment

import androidx.fragment.app.Fragment
import com.zero.golgol.controller.User

open class BaseFragment: Fragment() {

    val user: User = User.getInstance()

}