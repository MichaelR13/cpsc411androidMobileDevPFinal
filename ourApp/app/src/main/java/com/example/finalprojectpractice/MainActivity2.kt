package com.example.finalprojectpractice
import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class MainActivity2 : AppCompatActivity() {

    //val CITY2: String = "los angeles, ca"
    // Get CITY2 from MainActivity.kt
    
    val API2: String = "297ad384163952fbaac05168ae4c7381"
    private val LOG_TAG_BUTTON = "Button"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
        
        // Store text input from searchBarCity as a string called CITY2
        // Finally, have button go to MainActivity3.kt, and starting a loop
        val searchBarCity = findViewById<TextView>(R.id.searchBarCity)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("CITY2", searchBarCity.text.toString())
            startActivity(intent)
        }
    }

    inner class weatherTask() : AsyncTask<String, Void, String>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try {
                // Get user input string, and put that string into the URL
                // Ex : 
                // If input is "Anaheim"
                // https://api.openweathermap.org/data/2.5/weather?q=Anaheim&units=imperial&appid=297ad384163952fbaac05168ae4c7381
                // If input is "Los Angeles"
                // https://api.openweathermap.org/data/2.5/weather?q=Los Angeles&units=imperial&appid=297ad384163952fbaac05168ae4c7381    
                val CITY2 = intent.getStringExtra("CITY2")
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY2&units=imperial&appid=$API2")                  
                    .readText(Charsets.UTF_8)
                Log.i(LOG_TAG_BUTTON,"Button was clicked!")
            }
            catch (e: Exception)
            {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp") + "°F"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°F"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°F"
                val pressure = main.getString("pressure") + " hPa"
                val humidity = main.getString("humidity") + " %"
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed") + " mph"
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name") + ", " + sys.getString("country")
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }
            catch(e : Exception)
            {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }
        }
    }
}
