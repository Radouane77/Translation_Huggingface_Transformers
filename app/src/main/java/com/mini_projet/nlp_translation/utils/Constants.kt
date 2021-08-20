package com.mini_projet.nlp_translation.utils

import com.mini_projet.nlp_translation.utils.modules.Language
import java.util.*

class Constants {
    companion object {
        fun getAllLanguages(): ArrayList<Language> {
            val languages = ArrayList<Language>()
            languages.add(Language("English", "en"))
            languages.add(Language("French", "fr"))
            languages.add(Language("Arabic", "ar"))
            languages.add(Language("Spanish", "es"))
            return languages
        }

        fun getAllLanguagesName(): ArrayList<String> {
            val languages = ArrayList<String>()
            getAllLanguages().forEachIndexed { index, element ->
                languages.add(element.languageName)
            }
            return languages
        }

        fun getAllLanguagesISO(): ArrayList<String> {
            val iso = ArrayList<String>()
            getAllLanguages().forEachIndexed { index, element ->
                iso.add(element.languageISO)
            }
            return iso
        }
    }
}