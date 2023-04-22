package com.example.test

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import com.example.test.Common.isEmpty
import com.example.test.Common.isMobileValid
import com.example.test.Common.makeToast
import com.example.test.SharedPrefs.ISLOGGEDIN
import com.example.test.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mAuth: FirebaseAuth
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var code = ""
    private var mVerificationId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = Common.createLoadingDialog(this)
        mAuth = FirebaseAuth.getInstance()
        mAuth.useAppLanguage()
        if (BuildConfig.DEBUG) {
            binding.etMobileNo.setText("1234567890")
        }
        if (SharedPrefs.getBoolean(ISLOGGEDIN)) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
        binding.btnLogin.setOnClickListener {
            if (isValid()) {
                if (binding.layoutOTP.visibility == VISIBLE) {
                    code = binding.etOTP.text.toString()
                    verifyVerificationCode()
                } else sendVerificationCode("+91" + binding.etMobileNo.text.toString())
            }
        }
        binding.btnChangeMobileNo.setOnClickListener {
            setOTPVisibility(GONE)
        }
    }

    private fun isValid(): Boolean {
        var isVal = true
        if (!isEmpty(binding.layoutMobileNo, "Please enter mobile No")) {
            if (!isMobileValid(binding.etMobileNo.text.toString())) {
                binding.layoutMobileNo.error = "Invalid Mobile No"
                isVal = false
            } else binding.layoutMobileNo.isErrorEnabled = false
        } else isVal = false


        if (binding.layoutOTP.visibility == VISIBLE) {
            if (!isEmpty(binding.layoutOTP, "Please enter OTP")) {
                if (binding.etOTP.text.toString().length < 6) {
                    isVal = false
                    binding.layoutOTP.error = "Invalid OTP"
                } else binding.layoutOTP.isErrorEnabled = false
            } else isVal = false
        }

        return isVal
    }

    private fun sendVerificationCode(phone: String) {
        progressDialog.show()
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    code = credential.smsCode.toString()
                    binding.etOTP.setText(code)
                    verifyVerificationCode()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> makeToast("Invalid Credentials")
                        is FirebaseTooManyRequestsException -> makeToast("Too many attempts, Try again later")
                        is FirebaseAuthException -> makeToast("Please verify..")
                        else -> makeToast("Something went wrong !!")
                    }
                    progressDialog.dismiss()
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d("TAG", "onCodeSent:$verificationId")
                    mVerificationId = verificationId
                    mResendToken = token
                    setOTPVisibility(VISIBLE)
                    progressDialog.dismiss()
                }

                override fun onCodeAutoRetrievalTimeOut(s: String) {
                    super.onCodeAutoRetrievalTimeOut(s)
                    makeToast("Time Out . Please resend OTP")
                    progressDialog.dismiss()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun setOTPVisibility(visibility: Int) {
        if (visibility == VISIBLE) {
            binding.btnLogin.text = "Verify"
            binding.layoutMobileNo.isEnabled = false
        } else {
            binding.layoutMobileNo.isEnabled = true
            binding.btnLogin.text = "Send OTP"
        }
        binding.btnChangeMobileNo.visibility = visibility
        binding.layoutOTP.visibility = visibility
    }

    private fun verifyVerificationCode() {
        if (code != "") {
            progressDialog.show()
            val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
            progressDialog.show()
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        SharedPrefs.setBoolean(ISLOGGEDIN, true)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        var message = "Something is wrong, we will fix it soon..."
                        if (it.exception is FirebaseAuthInvalidCredentialsException)
                            message = "Invalid OTP"
                        makeToast(message)
                    }
                }
        } else makeToast("Please enter OTP")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        progressDialog.dismiss()
    }
}