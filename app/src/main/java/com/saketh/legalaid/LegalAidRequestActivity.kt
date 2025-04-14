package com.saketh.legalaid

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.saketh.legalaid.adapter.LawyerProfileAdapter
import com.saketh.legalaid.databinding.ActivityLegalAidRequestBinding
import com.saketh.legalaid.model.LawyerProfile

class LegalAidRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLegalAidRequestBinding
    private lateinit var lawyerAdapter: LawyerProfileAdapter

    private val regions = listOf(
        "Delhi",
        "Mumbai",
        "Bangalore",
        "Hyderabad",
        "Chennai",
        "Kolkata",
        "Nagpur"
    )

    private val mockLawyers = listOf(
        // Delhi Lawyers
        LawyerProfile("1", "Adv. Rajesh Kumar", "Criminal Law", "15 years", "92%", "4.8/5", "+91 98765 43210", "Delhi"),
        LawyerProfile("2", "Adv. Priya Sharma", "Family Law", "12 years", "88%", "4.7/5", "+91 98765 43211", "Delhi"),
        LawyerProfile("3", "Adv. Amit Verma", "Corporate Law", "18 years", "95%", "4.9/5", "+91 98765 43212", "Delhi"),
        LawyerProfile("4", "Adv. Neha Gupta", "Property Law", "10 years", "85%", "4.6/5", "+91 98765 43213", "Delhi"),
        LawyerProfile("5", "Adv. Vikram Singh", "Civil Law", "20 years", "94%", "4.9/5", "+91 98765 43214", "Delhi"),
        LawyerProfile("6", "Adv. Anjali Patel", "Tax Law", "8 years", "82%", "4.5/5", "+91 98765 43215", "Delhi"),
        LawyerProfile("7", "Adv. Rohit Malhotra", "IP Law", "14 years", "90%", "4.7/5", "+91 98765 43216", "Delhi"),
        LawyerProfile("8", "Adv. Meera Khanna", "Employment Law", "11 years", "87%", "4.6/5", "+91 98765 43217", "Delhi"),
        LawyerProfile("9", "Adv. Arjun Mehta", "Banking Law", "16 years", "93%", "4.8/5", "+91 98765 43218", "Delhi"),
        LawyerProfile("10", "Adv. Shreya Kapoor", "Consumer Law", "9 years", "84%", "4.5/5", "+91 98765 43219", "Delhi"),

        // Mumbai Lawyers
        LawyerProfile("11", "Adv. Rohan Desai", "Criminal Law", "17 years", "91%", "4.8/5", "+91 98765 43220", "Mumbai"),
        LawyerProfile("12", "Adv. Priyanka Joshi", "Family Law", "13 years", "89%", "4.7/5", "+91 98765 43221", "Mumbai"),
        LawyerProfile("13", "Adv. Arvind Patel", "Corporate Law", "19 years", "96%", "4.9/5", "+91 98765 43222", "Mumbai"),
        LawyerProfile("14", "Adv. Sneha Shah", "Property Law", "11 years", "86%", "4.6/5", "+91 98765 43223", "Mumbai"),
        LawyerProfile("15", "Adv. Vikram Mehta", "Civil Law", "21 years", "95%", "4.9/5", "+91 98765 43224", "Mumbai"),
        LawyerProfile("16", "Adv. Ananya Reddy", "Tax Law", "9 years", "83%", "4.5/5", "+91 98765 43225", "Mumbai"),
        LawyerProfile("17", "Adv. Rahul Sharma", "IP Law", "15 years", "91%", "4.7/5", "+91 98765 43226", "Mumbai"),
        LawyerProfile("18", "Adv. Meera Patel", "Employment Law", "12 years", "88%", "4.6/5", "+91 98765 43227", "Mumbai"),
        LawyerProfile("19", "Adv. Arjun Kapoor", "Banking Law", "17 years", "94%", "4.8/5", "+91 98765 43228", "Mumbai"),
        LawyerProfile("20", "Adv. Shreya Desai", "Consumer Law", "10 years", "85%", "4.5/5", "+91 98765 43229", "Mumbai"),

        // Bangalore Lawyers
        LawyerProfile("21", "Adv. Ramesh Kumar", "Criminal Law", "16 years", "90%", "4.8/5", "+91 98765 43230", "Bangalore"),
        LawyerProfile("22", "Adv. Priya Reddy", "Family Law", "14 years", "88%", "4.7/5", "+91 98765 43231", "Bangalore"),
        LawyerProfile("23", "Adv. Amit Sharma", "Corporate Law", "20 years", "95%", "4.9/5", "+91 98765 43232", "Bangalore"),
        LawyerProfile("24", "Adv. Neha Patel", "Property Law", "12 years", "87%", "4.6/5", "+91 98765 43233", "Bangalore"),
        LawyerProfile("25", "Adv. Vikram Reddy", "Civil Law", "22 years", "96%", "4.9/5", "+91 98765 43234", "Bangalore"),
        LawyerProfile("26", "Adv. Anjali Kumar", "Tax Law", "10 years", "84%", "4.5/5", "+91 98765 43235", "Bangalore"),
        LawyerProfile("27", "Adv. Rohit Patel", "IP Law", "16 years", "92%", "4.7/5", "+91 98765 43236", "Bangalore"),
        LawyerProfile("28", "Adv. Meera Sharma", "Employment Law", "13 years", "89%", "4.6/5", "+91 98765 43237", "Bangalore"),
        LawyerProfile("29", "Adv. Arjun Reddy", "Banking Law", "18 years", "93%", "4.8/5", "+91 98765 43238", "Bangalore"),
        LawyerProfile("30", "Adv. Shreya Kumar", "Consumer Law", "11 years", "86%", "4.5/5", "+91 98765 43239", "Bangalore"),

        // Hyderabad Lawyers
        LawyerProfile("31", "Adv. Rajesh Reddy", "Criminal Law", "18 years", "93%", "4.8/5", "+91 98765 43240", "Hyderabad"),
        LawyerProfile("32", "Adv. Priya Sharma", "Family Law", "15 years", "90%", "4.7/5", "+91 98765 43241", "Hyderabad"),
        LawyerProfile("33", "Adv. Amit Kumar", "Corporate Law", "21 years", "96%", "4.9/5", "+91 98765 43242", "Hyderabad"),
        LawyerProfile("34", "Adv. Neha Reddy", "Property Law", "13 years", "88%", "4.6/5", "+91 98765 43243", "Hyderabad"),
        LawyerProfile("35", "Adv. Vikram Sharma", "Civil Law", "23 years", "97%", "4.9/5", "+91 98765 43244", "Hyderabad"),
        LawyerProfile("36", "Adv. Anjali Patel", "Tax Law", "11 years", "85%", "4.5/5", "+91 98765 43245", "Hyderabad"),
        LawyerProfile("37", "Adv. Rohit Kumar", "IP Law", "17 years", "93%", "4.7/5", "+91 98765 43246", "Hyderabad"),
        LawyerProfile("38", "Adv. Meera Reddy", "Employment Law", "14 years", "90%", "4.6/5", "+91 98765 43247", "Hyderabad"),
        LawyerProfile("39", "Adv. Arjun Sharma", "Banking Law", "19 years", "94%", "4.8/5", "+91 98765 43248", "Hyderabad"),
        LawyerProfile("40", "Adv. Shreya Patel", "Consumer Law", "12 years", "87%", "4.5/5", "+91 98765 43249", "Hyderabad"),

        // Chennai Lawyers
        LawyerProfile("41", "Adv. Ramesh Sharma", "Criminal Law", "19 years", "94%", "4.8/5", "+91 98765 43250", "Chennai"),
        LawyerProfile("42", "Adv. Priya Kumar", "Family Law", "16 years", "91%", "4.7/5", "+91 98765 43251", "Chennai"),
        LawyerProfile("43", "Adv. Amit Reddy", "Corporate Law", "22 years", "97%", "4.9/5", "+91 98765 43252", "Chennai"),
        LawyerProfile("44", "Adv. Neha Sharma", "Property Law", "14 years", "89%", "4.6/5", "+91 98765 43253", "Chennai"),
        LawyerProfile("45", "Adv. Vikram Kumar", "Civil Law", "24 years", "98%", "4.9/5", "+91 98765 43254", "Chennai"),
        LawyerProfile("46", "Adv. Anjali Reddy", "Tax Law", "12 years", "86%", "4.5/5", "+91 98765 43255", "Chennai"),
        LawyerProfile("47", "Adv. Rohit Sharma", "IP Law", "18 years", "94%", "4.7/5", "+91 98765 43256", "Chennai"),
        LawyerProfile("48", "Adv. Meera Kumar", "Employment Law", "15 years", "91%", "4.6/5", "+91 98765 43257", "Chennai"),
        LawyerProfile("49", "Adv. Arjun Reddy", "Banking Law", "20 years", "95%", "4.8/5", "+91 98765 43258", "Chennai"),
        LawyerProfile("50", "Adv. Shreya Sharma", "Consumer Law", "13 years", "88%", "4.5/5", "+91 98765 43259", "Chennai"),

        // Kolkata Lawyers
        LawyerProfile("51", "Adv. Rajesh Kumar", "Criminal Law", "20 years", "95%", "4.8/5", "+91 98765 43260", "Kolkata"),
        LawyerProfile("52", "Adv. Priya Sharma", "Family Law", "17 years", "92%", "4.7/5", "+91 98765 43261", "Kolkata"),
        LawyerProfile("53", "Adv. Amit Patel", "Corporate Law", "23 years", "98%", "4.9/5", "+91 98765 43262", "Kolkata"),
        LawyerProfile("54", "Adv. Neha Kumar", "Property Law", "15 years", "90%", "4.6/5", "+91 98765 43263", "Kolkata"),
        LawyerProfile("55", "Adv. Vikram Sharma", "Civil Law", "25 years", "99%", "4.9/5", "+91 98765 43264", "Kolkata"),
        LawyerProfile("56", "Adv. Anjali Reddy", "Tax Law", "13 years", "87%", "4.5/5", "+91 98765 43265", "Kolkata"),
        LawyerProfile("57", "Adv. Rohit Kumar", "IP Law", "19 years", "95%", "4.7/5", "+91 98765 43266", "Kolkata"),
        LawyerProfile("58", "Adv. Meera Sharma", "Employment Law", "16 years", "92%", "4.6/5", "+91 98765 43267", "Kolkata"),
        LawyerProfile("59", "Adv. Arjun Kumar", "Banking Law", "21 years", "96%", "4.8/5", "+91 98765 43268", "Kolkata"),
        LawyerProfile("60", "Adv. Shreya Reddy", "Consumer Law", "14 years", "89%", "4.5/5", "+91 98765 43269", "Kolkata"),

        // Nagpur Lawyers
        LawyerProfile("61", "Adv. Rekha Gopal Shahare", "Criminal Law", "15 years", "92%", "4.8/5", "+91 98765 43270", "Nagpur"),
        LawyerProfile("62", "Adv. Rahul D Hunge", "Family Law", "5 years", "85%", "4.5/5", "+91 98765 43271", "Nagpur"),
        LawyerProfile("63", "Adv. Ketan Ganorkar", "Corporate Law", "6 years", "86%", "4.6/5", "+91 98765 43272", "Nagpur"),
        LawyerProfile("64", "Adv. Mangesh Moon", "Property Law", "10 years", "89%", "4.7/5", "+91 98765 43273", "Nagpur"),
        LawyerProfile("65", "Adv. Eeshan Deoras", "Civil Law", "11 years", "90%", "4.7/5", "+91 98765 43274", "Nagpur"),
        LawyerProfile("66", "Adv. Shweta Bhawsagar", "Tax Law", "14 years", "93%", "4.8/5", "+91 98765 43275", "Nagpur"),
        LawyerProfile("67", "Adv. Rehan M.Y. Sobani", "IP Law", "28 years", "98%", "4.9/5", "+91 98765 43276", "Nagpur"),
        LawyerProfile("68", "Adv. Abolee Kawale", "Employment Law", "6 years", "87%", "4.6/5", "+91 98765 43277", "Nagpur"),
        LawyerProfile("69", "Adv. Pravesh Biswas", "Banking Law", "18 years", "94%", "4.8/5", "+91 98765 43278", "Nagpur"),
        LawyerProfile("70", "Adv. Mohseen Akbani", "Consumer Law", "5 years", "84%", "4.5/5", "+91 98765 43279", "Nagpur")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegalAidRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up system UI
        window.apply {
            statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setupBackButton()
        setupRegionDropdown()
        setupLawyersRecyclerView()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRegionDropdown() {
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, regions)
        binding.regionInput.setAdapter(adapter)
        binding.regionInput.dropDownHeight = 400 // Set max height for dropdown

        binding.regionInput.setOnItemClickListener { _, _, position, _ ->
            val selectedRegion = regions[position]
            filterLawyersByRegion(selectedRegion)
        }
    }

    private fun setupLawyersRecyclerView() {
        lawyerAdapter = LawyerProfileAdapter { lawyer ->
            // Handle contact button click - open dialer with the lawyer's number
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:${lawyer.contactNumber}")
            }
            startActivity(intent)
        }

        binding.lawyersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@LegalAidRequestActivity)
            adapter = lawyerAdapter
        }
    }

    private fun filterLawyersByRegion(region: String) {
        val filteredLawyers = mockLawyers.filter { it.region == region }
        lawyerAdapter.updateLawyers(filteredLawyers)
    }
} 