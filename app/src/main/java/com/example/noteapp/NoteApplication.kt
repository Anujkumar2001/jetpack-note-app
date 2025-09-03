package com.example.noteapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Note app.
 * The @HiltAndroidApp annotation triggers Hilt's code generation.
 */
@HiltAndroidApp
class NoteApplication : Application()
