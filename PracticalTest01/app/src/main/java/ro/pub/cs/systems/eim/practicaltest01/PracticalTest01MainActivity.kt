package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.widget.Button        // B.1: ca să poți folosi Button
import android.widget.EditText      // B.1: ca să poți folosi EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Build

class PracticalTest01MainActivity : AppCompatActivity() {


    private var leftCount = 0
    private var rightCount = 0
    // C.2: "curierul" care deschide ecranul 2 și aduce înapoi răspunsul
    private val secondaryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Rezultat: OK", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Rezultat: CANCEL", Toast.LENGTH_LONG).show()
        }
    }
    // D.2: "radioul" care primește mesajele și le scrie în Logcat
    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PracticalTest01Broadcast", intent?.getStringExtra("message") ?: "fără mesaj")
        }
    }

    // D.2: frecvențele pe care ascultă radioul (3 acțiuni, folosite și la D.1)
    private val intentFilter = IntentFilter().apply {
        addAction("ro.pub.cs.systems.eim.practicaltest01.action1")
        addAction("ro.pub.cs.systems.eim.practicaltest01.action2")
        addAction("ro.pub.cs.systems.eim.practicaltest01.action3")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // B.1 pasul 1: găsești elementele după id-urile puse la A.2
        val leftText = findViewById<EditText>(R.id.left_text)
        val rightText = findViewById<EditText>(R.id.right_text)
        val leftButton = findViewById<Button>(R.id.left_button)
        val rightButton = findViewById<Button>(R.id.right_button)

        if (savedInstanceState != null) {
            leftCount = savedInstanceState.getInt("leftCount")
            rightCount = savedInstanceState.getInt("rightCount")
            leftText.setText(leftCount.toString())
            rightText.setText(rightCount.toString())
            Log.d("PracticalTest01", "RESTORE: left=$leftCount right=$rightCount")
        }

        // B.1 pasul 2: paznicul de pe butonul stâng
        leftButton.setOnClickListener {
            leftCount++                        // crește cu 1
            leftText.setText(leftCount.toString())    // afișează noul număr
        }

        // B.1 pasul 3: la fel pentru butonul drept (scrii tu tot blocul)
        rightButton.setOnClickListener {
            rightCount++
            rightText.setText(rightCount.toString())
        }
        // C.2: butonul care deschide ecranul 2
        val navigateButton = findViewById<Button>(R.id.navigate_button)
        navigateButton.setOnClickListener {
            val intent = Intent(this, PracticalTest01SecondaryActivity::class.java) // biletul: "deschide ecranul 2"
            intent.putExtra("total", leftCount + rightCount)                        // pui totalul pe bilet
            secondaryLauncher.launch(intent)                                        // curierul îl duce și așteaptă răspunsul
        }
    }
        // B.2 b) SALVARE: Android o apelează singur chiar înainte să distrugă activitatea
        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putInt("leftCount", leftCount)
            outState.putInt("rightCount", rightCount)
            Log.d("PracticalTest01", "SAVE: left=$leftCount right=$rightCount")
        }
        // D.2: pornești radioul cât timp ecranul e vizibil
        override fun onResume() {
            super.onResume()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(messageReceiver, intentFilter, Context.RECEIVER_EXPORTED)
            } else {
                registerReceiver(messageReceiver, intentFilter)
            }
        }

        // D.2: îl oprești când ecranul nu mai e vizibil
        override fun onPause() {
            unregisterReceiver(messageReceiver)
            super.onPause()
        }

}