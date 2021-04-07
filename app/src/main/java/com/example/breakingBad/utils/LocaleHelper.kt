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
    val ctx = context.createConfigurationContext(configuration)
    return ctx
}

fun getSplitName(name: String): String {
    val nameSurname = name.split(" ")
    if (nameSurname.size == 1 && nameSurname.first() == "Krazy-8") return "Domingo+Molina"
    if (nameSurname.size == 2 && nameSurname.first() == "Hank") return "Henry+Schrader"
    if (nameSurname.size == 2 && nameSurname[1] == "Wins") return "Ken"
    if (nameSurname.size == 1 && nameSurname.first() == "Badger") return "Brandon+Mayhew"
    if (nameSurname.size == 2 && nameSurname.first() == "Elliott" || nameSurname.first() == "Eliott") return "Elliot+Schwartz"
    if (nameSurname.size == 2 && nameSurname[1] == "Swartz") return "Gretchen+Schwartz"
    if (nameSurname.size == 2 && nameSurname.first() == "Ted") return "Theodore+Beneke"
    if (nameSurname.size == 1 && nameSurname.first() == "Combo") return "Christian+Ortgea"
    if (nameSurname.size == 2 && nameSurname.first() == "The") return "Marco+&+Leonel+Salamanca"
    if (nameSurname.size == 2 && nameSurname[1] == "fly") return "Walter+White"
    if (nameSurname.size == 3 && nameSurname.first() == "White" && nameSurname[1] == "White") return "Walter+White+Jr."
    if (nameSurname.size == 2 && nameSurname[1] == "Eladio") return "Hector+Salamanca"
    if (nameSurname.size == 2 && nameSurname.first() == "Steve" && nameSurname[1] == "Gomez") return "Steven+Gomez"
    if (nameSurname.size == 2 && nameSurname.first() == "Jimmy" && nameSurname[1] == "McGill") return "Saul+Goodman"
    if (nameSurname.size == 2 && nameSurname[1] == "Erhmantraut") return "Mike+Ehrmantraut"
    if (nameSurname.size == 2 && nameSurname.first() == "Kim" && nameSurname[1] == "Wexler") return "Kimberly+Wexler"
    if (nameSurname.size == 2 && nameSurname.first() == "Chuck" && nameSurname[1] == "McGill") return "Charles+McGill"
    if (nameSurname.size == 2 && nameSurname.first() == "Nacho" && nameSurname[1] == "Varga") return "Ignacio+Varga"

    return when (nameSurname.size) {
        1 -> nameSurname.first()
        3 -> nameSurname.first() + "+" + nameSurname[1] + "+" + nameSurname[2]
        4 -> nameSurname.first() + "+" + nameSurname[1] + "+" + nameSurname[2] + "+" + nameSurname[3]
        else -> nameSurname.first() + "+" + nameSurname[1]
    }
}

fun applyOverrideConfigurationLocale(
    base: Context,
    overrideConfiguration: Configuration?
): Configuration? {
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
