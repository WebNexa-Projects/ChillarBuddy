package com.arisu.chillarbuddy

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getSystemService


class ReferFragment : Fragment(R.layout.fragment_refer) {

    companion object {
        fun newInstance(): ReferFragment {
            return ReferFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refer, container, false)
    }
    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val code=view.findViewById<TextView>(R.id.code)
        val sh = view.findViewById<ImageView>(R.id.sh)
        val wb = view.findViewById<ImageView>(R.id.wb)
        val te = view.findViewById<ImageView>(R.id.te)
        val fb = view.findViewById<ImageView>(R.id.fb)
        val balance=view.findViewById<TextView>(R.id.bala)
        val copy=view.findViewById<LinearLayout>(R.id.copy_refer)
        val help=view.findViewById<LinearLayout>(R.id.help)



        val savedata = requireActivity().getSharedPreferences("ChillarBuddy", Context.MODE_PRIVATE)
        code.text=savedata.getString("myrefer", "")
        balance.text=savedata.getString("balance", "")


        copy.setOnClickListener {
            val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
            clipboard?.setPrimaryClip(ClipData.newPlainText("Refer Code", code.text))
            Toast.makeText(requireContext(), "Refer Code Copied ", Toast.LENGTH_SHORT).show()
        }
        help.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        fb.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, code.text)
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }

        wb.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, code.text)
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }
        te.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, code.text)
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }
        sh.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, code.text)
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }

    }

}