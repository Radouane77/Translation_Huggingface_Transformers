package com.mini_projet.nlp_translation.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.jaredrummler.materialspinner.MaterialSpinner
import com.mini_projet.nlp_translation.R
import com.mini_projet.nlp_translation.utils.Constants
import com.mini_projet.nlp_translation.utils.modules.CommandLine

class MainActivity : AppCompatActivity() {

    private var output: TextView? = null
    private lateinit var mDatabase: DatabaseReference
    private lateinit var inputLan: MaterialSpinner
    private lateinit var outputLang: MaterialSpinner
    private lateinit var input: EditText
    private lateinit var translateButton: Button
    private lateinit var progress_horizontal: ProgressBar
    private lateinit var copy: ImageView


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        translateButton.setOnClickListener { v: View? ->
            output?.hint = "Translating..."
            progress_horizontal.visibility = View.VISIBLE
            translateButton.visibility = View.GONE
            copy.visibility = View.GONE
            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase.child("Commands").child("command")
                .setValue(CommandLine(generateInput(), ""))
                .addOnSuccessListener { getOutputCommand() }
        }
        copy.setOnClickListener { v: View? ->
            ClipboardUtils.copyText(output!!.text.toString())
            ToastUtils.showShort("Text copied!!")
        }
    }

    private fun generateInput(): String {

        val toInput = input.text.toString()
        val isoInputLan = Constants.getAllLanguages()[inputLan.selectedIndex].languageISO
        val isoOutputLan = Constants.getAllLanguages()[outputLang.selectedIndex].languageISO

        return "$isoInputLan$${toInput.replace("é", "e")}$$isoOutputLan"
    }

    private fun initializeViews() {
        input = findViewById(R.id.input)
        output = findViewById(R.id.output)
        inputLan = findViewById(R.id.inputLang)
        outputLang = findViewById(R.id.outputLang)
        translateButton = findViewById(R.id.translate)
        progress_horizontal = findViewById(R.id.progress_horizontal)
        copy = findViewById(R.id.copy)
        inputLan.setItems(Constants.getAllLanguagesName())
        inputLan.selectedIndex = 0
        outputLang.setItems(Constants.getAllLanguagesName())
        outputLang.selectedIndex = 1
        FirebaseApp.initializeApp(applicationContext)
    }

    private fun getOutputCommand() {
        mDatabase =
            FirebaseDatabase.getInstance().getReference("Commands").child("command")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                output!!.text = dataSnapshot.child("output")
                    .getValue(String::class.java)!!
                    .replace("'", "")
                if (output!!.text.isNotEmpty() || output!!.text.isNotBlank()) {
                    progress_horizontal.visibility = View.GONE
                    translateButton.visibility = View.VISIBLE
                    copy.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}