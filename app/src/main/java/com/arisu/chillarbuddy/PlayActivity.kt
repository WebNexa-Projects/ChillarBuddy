package com.arisu.chillarbuddy

import android.animation.Animator
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.arisu.chillarbuddy.databinding.ActivityPlayBinding
import com.arisu.chillarbuddy.databinding.ActivitySpinBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64

class PlayActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlayBinding
    private var isinstall = false
    private var lottiClickable = true
    private var oldusedstorage: Long = 0
    private var codeRunning = false
    private var backable = true
    var clicked = false

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPlayBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.back.setOnClickListener {
            if (backable) {
                finish()
            }

        }
        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        binding.bala.text = savedata.getString("balance", "")

        binding.playBtn.setOnClickListener {
            if (lottiClickable) {

                binding.l1.setMinAndMaxFrame(1, 100)
                oldusedstorage = internalMemoryUsed()
                binding.l1.addAnimatorListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(p0: Animator) {
                        lottiClickable = false
                    }

                    override fun onAnimationEnd(p0: Animator) {


                        //  addPoint()
                        //   Toast.makeText(this@PlayActivity, "Hello Buddy !", Toast.LENGTH_SHORT).show()

                        binding.startbtn.visibility = View.VISIBLE
                        binding.l2.addAnimatorListener(object :
                            Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                                backable = false
                                binding.playBtn.isClickable = false

                            }

                            override fun onAnimationEnd(animation: Animator) {


                                backable = true

                                val spin_install = savedata.getString("play_install", "")
                                val spinValue = savedata.getString("ani", "")

                                if (spin_install == spinValue) {
                                    binding.startbtn.visibility = View.GONE
                                    binding.playBtn.isClickable = true
                                    Toast.makeText(
                                        applicationContext,
                                        "Install And Wait For 30 Sec",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //add condtion

                                    // (internalMemoryUsed() - oldusedstorage) >= 16000000)

                                } else {

                                    addPoint()
                                }
                                binding.l2.visibility = View.GONE
                                binding.playBtn.isClickable = true
                                binding.startbtn.visibility = View.GONE

                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }


                        })
                        binding.l2.setMinAndMaxFrame(240, 625)
                        binding.l2.playAnimation()


                    }

                    override fun onAnimationCancel(p0: Animator) {

                    }

                    override fun onAnimationRepeat(p0: Animator) {

                    }


                })
                binding.l1.playAnimation()


            }
        }
    }

    private fun internalMemoryAvailable(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.availableBlocksLong * statFs.blockSizeLong
    }

    private fun internalMemoryTotal(): Long {
        val statFs = StatFs(Environment.getDataDirectory().path)
        return statFs.blockSizeLong * statFs.blockCountLong
    }

    private fun internalMemoryUsed(): Long {
        val total = internalMemoryTotal()
        val available = internalMemoryAvailable()
        return if (total > available) {
            total - available
        } else {
            -11111111
        }
    }

    override fun onBackPressed() {
        if (backable)
            super.onBackPressed()
    }

    @SuppressLint("HardwareIds")
    private fun addPoint() {
        val queue2: RequestQueue = Volley.newRequestQueue(this)


        if (!codeRunning) {
            lottiClickable = false

            codeRunning = true

            val url = "${Constant.urllink}animation.php"

            val deviceid: String = Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val time = System.currentTimeMillis()
            val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
            val email = savedata.getString("email", "0")

            val stringRequest = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    binding.startbtn.visibility = View.VISIBLE

                    if (isinstall)
                        isinstall = false
                    binding.l2.addAnimatorListener(object :
                        Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {

                            lottiClickable = false
                            backable = false

                        }

                        override fun onAnimationEnd(animation: Animator) {

                            clicked = true
                            codeRunning = false
                            backable = true
                            binding.startbtn.visibility = View.GONE
                            lottiClickable = true

                        }

                        override fun onAnimationCancel(animation: Animator) {

                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }


                    })
                    binding.l2.setMinAndMaxFrame(240, 625)
                    binding.l2.playAnimation()

                    if (response.contains(",")) {

                        val response_value = response.split(",")

                        Toast.makeText(this@PlayActivity, response_value[0], Toast.LENGTH_SHORT)
                            .show()

                        binding.bala.text = response_value[1]
                        val editor = savedata.edit()
                        editor.putString("balance", response_value[1])
                        // editor.putString("ins_time",response_value[3])
                        editor.apply()
                        binding.startbtn.visibility = View.GONE

                    } else {

                        Toast.makeText(
                            this@PlayActivity,
                            response,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.startbtn.visibility = View.GONE

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
                    val email32 = videoplayyer.encrypt(email.toString(), Hatbc()).toString()

                    val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
                    val ten64 = Base64.getEncoder().encodeToString(tbit32.toByteArray())
                    val email64 = Base64.getEncoder().encodeToString(email32.toByteArray())

                    val encodemap: MutableMap<String, String> = HashMap()
                    encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                    encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                    encodemap["emofhfuisailrrefssfgsgsgdhddgd"] = email64


                    val jason = Json.encodeToString(encodemap)

                    val den264 =
                        Base64.getEncoder().encodeToString(jason.toByteArray())

                    val final =
                        URLEncoder.encode(den264, StandardCharsets.UTF_8.toString())

                    params["dase"] = final

                    return params
                }
            }

            queue2.add(stringRequest)
        }
    }


}
