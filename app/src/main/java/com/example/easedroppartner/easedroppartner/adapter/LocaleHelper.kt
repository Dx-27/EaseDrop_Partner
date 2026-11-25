package com.example.easedroppartner.easedroppartner.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import java.util.Locale

object LocaleHelper {

    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    private const val LANGUAGE_PREF_KEY="language"

    fun getLanguage(context: Context): String {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            updateResourcesLegacy(context, language)
        }
    }
    fun setLanguage(context: Context,languageCode:String){
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString(LANGUAGE_PREF_KEY,languageCode).apply()
    }

    fun clearLanguagePreference(context: Context){
        val sharedPreferences=PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().remove(SELECTED_LANGUAGE).apply()
    }

    private fun getPersistedData(context: Context, defaultLanguage: String): String {
        val preferences= PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage) ?: defaultLanguage
    }

    private fun persist(context: Context, language: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    @Suppress("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}