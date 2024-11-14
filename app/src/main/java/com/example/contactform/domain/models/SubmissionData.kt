package com.example.contactform.domain.models


data class SubmissionData(
    val Q1: String,
    val Q2: String,
    val Q3: String,
    val recording: String,
    val gps: String,
    val image_Path: String = "",
    val submit_time: String
)
