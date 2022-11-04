package com.example.plazapalm.validation


class TextOnlyValidator(private val editable: String, val message: String) : Validator {
    override fun isValid(): Boolean {
        return ValidatorUtils.isTextOnly(editable.trim())
    }

    override fun message(): String {
        return message
    }
}