package com.apsan.environmentapp.app

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import cn.pedant.SweetAlert.SweetAlertDialog

class mockup_dialogue(context: Context) {
    val TAG = "tag"
    val longTouchListener = View.OnLongClickListener {
        Log.d(TAG, "longclick")
        SweetAlertDialog(context)
            .setTitleText("No Real world payment")
            .setContentText("This is just a mockup app and no real money will be used")
            .setConfirmText("OK")
            .show()
        true
    }


    val touchListener = object : View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            Log.d(TAG, "touch")
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val view: ImageView = v as ImageView
                    //overlay is black with transparency of 0x77 (119)
                    view.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                    view.invalidate()
                    return true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val view: ImageView = v as ImageView
                    //clear the overlay
                    view.drawable.clearColorFilter()
                    view.invalidate()
                }
            }
            return true
        }

    }
}