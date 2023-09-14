package com.example.zuhauselernen.data.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import com.example.zuhauselernen.data.local.model.Invoice
import com.example.zuhauselernen.data.local.model.Payment
import com.example.zuhauselernen.data.local.model.Subject
import com.example.zuhauselernen.data.local.model.UserData
import com.example.zuhauselernen.data.remote.model.Game
import java.text.ParseException
import java.util.Date
import java.util.Locale

/**
UserProfile ist eine Unterklasse von SQLiteOpenHelper,die die Methoden bereitstellt, um die
Datenbank zu erstellen, zu aktualisieren und zu verwalten.

 */
class UserProfile(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    /**
    Diese Unterklasse verfügt über eine Reihe von Konstanten, die die Namen der Tabellen und Spalten
    der Datenbank definieren.
    Diese Konstanten werden verwendet, um auf die Tabellen und Spalten in der Datenbank zuzugreifen.
     */
    companion object {
        private const val DATABASE_NAME = "UserProfile"
        private const val DATABASE_VERSION = 13

        /**
        Die Tabellen der Datenbank
         */
        const val TABLE_NAME_USERS = "users"
        const val TABLE_NAME_SUBJECTS = "subjects"
        const val TABLE_NAME_PAYMENTS = "payments"
        const val TABLE_NAME_INVOICE = "invoice"
        const val TABLE_NAME_FAVOURITES = "favourites"

        /**
        Die Daten, die in der Tabelle 'users' sind.
         */
        const val COLUMN_FIRSTNAME = "firstName"
        const val COLUMN_LASTNAME = "lastName"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_LAND = "land"
        const val COLUMN_CITY = "city"
        const val COLUMN_REASON = "reason"
        const val COLUMN_SCHOOL = "school"
        const val COLUMN_CLASS = "class"
        const val COLUMN_PHOTO = "userPhoto"


        /**
        Die Daten, die in der Tabelle 'subjects' sind.
        Die email wird verwendet, um zwischen dem Nutzer und seinen ausgewählten Fächer zu
        verknüpfen.
         */
        const val COLUMN_SUBJECT_EMAIL = "email"
        const val COLUMN_SUBJECT_ID = "subjectId"
        const val COLUMN_SUBJECT_NAME = "subjectName"
        const val COLUMN_SUBJECT_IMAGE = "subjectImage"
        const val COLUMN_SUBJECT_CHECKED = "isChecked"
        const val COLUMN_SUBJECT_MONTHLY_PRICE = "monthlySubscriptionPrice"
        const val COLUMN_SUBJECT_YEARLY_PRICE = "yearlySubscriptionPrice"
        const val COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION = "isMonthlySubscribed"
        const val COLUMN_SUBJECT_YEARLY_SUBSCRIPTION = "isYearlySubscribed"
        const val COLUMN_SUBJECT_INVOICE_NUMBER = "invoiceNumber"
        const val COLUMN_SUBJECT_SUBSCRIPTION_PRICE = "subscriptionPrice"
        const val COLUMN_SUBJECT_INSERTION_TIME = "insertTime"

        /**
        Die Daten, die in der Tabelle 'payments' sind.
        Die email wird verwendet, um zwischen dem Nutzer und seinen Zahlungsinformationen zu
        verknüpfen.
         */
        const val COLUMN_PAYMENT_EMAIL = "email"
        const val COLUMN_PAYMENT_ID = "paymentId"
        const val COLUMN_PAYMENT_NAME = "paymentName"
        const val COLUMN_PAYMENT_IMAGE = "paymentImage"
        const val COLUMN_PAYMENT_SELECTED = "isSelected"
        const val COLUMN_PAYMENT_ACCOUNT_OWNER = "accountOwner"
        const val COLUMN_PAYMENT_CARD_NUMBER = "cardNumber"
        const val COLUMN_PAYMENT_CARD_EXPIRY_DATE = "cardExpiryDate"
        const val COLUMN_PAYMENT_SECURITY_CODE = "securityCode"
        const val COLUMN_PAYMENT_BIRTHDATE = "birthDate"
        const val COLUMN_PAYMENT_ADRESS = "adress"
        const val COLUMN_PAYMENT_PLZ = "PLZ"
        const val COLUMN_PAYMENT_INVOICE_NUMBER = "invoiceNumber"
        const val COLUMN_PAYMENT_INSERTION_TIME = "paymentInsertTime"

        /**
        Die Daten, die in der Tabelle 'payments' sind.
        Die email wird verwendet, um zwischen dem Nutzer und seinen Zahlungsinformationen zu
        verknüpfen.
         */
        const val COLUMN_INVOICE_EMAIL = "email"
        const val COLUMN_INVOICE_NUMBER = "invoiceNumber"
        const val COLUMN_INVOICE_DATE = "invoiceDate"
        const val COLUMN_INVOICE_SUBTOTAL = "invoiceSubtotal"
        const val COLUMN_INVOICE_INSERTION_TIME = "invoiceInsertTime"

        /**
        Die Daten, die in der Tabelle 'favourites' sind.
        Die email wird verwendet, um zwischen dem Nutzer und seinen Zahlungsinformationen zu
        verknüpfen.
         */
        const val COLUMN_GAME_EMAIL = "email"
        const val COLUMN_GAME_IMAGE = "thumbnail"
        const val COLUMN_GAME_ID = "gameId"
        const val COLUMN_GAME_URL = "game_url"
        const val COLUMN_GAME_TITLE = "title"

    }

    /**
     * *********************************************************************************************
     */
    /**
    onCreate wird aufgerufen, wenn die Datenbank zum ersten Mal erstellt wird. Sie erstellt die Tabellen
    für Benutzerprofile und Fächer (Subjects). Beide Tabellen haben Fremdschlüsselbeziehungen, die
    durch die Spalte COLUMN_SUBJECT_EMAIL verknüpft sind.
     */
    override fun onCreate(db: SQLiteDatabase) {
        /**
        Erstellung der Tabelle 'Users'
         */
        val createUserTableQuery = "CREATE TABLE $TABLE_NAME_USERS (" +
                "$COLUMN_FIRSTNAME TEXT, " +
                "$COLUMN_LASTNAME TEXT, " +
                "$COLUMN_EMAIL TEXT PRIMARY KEY, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_LAND TEXT, " +
                "$COLUMN_CITY TEXT, " +
                "$COLUMN_REASON TEXT, " +
                "$COLUMN_SCHOOL TEXT, " +
                "$COLUMN_CLASS TEXT ," +
                "$COLUMN_PHOTO TEXT " +
                ")"
        db.execSQL(createUserTableQuery)
        /**
        Erstellung der Tabelle 'Subjects'
        Hier ist SQL Statement:
        CREATE TABLE subjects (email TEXT, subjectId INTEGER,subjectName INTEGER, subjectImage INTEGER,
        isChecked INTEGER, monthlySubscriptionPrice DOUBLE,yearlySubscriptionPrice DOUBLE,
        isMonthlySubscribed BOOLEAN,isYearlySubscribed BOOLEAN,FOREIGN KEY(email) REFERENCES users(email))
         */
        val createSubjectsTableQuery = "CREATE TABLE $TABLE_NAME_SUBJECTS (" +
                "$COLUMN_SUBJECT_EMAIL TEXT, " +
                "$COLUMN_SUBJECT_ID INTEGER," +
                "$COLUMN_SUBJECT_NAME TEXT, " +
                "$COLUMN_SUBJECT_IMAGE INTEGER, " +
                "$COLUMN_SUBJECT_CHECKED INTEGER, " +
                "$COLUMN_SUBJECT_MONTHLY_PRICE DOUBLE," +
                "$COLUMN_SUBJECT_YEARLY_PRICE DOUBLE," +
                "$COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION BOOLEAN," +
                "$COLUMN_SUBJECT_YEARLY_SUBSCRIPTION BOOLEAN," +
                "$COLUMN_SUBJECT_INVOICE_NUMBER INTEGER," +
                "$COLUMN_SUBJECT_SUBSCRIPTION_PRICE DOUBLE," +
                "$COLUMN_SUBJECT_INSERTION_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY($COLUMN_SUBJECT_EMAIL) REFERENCES $TABLE_NAME_USERS($COLUMN_EMAIL)" +
                ")"
        db.execSQL(createSubjectsTableQuery)
        /**
        Erstellung der Tabelle 'Payments'
         */
        val createPaymentsTableQuery = "CREATE TABLE $TABLE_NAME_PAYMENTS (" +
                "$COLUMN_PAYMENT_EMAIL TEXT, " +
                "$COLUMN_PAYMENT_ID  INTEGER," +
                "$COLUMN_PAYMENT_NAME INTEGER, " +
                "$COLUMN_PAYMENT_IMAGE INTEGER, " +
                "$COLUMN_PAYMENT_SELECTED INTEGER, " +
                "$COLUMN_PAYMENT_ACCOUNT_OWNER TEXT," +
                "$COLUMN_PAYMENT_CARD_NUMBER TEXT," +
                "$COLUMN_PAYMENT_CARD_EXPIRY_DATE DATE," +
                "$COLUMN_PAYMENT_SECURITY_CODE TEXT," +
                "$COLUMN_PAYMENT_BIRTHDATE DATE," +
                "$COLUMN_PAYMENT_ADRESS TEXT," +
                "$COLUMN_PAYMENT_PLZ TEXT," +
                "$COLUMN_PAYMENT_INVOICE_NUMBER INTEGER," +
                "$COLUMN_PAYMENT_INSERTION_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY($COLUMN_PAYMENT_EMAIL) REFERENCES $TABLE_NAME_PAYMENTS($COLUMN_EMAIL)" +
                ")"
        db.execSQL(createPaymentsTableQuery)
        /**
        Erstellung der Tabelle 'Invoice'
         */
        val createInvoiceTableQuery = "CREATE TABLE $TABLE_NAME_INVOICE (" +
                "$COLUMN_INVOICE_EMAIL TEXT, " +
                "$COLUMN_INVOICE_NUMBER INTEGER, " +
                "$COLUMN_INVOICE_DATE DATE, " +
                "$COLUMN_INVOICE_SUBTOTAL DOUBLE, " +
                "$COLUMN_INVOICE_INSERTION_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY($COLUMN_INVOICE_EMAIL) REFERENCES $TABLE_NAME_USERS($COLUMN_EMAIL)," +
                "FOREIGN KEY($COLUMN_INVOICE_NUMBER) REFERENCES $TABLE_NAME_PAYMENTS($COLUMN_PAYMENT_INVOICE_NUMBER)" +
                ")"
        db.execSQL(createInvoiceTableQuery)
        /**
        Erstellung der Tabelle 'Favourites'
         */
        val createFavouritesTableQuery = "CREATE TABLE $TABLE_NAME_FAVOURITES(" +
                "$COLUMN_GAME_EMAIL TEXT," +
                "$COLUMN_GAME_ID LONG, " +
                "$COLUMN_GAME_URL TEXT, " +
                "$COLUMN_GAME_IMAGE TEXT, " +
                "$COLUMN_GAME_TITLE TEXT," +
                "FOREIGN KEY($COLUMN_GAME_EMAIL) REFERENCES $TABLE_NAME_USERS($COLUMN_EMAIL)" +
                ")"
        db.execSQL(createFavouritesTableQuery)
    }

    /**
    onUpgrade wird aufgerufen, wenn die Datenbankversion aktualisiert wird. In diesem Fall wird die
    Funktion verwendet, um die Tabelle für Fächer zu erstellen, wenn die Datenbankversion kleiner .
    als 4 ist.
    Dies stellt sicher, dass die Tabelle für Fächer vorhanden ist, wenn die App von einer älteren
    Version aktualisiert wird.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            val createSubjectsTableQuery = "CREATE TABLE $TABLE_NAME_SUBJECTS (" +
                    "$COLUMN_SUBJECT_EMAIL TEXT, " +
                    "$COLUMN_SUBJECT_NAME TEXT, " +
                    "$COLUMN_SUBJECT_IMAGE INTEGER, " +
                    "$COLUMN_SUBJECT_CHECKED INTEGER, " +
                    "$COLUMN_SUBJECT_MONTHLY_PRICE DOUBLE, " +
                    "$COLUMN_SUBJECT_YEARLY_PRICE DOUBLE, " +
                    "$COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION INTEGER, " +
                    "$COLUMN_SUBJECT_YEARLY_SUBSCRIPTION INTEGER, " +
                    "FOREIGN KEY($COLUMN_SUBJECT_EMAIL) REFERENCES $TABLE_NAME_USERS($COLUMN_EMAIL)" +
                    ")"
            db.execSQL(createSubjectsTableQuery)
            val createPaymentsTableQuery = "CREATE TABLE $TABLE_NAME_PAYMENTS (" +
                    "$COLUMN_PAYMENT_EMAIL TEXT, " +
                    "$COLUMN_PAYMENT_ID  INTEGER," +
                    "$COLUMN_PAYMENT_NAME INTEGER, " +
                    "$COLUMN_PAYMENT_IMAGE INTEGER, " +
                    "$COLUMN_PAYMENT_SELECTED INTEGER, " +
                    "$COLUMN_PAYMENT_ACCOUNT_OWNER TEXT," +
                    "$COLUMN_PAYMENT_CARD_NUMBER TEXT," +
                    "$COLUMN_PAYMENT_CARD_EXPIRY_DATE DATE," +
                    "$COLUMN_PAYMENT_SECURITY_CODE TEXT," +
                    "$COLUMN_PAYMENT_BIRTHDATE DATE," +
                    "$COLUMN_PAYMENT_ADRESS TEXT," +
                    "$COLUMN_PAYMENT_PLZ TEXT," +
                    "$COLUMN_PAYMENT_INVOICE_NUMBER INTEGER," +
                    "FOREIGN KEY($COLUMN_PAYMENT_EMAIL) REFERENCES $TABLE_NAME_PAYMENTS($COLUMN_EMAIL)" +
                    ")"
            db.execSQL(createPaymentsTableQuery)

        }
    }

    /**
    ---------------------Einstellung Schritt 1 (Land,City,Reason,School,Class)---------------------
    updateUserProfile aktualisiert die Benutzerprofilinformationen in der Datenbank. Sie verwendet
    die writableDatabase, um die Datenbank im Schreibmodus zu öffnen und die aktualisierten Werte in
    die entsprechenden Spalten einzufügen.
    Diese Methode fügt die Werten (Land,City,Reason,School,Class) zum aktuellen Benutzer hinzu.
     */
    fun updateUserProfile(userData: UserData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_LAND, userData.land)
            put(COLUMN_CITY, userData.city)
            put(COLUMN_REASON, userData.reason)
            put(COLUMN_SCHOOL, userData.schoolType)
            put(COLUMN_CLASS, userData.classLevel)
        }
        val whereClause = "$COLUMN_EMAIL = ?"
        val whereArgs = arrayOf(userData.emailAdress)
        db.update(TABLE_NAME_USERS, values, whereClause, whereArgs)

    }

    fun updateUserPersonalData(userData: UserData) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRSTNAME, userData.firstName)
            put(COLUMN_LASTNAME, userData.lastName)
            put(COLUMN_PASSWORD, userData.password)
            put(COLUMN_LAND, userData.land)
            put(COLUMN_CITY, userData.city)

        }
        val whereClause = "$COLUMN_EMAIL = ?"
        val whereArgs = arrayOf(userData.emailAdress)
        db.update(TABLE_NAME_USERS, values, whereClause, whereArgs)
    }

    fun updateUserProfilePhoto(email: String, photoPath: String) {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PHOTO, photoPath)
        db.update(TABLE_NAME_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
        db.close()
    }

    fun deleteUserByEmail(email: String): Boolean {
        val db = writableDatabase

        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val deletedRows = db.delete(TABLE_NAME_USERS, selection, selectionArgs)

        return deletedRows > 0
    }

    fun addNewGameToUserProfile(userData: UserData, game: Game) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val gameValues = ContentValues().apply {
                put(COLUMN_GAME_EMAIL, userData.emailAdress)
                put(COLUMN_GAME_ID, game.gameId)
                put(COLUMN_GAME_URL, game.gameUrl)
                put(COLUMN_GAME_IMAGE, game.thumbnail)
                put(COLUMN_GAME_TITLE,game.title)

            }
            db.insertWithOnConflict(
                TABLE_NAME_FAVOURITES,
                null,
                gameValues,
                SQLiteDatabase.CONFLICT_REPLACE
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun removeGameFromUserProfile(userData: UserData, gameId: Long) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            db.delete(
                TABLE_NAME_FAVOURITES,
                "$COLUMN_GAME_EMAIL = ? AND $COLUMN_GAME_ID = ?",
                arrayOf(userData.emailAdress, gameId.toString())
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    /**
    -----------------------------Einstellung Schritt 2 (Subjects)-----------------------------------
    updateUserProfileSubjects aktualisiert die Fächer (Subjects) eines Benutzers in der Datenbank.
    Diese Funktion wird in Transaktionen ausgeführt, um sicherzustellen, dass die Datenbank
    konsistent bleibt. Sie löscht zunächst die vorhandenen Fächer für den Benutzer und fügt dann die
    neuen Fächer aus der Liste userData.subjects hinzu.
     */
    fun updateUserProfileSubjects(userData: UserData) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            /**
            Die Aktuelle Fächer löschen
             */
            val subjectWhereClause = "$COLUMN_SUBJECT_EMAIL = ?"
            val subjectWhereArgs = arrayOf(userData.emailAdress)

            db.delete(TABLE_NAME_SUBJECTS, subjectWhereClause, subjectWhereArgs)
            /**
            Neue Fächer hinzufügen
             */
            userData.subjects.forEach { subject ->
                val subjectValues = ContentValues().apply {
                    put(COLUMN_SUBJECT_EMAIL, userData.emailAdress)
                    put(COLUMN_SUBJECT_ID, subject.subjectId)
                    put(COLUMN_SUBJECT_NAME, subject.subjectName)
                    put(COLUMN_SUBJECT_IMAGE, subject.subjectImage)
                    put(COLUMN_SUBJECT_CHECKED, if (subject.isChecked) 1 else 0)
                    put(COLUMN_SUBJECT_MONTHLY_PRICE, subject.monthlySubscriptionPrice)
                    put(COLUMN_SUBJECT_YEARLY_PRICE, subject.yearlySubscriptionPrice)
                    put(
                        COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION,
                        if (subject.isMonthlySubscribed) 1 else 0
                    )
                    put(
                        COLUMN_SUBJECT_YEARLY_SUBSCRIPTION,
                        if (subject.isYearlySubscribed) 1 else 0
                    )
                }

                db.insert(TABLE_NAME_SUBJECTS, null, subjectValues)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun updateUserProfileSubjectsInvoice(user: UserData, subject: Subject, invoiceNumber: Int) {

        subject.invoiceNumber = invoiceNumber
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SUBJECT_INVOICE_NUMBER, subject.invoiceNumber)

        }

        val whereClause = "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_ID = ?"
        val whereArgs = arrayOf(user.emailAdress, subject.subjectId.toString())

        db.update(TABLE_NAME_SUBJECTS, values, whereClause, whereArgs)

    }

    fun addNewSubjectToUserProfile(userData: UserData, subject: Subject) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val subjectValues = ContentValues().apply {
                put(COLUMN_SUBJECT_EMAIL, userData.emailAdress)
                put(COLUMN_SUBJECT_ID, subject.subjectId)
                put(COLUMN_SUBJECT_NAME, subject.subjectName)
                put(COLUMN_SUBJECT_IMAGE, subject.subjectImage)
                put(COLUMN_SUBJECT_CHECKED, if (subject.isChecked) 1 else 0)
                put(COLUMN_SUBJECT_MONTHLY_PRICE, subject.monthlySubscriptionPrice)
                put(COLUMN_SUBJECT_YEARLY_PRICE, subject.yearlySubscriptionPrice)
                put(COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION, if (subject.isMonthlySubscribed) 1 else 0)
                put(COLUMN_SUBJECT_YEARLY_SUBSCRIPTION, if (subject.isYearlySubscribed) 1 else 0)
            }
            db.insertWithOnConflict(
                TABLE_NAME_SUBJECTS,
                null,
                subjectValues,
                SQLiteDatabase.CONFLICT_REPLACE
            )
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
    Die folgende Methode fügt dem Profil des Nutzers eine Liste von Subjects hinzu.
     */
    fun addListOfSubjectsToUserProfile(userData: UserData, subjects: List<Subject>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (subject in subjects) {
                val subjectValues = ContentValues().apply {
                    put(COLUMN_SUBJECT_EMAIL, userData.emailAdress)
                    put(COLUMN_SUBJECT_ID, subject.subjectId)
                    put(COLUMN_SUBJECT_NAME, subject.subjectName)
                    put(COLUMN_SUBJECT_IMAGE, subject.subjectImage)
                    put(COLUMN_SUBJECT_CHECKED, if (subject.isChecked) 1 else 0)
                    put(COLUMN_SUBJECT_MONTHLY_PRICE, subject.monthlySubscriptionPrice)
                    put(COLUMN_SUBJECT_YEARLY_PRICE, subject.yearlySubscriptionPrice)
                    put(
                        COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION,
                        if (subject.isMonthlySubscribed) 1 else 0
                    )
                    put(
                        COLUMN_SUBJECT_YEARLY_SUBSCRIPTION,
                        if (subject.isYearlySubscribed) 1 else 0
                    )
                    put(COLUMN_SUBJECT_INVOICE_NUMBER, subject.invoiceNumber)
                    put(COLUMN_SUBJECT_SUBSCRIPTION_PRICE, subject.subscriptionPrice)
                }
                db.insertWithOnConflict(
                    TABLE_NAME_SUBJECTS,
                    null,
                    subjectValues,
                    SQLiteDatabase.CONFLICT_REPLACE
                )
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
    Die folgende Methode löscht eine Liste von Subjects aus dem Profil des Nutzers.
     */
    fun deleteListOfSubjectsFromUserProfile(userData: UserData, subjects: List<Subject>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (subject in subjects) {
                val whereClause = "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_ID = ?"
                val whereArgs = arrayOf(userData.emailAdress, subject.subjectId.toString())
                db.delete(TABLE_NAME_SUBJECTS, whereClause, whereArgs)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    /**
    In dieser Funktion  wird 'writableDatabase' verwendet, um die Datenbank im Schreibmodus zu öffnen
    und eine Transaktion zu starten, um die Datenkonsistenz sicherzustellen.
    Anschließend wird die Methode db.delete verwendet, um die mit der angegebenen E-Mail-Adresse
    verknüpften Betreffzeilen zu entfernen. Für die Angabe der Löschbedingung (in diesem Fall sollte die
    E-Mail-Adresse des Betreffs mit der bereitgestellten E-Mail übereinstimmen) und z. B. um die
    E-Mail als Argument an die Löschmethode zu übergeben.
     */
    fun deleteUserProfileSubjects(email: String) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            /**
            Die Fächer des aktuellen Nutzer löschen.
             */
            val subjectWhereClause = "$COLUMN_SUBJECT_EMAIL = ?"
            val subjectWhereArgs = arrayOf(email)
            db.delete(TABLE_NAME_SUBJECTS, subjectWhereClause, subjectWhereArgs)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun deleteLastAddedSubject(email: String) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Get the ID of the last added subject for the specific user
            val getLastSubjectIdQuery =
                "SELECT $COLUMN_SUBJECT_ID FROM $TABLE_NAME_SUBJECTS WHERE $COLUMN_SUBJECT_EMAIL = ? ORDER BY $COLUMN_SUBJECT_INSERTION_TIME DESC LIMIT 1"
            val getLastSubjectIdArgs = arrayOf(email)
            val cursor = db.rawQuery(getLastSubjectIdQuery, getLastSubjectIdArgs)

            var lastSubjectId: Long? = null
            if (cursor.moveToFirst()) {
                lastSubjectId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT_ID))
            }
            cursor.close()

            // Delete the last added subject for the specific user
            if (lastSubjectId != null) {
                val subjectWhereClause = "$COLUMN_SUBJECT_ID = ? AND $COLUMN_SUBJECT_EMAIL = ?"
                val subjectWhereArgs = arrayOf(lastSubjectId.toString(), email)

                db.delete(TABLE_NAME_SUBJECTS, subjectWhereClause, subjectWhereArgs)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
    Die folgende Methode löscht eine angegebene Liste von „Subjects“ aus dem Profil des aktuellen
    Benutzers.
     */
    fun deleteListOfSubjects(email: String, subjectIds: List<Long>) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            for (subjectId in subjectIds) {
                // Delete the subject with the given ID for the specific user
                val subjectWhereClause = "$COLUMN_SUBJECT_ID = ? AND $COLUMN_SUBJECT_EMAIL = ?"
                val subjectWhereArgs = arrayOf(subjectId.toString(), email)
                db.delete(TABLE_NAME_SUBJECTS, subjectWhereClause, subjectWhereArgs)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }


    /**
    Den aktuellen SubscriptionPrice abrufen
     */
    fun getSubscriptionPrice(subject: Subject): Double {
        return subject.subscriptionPrice
    }

    /**
    Ein Benutzers Subject mit einer bestimmten Id löschen
     */
    fun deleteUserSpecificSubjectById(user: UserData, subjectId: Int) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            /**
            Subject mit bestimmten Id löschen (für aktuellen Nutzer)
             */
            val subjectWhereClause = "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_ID = ?"
            val subjectWhereArgs = arrayOf(user.emailAdress, subjectId.toString())
            val deletedRowCount =
                db.delete(TABLE_NAME_SUBJECTS, subjectWhereClause, subjectWhereArgs)
            if (deletedRowCount > 0) {
                /**
                Subject wurde erfolgreich gelöscht
                 */
            } else {
                /**
                Subject nicht gefunden oder Löschen fehlgeschlagen
                 */
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun deleteUserSubjectAtIndex(user: UserData, index: Int) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            /**
             * Get the subject ID at the specific index for the current user
             */
            val subjectWhereClause = "$COLUMN_SUBJECT_EMAIL = ?"
            val subjectWhereArgs = arrayOf(user.emailAdress)

            val cursor = db.query(
                TABLE_NAME_SUBJECTS,
                null,
                subjectWhereClause,
                subjectWhereArgs,
                null,
                null,
                null
            )

            if (cursor != null) {
                if (cursor.moveToPosition(index)) {
                    val subjectId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SUBJECT_ID))

                    val subjectDeleteWhereClause =
                        "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_ID = ?"
                    val subjectDeleteWhereArgs = arrayOf(user.emailAdress, subjectId.toString())
                    val deletedRowCount = db.delete(
                        TABLE_NAME_SUBJECTS,
                        subjectDeleteWhereClause,
                        subjectDeleteWhereArgs
                    )

                    if (deletedRowCount > 0) {
                        /**
                         * Subject was deleted successfully
                         */
                    } else {
                        /**
                         * Subject not found or deletion failed
                         */
                    }
                } else {
                    /**
                     * Invalid index
                     */
                }

                cursor.close()
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
    ruft die Fächer (Subjects) eines Benutzers aus der Datenbank ab, die mit der übergebenen
    E-Mail-Adresse übereinstimmen. Sie gibt eine Liste von Subject-Objekten zurück, die die
    abgerufenen Fächer repräsentieren.
     */
    fun getUserSubjectsByEmail(email: String): MutableList<Subject> {
        val db = readableDatabase
        val selection = "$COLUMN_SUBJECT_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_SUBJECTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val subjects = mutableListOf<Subject>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val subjectIdIndex = cursor.getColumnIndex(COLUMN_SUBJECT_ID)
                val subjectNameColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_NAME)
                val subjectImageColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_IMAGE)
                val subjectCheckedColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_CHECKED)
                val subjectMonthlyPriceColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_MONTHLY_PRICE)
                val subjectYearlyPriceColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_YEARLY_PRICE)
                val subjectMonthlySubscribedColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION)
                val subjectYearlySubscribedColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_YEARLY_SUBSCRIPTION)
                val subjectInvoiceIdColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_INVOICE_NUMBER)
                val subjectSubscriptionPriceColumnIndex = cursor.getColumnIndex(
                    COLUMN_SUBJECT_SUBSCRIPTION_PRICE
                )

                Log.d("Subject Columns", "subjectIdIndex: $subjectIdIndex")
                Log.d("Subject Columns", "subjectNameColumnIndex: $subjectNameColumnIndex")
                // ... Hier können mehr Logs erwähnt werden

                if (subjectNameColumnIndex != -1 && subjectImageColumnIndex != -1 && subjectCheckedColumnIndex != -1 &&
                    subjectMonthlyPriceColumnIndex != -1 && subjectYearlyPriceColumnIndex != -1 &&
                    subjectMonthlySubscribedColumnIndex != -1 && subjectYearlySubscribedColumnIndex != -1
                    && subjectInvoiceIdColumnIndex != -1 && subjectSubscriptionPriceColumnIndex != -1
                ) {
                    val subjectId = cursor.getInt(subjectIdIndex)
                    val subjectName = cursor.getString(subjectNameColumnIndex)
                    val subjectImage = cursor.getInt(subjectImageColumnIndex)
                    val isChecked = cursor.getInt(subjectCheckedColumnIndex) == 1
                    val monthlySubscriptionPrice = cursor.getDouble(subjectMonthlyPriceColumnIndex)
                    val yearlySubscriptionPrice = cursor.getDouble(subjectYearlyPriceColumnIndex)
                    val isMonthlySubscribed =
                        cursor.getInt(subjectMonthlySubscribedColumnIndex) == 1
                    val isYearlySubscribed = cursor.getInt(subjectYearlySubscribedColumnIndex) == 1
                    val invoiceId = cursor.getInt(subjectInvoiceIdColumnIndex)
                    val subscriptionPrice = cursor.getDouble(subjectSubscriptionPriceColumnIndex)
                    val subject = Subject(
                        subjectId,
                        subjectName,
                        subjectImage,
                        isChecked,
                        monthlySubscriptionPrice,
                        yearlySubscriptionPrice,
                        isMonthlySubscribed,
                        isYearlySubscribed,
                        invoiceId,
                        subscriptionPrice
                    )
                    subjects.add(subject)
                } else {
                    /**
                    Hier kann Log hinzugefügt werden ,falls der Index der Spalte nicht gefunden ist
                     */
                }
            }
        }

        cursor.close()
        db.close()

        return subjects
    }

    /**
    Die folgende Methode wird in SubscriptionAdapter verwendet, um die (SubscriptionType) zu
    aktualisieren.
     */
    fun updateUserSpecificSubjectSubscription(user: UserData, subject: Subject) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION, subject.isMonthlySubscribed)
            put(COLUMN_SUBJECT_YEARLY_SUBSCRIPTION, subject.isYearlySubscribed)
            put(COLUMN_SUBJECT_SUBSCRIPTION_PRICE, subject.subscriptionPrice)
        }

        val whereClause = "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_ID = ?"
        val whereArgs = arrayOf(user.emailAdress, subject.subjectId.toString())

        db.update(TABLE_NAME_SUBJECTS, values, whereClause, whereArgs)
    }

    /**
    -----------------------------Einstellung Schritt 3 (Invoices)-----------------------------------
     */
    fun createNewInvoice(
        userEmail: String,
        invoiceNumber: Int,
        invoiceDate: String,
        subtotal: Double,

        ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INVOICE_EMAIL, userEmail)
            put(COLUMN_INVOICE_NUMBER, invoiceNumber)
            put(COLUMN_INVOICE_DATE, invoiceDate)
            put(COLUMN_INVOICE_SUBTOTAL, subtotal)
        }

        val newRowId = db.insert(TABLE_NAME_INVOICE, null, values)
        db.close()

        return newRowId
    }


    fun addNewInvoiceToUserProfile(userData: UserData, invoice: Invoice) {
        val db = writableDatabase

        db.beginTransaction()

        try {
            val invoiceValues = ContentValues().apply {
                put(COLUMN_INVOICE_EMAIL, userData.emailAdress)
                put(COLUMN_INVOICE_NUMBER, invoice.invoiceNumber)
                put(COLUMN_INVOICE_DATE, formatDateToString(invoice.invoiceDate))
                put(COLUMN_INVOICE_SUBTOTAL, invoice.invoiceSubtotal)

            }

            db.insertWithOnConflict(
                TABLE_NAME_INVOICE,
                null,
                invoiceValues,
                SQLiteDatabase.CONFLICT_REPLACE
            )

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /**
    Diese Methode ruft ab die Rechnungen, die zum genannten Nutzern gehören.
     */
    fun getUserInvoices(email: String): List<Invoice> {


        val query = "SELECT * FROM $TABLE_NAME_INVOICE WHERE $COLUMN_INVOICE_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val db = readableDatabase
        val cursor = db.rawQuery(query, selectionArgs)

        val invoices = mutableListOf<Invoice>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val invoiceNumberColumnIndex = cursor.getColumnIndex(COLUMN_INVOICE_NUMBER)
                val invoiceDateColumnIndex = cursor.getColumnIndex(COLUMN_INVOICE_DATE)
                val invoiceSubtotalColumnIndex = cursor.getColumnIndex(COLUMN_INVOICE_SUBTOTAL)


                if (invoiceNumberColumnIndex != -1 && invoiceDateColumnIndex != -1 && invoiceSubtotalColumnIndex != -1) {
                    val invoiceNumber = cursor.getInt(invoiceNumberColumnIndex)
                    val invoiceDateString = cursor.getString(invoiceDateColumnIndex)
                    val invoiceSubtotal = cursor.getDouble(invoiceSubtotalColumnIndex)

                    val invoiceDate = convertDateStringToSqlDate(invoiceDateString)

                    if (invoiceDate != null) {
                        val invoice = getInvoicePaymentByInvoiceNumber(invoiceNumber)?.let {
                            Invoice(
                                invoiceNumber,
                                invoiceDate,
                                mutableListOf(),
                                it,
                                invoiceSubtotal,
                                true
                            )
                        }
                        if (invoice != null) {
                            invoices.add(invoice)
                        }
                    } else {
                        // Handle the case where invoiceDate is null
                    }
                } else {
                    // ...
                }
            }
        }
        cursor.close()
        db.close()

        return invoices
    }

    /**
    Methoden erstellen, um die (Subjects,Payment) für bestimmte Invoice ID zurück zu geben.
     */
    fun getInvoiceSubjectsByInvoiceId(email: String, invoiceNumber: Int): MutableList<Subject> {
        val db = readableDatabase
        val selection = "$COLUMN_SUBJECT_EMAIL = ? AND $COLUMN_SUBJECT_INVOICE_NUMBER = ?"
        val selectionArgs = arrayOf(email, invoiceNumber.toString())

        val cursor = db.query(
            TABLE_NAME_SUBJECTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val subjects = mutableListOf<Subject>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val subjectIdIndex = cursor.getColumnIndex(COLUMN_SUBJECT_ID)
                val subjectNameColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_NAME)
                val subjectImageColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_IMAGE)
                val subjectCheckedColumnIndex = cursor.getColumnIndex(COLUMN_SUBJECT_CHECKED)
                val subjectMonthlyPriceColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_MONTHLY_PRICE)
                val subjectYearlyPriceColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_YEARLY_PRICE)
                val subjectMonthlySubscribedColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_MONTHLY_SUBSCRIPTION)
                val subjectYearlySubscribedColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_YEARLY_SUBSCRIPTION)
                val subjectInvoiceIdColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_INVOICE_NUMBER)
                val subscriptionPriceColumnIndex =
                    cursor.getColumnIndex(COLUMN_SUBJECT_SUBSCRIPTION_PRICE)
                // Check if the required columns are present in the cursor
                if (subjectNameColumnIndex != -1 &&
                    subjectImageColumnIndex != -1 &&
                    subjectCheckedColumnIndex != -1 &&
                    subjectMonthlyPriceColumnIndex != -1 &&
                    subjectYearlyPriceColumnIndex != -1 &&
                    subjectMonthlySubscribedColumnIndex != -1 &&
                    subjectYearlySubscribedColumnIndex != -1 &&
                    subjectInvoiceIdColumnIndex != -1 &&
                    subscriptionPriceColumnIndex != -1
                ) {
                    val subjectId = cursor.getInt(subjectIdIndex)
                    val subjectName = cursor.getString(subjectNameColumnIndex)
                    val subjectImage = cursor.getInt(subjectImageColumnIndex)
                    val isChecked = cursor.getInt(subjectCheckedColumnIndex) == 1
                    val monthlySubscriptionPrice = cursor.getDouble(subjectMonthlyPriceColumnIndex)
                    val yearlySubscriptionPrice = cursor.getDouble(subjectYearlyPriceColumnIndex)
                    val isMonthlySubscribed =
                        cursor.getInt(subjectMonthlySubscribedColumnIndex) == 1
                    val isYearlySubscribed = cursor.getInt(subjectYearlySubscribedColumnIndex) == 1
                    val subjectInvoiceId = cursor.getInt(subjectInvoiceIdColumnIndex)
                    val subscriptionPrice = cursor.getDouble(subscriptionPriceColumnIndex)

                    // Create the Subject object
                    val subject = Subject(
                        subjectId,
                        subjectName,
                        subjectImage,
                        isChecked,
                        monthlySubscriptionPrice,
                        yearlySubscriptionPrice,
                        isMonthlySubscribed,
                        isYearlySubscribed,
                        subjectInvoiceId,
                        subscriptionPrice
                    )
                    subjects.add(subject)
                } else {
                    // Log or handle the case when the required columns are not found
                }
            }
            cursor.close()
        }

        db.close()

        return subjects
    }

    fun updateUserSpecificInvoiceSubtotal(user: UserData, invoice: Invoice) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INVOICE_SUBTOTAL, invoice.invoiceSubtotal)

        }

        val whereClause = "$COLUMN_INVOICE_EMAIL = ? AND $COLUMN_INVOICE_NUMBER = ?"
        val whereArgs = arrayOf(user.emailAdress, invoice.invoiceNumber.toString())

        db.update(TABLE_NAME_INVOICE, values, whereClause, whereArgs)
    }

    private fun getInvoicePaymentByInvoiceNumber(invoiceNumber: Int): Payment? {
        val db = readableDatabase
        val query = """
        SELECT P.* -- Use table alias for payments table
        FROM $TABLE_NAME_PAYMENTS P
        INNER JOIN $TABLE_NAME_INVOICE I ON P.$COLUMN_PAYMENT_INVOICE_NUMBER = I.$COLUMN_INVOICE_NUMBER
        WHERE I.$COLUMN_INVOICE_NUMBER = ?
    """.trimIndent()

        val selectionArgs = arrayOf(invoiceNumber.toString())

        val cursor = db.rawQuery(query, selectionArgs)
        var payment: Payment? = null

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val paymentIdIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ID)
                val paymentNameColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_NAME)
                val paymentImageColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_IMAGE)
                val isSelectedColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SELECTED)
                val accountOwnerColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ACCOUNT_OWNER)
                val cardNumberColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_CARD_NUMBER)
                val cardExpiryDateColumnIndex =
                    cursor.getColumnIndex(COLUMN_PAYMENT_CARD_EXPIRY_DATE)
                val securityCodeColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SECURITY_CODE)
                val birthDateColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_BIRTHDATE)
                val addressColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ADRESS)
                val pLZColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_PLZ)
                val invoiceNumberColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_INVOICE_NUMBER)

                if (paymentIdIndex != -1 && paymentNameColumnIndex != -1 && paymentImageColumnIndex != -1 &&
                    isSelectedColumnIndex != -1 && accountOwnerColumnIndex != -1 && cardNumberColumnIndex != -1 &&
                    cardExpiryDateColumnIndex != -1 && securityCodeColumnIndex != -1 && birthDateColumnIndex != -1 &&
                    addressColumnIndex != -1 && pLZColumnIndex != -1 && invoiceNumberColumnIndex != -1
                ) {
                    val paymentId = cursor.getInt(paymentIdIndex)
                    val paymentName = cursor.getString(paymentNameColumnIndex)
                    val paymentImage = cursor.getInt(paymentImageColumnIndex)
                    val isSelected = cursor.getInt(isSelectedColumnIndex) == 1
                    val accountOwner = cursor.getString(accountOwnerColumnIndex)
                    val cardNumber = cursor.getString(cardNumberColumnIndex)
                    val cardExpiryDateString = cursor.getString(cardExpiryDateColumnIndex)
                    val cardExpiryDate = if (cardExpiryDateString != null) {
                        convertDateStringToSqlDate(cardExpiryDateString)
                    } else {
                        null
                    }
                    val securityCode = cursor.getString(securityCodeColumnIndex)
                    val birthDateString = cursor.getString(birthDateColumnIndex)
                    val birthDate = if (birthDateString != null) {
                        convertDateStringToSqlDate(birthDateString)
                    } else {
                        null
                    }

                    val address = cursor.getString(addressColumnIndex)
                    val pLZ = cursor.getString(pLZColumnIndex)
                    val invoiceNumber = cursor.getInt(invoiceNumberColumnIndex)

                    payment = Payment(
                        paymentId,
                        paymentName,
                        paymentImage,
                        isSelected,
                        accountOwner,
                        cardNumber,
                        cardExpiryDate,
                        securityCode,
                        birthDate,
                        address,
                        pLZ,
                        invoiceNumber

                    )
                }
            }
            cursor.close()
        }

        db.close()

        return payment
    }

    fun deleteLastAddedInvoice(email: String) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            // Get the ID of the last added subject for the specific user
            val getLastInvoiceNumberQuery =
                "SELECT $COLUMN_INVOICE_NUMBER FROM $TABLE_NAME_INVOICE WHERE $COLUMN_INVOICE_EMAIL = ? ORDER BY $COLUMN_INVOICE_INSERTION_TIME DESC LIMIT 1"

            val getLastInvoiceNumberArgs = arrayOf(email)
            val cursor = db.rawQuery(getLastInvoiceNumberQuery, getLastInvoiceNumberArgs)

            var lastInvoiceNumber: Int? = null
            if (cursor.moveToFirst()) {
                lastInvoiceNumber =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INVOICE_NUMBER))
            }
            cursor.close()

            // Delete the last added subject for the specific user
            if (lastInvoiceNumber != null) {
                val invoiceWhereClause = "$COLUMN_INVOICE_NUMBER = ? AND $COLUMN_INVOICE_EMAIL = ?"
                val invoiceWhereArgs = arrayOf(lastInvoiceNumber.toString(), email)

                db.delete(TABLE_NAME_INVOICE, invoiceWhereClause, invoiceWhereArgs)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun getLastInvoiceNumberByEmail(email: String): Int? {
        val db = readableDatabase
        val selection = "$COLUMN_INVOICE_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        val orderBy = "$COLUMN_INVOICE_NUMBER DESC" // Order by invoice number in descending order
        val cursor = db.query(
            TABLE_NAME_INVOICE,
            arrayOf(COLUMN_INVOICE_NUMBER),
            selection,
            selectionArgs,
            null,
            null,
            orderBy,
            "1" // Limit the result to 1 row
        )

        var lastInvoiceNumber: Int? = null

        cursor?.use {
            if (it.moveToFirst()) {
                val invoiceNumberColumnIndex = it.getColumnIndex(COLUMN_INVOICE_NUMBER)

                // Check if the invoice number column is present in the cursor
                if (invoiceNumberColumnIndex != -1) {
                    lastInvoiceNumber = it.getInt(invoiceNumberColumnIndex)
                } else {
                    // Log or handle the case when the invoice number column is not found
                }
            }
        }

        db.close()

        return lastInvoiceNumber
    }

    fun createNewPayment(
        userEmail: String,
        paymentId: String,
        paymentName: String,
        paymentImage: Int,
        isSelected: Boolean,
        accountOwner: String,
        cardNumber: String,
        cardExpiryDate: Date?,
        securityCode: String,
        birthDate: Date?,
        adress: String,
        pLZ: String,
        invoiceNumber: Int

    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PAYMENT_EMAIL, userEmail)
            put(COLUMN_PAYMENT_ID, paymentId)
            put(COLUMN_PAYMENT_NAME, paymentName)
            put(COLUMN_PAYMENT_IMAGE, paymentImage)
            put(COLUMN_PAYMENT_SELECTED, isSelected)
            put(COLUMN_PAYMENT_ACCOUNT_OWNER, accountOwner)
            put(COLUMN_PAYMENT_CARD_NUMBER, cardNumber)
            put(COLUMN_PAYMENT_CARD_EXPIRY_DATE, cardExpiryDate?.let { formatDateToString(it) })
            put(COLUMN_PAYMENT_SECURITY_CODE, securityCode)
            put(COLUMN_PAYMENT_BIRTHDATE, birthDate?.let { formatDateToString(it) })
            put(COLUMN_PAYMENT_ADRESS, adress)
            put(COLUMN_PAYMENT_PLZ, pLZ)
            put(COLUMN_PAYMENT_INVOICE_NUMBER, invoiceNumber)
        }

        val newRowId = db.insert(TABLE_NAME_PAYMENTS, null, values)
        db.close()

        return newRowId // Return the ID of the newly inserted row
    }


    /**
    ------------------------------------Zusätzliche Methoden---------------------------------------
    - getUserProfileByEmail(email: String)
    - getUserLandByEmail(email: String) bZw city,reason,school, class
    - getDateFromCursor(cursor: Cursor, columnIndex: Int)
    - formatDateToString(date: Date)
     */
    /**
    ruft die Benutzerprofilinformationen aus der Datenbank ab, die mit der übergebenen E-Mail-Adresse
    übereinstimmen. Es verwendet die readableDatabase, um die Datenbank im Lesemodus zu öffnen, und
    gibt ein UserData-Objekt mit den abgerufenen Informationen zurück.
     */
    @SuppressLint("Range")
    fun getUserProfileByEmail(email: String): UserData? {
        val db = readableDatabase

        /**
        Die Daten des Benutzers abrufen
         */
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
        val columnNames = cursor.columnNames
        columnNames.forEachIndexed { index, columnName ->
            Log.d("Column", "Index: $index, Name: $columnName")
        }
        val userData: UserData? = if (cursor != null && cursor.moveToFirst()) {
            val firstNameIndex = cursor.getColumnIndex(COLUMN_FIRSTNAME)
            val lastNameIndex = cursor.getColumnIndex(COLUMN_LASTNAME)
            val passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD)
            // ... hier können mehr Spalten abgerufen werden.
            val userProfilePhotoIndex = cursor.getColumnIndex(COLUMN_PHOTO)
            /**
            Überprüfen,ob  die benötigten Spalten existieren
             */
            if (firstNameIndex != -1 && lastNameIndex != -1) {
                val firstName = cursor.getString(firstNameIndex)
                val lastName = cursor.getString(lastNameIndex)
                val password = cursor.getString(passwordIndex)
                val userProfilePhoto = cursor.getString(userProfilePhotoIndex)
                // ... hier können mehr Spalten abgerufen werden.

                UserData(
                    firstName,
                    lastName,
                    email,
                    password,
                    "",
                    "",
                    "",
                    "",
                    "",
                    mutableListOf(),
                    mutableListOf(), userProfilePhoto ?: "", mutableListOf()
                )

            } else {
                /**
                Der Index der Spalte wurde nicht gefunden
                 */
                Log.e(
                    "User Profile",
                    "Die Spalte wurde nicht gefunden für firstName oder lastName "
                )
                null
            }
        } else {
            Log.e("User Profile", "Der Cursor ist null oder es wurden keine Zeilen gefunden.")
            null
        }

        cursor?.close()
        db.close()

        return userData
    }
    /**
    Die Folgenden Methoden rufen die Werten (land,city,reason,school,class) für bestimmten Nutzer
    mittels seiner e-Mail Adresse ab.
     */
    /**
    Die 'land' Einstellung für einen bestimmten Benutzer per e-mail abrufen.
     */
    fun getUserLandByEmail(email: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            arrayOf(COLUMN_LAND),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var land: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_LAND)
            if (columnIndex != -1) {
                land = cursor.getString(columnIndex)
            }
        }

        cursor?.close()
        db.close()

        return land
    }

    /**
    Die 'city' Einstellung für einen bestimmten Benutzer per e-mail abrufen.
     */
    fun getUserCityByEmail(email: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            arrayOf(COLUMN_CITY),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var city: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_CITY)
            if (columnIndex != -1) {
                city = cursor.getString(columnIndex)
            }
        }

        cursor?.close()
        db.close()

        return city
    }

    /**
    Die 'reason' Einstellung  für einen bestimmten Benutzer per e-mail abrufen.
     */
    fun getUserReasonByEmail(email: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            arrayOf(COLUMN_REASON),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var reason: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_REASON)
            if (columnIndex != -1) {
                reason = cursor.getString(columnIndex)
            }
        }

        cursor?.close()
        db.close()

        return reason
    }

    /**
    Die 'school' Einstellung für einen bestimmten Benutzer per e-mail abrufen.
     */
    fun getUserSchoolByEmail(email: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            arrayOf(COLUMN_SCHOOL),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var school: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_SCHOOL)
            if (columnIndex != -1) {
                school = cursor.getString(columnIndex)
            }
        }

        cursor?.close()
        db.close()

        return school
    }

    /**
    Die 'class' Einstellung für einen bestimmten Benutzer per e-mail abrufen.
     */
    fun getUserClassByEmail(email: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_USERS,
            arrayOf(COLUMN_CLASS),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var classLevel: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_CLASS)
            if (columnIndex != -1) {
                classLevel = cursor.getString(columnIndex)
            }
        }

        cursor?.close()
        db.close()

        return classLevel
    }

    /**
    Die Zahlungsmethode des Benutzers per e-mail abrufen.
     */

    //Nue hinzugefügter Code------------------------------------------------------------------------
    fun getUserPaymentsByEmail(email: String): Payment {
        val db = readableDatabase
        val selection = "$COLUMN_SUBJECT_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            TABLE_NAME_PAYMENTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val payment = Payment(
            1, "", 0, false, "", "",
            null, "", null, "", "", 1
        )

        if (cursor != null && cursor.moveToFirst()) {
            val paymentIdIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ID)
            val paymentNameColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_NAME)
            val paymentImageColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_IMAGE)
            val paymentSelectedColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SELECTED)
            val paymentAccountOwnerColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_ACCOUNT_OWNER)
            val paymentCardNumberColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_CARD_NUMBER)
            val paymentCardExpiryDateColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_CARD_EXPIRY_DATE)
            val paymentSecurityCodeColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_SECURITY_CODE)
            val paymentBirthDateColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_BIRTHDATE)
            val paymentAdressColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_ADRESS)
            val paymentPLZColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_PLZ)
            val paymentInvoiceNumberColumnIndex =
                cursor.getColumnIndex(COLUMN_PAYMENT_INVOICE_NUMBER)
            Log.d("Subject Columns", "subjectIdIndex: $paymentIdIndex")
            Log.d("Subject Columns", "subjectNameColumnIndex: $paymentNameColumnIndex")
            // ... Add logs for other column indices
            if (paymentNameColumnIndex != -1 && paymentImageColumnIndex != -1 && paymentSelectedColumnIndex != -1 &&
                paymentAccountOwnerColumnIndex != -1 && paymentCardNumberColumnIndex != -1 &&
                paymentCardExpiryDateColumnIndex != -1 && paymentSecurityCodeColumnIndex != -1 &&
                paymentBirthDateColumnIndex != -1 && paymentAdressColumnIndex != -1 &&
                paymentPLZColumnIndex != -1 && paymentInvoiceNumberColumnIndex != -1
            ) {
                val paymentId = cursor.getInt(paymentIdIndex)
                val paymentName = cursor.getString(paymentNameColumnIndex)
                val paymentImage = cursor.getInt(paymentImageColumnIndex)
                val isSelected = cursor.getInt(paymentSelectedColumnIndex) == 1
                val accountOwner = cursor.getString(paymentAccountOwnerColumnIndex)
                val cardNumber = cursor.getString(paymentCardNumberColumnIndex)

                val cardExpiryDateString = cursor.getString(paymentCardExpiryDateColumnIndex)
                val cardExpiryDate = if (cardExpiryDateString != null) {
                    convertDateStringToSqlDate(cardExpiryDateString)
                } else {
                    null
                }

                val securityCode = cursor.getString(paymentSecurityCodeColumnIndex)
                val birthDateString = cursor.getString(paymentBirthDateColumnIndex)
                val birthDate = if (birthDateString != null) {
                    convertDateStringToSqlDate(birthDateString)
                } else {
                    null
                }

                val adress = cursor.getString(paymentAdressColumnIndex)
                val pLZ = cursor.getString(paymentPLZColumnIndex)
                val invoiceNumber = cursor.getInt(paymentInvoiceNumberColumnIndex)

                val payment = Payment(
                    paymentId,
                    paymentName,
                    paymentImage,
                    isSelected,
                    accountOwner,
                    cardNumber,
                    cardExpiryDate,
                    securityCode,
                    birthDate,
                    adress,
                    pLZ,
                    invoiceNumber
                )
            } else {

            }

        }
        cursor.close()
        db.close()

        return payment
    }


    fun getPaymentById(paymentId: Int): Payment? {
        val db = readableDatabase
        val selection = "$COLUMN_PAYMENT_ID = ?"
        val selectionArgs = arrayOf(paymentId.toString())

        val cursor = db.query(
            TABLE_NAME_PAYMENTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var payment: Payment? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val paymentNameColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_NAME)
                val paymentImageColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_IMAGE)
                val isSelectedColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SELECTED)
                val accountOwnerColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ACCOUNT_OWNER)
                val cardNumberColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_CARD_NUMBER)
                val cardExpiryDateColumnIndex =
                    cursor.getColumnIndex(COLUMN_PAYMENT_CARD_EXPIRY_DATE)
                val securityCodeColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SECURITY_CODE)
                val birthDateColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_BIRTHDATE)
                val addressColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ADRESS)
                val pLZColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_PLZ)
                val invoiceNumberColumnIndex = cursor.getColumnIndex(COLUMN_PAYMENT_INVOICE_NUMBER)

                // Check if the required columns are present in the cursor
                if (paymentNameColumnIndex != -1 &&
                    paymentImageColumnIndex != -1 &&
                    isSelectedColumnIndex != -1 &&
                    accountOwnerColumnIndex != -1 &&
                    cardNumberColumnIndex != -1 &&
                    cardExpiryDateColumnIndex != -1 &&
                    securityCodeColumnIndex != -1 &&
                    birthDateColumnIndex != -1 &&
                    addressColumnIndex != -1 &&
                    pLZColumnIndex != -1 &&
                    invoiceNumberColumnIndex != -1
                ) {
                    val paymentName = cursor.getString(paymentNameColumnIndex)
                    val paymentImage = cursor.getInt(paymentImageColumnIndex)
                    val isSelected = cursor.getInt(isSelectedColumnIndex) == 1
                    val accountOwner = cursor.getString(accountOwnerColumnIndex)
                    val cardNumber = cursor.getString(cardNumberColumnIndex)
                    val cardExpiryDateString = cursor.getString(cardExpiryDateColumnIndex)
                    val cardExpiryDate = if (!cardExpiryDateString.isNullOrEmpty()) {
                        java.sql.Date.valueOf(cardExpiryDateString)
                    } else {
                        null
                    }

                    val securityCode = cursor.getString(securityCodeColumnIndex)

                    val birthDateString = cursor.getString(birthDateColumnIndex)
                    val birthDate = if (!birthDateString.isNullOrEmpty()) {
                        java.sql.Date.valueOf(birthDateString)
                    } else {
                        null
                    }

                    val address = cursor.getString(addressColumnIndex)
                    val pLZ = cursor.getString(pLZColumnIndex)
                    val invoiceNumber = cursor.getInt(invoiceNumberColumnIndex)

                    // Create the Payment object
                    payment = Payment(
                        paymentId,
                        paymentName,
                        paymentImage,
                        isSelected,
                        accountOwner,
                        cardNumber,
                        cardExpiryDate,
                        securityCode,
                        birthDate,
                        address,
                        pLZ,
                        invoiceNumber
                    )
                } else {
                    // Log or handle the case when the required columns are not found
                }
            }
            cursor.close()
        }

        db.close()

        return payment
    }

    fun getLastUserPaymentByEmail(email: String): Payment? {
        val db = readableDatabase

        val selection = "$COLUMN_PAYMENT_EMAIL = ?"
        val selectionArgs = arrayOf(email)

        val orderBy = "$COLUMN_PAYMENT_INSERTION_TIME DESC"
        val limit = "1"

        val cursor = db.query(
            TABLE_NAME_PAYMENTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            orderBy,
            limit
        )

        var lastPayment: Payment? = null

        if (cursor != null && cursor.moveToFirst()) {
            val paymentIdIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ID)
            val paymentNameIndex = cursor.getColumnIndex(COLUMN_PAYMENT_NAME)
            val paymentImageIndex = cursor.getColumnIndex(COLUMN_PAYMENT_IMAGE)
            val isSelectedIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SELECTED)
            val accountOwnerIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ACCOUNT_OWNER)
            val cardNumberIndex = cursor.getColumnIndex(COLUMN_PAYMENT_CARD_NUMBER)
            val cardExpiryDateIndex = cursor.getColumnIndex(COLUMN_PAYMENT_CARD_EXPIRY_DATE)
            val securityCodeIndex = cursor.getColumnIndex(COLUMN_PAYMENT_SECURITY_CODE)
            val birthDateIndex = cursor.getColumnIndex(COLUMN_PAYMENT_BIRTHDATE)
            val addressIndex = cursor.getColumnIndex(COLUMN_PAYMENT_ADRESS)
            val pLZIndex = cursor.getColumnIndex(COLUMN_PAYMENT_PLZ)
            val invoiceNumberIndex = cursor.getColumnIndex(COLUMN_PAYMENT_INVOICE_NUMBER)

            val paymentId = cursor.getInt(paymentIdIndex)
            val paymentName = cursor.getString(paymentNameIndex)
            val paymentImage = cursor.getInt(paymentImageIndex)
            val isSelected = cursor.getInt(isSelectedIndex) == 1
            val accountOwner = cursor.getString(accountOwnerIndex)
            val cardNumber = cursor.getString(cardNumberIndex)
            val cardExpiryDateString = cursor.getString(cardExpiryDateIndex)
            val cardExpiryDate = if (!cardExpiryDateString.isNullOrEmpty()) {
                java.sql.Date.valueOf(cardExpiryDateString)
            } else {
                null
            }
            val securityCode = cursor.getString(securityCodeIndex)
            val birthDateString = cursor.getString(birthDateIndex)
            val birthDate = if (!birthDateString.isNullOrEmpty()) {
                java.sql.Date.valueOf(birthDateString)
            } else {
                null
            }
            val address = cursor.getString(addressIndex)
            val pLZ = cursor.getString(pLZIndex)
            val invoiceNumber = cursor.getInt(invoiceNumberIndex)

            // Create the Payment object
            lastPayment = Payment(
                paymentId,
                paymentName,
                paymentImage,
                isSelected,
                accountOwner,
                cardNumber,
                cardExpiryDate,
                securityCode,
                birthDate,
                address,
                pLZ,
                invoiceNumber
            )
        }

        cursor?.close()
        db.close()

        return lastPayment
    }


    fun updateUserSpecificPayment(user: UserData, payment: Payment) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PAYMENT_ACCOUNT_OWNER, payment.accountOwner)
            put(COLUMN_PAYMENT_CARD_NUMBER, payment.cardNumber)
            payment.cardExpiryDate?.let { put(COLUMN_PAYMENT_CARD_EXPIRY_DATE, it.time) }
            put(COLUMN_PAYMENT_SECURITY_CODE, payment.securityCode)
            payment.birthDate?.let { put(COLUMN_PAYMENT_BIRTHDATE, it.time) }
            put(COLUMN_PAYMENT_ADRESS, payment.adress)
            put(COLUMN_PAYMENT_PLZ, payment.pLZ)
        }

        val whereClause =
            "$COLUMN_PAYMENT_ID = ? AND $COLUMN_PAYMENT_INVOICE_NUMBER = ? AND $COLUMN_PAYMENT_EMAIL = ?"

        val whereArgs = arrayOf(
            payment.paymentId.toString(),
            payment.invoiceNumber.toString(),
            user.emailAdress
        )

        db.update(TABLE_NAME_PAYMENTS, values, whereClause, whereArgs)
    }


    fun deleteUserSpecificPaymentById(user: UserData, paymentId: Int, invoiceNumber: Int) {
        val db = writableDatabase
        db.beginTransaction()
        try {
            /**
            Payment mit bestimmter ID löschen (für aktuellen Nutzer und Invoice)
             */
            val paymentWhereClause =
                "$COLUMN_PAYMENT_EMAIL = ? AND $COLUMN_PAYMENT_ID = ? AND $COLUMN_PAYMENT_INVOICE_NUMBER = ?"
            val paymentWhereArgs =
                arrayOf(user.emailAdress, paymentId.toString(), invoiceNumber.toString())
            val deletedRowCount =
                db.delete(TABLE_NAME_PAYMENTS, paymentWhereClause, paymentWhereArgs)
            if (deletedRowCount > 0) {
                /**
                Payment wurde erfolgreich gelöscht
                 */
            } else {
                /**
                Payment nicht gefunden oder Löschen fehlgeschlagen
                 */
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun addNewPaymentToUserProfile(userData: UserData, payment: Payment) {
        val db = writableDatabase

        db.beginTransaction()

        try {
            val paymentValues = ContentValues().apply {
                put(COLUMN_PAYMENT_EMAIL, userData.emailAdress)
                put(COLUMN_PAYMENT_ID, payment.paymentId)
                put(COLUMN_PAYMENT_NAME, payment.paymentName)
                put(COLUMN_PAYMENT_IMAGE, payment.paymentImage)
                put(COLUMN_PAYMENT_SELECTED, if (payment.isSelected) 1 else 0)
                put(COLUMN_PAYMENT_ACCOUNT_OWNER, payment.accountOwner)
                put(COLUMN_PAYMENT_CARD_NUMBER, payment.cardNumber)
                put(COLUMN_PAYMENT_CARD_EXPIRY_DATE, formatDateToString(payment.cardExpiryDate!!))
                put(COLUMN_PAYMENT_SECURITY_CODE, payment.securityCode)
                put(COLUMN_PAYMENT_BIRTHDATE, formatDateToString(payment.birthDate!!))
                put(COLUMN_PAYMENT_ADRESS, payment.adress)
                put(COLUMN_PAYMENT_PLZ, payment.pLZ)
            }

            db.insertWithOnConflict(
                TABLE_NAME_PAYMENTS,
                null,
                paymentValues,
                SQLiteDatabase.CONFLICT_REPLACE
            )

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    /* private fun getDateFromCursor(cursor: Cursor, columnIndex: Int): Date? {
        val dateString = cursor.getString(columnIndex)
        return dateString?.let {
            val dateFormatPattern = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(dateFormatPattern)
            dateFormat.parse(dateString)
        }

    }*/
    private fun getDateFromCursor(cursor: Cursor, columnIndex: Int): Date? {
        val dateString = cursor.getString(columnIndex)
        return dateString?.let {
            val dateFormatPattern = "yyyy-MM-dd"
            val inputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())

            try {
                val parsedDate = inputDateFormat.parse(dateString)
                val formattedDate = outputDateFormat.format(parsedDate)
                outputDateFormat.parse(formattedDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                null
            }
        }
    }


    private fun formatDateToString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    fun convertDateStringToSqlDate(dateString: String): java.sql.Date? {
        if (dateString.isNotEmpty()) {
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateFormatter.timeZone = TimeZone.getDefault()

            try {
                val parsedDate = dateFormatter.parse(dateString)
                return java.sql.Date(parsedDate.time)
            } catch (e: ParseException) {
                // Log the error or handle it appropriately
                e.printStackTrace()
            }
        }
        return null
    }

    /**
    Diese Methode wird in PaymentFragment verwendet, um den Datumstyp von String in java.sql.Date?
    zu konvertieren.
     */
    fun convertDateStringToSqlDateFormat(dateString: String): java.sql.Date? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getDefault()
        try {
            val parsedDate = dateFormat.parse(dateString)
            return java.sql.Date(parsedDate.time)

        } catch (e: ParseException) {
            e.printStackTrace()

        }
        return null
    }
    fun getUserFavorites(userData: UserData): MutableList<Game> {
        val db = readableDatabase
        val selection = "$COLUMN_GAME_EMAIL = ?"
        val selectionArgs = arrayOf(userData.emailAdress)
        val cursor = db.query(
            TABLE_NAME_FAVOURITES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val games = mutableListOf<Game>()
        if (cursor != null) {
            while (cursor.moveToNext()){
                val gameIdIndex = cursor.getColumnIndex(COLUMN_GAME_ID)
                val gameTitleIndex = cursor.getColumnIndex(COLUMN_GAME_TITLE)
                val gameUrlIndex = cursor.getColumnIndex(COLUMN_GAME_URL)
                val gameImageIndex = cursor.getColumnIndex(COLUMN_GAME_IMAGE)
                if (gameIdIndex != -1 && gameUrlIndex != -1 && gameImageIndex != -1
                    && gameTitleIndex != -1
                ){
                    val gameId = cursor.getLong(gameIdIndex)
                    val gameUrl = cursor.getString(gameUrlIndex)
                    val gameImage = cursor.getString(gameImageIndex)
                    val gameTitle = cursor.getString(gameTitleIndex)
                    val game = Game(
                        gameId,
                        gameTitle,
                        gameImage,
                        "",
                        gameUrl,
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        false

                    )
                    games.add(game)
                }
            }
        }else{

        }
        cursor.close()
        db.close()

        return games

    }

}







