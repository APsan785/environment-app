package com.apsan.environmentapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.apsan.environmentapp.R
import com.apsan.environmentapp.app.mockup_dialogue
import com.apsan.environmentapp.databinding.FragCredBinding
import com.hoc081098.viewbindingdelegate.viewBinding

class PlasticCredFragment:Fragment(R.layout.frag_cred) {

    private val binding by viewBinding(FragCredBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.root
    }

    private fun init(){
        binding.infoBtn.setOnClickListener {
            showInfoDialog()
        }

        FirstTimeDialog()

        arrayListOf(
            binding.gpayBtn,
            binding.paytmBtn,
            binding.upiBtn
        ).forEach {
            it.setOnLongClickListener (mockup_dialogue(context!!).longTouchListener)
        }
    }

    fun FirstTimeDialog(){

        val FIRST_TIME_KEY = "com.example.app.MainActivity.firstTimeKey"
        val PREFERENCES = "AlarmRepeatPreferences"
        val sp = requireContext().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val isFirstTime = sp.getBoolean(FIRST_TIME_KEY, true)
        if (isFirstTime) {
            val edit = sp.edit()
            edit.putBoolean(FIRST_TIME_KEY, false)
            edit.apply()

            //show the dialog
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {
        SweetAlertDialog(context!!)
            .setTitleText("Introducing Plastic Creds")
            .setContentText("Plastic Creds is a private currency, which can be cashed with Rupees and transferred using Paytm or UPI or GPay. You can get Plastic Creds by the amount of Plastic you recycled, they can be used for availing exciting offers.")
            .setConfirmText("OK")
            .show()
    }

}