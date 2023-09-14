package com.example.zuhauselernen.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zuhauselernen.data.local.model.Subject

class SpinnerRepository {

  val landOptions = arrayOf("", "Deutschland", "England", "Spain")
    val cityOptions = mapOf(

        "Deutschland" to arrayOf( "Berlin", "Bremen", "Hamburg"),
        "England" to arrayOf("London", "Manchester", "Liverpool"),
        "Spain" to arrayOf("Madrid", "Valencia", "Granada")
    )
    private val reasonOptions = arrayOf("","Schule", "Hochschule")
    val schoolOptions = mapOf(

        "Schule" to arrayOf(
            "Grundschule (Klasse 1-4)",
            "Hauptschule (Klasse 5–9/10)",
            "Realschule (Klasse 5–10)",
            "Gesamtschule (Klasse 5–12/13)",
            "Gymnasium (Klasse 5–12/13)"
        ),
        "Hochschule" to arrayOf(
            "Business and Business Law",
            "Mechanical Engineering",
            "Electrical Engineering and Information Technology",
            "Civil Engineering",
            "Computer Science"
        )

    )
    private val classOptions = mapOf(
        "Grundschule (Klasse 1-4)" to arrayOf("1", "2", "3", "4"),
        "Hauptschule (Klasse 5–9/10)" to arrayOf("5", "6", "7", "8", "9", "10"),
        "Realschule (Klasse 5–10)" to arrayOf("5", "6", "7", "8", "9", "10"),
        "Gesamtschule (Klasse 5–12/13)" to arrayOf(
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13"
        ),
        "Gymnasium (Klasse 5–12/13)" to arrayOf(
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13"
        )
    )
    fun getCityOptions(land: String): Array<String> {
        return cityOptions[land] ?: emptyArray()
    }

    fun getSchoolOptions(reason: String): Array<String> {
        return schoolOptions[reason] ?: emptyArray()
    }

    fun getClassOptions(schoolType: String): Array<String> {
        return classOptions[schoolType] ?: emptyArray()
    }
}