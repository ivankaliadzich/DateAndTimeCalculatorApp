package edu.put.inf153936

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goToDates(v: View){
        val to_date = findViewById<Button>(R.id.button_date)
        to_date.setOnClickListener{
            val intent = Intent(this, DateCalculator::class.java)
            startActivity(intent)
        }
    }

    fun goToTimes(v: View){
        val to_time = findViewById<Button>(R.id.button_time)
        to_time.setOnClickListener{
            val intent = Intent(this, TimeCalculator::class.java)
            startActivity(intent)
        }
    }

}


