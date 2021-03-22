package com.example.breakingBad.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import java.util.*

@Suppress("deprecation")
private fun updateResourcesLegacy(context: Context, languageCode: String): Context {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return context.createConfigurationContext(configuration)
}

@TargetApi(Build.VERSION_CODES.N)
private fun updateResources(context: Context, languageCode: String): Context {
    val configuration = Configuration(context.resources.configuration)

    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    configuration.setLocale(locale)

    val localeList = LocaleList(locale)
    LocaleList.setDefault(localeList)
    configuration.setLocales(localeList)
    val ctx=context.createConfigurationContext(configuration)
    return ctx
}

fun applyOverrideConfigurationLocale(base: Context, overrideConfiguration: Configuration?): Configuration? {
    if (overrideConfiguration != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        val uiMode = overrideConfiguration.uiMode
        overrideConfiguration.setTo(base.resources.configuration)
        overrideConfiguration.uiMode = uiMode
    }
    return overrideConfiguration
}

fun updateLocale(context: Context, language: String): Context {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        updateResources(context, language)
    } else updateResourcesLegacy(context, language)
}
