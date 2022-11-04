package com.example.plazapalm.validation

interface Validator {
    fun isValid(): Boolean
    fun message(): String?
}