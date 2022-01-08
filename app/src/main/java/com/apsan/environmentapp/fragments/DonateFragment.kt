package com.apsan.environmentapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.apsan.environmentapp.R
import com.apsan.environmentapp.databinding.FragDonateBinding
import com.hoc081098.viewbindingdelegate.viewBinding
import android.view.MotionEvent

import android.graphics.PorterDuff

import android.view.View.OnTouchListener

import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.apsan.environmentapp.app.mockup_dialogue


class DonateFragment:Fragment(R.layout.frag_donate) {

    private val binding by viewBinding(FragDonateBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.root
    }

    private fun init() {

        //touchListener of ImageClick




        arrayListOf(
            binding.afforestImg,
            binding.ngoImg,
            binding.offsetImg,
            binding.poachingImg,
            binding.solarImg,
        ).forEach {
            val mockupDialogue = mockup_dialogue(context!!)
//            it.setOnLongClickListener(mockupDialogue.longTouchListener)
            it.setOnTouchListener(mockupDialogue.touchListener)
        }

        //link for calculating carbon
        binding.calculateLink.setOnClickListener {
            val navController = findNavController()
            navController.navigate(DonateFragmentDirections.actionDonateFragmentToVehicleCarbonFragment())
        }


    }
}