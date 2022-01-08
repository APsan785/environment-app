package com.apsan.environmentapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.apsan.environmentapp.R
import com.apsan.environmentapp.databinding.FragVehicleBinding
import com.google.android.material.textfield.TextInputLayout
import com.hoc081098.viewbindingdelegate.viewBinding
import java.lang.Exception

class VehicleCarbonFragment : Fragment(R.layout.frag_vehicle) {

    val TAG = "tag"
    val fuels: Array<String> by lazy { context?.resources?.getStringArray(R.array.fuels)!! }
    val fuel_mix = listOf(
        "Austria",
        "Germany",
        "Sweden",
        "Switzerland",
        "Certified Green Electricity",
        "Other"
    )
    val fuel_with_mix: Array<String> by lazy { context?.resources?.getStringArray(R.array.fuels_wit_mix)!! }

    private val binding by viewBinding(FragVehicleBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        binding.root
    }

    private fun init() {

        val adapter_fuel =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fuels)
        (binding.fuelType.editText as? AutoCompleteTextView)?.setAdapter(adapter_fuel)

        val adapter_mix =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, fuel_mix)
        (binding.fuelMix.editText as? AutoCompleteTextView)?.setAdapter(adapter_mix)

        (binding.fuelType.editText as? AutoCompleteTextView)?.setOnItemClickListener(
            object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (fuel_with_mix.contains(parent?.getItemAtPosition(position))) {
                        binding.fuelMix.visibility = View.VISIBLE
                    } else{
                        binding.fuelMix.visibility = View.GONE
                        binding.fuelMixDropdown.setText("")
                    }
                    if (parent?.getItemAtPosition(position) in arrayListOf("Biogas", "Natural Gas")) binding.mileageField.suffixText = "kms/Kg"
                    else if (parent?.getItemAtPosition(position) == "Electric") binding.mileageField.suffixText = "kms/KWh"
                    else binding.mileageField.suffixText = "kms/Litre"
                }
            }
        )
        (binding.fuelType.editText as? AutoCompleteTextView)?.setOnDismissListener { (binding.fuelType.editText as? AutoCompleteTextView)?.clearFocus() }

        addDistanceListeners()
        setCalculateListener()

    }

    private fun setCalculateListener() {
        binding.calculateBtn.setOnClickListener {
            if (binding.distanceEt.text.toString().trim() == "") {
                binding.distanceField.isWrong(true); return@setOnClickListener
            } else binding.distanceField.isWrong(false)
            if (binding.fuelType.editText?.text.toString().trim() == "") {
                binding.fuelType.isWrong(true); return@setOnClickListener
            } else binding.fuelType.isWrong(false)
           if (binding.mileageEt.text.toString().trim() == "") {
                binding.mileageField.isWrong(true); return@setOnClickListener
            } else binding.mileageField.isWrong(false)

            try{
            binding.tripSummary.text = offsetARide(binding.distanceEt.text.toString(), binding.fuelType.editText?.text.toString())
            binding.offsetCo2.text = co2amount(binding.distanceEt.text.toString().toFloat(),
                binding.mileageEt.text.toString().toFloat())}
            catch (e:Exception){
                SweetAlertDialog(context!!)
                    .setTitleText("Error")
                    .setContentText("Check the Inputs again")
                    .setConfirmText("OK")
                    .show()
            }
        }
    }

    private fun offsetARide(km : String, fuel:String) = "Offset a ride of $km km, Fuel: $fuel"
    private fun co2amount(km : Float, mileage : Float) = "CO2 Amount : ${"%.4f".format(km/1000 * mileage * 0.93)}t"

    private fun TextInputLayout.isWrong(bool: Boolean, s: String = "This can't remain empty") {
        if (bool) this.error = s else this.error = ""
    }

    @SuppressLint("SetTextI18n")
    private fun addDistanceListeners() {
        binding.thous.setOnClickListener {
            if (binding.distanceEt.text.toString().isNotEmpty()) binding.distanceEt.setText(
                (binding.distanceEt.text.toString().toInt() + 1000).toString()
            ) else binding.distanceEt.setText("1000")
        }
        binding.fiveHund.setOnClickListener {
            if (binding.distanceEt.text.toString().isNotEmpty()) {
                binding.distanceEt.setText(
                    (binding.distanceEt.text.toString().toInt() + 500).toString()
                )
            } else {
                binding.distanceEt.setText(
                    "500"
                )
            }
        }
        binding.hund.setOnClickListener {
            if (binding.distanceEt.text.toString().isNotEmpty()) {
                binding.distanceEt.setText(
                    (binding.distanceEt.text.toString().toInt() + 100).toString()
                )
            } else {
                binding.distanceEt.setText(
                    "100"
                )
            }
        }
        binding.clearDist.setOnClickListener {
            binding.distanceEt.setText("")
        }
    }


}