package by.ignot.messupger.activity


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import by.ignot.messupger.R
import com.google.firebase.auth.FirebaseAuth

class MainPageActivity : AppCompatActivity() {

    private lateinit var logOutButton : Button
    private lateinit var findUserButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        logOutButton = findViewById(R.id.logOutButtonId)

        logOutButton.setOnClickListener{FirebaseAuth.getInstance().signOut();
            val intent = Intent(applicationContext, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
            return@setOnClickListener
        }

        findUserButton = findViewById(R.id.findUserButtonId)

        findUserButton.setOnClickListener{
            startActivity(Intent(applicationContext, FindUserActivity::class.java))
        }

        getPermissions()
    }

    private fun getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS), 1)

        }
    }
}
