package com.arisu.chillarbuddy

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.arisu.chillarbuddy.databinding.ActivityMainBinding
import com.google.android.gms.common.AccountPicker
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64




private const val ACCOUNT_PICK = 10
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var progressDialog: CustomProgressDialog

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        progressDialog = CustomProgressDialog(this)


        val countdownTimer = object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                val email_id = savedata.getString("email", "")
                if (email_id == "") {
                    binding.googleBtn.visibility = View.VISIBLE
                    binding.load.visibility = View.GONE
                } else {
                    binding.signupForm.visibility = View.GONE
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                }

            }
        }
        countdownTimer.start()

        binding.googleBtn.setOnClickListener {
            progressDialog.show("Loading....")
            progressDialog.dismiss()
            binding.googleBtn.setOnClickListener {
                val intent: Intent = AccountPicker.newChooseAccountIntent(
                    null, null, arrayOf("com.google"),
                    false, null, null, null, null
                )
                startActivityForResult(intent, ACCOUNT_PICK)
            }
        }

        binding.signnBtn.setOnClickListener {
            progressDialog.show("")

            val url = "${Constant.urllink}signup.php"
            val queue1: RequestQueue = Volley.newRequestQueue(this)

            val deviceid: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            val time = System.currentTimeMillis()

            val stringRequest = object : StringRequest(
                Method.POST, url,
                { response ->
                    progressDialog.dismiss()

                    Log.e("qwq", response)

                   if (response.contains("Signup Successful")) {
                       Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                        val myEdit: SharedPreferences.Editor = savedata.edit()
                        myEdit.putString("email", binding.email.editText?.text.toString())
                        myEdit.putString("status", "login")
                        myEdit.apply();
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    }else{
                       Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                   }

                },
                Response.ErrorListener { error ->
                    // Handle the error here
                    progressDialog.dismiss()
                    println("Error: ${error.message}")
                }) {

                @RequiresApi(Build.VERSION_CODES.O)
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()


                    val dbit32 = videoplayyer.encrypt(deviceid, Hatbc()).toString()
                    val tbit32 = videoplayyer.encrypt(time.toString(), Hatbc()).toString()
                    val name = videoplayyer.encrypt(binding.ename.editText?.text.toString(), Hatbc()).toString()
                    val email =videoplayyer.encrypt(binding.email.editText?.text.toString(), Hatbc()).toString()
                    val phone =videoplayyer.encrypt(binding.phedit.editText?.text.toString(), Hatbc()).toString()
                    val refer =videoplayyer.encrypt(binding.refercode.editText?.text.toString(), Hatbc()).toString()

                    val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
                    val ten64 = Base64.getEncoder().encodeToString(tbit32.toByteArray())
                    val name64 = Base64.getEncoder().encodeToString(name.toByteArray())
                    val email64 = Base64.getEncoder().encodeToString(email.toByteArray())
                    val phone64 = Base64.getEncoder().encodeToString(phone.toByteArray())
                    val refer64 = Base64.getEncoder().encodeToString(refer.toByteArray())

                    val encodemap: MutableMap<String, String> = HashMap()
                    encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                    encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                    encodemap["nameofhfuismnwdredfgsgsgdhddgd"] = name64
                    encodemap["emofhfuisgdtdrefssfilgsgdhddgd"] = email64
                    encodemap["phofhfuisgdtdrefssfgsgsgneddgd"] = phone64
                    encodemap["reffhfuisgdtdrefssfgsgsgnerdgd"] = refer64


                    val jason = Json.encodeToString(encodemap)

                    val den264 = Base64.getEncoder().encodeToString(jason.toByteArray())

                    val final = URLEncoder.encode(den264, StandardCharsets.UTF_8.toString())

                    params["dase"] = final
                    return params
                }
            }

            queue1.add(stringRequest)

        }

    }
    @SuppressLint("HardwareIds")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACCOUNT_PICK) {
            var accountName = data?.extras?.get(AccountManager.KEY_ACCOUNT_NAME) ?: "NONE"
//            Toast.makeText(this, "$accountName", Toast.LENGTH_SHORT).show()
            accountName = accountName.toString()
            if (accountName.toString() != "NONE") {
                val url = "${Constant.urllink}googlelogin.php"
                val queue: RequestQueue = Volley.newRequestQueue(this)

                val deviceid: String = Settings.Secure.getString(
                    contentResolver,
                    Settings.Secure.ANDROID_ID
                )

                val time = System.currentTimeMillis()

                val stringRequest = object : StringRequest(
                    Method.POST, url,
                    { response ->

                        if (response.contains("Signup")) {
                            binding.login.visibility = View.GONE
                            binding.signupForm.visibility = View.VISIBLE
                            binding.email.editText?.setText(accountName)
                            binding.email.isEnabled = false

                        } else {
                            val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                            val myEdit: SharedPreferences.Editor = savedata.edit()
                            myEdit.putString("email",accountName)
                            myEdit.apply();


                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                            finish()
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
                        val embit32 =
                            videoplayyer.encrypt(accountName.toString(), Hatbc()).toString()

                        val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
                        val ten64 = Base64.getEncoder().encodeToString(tbit32.toByteArray())
                        val emen64 = Base64.getEncoder().encodeToString(embit32.toByteArray())

                        val encodemap: MutableMap<String, String> = HashMap()
                        encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                        encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                        encodemap["emofhfuisgdtdrefssonsgsgdhddgd"] = emen64


                        val jason = Json.encodeToString(encodemap)

                        val den264 = Base64.getEncoder().encodeToString(jason.toByteArray())

                        val final = URLEncoder.encode(den264, StandardCharsets.UTF_8.toString())

                        params["dase"] = final


                        return params
                    }
                }
                queue.add(stringRequest)

            }
        }
    }

}