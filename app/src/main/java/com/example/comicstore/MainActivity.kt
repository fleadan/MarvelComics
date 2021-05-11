package com.example.comicstore

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private val BASE_URL = "http://gateway.marvel.com/v1/public/"
    lateinit var results: Results
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_comics)
        manager = LinearLayoutManager(this)
        getAllData()

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAllData() {

        val callToService = getRetrofit().create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val responseFromService = callToService.getComics()
            runOnUiThread {
                results = responseFromService.body() as Results


                if (responseFromService.isSuccessful) {
                    Log.i("Comics", results.data?.results.toString())
                    Toast.makeText(applicationContext, "Exit!", Toast.LENGTH_LONG).show()


                    recyclerView = findViewById<RecyclerView>(R.id.comics_recycler).apply {

                        layoutManager = manager
                        myAdapter = ComicsAdapter(results.data?.results)
                        adapter = myAdapter

                    }


                }  else {
                    Log.i("Comics", "No jaló")
                    Toast.makeText(applicationContext, "Error!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}