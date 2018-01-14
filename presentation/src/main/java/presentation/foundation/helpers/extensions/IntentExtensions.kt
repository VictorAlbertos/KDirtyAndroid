package presentation.foundation.helpers.extensions

import android.content.Intent
import android.os.Bundle
import com.google.gson.reflect.TypeToken
import io.victoralbertos.jolyglot.JolyglotGenerics

val wereChangesCode = 1

// General implementation for any Bundle
inline fun <reified T> Bundle.put(key: String, data: T, jolyglot: JolyglotGenerics): Bundle {
    val json = jolyglot.toJson(data, object : TypeToken<T>() {}.type)
    putString(key, json)
    return this
}

inline fun <reified T> Bundle.getOrNull(key: String, jolyglot: JolyglotGenerics): T? = this.run {
    jolyglot.fromJson<T>(getString(key), object : TypeToken<T>() {}.type)
}

inline fun <reified T> Bundle.get(key: String, jolyglot: JolyglotGenerics): T = getOrNull(key, jolyglot)!!


// Specific implementations of Bundle get/put for intent.extras
inline fun <reified T> Intent.put(key: String, data: T, jolyglot: JolyglotGenerics): Intent {
    val bundle = (extras ?: Bundle()).put(key, data, jolyglot)
    putExtras(bundle)
    return this
}

inline fun <reified T> Intent.getOrNull(key: String, jolyglot: JolyglotGenerics): T? = extras?.getOrNull<T>(key, jolyglot)

inline fun <reified T> Intent.get(key: String, jolyglot: JolyglotGenerics): T = getOrNull<T>(key, jolyglot)!!