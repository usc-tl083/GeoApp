package com.example.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.geoquiz.databinding.ActivityCheatBinding

const val CHEAT_STATUS = "com.example.geoquiz.cheat_button_is_pressed"

class CheatViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
    var cheatStatus: Boolean
        get() = savedStateHandle.get(EXTRA_ANSWER_SHOWN) ?: false
        set(value) = savedStateHandle.set(EXTRA_ANSWER_SHOWN, value)
}