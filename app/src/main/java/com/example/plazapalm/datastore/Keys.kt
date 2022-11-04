package com.example.plazapalm.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


const val DATA_STORE_NAME = "PalmPlazza"
const val FOREVER_DATA_STORE_NAME = "ForeverBaronCustomer"
val THEME_KEY by lazy { stringPreferencesKey("theme_key") }
val BOOLEAN_DATA by lazy { booleanPreferencesKey("BOOLEAN") }
val LOGIN_DATA by lazy { stringPreferencesKey("LOGIN_DATA") }
val PROFILE_DATA by lazy { stringPreferencesKey("PROFILE_DATA") }
val PROFILE_IMAGE by lazy { stringPreferencesKey("PROFILE_IMAGE") }
val ADD_PHOTO_URI by lazy { stringPreferencesKey("ADD_PHOTO_URI") }

