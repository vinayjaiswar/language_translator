package com.example.language_translator

import android.app.ProgressDialog
import android.nfc.Tag
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var sourceLanguageEt:EditText
    private lateinit var targetLanguageTv:TextView
    private lateinit var sourceLanguageChooseBtn:MaterialButton
    private lateinit var targetLanguageChooseBtn:MaterialButton
    private lateinit var translatebtn:MaterialButton

    companion object{
        private const val TAG="MAIN_TAG"
    }

    private var languageArrayList:ArrayList<ModelLanguage>?=null

    private var sourceLanguageCode="en"
    private var sourceLanguageTitle="English"
    private var targetLanguageCode="hi"
    private var targetLanguageTitle="Hindi"

    private lateinit var translatorOptions: TranslatorOptions
    private lateinit var translator: Translator
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        sourceLanguageEt=findViewById(R.id.sourceLanguageEt)
        targetLanguageTv=findViewById(R.id.targetLanguageTv)
        sourceLanguageChooseBtn=findViewById(R.id.sourceLanguageChooseBtn)
        targetLanguageChooseBtn=findViewById(R.id.targetLanguageChooseBtn)
        translatebtn=findViewById(R.id.translatebtn)

        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        loadAvailableLanguages()

        sourceLanguageChooseBtn.setOnClickListener {
            sourceLanguageChoose()

        }

        targetLanguageChooseBtn.setOnClickListener {
            targetLanguageChoose()
        }

        translatebtn.setOnClickListener {
            validateData()
        }
    }

    private var sourceLanguageText=""
    private fun validateData() {
        sourceLanguageText=sourceLanguageEt.text.toString().trim()
        Log.d(TAG, "validateData: sourcelanguageText: $sourceLanguageText")

        if(sourceLanguageText.isEmpty()){
            showToast("Enter text to translate...")
        }
        else{
            startTranslation()
        }
    }

    private fun startTranslation(){
        progressDialog.setMessage("Processing Language model...")
        progressDialog.show()

        translatorOptions=TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        translator= Translation.getClient(translatorOptions)

        val downloadCondtions=DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(downloadCondtions)
            .addOnSuccessListener {
            Log.d(TAG, "startTranslation: model ready, start translation...")
            progressDialog.setMessage("Translating...")

            translator.translate(sourceLanguageText)
                .addOnSuccessListener { translatedText ->
                    Log.d(TAG, "startTranslation: translatedText:$translatedText")
                    progressDialog.dismiss()
                    targetLanguageTv.text=translatedText

                }
                .addOnFailureListener { e->

                    progressDialog.dismiss()
                    Log.e(TAG, "startTranslation: ", e)

                    showToast("Failed to translate due to ${e.message}")
                }
        }

            .addOnFailureListener { e->
                progressDialog.dismiss()
                Log.e(TAG, "startTranslation: ",e)
                showToast("Failed due to ${e.message}")

            }
    }

    private fun loadAvailableLanguages(){
        languageArrayList= ArrayList()
        val languageCodeList=TranslateLanguage.getAllLanguages()

        for (languageCode in languageCodeList){

            val languageTitle= Locale(languageCode).displayLanguage

            Log.d(TAG, "loadAvailableLanguages: languageCode: $languageCode")
            Log.d(TAG, "loadAvailableLanguages: languageTitle: $languageTitle")
            val modelLanguage=ModelLanguage(languageCode,languageTitle)
            languageArrayList!!.add(modelLanguage)
        }
    }
    private fun sourceLanguageChoose(){
        val popupMenu= PopupMenu(this,sourceLanguageChooseBtn)
        for (i in languageArrayList!!.indices){
            popupMenu.menu.add(Menu.NONE,i,i, languageArrayList!![i].languageTitle)
        }

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val position=menuItem.itemId

            sourceLanguageCode=languageArrayList!![position].languageCode
            sourceLanguageCode=languageArrayList!![position].languageTitle

            sourceLanguageChooseBtn.text=sourceLanguageTitle
            sourceLanguageEt.hint="Enter $sourceLanguageTitle"

            Log.d(TAG, "sourceLanguageChoose: sourceLanguageCode:$sourceLanguageCode")
            Log.d(TAG, "sourceLanguageChoose: sourceLanguageTitle:$sourceLanguageTitle")

            false
        }
    }

    private fun targetLanguageChoose(){
        val popupMenu=PopupMenu(this,targetLanguageChooseBtn)

        for (i in languageArrayList!!.indices){
            popupMenu.menu.add(Menu.NONE,i,i, languageArrayList!![i].languageTitle)
        }
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener { menuItem ->
                val position=menuItem.itemId

                targetLanguageCode=languageArrayList!![position].languageCode
                targetLanguageTitle=languageArrayList!![position].languageTitle

                targetLanguageChooseBtn.text=targetLanguageTitle

                Log.d(TAG, "sourceLanguageChoose: sourceLanguageCode:$sourceLanguageCode")
                Log.d(TAG, "sourceLanguageChoose: sourceLanguageTitle:$sourceLanguageTitle")

                false
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message,Toast.LENGTH_LONG).show()
    }
}
