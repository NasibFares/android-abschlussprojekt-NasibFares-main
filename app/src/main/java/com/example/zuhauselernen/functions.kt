package com.example.zuhauselernen

import android.content.Context
import com.example.zuhauselernen.data.local.SpinnerRepository
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_EMAIL
import com.example.zuhauselernen.data.local.UserProfile.Companion.COLUMN_PASSWORD
import com.example.zuhauselernen.data.local.UserProfile.Companion.TABLE_NAME_USERS

fun main() {

}
object FunctionUtils {
    /**
    Prüfen ob die Vorname und Nachname  gültig sind.
     */
    fun checkFirstName(firstName: String): Boolean {
        return !(firstName.isEmpty() || !firstName.all { it.isLetter() })
    }
    fun checkLastName(lastName: String): Boolean {
        return !(lastName.isEmpty() || !lastName.all { it.isLetter() })
    }
    /**
    Prüfen ob die Email Adresse gültig ist
     */
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[a-z\\d.]+@[a-z\\d]+\\.[a-z]{2,}\$")
        return emailPattern.matches(email)
    }
    /**
    Prüfen ob die Email Adresse schon registriert ist
     */
    fun isEmailExists(context: Context, email: String): Boolean {
        val userProfile = UserProfile(context)
        val db = userProfile.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val emailExists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return emailExists
    }
    /**
    Prüfen ob das Kennwort gültig ist.
     */
    private fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
        return passwordPattern.matches(password)
    }
    fun checkPassword(password: String): Boolean {
        val checkValidity = isValidPassword(password)
        return if (!checkValidity) {
            println("Ihr Kennwort entspricht nicht die Bedienungen ")
            false
        } else {

            true
        }
    }
    fun getUserPassword(context: Context, email: String): String? {
        val userProfile = UserProfile(context)
        val db = userProfile.readableDatabase
        val query = "SELECT $COLUMN_PASSWORD FROM $TABLE_NAME_USERS WHERE $COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val cursor = db.rawQuery(query, selectionArgs)

        var password: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_PASSWORD)
            if (columnIndex >= 0) {
                password = cursor.getString(columnIndex)
            }
        }

        cursor.close()
        return password
    }

    /**
    Prüfen ob die Kennwörter übereinstimmen.
     */
    fun checkPasswordMatch(originalPassword:String, repeatedPassword:String):Boolean{
        return repeatedPassword==originalPassword
    }
    object SpinnerDataRepository {
        val instance = SpinnerRepository()
    }
}

