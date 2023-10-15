package com.arisu.chillarbuddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val help = view.findViewById<LinearLayout>(R.id.help)
        val tc = view.findViewById<LinearLayout>(R.id.tc)
        val tutor = view.findViewById<LinearLayout>(R.id.tutor)
        val privacy = view.findViewById<LinearLayout>(R.id.privacy)
        val rate = view.findViewById<LinearLayout>(R.id.rate)
        val contact = view.findViewById<LinearLayout>(R.id.contact)

        help.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        tc.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        tutor.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        privacy.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        rate.setOnClickListener {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.arisu.chillarbuddy"))
            startActivity(intent)
        }
        contact.setOnClickListener {
            val uri = Uri.parse("http://www.google.com")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }
}