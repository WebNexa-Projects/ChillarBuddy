package com.arisu.chillarbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64


class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("HardwareIds")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        changeStatusBarColor( R.color.app_color)

        return inflater.inflate(R.layout.fragment_home, container, false)




        return view

    }


    @SuppressLint("HardwareIds")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)

        val savedata = requireActivity().getSharedPreferences("ChillarBuddy", MODE_PRIVATE)

        val walletbtn = view.findViewById<CardView>(R.id.wallet)
        val dailybtn = view.findViewById<LinearLayout>(R.id.daily)
        val b=view.findViewById<TextView>(R.id.bala)
        val spin=view.findViewById<LinearLayout>(R.id.spin)
        val scratch=view.findViewById<LinearLayout>(R.id.scratch)
        val play=view.findViewById<LinearLayout>(R.id.play)
        val user=view.findViewById<TextView>(R.id.username)
        val lotti=view.findViewById<LottieAnimationView>(R.id.lotti)

        lotti.playAnimation()

        b.text=savedata.getString("balance", "")
        user.text=savedata.getString("email", "")


        dailybtn.setOnClickListener {
            val dailyValue = savedata.getString("daily", "")
            if (dailyValue?.isNotEmpty() == true && dailyValue.toInt() == 0) {
                dailybonus()
            } else {
                Toast.makeText(requireContext(), "You Hava Already Claim !", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        spin.setOnClickListener {
            val intent = Intent(activity, SpinActivity::class.java)
            startActivity(intent)
        }
        scratch.setOnClickListener {
            val intent = Intent(activity, ScratchActivity::class.java)
            startActivity(intent)
        }
        play.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            startActivity(intent)
        }


        walletbtn?.setOnClickListener {

            val intent = Intent(activity, WalletActivity::class.java)
            startActivity(intent)
        }


        val deviceid: String =
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

        // Continue with your code here

        val time = System.currentTimeMillis()

        val url2 = "${Constant.urllink}get_user_value.php"


        val email = savedata.getString("email", "0")

        val queue1: RequestQueue = Volley.newRequestQueue(requireContext())
        val stringRequest = object : StringRequest(
            Request.Method.POST, url2,
            { response ->

                if (response.contains(",")) {
                    val alldata = response.split(",")
                    val myEdit: SharedPreferences.Editor = savedata.edit()
                    myEdit.putString("balance", alldata[0])
                    myEdit.putString("spin", alldata[1])
                    myEdit.putString("scratch", alldata[2])
                    myEdit.putString("install", alldata[3])
                    myEdit.putString("ani", alldata[4])
                    myEdit.putString("myrefer", alldata[5])
                    myEdit.putString("refercount", alldata[6])
                    myEdit.putString("successcount", alldata[7])
                    myEdit.putString("daily", alldata[8])
                    myEdit.putString("play_install", alldata[9])
                    myEdit.putString("scratch_install", alldata[10])
                    myEdit.putString("spin_install", alldata[11])

                    myEdit.apply();
                }

            },
            Response.ErrorListener { error ->
// Handle the error here
                println("Error: ${error.message}")
            }) {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()


                val dbit32 = videoplayyer.encrypt(deviceid, Hatbc()).toString()
                val tbit32 = videoplayyer.encrypt(time.toString(), Hatbc()).toString()
                val email = videoplayyer.encrypt(email.toString(), Hatbc()).toString()

                val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
                val ten64 = Base64.getEncoder().encodeToString(tbit32.toByteArray())
                val email64 = Base64.getEncoder().encodeToString(email.toByteArray())

                val encodemap: MutableMap<String, String> = HashMap()
                encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                encodemap["emofhfuisailrrefssfgsgsgdhddgd"] = email64


                val jason = Json.encodeToString(encodemap)

                val den264 = Base64.getEncoder().encodeToString(jason.toByteArray())

                val final = URLEncoder.encode(den264, StandardCharsets.UTF_8.toString())

                params["dase"] = final


                return params
            }
        }

        queue1.add(stringRequest)


        val offerwallview = view.findViewById<RecyclerView>(R.id.offerwall_recyclerview)
        offerwallview?.layoutManager = LinearLayoutManager(requireContext())
        val adapter = offerwallAdapter(ArrayList())
        offerwallview?.adapter = adapter
        val url = "${Constant.urllink}get_offerwall.php"

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())


// Create a JSON array request
        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
// Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show()

                var transarray = ArrayList<offerwallData>()

                for (i in 0 until response.length()) {

                    val dataObject = response.getJSONObject(i)
                    val title = dataObject.getString("title")
                    val coin = dataObject.getString("amount")
                    val image = dataObject.getString("image")
                    val link = dataObject.getString("link")
                    val des = dataObject.getString("description")
                    val transObj = offerwallData(title, coin, image, link, des)
                    transarray.add(transObj)

                }
                adapter.updateTrans(transarray)


            },
            Response.ErrorListener { error ->

              //  Toast.makeText(requireContext(), "Link Not Work !", Toast.LENGTH_SHORT).show()

            }) {}

        requestQueue.add(jsonArrayRequest)
    }

    @SuppressLint("HardwareIds")
    private fun dailybonus() {

        val savedata = requireActivity().getSharedPreferences("ChillarBuddy", MODE_PRIVATE)

        val deviceid: String = Settings.Secure.getString(
            requireActivity().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val time = System.currentTimeMillis()

        val url3 = "${Constant.urllink}daily.php"

        val email = savedata.getString("email", "0")

        val queue3: RequestQueue = Volley.newRequestQueue(requireContext())
        val stringRequest = object : StringRequest(
            Request.Method.POST, url3,
            { response ->

                if (response.contains(",")) {
                    val alldata = response.split(",")
                    val savedata =
                        requireActivity().getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                    val myEdit: SharedPreferences.Editor = savedata.edit()
                    myEdit.putString("balance", alldata[1])
                    myEdit.apply();
                    view?.findViewById<TextView>(R.id.bala)?.text = alldata[1]
                    Toast.makeText(requireContext(), alldata[0], Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error ->
// Handle the error here
                println("Error: ${error.message}")
            }) {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()


                val dbit32 = videoplayyer.encrypt(deviceid, Hatbc()).toString()
                val tbit32 = videoplayyer.encrypt(time.toString(), Hatbc()).toString()
                val email = videoplayyer.encrypt(email.toString(), Hatbc()).toString()

                val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
                val ten64 = Base64.getEncoder().encodeToString(tbit32.toByteArray())
                val email64 = Base64.getEncoder().encodeToString(email.toByteArray())

                val encodemap: MutableMap<String, String> = HashMap()
                encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                encodemap["emofhfuisailrrefssfgsgsgdhddgd"] = email64


                val jason = Json.encodeToString(encodemap)

                val den264 = Base64.getEncoder().encodeToString(jason.toByteArray())

                val final = URLEncoder.encode(den264, StandardCharsets.UTF_8.toString())

                params["dase"] = final


                return params
            }
        }

        queue3.add(stringRequest)
    }
    private fun changeStatusBarColor(colorResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = resources.getColor(colorResource, requireActivity().theme)
        }
    }
}