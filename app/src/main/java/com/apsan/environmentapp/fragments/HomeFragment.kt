package com.apsan.environmentapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.apsan.environmentapp.R
import com.apsan.environmentapp.api.QuotesAPI
import com.apsan.environmentapp.databinding.FragHomeBinding
import com.hoc081098.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.frag_home) {

    private val binding by viewBinding(FragHomeBinding::bind)
    private val TAG = "tag"

    @Inject
    lateinit var quotesAPI: QuotesAPI
    lateinit var navController: NavController

    lateinit var job: Job

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        job = lifecycleScope.launch(Dispatchers.IO) {
            publishQuotes()
        }
        setClickListeners()

        binding.root
    }

    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    private fun setClickListeners() {
        navController = this.findNavController()
        binding.recyclingTruck.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToRecyclingFragment())
        }
        binding.plasticCred.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToPlasticCredFragment())
        }
        binding.donateMoney.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToDonateFragment())
        }
        binding.vehicleCarbon.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToVehicleCarbonFragment())
        }
        binding.oxygenArea.setOnClickListener {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToOxygenAreaFragment())
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun publishQuotes() {

        val quoteResp = quotesAPI.getEnvironmentQuotes()
        if (quoteResp.isSuccessful) {
            withContext(Dispatchers.Main) {
                binding.quote.text = quoteResp.body()!![0].text
                binding.author.text = "- ${quoteResp.body()!![0].author}"
            }
        } else {
            Log.d(TAG, "publishQuotes: ${quoteResp.code()}")
            binding.quote.text = "\n\n\n"
            binding.author.text = "\n"
        }
        activity!!.setTheme(R.style.Theme_EnvironmentApp)
    }
}