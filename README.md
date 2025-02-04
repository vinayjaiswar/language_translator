# language_translator


Text Language Translator using Google ML Kit

Project Documentation

1. Introduction

This project is an Android application developed using Kotlin that translates text between different languages using Google's ML Kit. The app takes input text from the user, detects the language, and translates it into the selected target language.

2. Features

Detects the input text language.

Translates text into multiple target languages.

Simple and user-friendly interface.

Uses Google’s ML Kit for translation.

Lightweight and efficient performance.


3. Technologies Used

Language: Kotlin

Framework: Android SDK

Translation API: Google ML Kit

IDE: Android Studio


4. Project Structure

📂 LanguageTranslatorApp  
 ├── 📂 app  
 │   ├── 📂 src/main  
 │   │   ├── 📂 java/com/example/languagetranslator  
 │   │   │   ├── MainActivity.kt  
 │   │   │   ├── TranslatorHelper.kt  
 │   │   ├── 📂 res/layout  
 │   │   │   ├── activity_main.xml  
 │   │   ├── 📂 res/values  
 │   │   │   ├── strings.xml  
 │   ├── AndroidManifest.xml  
 ├── build.gradle  
 ├── settings.gradle

5. Implementation Details

5.1 Dependencies

Add the ML Kit translation dependency in build.gradle (Module: app):

dependencies {
    implementation 'com.google.mlkit:translate:17.0.1'
}

5.2 XML Layout (activity_main.xml)

Designing the user interface with an input field, buttons, and a text view for translated output.

<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="16dp">

    <EditText
        android:id="@+id/inputText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Enter text to translate"/>

    <Spinner
        android:id="@+id/languageSpinner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <Button
        android:id="@+id/translateButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Translate"/>

    <TextView
        android:id="@+id/translatedText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Translation will appear here"/>
</LinearLayout>

5.3 Kotlin Code (MainActivity.kt)

Handles user input and translation logic.

package com.example.languagetranslator

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.*
import com.google.mlkit.common.model.DownloadConditions

class MainActivity : AppCompatActivity() {

    private lateinit var inputText: EditText
    private lateinit var translateButton: Button
    private lateinit var translatedText: TextView
    private lateinit var languageSpinner: Spinner
    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputText = findViewById(R.id.inputText)
        translateButton = findViewById(R.id.translateButton)
        translatedText = findViewById(R.id.translatedText)
        languageSpinner = findViewById(R.id.languageSpinner)

        val languages = mapOf(
            "French" to TranslateLanguage.FRENCH,
            "German" to TranslateLanguage.GERMAN,
            "Spanish" to TranslateLanguage.SPANISH,
            "Hindi" to TranslateLanguage.HINDI
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        translateButton.setOnClickListener {
            val targetLanguageCode = languages[languageSpinner.selectedItem.toString()] ?: return@setOnClickListener
            translateText(inputText.text.toString(), targetLanguageCode)
        }
    }

    private fun translateText(text: String, targetLanguage: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLanguage)
            .build()
        
        translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translatedText.text = it }
                    .addOnFailureListener { translatedText.text = "Translation failed" }
            }
            .addOnFailureListener {
                translatedText.text = "Model download failed"
            }
    }
}

6. How It Works

1. The user enters text in the input field.


2. The user selects a target language from the dropdown.


3. Clicking the Translate button initiates translation using Google ML Kit.


4. The translated text is displayed below the button.



7. Future Enhancements

Add support for more languages.

Implement voice input and text-to-speech.

Improve UI with animations and themes.

Offline translation support.


8. Conclusion

This project demonstrates the implementation of a real-time text translator in an Android application using Kotlin and Google ML Kit. It provides an easy and efficient way to translate text between multiple languages.

Let me know if you need any modifications or improvements in the documentation!

