package com.arisu.chillarbuddy

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
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
import com.anupkumarpanwar.scratchview.ScratchView
import com.arisu.chillarbuddy.databinding.ActivityPlayBinding
import com.arisu.chillarbuddy.databinding.ActivityScratchBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64

class ScratchActivity : AppCompatActivity() {

    lateinit var binding: ActivityScratchBinding
    var backable = true
    var sclimit = 0
    private var oldusedstorage: Long = 0

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    override fun onCreate(savedInstanceState: Bundle?) {


        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding = ActivityScratchBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            if (backable) {
                finish()
            }

        }
        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        binding.bala.text = savedata.getString("balance", "")
        sclimit = savedata.getString("scratch", "")!!.toInt()

        binding.scratchView.setRevealListener(object : ScratchView.IRevealListener {
            override fun onRevealed(scratchView: ScratchView) {
            }

            override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {
                backable = false
                if (percent >= 0.32) {

                    binding.startbtn.visibility = View.VISIBLE

                    //
                    val countdownTimer = object : CountDownTimer(2000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {


                        }


                        @SuppressLint("HardwareIds")
                        override fun onFinish() {


                            binding.l2.addAnimatorListener(object :
                                Animator.AnimatorListener {
                                override fun onAnimationStart(animation: Animator) {

                                    backable = false

                                }

                                override fun onAnimationEnd(animation: Animator) {

                                    val spin_install=savedata.getString("scratch_install", "")
                                    val spinValue = savedata.getString("scratch", "")

                                    if (spin_install == spinValue) {
                                        binding.startbtn.visibility = View.GONE
                                        binding.l2.visibility = View.GONE
                                        binding.scratchView.mask()
                                        backable = true
                                        Toast.makeText(applicationContext, "Install And Wait For 30 Sec", Toast.LENGTH_SHORT).show()
                                        //add condtion

                                       // (internalMemoryUsed() - oldusedstorage) >= 16000000)

                                    }else{

                                        addbala()
                                    }
                                    backable = true
                                    binding.l2.visibility = View.GONE
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
                    }
                    countdownTimer.start()
                }
            }
        })
    }
    override fun onBackPressed() {
        if (backable)
            super.onBackPressed()
    }

    fun addbala(){

        val deviceid: String = Settings.Secure.getString(contentResolver,Settings.Secure.ANDROID_ID)
        val time = System.currentTimeMillis()
        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        val email = savedata.getString("email", "0")
        // progressDialog.show()
        val url = "${Constant.urllink}scratch.php"
        val queue1: RequestQueue =
            Volley.newRequestQueue(this@ScratchActivity)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                binding.startbtn.visibility = View.GONE

                val countdownTimer = object : CountDownTimer(2500, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {
                        backable = true
                        //   progressDialog.dismiss()
                    }
                }

                countdownTimer.start()

                if (response.contains(",")) {

                    binding.l2.visibility = View.VISIBLE
                    val spindata = response.split(",")
                    val savedata =
                        getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                    val myEdit: SharedPreferences.Editor = savedata.edit()
                    myEdit.putString("balance", spindata[1])
                    myEdit.putString("scratch", spindata[2])
                    myEdit.apply();

                    Toast.makeText(this@ScratchActivity, spindata[0], Toast.LENGTH_SHORT).show()

                    sclimit = (savedata.getString("scratch", ""))!!.toInt()
                    binding.bala.setText(savedata.getString("balance", ""))

                    binding.scratchView.mask()
                }else{
                    Toast.makeText(this@ScratchActivity, response, Toast.LENGTH_SHORT).show()
                    binding.scratchView.mask()
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
                val email64 =Base64.getEncoder().encodeToString(email.toByteArray())


                val encodemap: MutableMap<String, String> = HashMap()
                encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                encodemap["emofhfuisailrrefssfgsgsgdhddgd"] = email64


                val jason = Json.encodeToString(encodemap)

                val den264 =
                    Base64.getEncoder().encodeToString(jason.toByteArray())

                val final =
                    URLEncoder.encode(
                        den264,
                        StandardCharsets.UTF_8.toString()
                    )

                params["dase"] = final


                return params
            }
        }

        queue1.add(stringRequest)
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
}
