package com.zero.golgol.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase

import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityVerifyPhoneBinding
import com.zero.golgol.utils.Utils
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : BaseActivity() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var verificationId : String
    private lateinit var binding : ActivityVerifyPhoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        binding = ActivityVerifyPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editText  = findViewById<EditText>(R.id.editTextCode)

        val phoneNumber = "+91"+intent.getStringExtra(CreateProfileActivity.PHONE_NUMBER)
        sendVerificationCode(phoneNumber)

        findViewById<Button>(R.id.buttonSignIn).setOnClickListener{

            val code  = editText.text.toString().trim()
            if(code.isEmpty() || code.length < 6){
                editText.error = "Enter code.."
                editText.requestFocus()
                return@setOnClickListener
            }
            binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE
            verifyCode(code)
        }
    }

    private fun signInWithCredential(credentials: PhoneAuthCredential) {

        if(intent.getBooleanExtra("from",false)){
            mAuth.signInWithCredential(credentials)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val intentNextActivity = Intent(this, AvatarActivity::class.java)
                        intentNextActivity.putExtra(CreateProfileActivity.USER_NAME,intent.getStringExtra(CreateProfileActivity.USER_NAME))
                        intentNextActivity.putExtra(CreateProfileActivity.USER_GENDER,intent.getStringExtra(CreateProfileActivity.USER_GENDER))
                        intentNextActivity.putExtra(CreateProfileActivity.USER_AGE,intent.getStringExtra(CreateProfileActivity.USER_AGE))
                        intentNextActivity.putExtra(CreateProfileActivity.USER_ID_CARD_NUMBER,intent.getStringExtra(CreateProfileActivity.USER_ID_CARD_NUMBER))
                        intentNextActivity.putExtra(CreateProfileActivity.PHONE_NUMBER,intent.getStringExtra(CreateProfileActivity.PHONE_NUMBER))
                        intentNextActivity.putExtra("login_type","Phone number authentication")
                        startActivity(intentNextActivity)
                        finish()
                    }else{
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }
            }
        else{
            mAuth.signInWithCredential(credentials)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        FirebaseDatabase.getInstance()
                            .getReference("${Utils.User}/${FirebaseAuth.getInstance().currentUser!!.uid}")
                            .get().addOnSuccessListener { snapshot->
                                if(snapshot.exists()){
                                    fetchUserDetails(FirebaseAuth.getInstance().currentUser!!.uid)
                                    user.userId = FirebaseAuth.getInstance().currentUser!!.uid
                                    startActivity(Intent(this,HomeActivity::class.java))
                                    finish()
                                }else{
                                    Toast.makeText(this,"Account does not exist",Toast.LENGTH_LONG).show()
                                    FirebaseAuth.getInstance().signOut()
                                    finish()
                                }
                            }

                    } else{
                        Toast.makeText(this,it.exception?.message,Toast.LENGTH_LONG).show()
                    }
                }

        }
    }

    private fun verifyCode(code : String){
        val credentials  = PhoneAuthProvider.getCredential(verificationId , code)
        signInWithCredential(credentials)
    }


    private fun sendVerificationCode(phoneNumber : String){
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(mCallback)
            .setActivity(this)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(s, forceResendingToken)
            verificationId = s
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code : String? = p0.smsCode
            if(code != null){
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@VerifyPhoneActivity,p0.message,Toast.LENGTH_LONG).show()
        }
    }
}