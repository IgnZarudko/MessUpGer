package by.ignot.messupger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class LogInActivity : AppCompatActivity() {

    private lateinit var userPhoneNumber : EditText
    private lateinit var verificationCode : EditText
    private lateinit var logInButton: Button
    private lateinit var sendCodeButton: Button

    private var isAuthenticationActive : Boolean = false

    private var storeVerificationId : String = ""
    private lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)

        userIsLoggedIn()

        userPhoneNumber = findViewById(R.id.userPhoneNumberId)
        verificationCode = findViewById(R.id.verificationCodeFromMessageId)
        storeVerificationId = ""

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                print("Failure")
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                storeVerificationId = verificationId
                resendToken = token
            }
        }

        logInButton = findViewById(R.id.logInButtonId)
        logInButton.setOnClickListener {
                verifyPhoneNumberAndCode()
        }

        sendCodeButton = findViewById(R.id.sendCodeButtonId)
        sendCodeButton.setOnClickListener{
            if (!isAuthenticationActive){
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    userPhoneNumber.text.toString(), // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    this, // Activity (for callback binding)
                    callbacks) // OnVerificationStateChangedCallbacks
                    sendCodeButton.text = "Resend Code"
            }
            else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    userPhoneNumber.text.toString(), // Phone number to verify
                    60, // Timeout duration
                    TimeUnit.SECONDS, // Unit of timeout
                    this, // Activity (for callback binding)
                    callbacks, // OnVerificationStateChangedCallbacks
                    resendToken)
            }
        }
    }

    private fun verifyPhoneNumberAndCode(){
        val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(storeVerificationId, verificationCode.text.toString())
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->
                if (task.isSuccessful){
                    userIsLoggedIn()
                }
            }
    }

    private fun userIsLoggedIn(){
        val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if (user != null){
            startActivity(Intent(applicationContext, MainPageActivity::class.java))
            finish()
            return
        }
    }
}
