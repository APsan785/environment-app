package com.apsan.environmentapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import com.apsan.environmentapp.R
import com.apsan.environmentapp.databinding.FragRecycleBinding


import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.hoc081098.viewbindingdelegate.viewBinding
import android.widget.LinearLayout

import android.widget.TextView

import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.runBlocking


class RecyclingFragment : Fragment(R.layout.frag_recycle) {

    private val binding by viewBinding(FragRecycleBinding::bind)
    private lateinit var dialog: AlertDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//
//        val (height, _) = resources.displayMetrics.run { heightPixels/density to widthPixels/density }
//        binding.bottomSheet.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (height*0.4).toInt())
        bottomSheetInit()

        binding.root
    }

    private fun bottomSheetInit() {
        binding.bottomSheetLinlay.visibility = View.GONE
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.slideBottomSheet.text = "SLIDE ME DOWN!!"
                }
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.slideBottomSheet.text = "SLIDE ME UP!!"
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset > 0) {
                    binding.bottomSheetLinlay.visibility = View.VISIBLE
                }
                if (slideOffset == 0f) {
                    binding.bottomSheetLinlay.visibility = View.GONE
                }
            }

        }
        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
            addBottomSheetCallback(bottomSheetCallback)
            isHideable = false
        }

        val validating_input_submission = object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if (binding.nameEt.text.toString().trim() == "") {
                    binding.nameField.isWrong(true); return
                } else binding.nameField.isWrong(false)
                if (binding.nameEt.text.toString().trim().split(" ").size < 2) {
                    binding.nameField.isWrong(
                        true,
                        "The Name should have both first and last names"
                    ); return
                } else binding.nameField.isWrong(false)
                if (binding.addressEt.text.toString().trim() == "") {
                    binding.addressField.isWrong(true); return
                } else binding.addressField.isWrong(false)
                if (binding.numbEt.text.toString().trim() == "") {
                    binding.numbField.isWrong(true); return
                } else binding.numbField.isWrong(false)
                if (binding.numbEt.text.toString().trim().length != 10) {
                    binding.numbField.isWrong(true, "Phone number must be of 10 digits"); return
                } else binding.numbField.isWrong(false)

                MaterialAlertDialogBuilder(
                    context!!
                ).setTitle("Submit it ?")
                    .setMessage("Truck would reach the submitted place and the details will be used to contact you")
                    .setPositiveButton("Yes") { dialog, _ ->
                        dialog.dismiss()
                        runBlocking {
                            ProgressDialog()

                        }
                    }
                    .setNegativeButton("No, Wait") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

            }
        }
        binding.submitBtn.setOnClickListener(validating_input_submission)

    }

    private fun TextInputLayout.isWrong(bool: Boolean, s: String = "This can't remain empty") {
        if (bool) this.error = s else this.error = ""
    }

    private fun ProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(context!!)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(context!!)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context!!)
        tvText.text = "Loading ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setCancelable(true)
        builder.setView(ll)
        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val window: Window? = dialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow()!!.getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.getWindow()!!.setAttributes(layoutParams)
        }
        Handler().postDelayed({ after_submission() }, 5000L)

    }

    private fun after_submission() {
        binding.bookedTv.visibility = View.VISIBLE
        binding.submitBtn.visibility = View.GONE
        dialog.dismiss()
    }


}