package com.zero.golgol.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.MutableLiveData
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    companion object {
        const val PHONE_NUMBER: String = "phone_number"
    }

    private lateinit var binding: ActivitySignInBinding
    private var isValid = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observers()
        clickListener()

    }

    private fun observers() {
        isValid.observe(this) {
            binding.btnContinue.isEnabled =
                binding.etPhone.text.toString().trim().isNotEmpty()
        }

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })
    }

    private fun clickListener(){
        binding.btnContinue.setOnClickListener {
            val phoneNumber : String = binding.etPhone.text.toString().trim()
            val intent  = Intent(this,VerifyPhoneActivity::class.java)
            intent.putExtra(CreateProfileActivity.PHONE_NUMBER,phoneNumber)
            startActivity(intent)
        }
    }
}