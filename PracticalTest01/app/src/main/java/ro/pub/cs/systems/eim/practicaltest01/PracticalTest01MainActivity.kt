package ro.pub.cs.systems.eim.practicaltest01

import android.os.Bundle
import android.widget.Button        // B.1: ca să poți folosi Button
import android.widget.EditText      // B.1: ca să poți folosi EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log

class PracticalTest01MainActivity : AppCompatActivity() {


    private var leftCount = 0
    private var rightCount = 0

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
    }
        // B.2 b) SALVARE: Android o apelează singur chiar înainte să distrugă activitatea
        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putInt("leftCount", leftCount)
            outState.putInt("rightCount", rightCount)
            Log.d("PracticalTest01", "SAVE: left=$leftCount right=$rightCount")
        }

}