package edu.itesm.marvelapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



lateinit var database: FirebaseDatabase
lateinit var reference: DatabaseReference

// Incluye las variables de Analytics:
lateinit var analytics: FirebaseAnalytics
lateinit var bundle: Bundle

class MainFragment : Fragment() {

    private val BASE_URL = "https://gateway.marvel.com/v1/public/"
    private lateinit var recyclerView: RecyclerView
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var myAdapter: RecyclerView.Adapter<*>
    private lateinit var  results: Results

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        manager = LinearLayoutManager(context)

        database = FirebaseDatabase.getInstance()

        analytics = FirebaseAnalytics.getInstance(context)
        bundle = Bundle()

        getAllData()

        imageViewLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        buttonMisComics.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_mainFragment_to_carritoFragment)
        }

    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getAllData(){

        val callToService = getRetrofit().create(APIService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val responseFromService = callToService.getComics()
            activity?.runOnUiThread {


                if(responseFromService.isSuccessful){
                    results = responseFromService.body() as Results


                    recyclerView = recyclerComics.apply {

                        layoutManager = manager
                        myAdapter = ComicsAdapter(results.data?.results)
                        adapter = myAdapter

                    }
                } else {
                    Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}