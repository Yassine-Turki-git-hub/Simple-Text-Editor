package com.yt.simpletexteditor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var newButton: ImageButton
    private lateinit var openButton: ImageButton
    private lateinit var saveButton: ImageButton
    private lateinit var textZone: EditText
    private lateinit var  fileTitle: TextView
    private lateinit var popupFileName: LinearLayout
    private lateinit var textViewFileName: EditText
    private lateinit var buttonSubmitFileName: Button
    private val workingDirectory: String = "/storage/emulated/0/Simple_Text_Editor_Folder"
    private lateinit var workingFile: File

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

            } else {

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        //to create the directory on which the program runs


        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) -> {

            }
            else -> {

                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {

            }
            else -> {

                requestPermissionLauncher.launch(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        val dir = File(workingDirectory)
        if (!(dir.exists() && dir.isDirectory())){
            val f = File(workingDirectory)
            f.mkdir()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initView()
        initAction()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun initView(){
        newButton = findViewById(R.id.newButton)
        openButton = findViewById(R.id.openButton)
        saveButton = findViewById(R.id.saveButton)
        textZone = findViewById(R.id.textZone)
        fileTitle = findViewById(R.id.fileTitle)
        popupFileName = findViewById(R.id.popupFileName)
        textViewFileName = findViewById(R.id.textViewFileName)
        buttonSubmitFileName = findViewById(R.id.buttonSubmitFileName)
    }

    private fun initAction(){

        newButton.setOnClickListener{
            textViewFileName.setText("unnamed.txt")
            popupFileName.visibility = View.VISIBLE
            buttonSubmitFileName.setOnClickListener{
                val fileName:String = textViewFileName.text.toString()
                popupFileName.visibility = View.GONE
                if (fileName.isNotEmpty()){
                    val file = File("$workingDirectory/$fileName")
                    val isCreated = file.createNewFile()
                    if (isCreated){
                        textZone.setText("")
                        fileTitle.text = fileName
                        workingFile = file
                    }
                    else{
                        Toast.makeText(this, "an existing file already have this name", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this, "empty file name", Toast.LENGTH_LONG).show()
                }
            }
        }

        openButton.setOnClickListener{
            textViewFileName.setText("")
            popupFileName.visibility = View.VISIBLE
            buttonSubmitFileName.setOnClickListener{
                val fileName:String = textViewFileName.text.toString()
                popupFileName.visibility = View.GONE
                if (fileName.isNotEmpty()){
                    val file = File("$workingDirectory/$fileName")
                    if (file.exists()){
                        textZone.setText(file.readText())
                        fileTitle.text = fileName
                        workingFile = file
                        Toast.makeText(this, "file opened", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this, "no such file", Toast.LENGTH_LONG).show()
                    }
                }
                else{
                    Toast.makeText(this, "empty file name", Toast.LENGTH_LONG).show()
                }
            }
        }

        saveButton.setOnClickListener{
            if (workingFile != null){
                workingFile.writeText(textZone.text.toString())
                Toast.makeText(this, "file saved", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "open a file before saving it", Toast.LENGTH_LONG).show()
            }
        }
    }
}