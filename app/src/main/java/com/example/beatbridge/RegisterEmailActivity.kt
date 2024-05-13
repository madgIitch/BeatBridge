package com.example.beatbridge

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.beatbridge.userModel.UserModel
import com.google.firebase.auth.FirebaseAuth

class RegisterEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sendEmailBtn: Button
    private lateinit var emailText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_email)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initializing views
        sendEmailBtn = findViewById(R.id.sendEmailBtn)
        emailText = findViewById(R.id.emailRegister)
        progressBar = findViewById(R.id.username_progressbar)

        sendEmailBtn.setOnClickListener {
            val email = emailText.text.toString().trim()
            userModel = UserModel(username = "default", passwordHash = "default", email = email, city = "default", country = "default", biography = "default", roles = setOf(UserModel.Role.MUSIC_LOVER))

            if (userModel.isValidEmail()) {
                progressBar.visibility = ProgressBar.VISIBLE
                checkEmailExists(email)
            } else {
                Toast.makeText(this, "Por favor, ingrese un correo electrónico válido.", Toast.LENGTH_LONG).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkEmailExists(email: String) {
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener { task ->
            progressBar.visibility = ProgressBar.INVISIBLE
            if (task.isSuccessful) {
                val isNewUser = task.result.signInMethods?.isEmpty() ?: true
                if (isNewUser) {
                    Toast.makeText(this, "El correo electrónico está disponible para registro.", Toast.LENGTH_SHORT).show()

                    // Cambiar a RegisterUsernameActivity pasando el email como extra
                    val intent = Intent(this, RegisterUsernameActivity::class.java).apply {
                        putExtra("email", email)
                    }
                    startActivity(intent)
                    finish() // Finaliza esta actividad para evitar volver a ella

                } else {
                    Toast.makeText(this, "El correo electrónico ya está en uso.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error al verificar el correo: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

}
