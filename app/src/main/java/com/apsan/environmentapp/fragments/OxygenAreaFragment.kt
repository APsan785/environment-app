package com.apsan.environmentapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.apsan.environmentapp.R
import com.apsan.environmentapp.databinding.FragOxygenBinding
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.hoc081098.viewbindingdelegate.viewBinding
import android.widget.TextView
import com.apsan.environmentapp.ml.MobilenetV110224Quant
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.util.*
import org.checkerframework.checker.units.qual.min


class OxygenAreaFragment : Fragment(R.layout.frag_oxygen) {

    private val binding by viewBinding(FragOxygenBinding::bind)

    val TAG = "tag"
    private val CAMERA_PERMISSION_CODE = 111
    private val CAMERA_REQUEST_CODE = 112
    val LABEL_FILE = "labels.txt"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.scanBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }


        val text = binding.environTv.text.toString()
//        val paint = Paint()
//        paint.textSize = 25f
//        paint.typeface = Typeface.SANS_SERIF
//        paint.color = Color.BLACK
//        paint.style = Paint.Style.FILL
//
//        val result = Rect()
//// Measure the text rectangle to get the height
//// Measure the text rectangle to get the height
//        paint.getTextBounds(text, 0, text.length, result)
//        val textHeight = result.height()

        val textHeight = (binding.environTv.getPaint().getFontMetrics().bottom - binding.environTv.getPaint().getFontMetrics().top).toInt()

        val lp = binding.whiteLine.layoutParams
        lp.height = (textHeight * 3) + 54
        binding.whiteLine.layoutParams = lp

        binding.continueBtn.setOnClickListener {
            val resized_bmp = Bitmap.createScaledBitmap(
                (binding.scannedIv.drawable as BitmapDrawable).bitmap,
                224, 224, true
            )
            useModel(resized_bmp)
            setLabels()

            binding.detailRelativeLay.visibility = View.VISIBLE
        }


    }

    @SuppressLint("SetTextI18n")
    private fun setLabels(){
        val r_env: Float = getRandValue(7f,10f)
        val r_health = getRandValue(5f,9f)
        val r_soci = getRandValue(4f, 10f)
        binding.environTv.text = binding.environTv.text.toString()+r_env.toString()
        binding.healthTv.text = binding.healthTv.text.toString()+r_health.toString()
        binding.sociTv.text = binding.sociTv.text.toString()+r_soci.toString()
    }

    private fun getRandValue(min: Float, max:Float) = "%.2f".format(min + Random().nextFloat() * (max - min)).toFloat()

    private fun useModel(bitmap:Bitmap) {
        val model = MobilenetV110224Quant.newInstance(context!!)

// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)
        val tensorBuffer = TensorImage.fromBitmap(bitmap)
        val byteBuffer = tensorBuffer.buffer
        inputFeature0.loadBuffer(byteBuffer)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        binding.resultTv.text = getMaxProbabLabel(outputFeature0.floatArray).capitalize()

// Releases model resources if no longer used.
        model.close()
    }

    private fun getMaxProbabLabel(arr:FloatArray):String{
        val max_val = arr.indices.maxByOrNull{arr[it]} ?: -1
        val label_txt = activity!!.assets!!.open(LABEL_FILE).bufferedReader().use { it.readText() }
        val label_list = label_txt.split("\n")
        return label_list[max_val]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(context!!, "Permission not given", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            binding.scannedIv.setImageBitmap(thumbnail)
            binding.continueBtn.visibility = View.VISIBLE
        }
    }


}