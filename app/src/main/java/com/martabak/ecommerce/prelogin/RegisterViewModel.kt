package com.martabak.ecommerce.prelogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel(){

    private var validEmail = false
    private var validPass = false


    fun setEmailValidity(valid: Boolean) {
        validEmail = valid
    }
    fun setPassValidity(valid: Boolean) {
        validPass = valid
    }






}