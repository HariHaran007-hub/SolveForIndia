package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityGoogleSignInUserDetailsBinding

class GoogleSignInUserDetails : BaseActivity() {

    private lateinit var binding: ActivityGoogleSignInUserDetailsBinding
    private var isValid = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in_user_details)

        binding = ActivityGoogleSignInUserDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        isValid.value = false
        setUpGenderSpinner()
        clickListeners()
        observers()
    }

    private fun observers() {
        isValid.observe(this) {
            binding.btnContinue.isEnabled =
                binding.etAge.text.toString().trim().isNotEmpty() && binding.etId.text.toString()
                    .trim().isNotEmpty() && binding.etPhone.text.toString().trim().isNotEmpty()
                        && binding.spinnerGender.selectedItemPosition != 0
        }
    }

    private fun clickListeners() {
        binding.btnContinue.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            val intent  = Intent(this,AvatarActivity::class.java)
            intent.putExtra(CreateProfileActivity.USER_NAME,account?.displayName.toString())
            intent.putExtra(CreateProfileActivity.PHONE_NUMBER,binding.etPhone.text.toString())
            intent.putExtra(CreateProfileActivity.USER_AGE,binding.etAge.text.toString().trim())
            intent.putExtra(CreateProfileActivity.USER_GENDER,binding.spinnerGender.selectedItem.toString())
            intent.putExtra(CreateProfileActivity.USER_ID_CARD_NUMBER,binding.etId.text.toString())
            intent.putExtra("login_type","Google")
            startActivity(intent)
            finish()
        }

        binding.spinnerGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
                isValid.value = selectedItem != 0
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Nothing selected
            }

        }

        binding.etAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

        binding.etPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

    }

    private fun setUpGenderSpinner() {
        val genderList: MutableList<Any?> = ArrayList()
        genderList.add("Gender")
        genderList.add("Male")
        genderList.add("Female")
        genderList.add("Other")

        val spinnerArrayAdapter = ArrayAdapter<Any?>(this, R.layout.custom_spinner, genderList)
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item)
        binding.spinnerGender.adapter = spinnerArrayAdapter
    }
}