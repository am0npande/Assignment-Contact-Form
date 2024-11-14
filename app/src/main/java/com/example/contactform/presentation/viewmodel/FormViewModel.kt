package com.example.contactform.presentation.viewmodel


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.media.MediaRecorder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactform.data.DataStoreManager
import com.example.contactform.domain.models.SubmissionData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@Suppress("DEPRECATION")
@HiltViewModel
class FormViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStoreManager: DataStoreManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        viewModelScope.launch {
            dataStoreManager.id.collect { id ->
                _userId.value = id
            }
        }
    }


    private var _userId = MutableStateFlow<Int?>(null)
    var userId = _userId.asStateFlow()

    fun saveId(id: Int) {
        viewModelScope.launch {
            dataStoreManager.saveId(id)
        }
    }

    private val _gender = savedStateHandle.getStateFlow("gender", 0)
    private val _age = savedStateHandle.getStateFlow("age", "")
    private val _selfiePath = savedStateHandle.getStateFlow("selfiePath", "")
    private val _audioFilePath = savedStateHandle.getStateFlow("audioFilePath", "")
    private val _gpsLocation = savedStateHandle.getStateFlow("gpsLocation", "")
    private val _submitTime = savedStateHandle.getStateFlow("submitTime", "")

    val gender: StateFlow<Int> = _gender
    val age: StateFlow<String> = _age
    val selfiePath: StateFlow<String> = _selfiePath
    val audioFilePath: StateFlow<String> = _audioFilePath
    val gpsLocation: StateFlow<String> = _gpsLocation
    val submitTime: StateFlow<String> = _submitTime

    private var mediaRecorder: MediaRecorder? = null

    // Setters - Update `SavedStateHandle` so the data persists across navigations
    fun setGender(selectedGender: Int) {
        savedStateHandle["gender"] = selectedGender
        Log.d("SubmitPage", "Captured Data: ${gender.value}, ${age.value}, ${selfiePath.value}")
    }

    fun setAge(inputAge: String) {
        savedStateHandle["age"] = inputAge
        Log.d("SubmitPage", "Captured Data: ${gender.value}, ${age.value}, ${selfiePath.value}")
    }

    fun setSelfiePath(path: String) {
        savedStateHandle["selfiePath"] = path
        Log.d("SubmitPage", "Captured Data: ${gender.value}, ${age.value}, ${selfiePath.value}")
    }

    @SuppressLint("MissingPermission")
    fun captureLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? =
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        location?.let {
            savedStateHandle["gpsLocation"] = "${it.latitude},${it.longitude}"
        }
    }
    fun saveBitmapToFile(bitmap: Bitmap): String {

        val selfieFile = File(context.cacheDir, "IMG.png")
        try {
            FileOutputStream(selfieFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return selfieFile.name
    }

    fun captureTimestamp() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        savedStateHandle["submitTime"] = sdf.format(Date())
    }

    fun startRecording() {
        if(_userId.value == null) saveId(1)
        val audioFile = File(context.cacheDir, "REC.wav")
        savedStateHandle["audioFilePath"] = audioFile.name
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFile.absolutePath)
            prepare()
            start()
        }
        Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()
    }

    fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        Toast.makeText(context, "Recording stopped", Toast.LENGTH_SHORT).show()
        val incrementedId = (_userId.value ?: 0) + 1  // If _userId.value is null, treat it as 0
        saveId(incrementedId)
    }

    // Save data locally
    fun saveDataToLocal() {
        val file = File(context.filesDir, "contact_form_submission.json")
        val existingData: MutableList<SubmissionData> = if (file.exists()) {
            val jsonString = file.readText()
            val type = object : TypeToken<MutableList<SubmissionData>>() {}.type
            Gson().fromJson(jsonString, type) ?: mutableListOf()
        } else {
            mutableListOf()
        }

        val genderMap = mapOf(0 to "Not Selected", 1 to "Male", 2 to "Female", 3 to "Other")
        val newEntry = SubmissionData(
            Q1 = genderMap[gender.value] ?: "Not Selected",
            Q2 = age.value,
            Q3 = selfiePath.value,
            recording = audioFilePath.value,
            gps = gpsLocation.value,
            submit_time = submitTime.value,
            image_Path = selfiePath.value
        )

        existingData.add(newEntry)
        val updatedJsonString = Gson().toJson(existingData)
        file.writeText(updatedJsonString)
    }

    fun loadSubmissionDataFromJson(): List<SubmissionData> {
        val file = File(context.filesDir, "contact_form_submission.json")
        if (!file.exists()) return emptyList()

        val jsonString = file.readText()
        val type = object : TypeToken<List<SubmissionData>>() {}.type
        return Gson().fromJson(jsonString, type)
    }
}

