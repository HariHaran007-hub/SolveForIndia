package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityCreateProfileBinding
import com.zero.golgol.model.UserDetails
import java.util.*

class CreateProfileActivity : BaseActivity() {

    companion object{
        const val PHONE_NUMBER : String = "phone_number"
        const val USER_NAME : String = "user_name"
        const val USER_AGE : String = "user_age"
        const val USER_GENDER : String = "user_gender"
        const val USER_ID_CARD_NUMBER  : String = "user_id_card_number"
        private const val RC_SIGN_IN = 120
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityCreateProfileBinding

    private var isValid = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
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
                    .trim().isNotEmpty()
                        && binding.etName.text.toString().trim()
                    .isNotEmpty() && binding.etPhone.text.toString().trim().isNotEmpty()
                        && binding.spinnerGender.selectedItemPosition != 0
        }
    }

    private fun clickListeners() {
        binding.btnContinue.setOnClickListener {
            val phoneNumber : String = binding.etPhone.text.toString().trim()

            val intent  = Intent(this,VerifyPhoneActivity::class.java)
            intent.putExtra("from",true)
            intent.putExtra(PHONE_NUMBER,phoneNumber)
            intent.putExtra(USER_NAME,binding.etName.text.toString().trim())
            intent.putExtra(USER_AGE,binding.etAge.text.toString().trim())
            intent.putExtra(USER_GENDER,binding.spinnerGender.selectedItem.toString())
            intent.putExtra(USER_ID_CARD_NUMBER,binding.etId.text.toString())
            startActivity(intent)
        }

        binding.tvSignIn.setOnClickListener { startActivity(Intent(this, SignInActivity::class.java)) }

        binding.ivGoogleButton.setOnClickListener{
            googleSign()
        }

        binding.spinnerGender.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, selectedItem: Int, p3: Long) {
                isValid.value = selectedItem != 0
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //Nothing selected
            }

        }

        binding.etName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

        binding.etAge.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

        binding.etId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

        binding.etPhone.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(name: Editable?) {
                isValid.value = name.toString().isNotEmpty()
            }
        })

    }
    //Setup Gender Spinner items - Male, female, etc.
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

    private fun googleSign(){
        Log.d("TAG", "googleSign: hii")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("214909792773-1i7roh4f67s7k65i6i7hs5f470p4s9ce.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mAuth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("CreateProfileActivity", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("CreateProfileActivity", "Google sign in failed", e)
                }
            }else{
                Log.w("CreateProfileActivity", exception.toString())
            }

        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("CreateProfileActivity", "signInWithCredential:success")
                    val userId = task.result.user?.uid
                    FirebaseDatabase.getInstance().getReference("users").child(userId.toString())
                        .get().addOnSuccessListener {snapshot->
                            if(snapshot.exists()){
                                val userDetails = snapshot.getValue(UserDetails::class.java)

                                user.age = userDetails?.userAge
                                user.username = userDetails?.userName
                                user.userId = userDetails?.userId
                                user.gender = userDetails?.userGender
                                user.idNumber = userDetails?.userIdCardNumber
                                user.phoneNumber = userDetails?.userPhoneNumber
                                user.gols = userDetails?.gols ?: 0
                                user.badges = userDetails?.badges ?: 0
                                user.challenges = userDetails?.activeChallenges

                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }else{
                                startActivity(Intent(this,GoogleSignInUserDetails::class.java))
                                finish()
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("CreateProfileActivity", "signInWithCredential:failure", task.exception)

                }
            }
    }

}