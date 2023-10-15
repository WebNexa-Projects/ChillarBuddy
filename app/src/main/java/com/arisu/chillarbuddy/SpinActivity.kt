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
import com.arisu.chillarbuddy.databinding.ActivityHomeBinding
import com.arisu.chillarbuddy.databinding.ActivitySpinBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.Random

class SpinActivity : AppCompatActivity() {
    lateinit var binding: ActivitySpinBinding
    private var oldusedstorage: Long = 0
    private lateinit var angle: Array<String>

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    var backable = true
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivitySpinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        angle = arrayOf("72", "110", "180", "254", "40")

        binding.back.setOnClickListener {
            if (backable) {

                finish()
            }
        }

        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        binding.bala.text = savedata.getString("balance", "")

        binding.spinBtn.setOnClickListener {
            val durationInMillis: Long = 2500 // 5 seconds
            val intervalInMillis: Long = 35

            val countdownTimer =
                object : CountDownTimer(durationInMillis, intervalInMillis) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.spinImage.rotation = binding.spinImage.rotation + 10


                    }

                    @SuppressLint("HardwareIds")
                    override fun onFinish() {


                        binding.l2.addAnimatorListener(object :
                            Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                                backable = false
                                binding.spinBtn.isClickable = false

                            }

                            override fun onAnimationEnd(animation: Animator) {


                                backable = true
                                binding.l2.visibility = View.GONE
                                binding.spinBtn.isClickable = true
                                binding.startbtn.visibility = View.GONE
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }


                        })
                        binding.l2.setMinAndMaxFrame(240, 625)
                        binding.l2.playAnimation()


                        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)

                        val spin_install = savedata.getString("spin_install", "")
                        val spinValue = savedata.getString("spin", "")

                        if (spin_install == spinValue) {
                            binding.startbtn.visibility = View.GONE
                            Toast.makeText(
                                applicationContext,
                                "Install And Wait For 30 Sec",
                                Toast.LENGTH_SHORT
                            ).show()

                            //here Condition add

                            // (internalMemoryUsed() - oldusedstorage) >= 16000000)
                        } else {

                            webhit()

                        }


                    }
                }
            countdownTimer.start()

        }

    }

    override fun onBackPressed() {
        if (backable)
            super.onBackPressed()
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

    fun webhit() {

        val random = Random()
        val randomIndex = random.nextInt(angle.size)

        val randomAngle = angle[randomIndex]
        binding.startbtn.visibility = View.VISIBLE
        binding.spinImage.rotation = randomAngle.toFloatOrNull() ?: 0.0f

        var coin = 0
        if (randomAngle.toInt() == 72) {
            coin = 15
            Toast.makeText(this@SpinActivity, "You have Own $coin Coins", Toast.LENGTH_SHORT).show()
        } else if (randomAngle.toInt() == 110) {
            coin = 14
            Toast.makeText(this@SpinActivity, "You have Own $coin Coins", Toast.LENGTH_SHORT).show()
        } else if (randomAngle.toInt() == 40) {
            coin = 16
            Toast.makeText(this@SpinActivity, "You have Own $coin Coins", Toast.LENGTH_SHORT).show()
        } else if (randomAngle.toInt() == 180) {
            coin = 12
            Toast.makeText(this@SpinActivity, "You have Own $coin Coins", Toast.LENGTH_SHORT).show()
        } else if (randomAngle.toInt() == 254) {
            coin = 10
            Toast.makeText(this@SpinActivity, "You have Own $coin Coins", Toast.LENGTH_SHORT).show()
        }

        val deviceid: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val time = System.currentTimeMillis()

        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        val email = savedata.getString("email", "0")

        val url = "${Constant.urllink}a.php"
        val queue1: RequestQueue =
            Volley.newRequestQueue(this@SpinActivity)
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                binding.startbtn.visibility = View.VISIBLE
                val countdownTimer = object : CountDownTimer(2500, 1000) {
                    override fun onTick(millisUntilFinished: Long) {

                    }

                    override fun onFinish() {

                    }
                }
                countdownTimer.start()

                if (response.contains(",")) {

                    binding.l2.visibility = View.VISIBLE

                    val spindata = response.split(",")
                    val savedata =
                        getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
                    val myEdit: SharedPreferences.Editor =
                        savedata.edit()
                    myEdit.putString("balance", spindata[1])
                    myEdit.putString("spin", spindata[2])
                    myEdit.apply();


                    binding.bala.text = savedata.getString("balance", "")


                } else {
                    Toast.makeText(this@SpinActivity, response, Toast.LENGTH_SHORT).show()
                }

            },
            Response.ErrorListener { error ->
                // Handle the error here
                println("Error: ${error.message}")
            }) {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()


                val dbit32 =
                    videoplayyer.encrypt(deviceid, Hatbc()).toString()
                val tbit32 =
                    videoplayyer.encrypt(time.toString(), Hatbc())
                        .toString()
                val email =
                    videoplayyer.encrypt(email.toString(), Hatbc())
                        .toString()
                val coin32 =
                    videoplayyer.encrypt(coin.toString(), Hatbc())
                        .toString()

                val den64 = Base64.getEncoder()
                    .encodeToString(dbit32.toByteArray())
                val ten64 = Base64.getEncoder()
                    .encodeToString(tbit32.toByteArray())
                val email64 =
                    Base64.getEncoder()
                        .encodeToString(email.toByteArray())
                val coin64 =
                    Base64.getEncoder()
                        .encodeToString(coin32.toByteArray())

                val encodemap: MutableMap<String, String> = HashMap()
                encodemap["deijvfijvmfhvfvhfbhbchbfybeud"] = den64
                encodemap["tiofhfuisgdtdrefssfgsgsgdhddgd"] = ten64
                encodemap["emofhfuisailrrefssfgsgsgdhddgd"] = email64
                encodemap["coinofhfuisailrremnnmgsgshddgd"] = coin64


                val jason = Json.encodeToString(encodemap)

                val den264 = Base64.getEncoder()
                    .encodeToString(jason.toByteArray())

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
}