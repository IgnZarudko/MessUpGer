package by.ignot.messupger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth

class MainPageActivity : AppCompatActivity() {

    private lateinit var logOutButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        logOutButton = findViewById(R.id.logOutButtonId)

        logOutButton.setOnClickListener{FirebaseAuth.getInstance().signOut();
            var intent = Intent(applicationContext, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            return@setOnClickListener
        }
    }
}
