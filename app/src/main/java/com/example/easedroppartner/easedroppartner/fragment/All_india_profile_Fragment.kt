package com.example.easedroppartner.easedroppartner.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.easedroppartner.R
import com.example.easedroppartner.easedroppartner.Model.ConstantData
import com.example.easedroppartner.easedroppartner.activity.Job_Type_Activity
import com.example.easedroppartner.easedroppartner.activity.Sign_in_Activity
import com.example.easedroppartner.easedroppartner.adapter.LocaleHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.hdodenhof.circleimageview.CircleImageView


class All_india_profile_Fragment : Fragment() {

    //for contact us
    private lateinit var lin_contactUs: LinearLayout
    private lateinit var btn_cancel: Button

    //for term and condition button
    private lateinit var lin_terms_and_condition: LinearLayout
    private lateinit var btn_accept: Button

    //for language
    private lateinit var lin_language: LinearLayout
    private var lang_selected: Int = 0

    //for logout
    private lateinit var lin_logout: LinearLayout
    private lateinit var btn_no: Button
    private lateinit var btn_yes: Button

    //declaration of

    private lateinit var tv_acc: TextView
    private lateinit var tv_dname: TextView
    private lateinit var tv_demail: TextView
    private lateinit var contact_us: TextView
    private lateinit var tv_contact_us: TextView
    private lateinit var terms_condition: TextView
    private lateinit var tv_terms_condition: TextView
    private lateinit var language: TextView
    private lateinit var img_dboy: CircleImageView
    private lateinit var tv_language: TextView
    private lateinit var logout: TextView
    private lateinit var tv_logout: TextView

    private val languageChangeReceiver=object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            val selectedLanguage=LocaleHelper.getLanguage(context!!)
            updateUIWithLanguage(selectedLanguage)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_india_profile_, container, false)
    }
    //Contact Us dialog box method

    @SuppressLint("InflateParams", "MissingInflatedId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        tv_acc = view.findViewById(R.id.tv_acc)
        tv_dname = view.findViewById(R.id.tv_dname)
        tv_demail = view.findViewById(R.id.tv_demail)
        contact_us = view.findViewById(R.id.contact_us)
        terms_condition = view.findViewById(R.id.terms_condition)
        tv_terms_condition = view.findViewById(R.id.tv_terms_condition)
        language = view.findViewById(R.id.language)
        tv_language = view.findViewById(R.id.tv_language)
        img_dboy=view.findViewById(R.id.img_dboy)
        logout = view.findViewById(R.id.logout)
        tv_logout = view.findViewById(R.id.tv_logout)
        tv_contact_us=view.findViewById(R.id.tv_contact_us)
        lin_contactUs = view.findViewById(R.id.lin_contactUs)
        lin_terms_and_condition = view.findViewById(R.id.lin_terms_and_condition)
        lin_language = view.findViewById(R.id.lin_Language)
        lin_logout = view.findViewById(R.id.lin_logout)

        lin_language.setOnClickListener {

            showLanguageSelectionDialog()

        }
        val sharedPreferences = requireActivity().getSharedPreferences(ConstantData.SP, Context.MODE_PRIVATE)
        val email=sharedPreferences.getString(ConstantData.KEY_EMAIL,"")
        val pic=sharedPreferences.getString(ConstantData.KEY_PIC,"")
        val name=sharedPreferences.getString(ConstantData.KEY_NAME,"")


        tv_demail.text=email
        tv_dname.text=name

        Glide.with(requireContext())
            .load(ConstantData.serveraddress+"deliveryboy/"+pic)
            .override(300, 300)
            .into(img_dboy)


        val selectedLanguage= LocaleHelper.getLanguage(requireContext())
        updateUIWithLanguage(selectedLanguage)

        lin_contactUs.setOnClickListener {

            val dialogBinding = layoutInflater.inflate(R.layout.contact_us_alertdialogbox, null)
            btn_cancel = dialogBinding.findViewById(R.id.btn_cancel)

            val myDialog = Dialog(requireContext())

            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()


            btn_cancel.setOnClickListener {
                myDialog.dismiss()
            }

        }
        //term and condition alert dialogbox mathod call
        lin_terms_and_condition.setOnClickListener {
            val dialogBinding =
                layoutInflater.inflate(R.layout.term_and_condation_alertdialogbox, null)

            btn_accept = dialogBinding.findViewById(R.id.btn_accept)
            val myDialog = Dialog(requireContext())

            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()


            btn_accept.setOnClickListener {
                myDialog.dismiss()
            }

        }

        //term and condition alert dialogbox mathod call

        lin_logout.setOnClickListener {
            showLogoutAlert()
        }

    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(
            languageChangeReceiver, IntentFilter("com.example.easedrop.ACTION_LANGUAGE_CHANGED")
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(languageChangeReceiver)
    }

    private fun updateUIWithLanguage(languageCode: String) {
        val context = LocaleHelper.setLocale(requireContext(), languageCode)
        val appResources = context.resources
        tv_language.text = when (languageCode) {
            "en" -> "Select Language"
            "hi" -> "भाषा चुने"
            "gu" -> "ભાષા પસંદ કરો"
            else -> "UNKNOWN"
        }
        setString(appResources)
    }

    private fun showLogoutAlert(){
        val dialogBinding = layoutInflater.inflate(R.layout.logout_alert_dialogbox, null)

        btn_no = dialogBinding.findViewById(R.id.btn_no)
        btn_yes = dialogBinding.findViewById(R.id.btn_yes)
        val myDialog = Dialog(requireContext())

        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog.show()

        if (btn_no.isClickable) {
            btn_no.setOnClickListener {
                myDialog.dismiss()
            }
        }
        if (btn_yes.isClickable) {
            btn_yes.setOnClickListener {
                val intent = Intent(activity, Job_Type_Activity::class.java)
                startActivity(intent)
            }

        }

    }

    private fun showLanguageSelectionDialog() {
        val language = arrayOf("ENGLISH", "हिन्दी", "ગુજરાતી")
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle("Select a Language")
            .setSingleChoiceItems(
                language,
                lang_selected
            ) { dialogInterface: DialogInterface, i: Int ->
                tv_language.text = language[i]
                val languageCode= when (language[i]) {
                    "ENGLISH" -> "en"
                    "हिन्दी" -> "hi"
                    "ગુજરાતી" -> "gu"
                    else -> "en"
                }
                changeLanguage(languageCode)
                dialogInterface.dismiss()
            }
            .setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        dialogBuilder.create().show()
    }

    private fun changeLanguage(languageCode: String)
    {
        //save the selected language preference
        LocaleHelper.setLanguage(requireContext(),languageCode)
        updateUIWithLanguage(languageCode)

        // Broadcast language change
        val intent= Intent("com.example.easedrop.ACTION_LANGUAGE_CHANGED")
        intent.putExtra("language",languageCode)
        requireContext().sendBroadcast(intent)
    }
    //
    private fun setString(appResources: Resources) {
        tv_acc.text = appResources.getString(R.string.tv_acc)
        //  tv_dname.text = appResources.getString(R.string.tv_dname)
        // tv_demail.text=appResources.getString(R.string.tv_demail)
        contact_us.text=appResources.getString(R.string.contact_us)
        tv_contact_us.text=appResources.getString(R.string.tv_contact_us)
        terms_condition.text=appResources.getString(R.string.terms_condition)
        language.text=appResources.getString(R.string.language)
        logout.text=appResources.getString(R.string.logout)
        tv_logout.text=appResources.getString(R.string.tv_logout)
    }

}