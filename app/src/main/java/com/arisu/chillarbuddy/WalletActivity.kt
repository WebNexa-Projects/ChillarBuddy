package com.arisu.chillarbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.arisu.chillarbuddy.databinding.ActivityWalletBinding

class WalletActivity : AppCompatActivity() {
    lateinit var binding: ActivityWalletBinding
    lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityWalletBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        email = savedata.getString("email", "").toString()

        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.bala.text = savedata.getString("balance", "")
        binding.back.setOnClickListener {
            finish()
        }

        binding.withBtn.setOnClickListener {


            if (savedata.getString("balance", "100")!!.toInt() >=300) {
                val intent = Intent(this, withVerifyActivity::class.java)
                startActivity(intent)
            } else {

                Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show()
            }
        }

        binding.hisRecycle.layoutManager = LinearLayoutManager(this)
        val adapter = AdapterHist(ArrayList())
        binding.hisRecycle.adapter = adapter



        val requestQueue: RequestQueue = Volley.newRequestQueue(this)


      val  url = "${Constant.urllink}get_trans.php?email=$email"
// Create a JSON array request
        val jsonArrayRequest = object : JsonArrayRequest(
            Method.GET,
            url,
            null,
            Response.Listener { response ->
                // Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()

                var transarray = ArrayList<historyData>()

                for (i in 0 until response.length()) {

                    val dataObject = response.getJSONObject(i)
                    val coin = dataObject.getString("amount")
                    val purpose = dataObject.getString("purpose")
                    val transObj = historyData(coin, purpose)
                    transarray.add(transObj)

                }
                adapter.updateTrans(transarray)


            },
            Response.ErrorListener { error ->

                Toast.makeText(this, "Link Not Work !", Toast.LENGTH_SHORT).show()

            }) {}

        requestQueue.add(jsonArrayRequest)


    }
}