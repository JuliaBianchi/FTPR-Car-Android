package com.example.myapitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapitest.databinding.ActivityCarDetailBinding
import com.example.myapitest.model.Car
import com.example.myapitest.service.Result
import com.example.myapitest.service.RetrofitClient
import com.example.myapitest.service.safeApiCall
import com.example.myapitest.ui.loadUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarDetailBinding
    private lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupView()

        loadItem()
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.deleteCTA.setOnClickListener {
            deleteItem()
        }
    }

    private fun loadItem() {
        val itemId = intent.getStringExtra(ARG_ID) ?: ""

        CoroutineScope(Dispatchers.IO).launch {
            val result = safeApiCall { RetrofitClient.apiService.getCarById(itemId) }

            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> {
                        car = result.data.value
                        handleSuccess()

                    }

                    is Result.Error -> {
                        hadleError()
                    }
                }
            }
        }
    }

    private fun deleteItem() {
        CoroutineScope(Dispatchers.IO).launch {
           val result = safeApiCall { RetrofitClient.apiService.deleteById(car.id) }

            withContext(Dispatchers.Main){
                when (result){
                    is Result.Error -> {
                        Toast.makeText(this@CarDetailActivity, R.string.erro_delete, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        Toast.makeText(this@CarDetailActivity, R.string.success_delete, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }

        }
    }

    private fun handleSuccess() {
        binding.etYear.setText(car.year)
        binding.etName.setText(car.name)
        binding.etLicense.setText(car.licence)
        binding.imageUrl.loadUrl(car.imageUrl)
    }

    private fun hadleError() {

    }

    companion object {
        private const val ARG_ID = "ARG_ID"
        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, CarDetailActivity::class.java).apply {
                putExtra(ARG_ID, id)
            }
        }
    }
}