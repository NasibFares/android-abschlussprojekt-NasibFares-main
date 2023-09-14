package com.example.zuhauselernen.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentPaymentBinding
import java.util.Calendar
import java.util.Locale


class PaymentFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentPaymentBinding
    private lateinit var userProfile: UserProfile
    private lateinit var viewModel: SharedViewModel
    private lateinit var currentUser: UserData
    private var userEmailPayment = ""
    private var paymentMethod = ""
    private var paymentId = 0
    private var isManualDateEntry = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailPayment = it.getString("userEmailPayment")!!
            paymentMethod = it.getString("paymentMethod")!!
            paymentId = it.getInt("paymentId")
        }
    }
    /**
    Diese Methode ist für die Erstellung und Rückgabe der Ansichtshierarchie des Fragments verantwortlich.
    Das Bindungsobjekt wird mithilfe der Datenbindungsbibliothek initialisiert, um das in
    R.layout.fragment_paid definierte Layout zu erweitern.
    Der Titel (tvPaymentTitle) der Zahlung wird basierend auf dem Wert von paymentMethod festgelegt.
    Die progressBar und andere UI-Elemente werden durch Verweisen auf die entsprechenden Ansichten
    im erweiterten Layout initialisiert.
    Für verschiedene UI-Elemente werden Click-Listener und andere Event-Handler eingerichtet.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        binding.tvPaymentTitle.text = paymentMethod
        progressBar = binding.progressBar
        /**
        Wenn der Benutzer auf das Texteingabefeld für das Ablaufdatum klickt, wird die Funktion
        showDatePicker() aufgerufen. Dies ermöglicht es dem Benutzer, das Datum auszuwählen.
         */
        binding.textInputCardExpiryDate.setOnClickListener {
            showDatePicker()
        }
        /**
        Hier wird ein FocusChangeListener zum textInputCardExpiryDate hinzugefügt. Wenn das
        Texteingabefeld den Fokus erhält, wird die showDatePicker()-Funktion aufgerufen. Dies
        ermöglicht es dem Benutzer auch, das Datum auszuwählen, indem er das Textfeld aktiviert.
         */
        binding.textInputCardExpiryDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        /**
        Hier wird ein Textänderungs-Listener (Teil der Android Data Binding Library) zum
        textInputCardExpiryDate hinzugefügt.
        Die übergebene Lambda-Funktion wird aufgerufen, nachdem sich der Text im Eingabefeld
        geändert hat.
        In dieser Funktion wird überprüft, ob der eingegebene Text ein formatiertes Datum
        ist (z. B. "MM/YY"). Je nachdem, ob das Datum formatiert ist oder nicht,wird der Eingabetyp
        des Textfelds entsprechend geändert.
         */
        binding.textInputCardExpiryDate.doAfterTextChanged { text ->
            val isDateFormatted = isDateFormatted(text.toString())
            binding.textInputCardExpiryDate.inputType =
                    /**
                    Wenn das Datum formatiert ist (isDateFormatted ist true), wird der Eingabetyp
                    auf InputType.TYPE_NULL gesetzt. Dadurch wird die Tastatur deaktiviert und der
                    Benutzer kann nicht direkt Text eingeben.
                    Wenn das Datum nicht formatiert ist (isDateFormatted ist false), wird der
                    Eingabetyp auf InputType.TYPE_CLASS_TEXT gesetzt, was die Tastatur aktiviert und
                    dem Benutzer die Eingabe von Text ermöglicht.
                     */
                if (isDateFormatted) android.text.InputType.TYPE_NULL else android.text.InputType.TYPE_CLASS_TEXT
        }
        /**
        Der gleiche Prozess wird für den textInputBirthDate wiederholt.
         */
        binding.textInputBirthDate.setOnClickListener {
            showDatePicker()
        }
        binding.textInputBirthDate.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        binding.textInputBirthDate.doAfterTextChanged { text ->
            val isDateFormatted = isDateFormatted(text.toString())
            binding.textInputBirthDate.inputType =
                if (isDateFormatted) android.text.InputType.TYPE_NULL else android.text.InputType.TYPE_CLASS_TEXT
        }

        return binding.root
    }

    /**
    Diese Methode zeigt einen Fortschrittsbalken für eine bestimmte Dauer an und simuliert den
    Fortschritt, indem der Fortschrittsbalken im Laufe der Zeit aktualisiert wird.
     */
    private fun showProgressBarForSomeTime() {
        /**
        Die Sichtbarkeit des Fortschrittsbalkens wird auf View.VISIBLE gesetzt, um ihn sichtbar zu machen.
         */
        binding.progressBar.visibility = View.VISIBLE
        /**
        Mit „delayMillis“ wird eine Verzögerung von 3000 Millisekunden (3 Sekunden) eingestellt.
         */
        val delayMillis = 3000L

        /**
        Der maxProgress ist auf 100 gesetzt, was bedeutet, dass der Fortschrittsbalken 100 % erreicht.
         */
        val maxProgress = 100

        /**
        Eine Fortschrittsvariable wird initialisiert, um den Fortschrittswert zu verfolgen.
         */
        var progress = 0
        /**
        Ein Handler wird verwendet, um eine verzögerte Aufgabe zu veröffentlichen, die den
        Fortschrittsbalken verbirgt, die Schaltfläche zum Beenden der Zahlung aktiviert und nach der
        angegebenen Verzögerung zu einem anderen Fragment navigiert.
         */
        Handler().postDelayed({
            binding.progressBar.visibility = View.GONE
            binding.btnPaymentFinish.isEnabled = true

            navigateToSecondFragment()
        }, delayMillis)
        /**
        Das progressUpdateInterval wird basierend auf der Verzögerung und dem maxProgress berechnet.
         */
        val progressUpdateInterval = delayMillis / maxProgress

        /**
        Ein Runnable wird definiert, um den Fortschritts- und Prozenttext des Fortschrittsbalkens im
        Laufe der Zeit zu aktualisieren.
         */
        val runnable = object : Runnable {
            override fun run() {
                /**
                Es verwendet eine Schleife, um den Fortschritt schrittweise zu erhöhen und die
                Benutzeroberfläche entsprechend zu aktualisieren.
                 */
                if (progress <= maxProgress) {
                    progressBar.progress = progress
                    binding.percentageText.text = "$progress%"
                    progress++
                    Handler().postDelayed(this, progressUpdateInterval)
                }
            }
        }
        Handler().post(runnable)
    }

    /**
    Diese Methode verwendet die Android-Navigationskomponente, um vom aktuellen Fragment zu einem
    anderen Fragment zu navigieren.
    Die Funktion findNavController() wird verwendet, um den Navigationscontroller abzurufen, der
    dem aktuellen Fragment zugeordnet ist.
    Die Navigationsfunktion wird mit der entsprechenden im Navigationsdiagramm definierten Aktion
    aufgerufen, wobei userEmailUserSubjects als Argument übergeben wird.
     */
    private fun navigateToSecondFragment() {
        findNavController().navigate(
            PaymentFragmentDirections.actionPaymentFragmentToUserSubjectsFragment(
                userEmailUserSubjects = userEmailPayment
            )
        )
    }

    /**
    Diese Methode ist für die Anzeige eines Datumsauswahldialogs verantwortlich, wenn der Benutzer
    mit dem Feld 'editTextExpiryDate' interagiert.
     */
    private fun showDatePicker() {
        isManualDateEntry = !isManualDateEntry
        binding.textInputCardExpiryDate.inputType =
            if (isManualDateEntry) android.text.InputType.TYPE_CLASS_TEXT else android.text.InputType.TYPE_NULL
        binding.textInputBirthDate.inputType =
            if (isManualDateEntry) android.text.InputType.TYPE_CLASS_TEXT else android.text.InputType.TYPE_NULL
        /**
        Der Eingabetyp von 'editTextExpiryDate' ist auf android.text.InputType.TYPE_NULL festgelegt,
        wodurch verhindert wird, dass die Softtastatur angezeigt wird, wenn auf das Feld geklickt wird.
         */
        if (!isManualDateEntry) {
            binding.textInputCardExpiryDate.inputType = android.text.InputType.TYPE_NULL
            binding.textInputBirthDate.inputType = android.text.InputType.TYPE_NULL
        }
        /**
        Die Methode beginnt mit dem Abrufen der aktuellen Instanz der Calendar-Klasse, um das
        aktuelle Datum abzurufen.
         */
        val calendar = Calendar.getInstance()

        /**
        Es ruft das aktuelle Jahr,
         */
        val year = calendar.get(Calendar.YEAR)

        /**
        den aktuellen Monat
         */
        val month = calendar.get(Calendar.MONTH)

        /**
        und den Tag des Monats aus der Calendar-Instanz ab.
         */
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        /**
        Mithilfe dieser Werte wird ein DatePickerDialog erstellt und mit einem Rückruf konfiguriert,
        wenn der Benutzer ein Datum auswählt. Als Parameter erhält der Callback das ausgewählte Jahr,
        den Monat und den Tag.
         */
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                /**
                Innerhalb des Rückrufs werden die ausgewählten Datumskomponenten verwendet, um das
                Datum mithilfe von String.format im Format „TT/MM/JJJJ“ zu formatieren.
                 */
                val formattedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                /**
                Dieses formatierte Datum wird dann als Text für das Feld editTextExpiryDate festgelegt.
                 */
                binding.textInputCardExpiryDate.setText(formattedDate)
                binding.textInputBirthDate.setText(formattedDate)
                /**
                Schließlich wird der Eingabetyp von editTextExpiryDate auf
                android.text.InputType.TYPE_CLASS_TEXT zurückgesetzt, um die Texteingabe zu ermöglichen.
                 */
                binding.textInputCardExpiryDate.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT
                binding.textInputBirthDate.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT
            },
            year, month, dayOfMonth
        )
        /**
        Anschließend wird dem Benutzer der datePickerDialog angezeigt.
         */
        datePickerDialog.show()
    }

    /**
    Diese Methode prüft, ob eine bestimmte Datumszeichenfolge korrekt als „TT/MM/JJJJ“ formatiert ist.
     */
    private fun isDateFormatted(dateText: String): Boolean {
        /**
        Es verwendet einen Try-Block, um zu versuchen, den bereitgestellten dateText mithilfe eines
        SimpleDateFormat mit dem Format „TT/MM/JJJJ“ zu analysieren.
         */
        return try {
            /**
            Wenn die Analyse erfolgreich ist und keine Ausnahmen ausgelöst werden, gibt die Methode
            „true“ zurück, was darauf hinweist, dass das Datum korrekt formatiert ist.
             */
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            format.parse(dateText)
            true
            /**
            Wenn beim Parsen eine Ausnahme auftritt, wird der Catch-Block ausgelöst und die Methode
            gibt „false“ zurück, was darauf hinweist, dass das Datum nicht korrekt formatiert ist.
             */
        } catch (e: Exception) {
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
        Das userProfile wird mithilfe der UserProfile-Klasse initialisiert, die die benutzerbezogene
        Daten und Interaktionen verwaltet.
         */
        userProfile = UserProfile(requireContext().applicationContext)
        /**
        currentPayment wird mithilfe der Methode getLastUserPaymentByEmail aus der letzten Zahlung
        des Benutzers abgerufen.
         */
        var currentPayment = userProfile.getLastUserPaymentByEmail(userEmailPayment)
        /**
        Das viewModel wird mit dem ViewModelProvider initialisiert, um auf die SharedViewModel-Klasse
        zuzugreifen.
         */
        viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        /**
        Verschiedene UI-Elemente werden mit Informationen aktualisiert, z. B. dem Festlegen
        - der E-Mail-Adresse des Benutzers,
        - dem Titel der Zahlungsmethode
        - und der Anzeige eines Zahlungsbilds basierend auf der Zahlungs-ID.
         */
        binding.tvUserEmailPayment.text = userEmailPayment
        binding.tvPaymentTitle.text = paymentMethod
        val paymentImage = viewModel.getPaymentImage(paymentId, requireContext())
        binding.ivPaymentMethod.setImageDrawable(paymentImage)
        /**
        Das Profil des aktuellen Nutzers abrufen.
         */
        currentUser = userProfile.getUserProfileByEmail(userEmailPayment)!!
        /**
        Die aktuelle (Subjects) Liste des aktuellen Nutzers abrufen.
         */
        val currentUserSubjects = userProfile.getUserSubjectsByEmail(userEmailPayment)

        /**
        Die Aktuelle Zahlungsmethode des Nutzer von Database abrufen.
         */
        val actualUserPayment = userProfile.getLastUserPaymentByEmail(userEmailPayment)
        /**
        Wenn die tatsächliche Zahlungsmethode des Benutzers (actualUserPayment) nicht null ist und
        eine bestimmte Zahlungs-ID (3) hat, wird die Benutzeroberfläche so angepasst, dass relevante
        Informationen zu dieser Zahlungsmethode angezeigt werden, z. B. das Ändern von Bezeichnungen
        und das Ausblenden sicherheitsrelevanter Ansichten.
         */
        if (actualUserPayment != null) {
            when (actualUserPayment.paymentId) {
                /**
                Klarna
                 */
                2 -> {
                    binding.tvCardOwnerLabel.hint = "Konto Inhaber"
                    binding.tvCardNumberLabel.visibility = View.GONE
                    binding.tvCardExpiryDate.visibility = View.GONE
                    binding.tvCardSecurityCode.visibility = View.GONE
                    binding.tvBirthDate.visibility = View.VISIBLE
                    binding.tvAdress.visibility = View.VISIBLE
                    binding.tvPostalCode.visibility = View.VISIBLE
                }
                /**
                Bankkonto
                 */
                3 -> {
                    binding.tvCardOwnerLabel.hint = "Bankkonto Inhaber"
                    binding.tvCardNumberLabel.hint = "IBAN"
                    binding.tvCardExpiryDate.visibility = View.GONE
                    binding.tvCardSecurityCode.visibility = View.GONE
                }
                /**
                Suf Rechnung
                 */
                4 -> {
                    binding.tvCardOwnerLabel.hint = "Vor-Nachname"
                    binding.tvCardNumberLabel.visibility = View.GONE
                    binding.tvCardExpiryDate.visibility = View.GONE
                    binding.tvCardSecurityCode.visibility = View.GONE
                    binding.tvBirthDate.visibility = View.VISIBLE
                    binding.tvAdress.visibility = View.VISIBLE
                    binding.tvPostalCode.visibility = View.VISIBLE
                }
            }


        }
        /**
        Fall der Benutzer zurück zum vorherigen Fragment kommen möchte.
         */
        binding.ivBackPayment.setOnClickListener {
            /**
            Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
             */
            val lastInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailPayment)
            val lastPayment = userProfile.getLastUserPaymentByEmail(userEmailPayment)
            if (lastInvoice != null) {
                if (lastPayment != null) {
                    userProfile.deleteUserSpecificPaymentById(
                        currentUser,
                        lastPayment.paymentId,
                        lastInvoice
                    )
                }
            }
            /**
            Die letzte erstellte Rechnung wird gelöscht und neue Rechnung wird erstellt.
            [Die Aktion,die neue Rechnung erstellt, wird im Fragment Subscription innerhalb dem
            Block onViewCreated ausgeführt]
             */
            userProfile.deleteLastAddedInvoice(userEmailPayment)
            /**
            Die Fächer, die im Fragment Subscription schon ausgewählt sind werden gelöscht.
             */
            currentUserSubjects.let {
                for (i in currentUserSubjects.lastIndex downTo 2) {
                    currentUserSubjects[i].isMonthlySubscribed = false
                    currentUserSubjects[i].isYearlySubscribed = false
                    userProfile.deleteUserSubjectAtIndex(currentUser, i)

                }
            }

            findNavController().navigate(
                PaymentFragmentDirections.actionPaymentFragmentToSubscriptionFragment(
                    userEmailSubscription = userEmailPayment
                )
            )
        }
        binding.btnPaymentFinish.setOnClickListener {
            val cardOwner = binding.textInputCardOwner.text.toString()
            val cardNumber = binding.textInputCardNumber.text.toString()
            val expiryDate = binding.textInputCardExpiryDate.text.toString()
            val securityCode = binding.textInputCardSecurityCode.text.toString()
            val birthDate = binding.textInputBirthDate.text.toString()
            val adress = binding.textInputAdress.text.toString()
            val postalCode = binding.textInputPostalCode.text.toString()


            if (currentPayment != null) {
                when (currentPayment.paymentId) {
                    1 -> {
                        if (cardOwner.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || securityCode.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Füllen Sie bitte alle Daten aus!!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            currentPayment.accountOwner = cardOwner
                            currentPayment.cardNumber = cardNumber
                            currentPayment.cardExpiryDate =
                                userProfile.convertDateStringToSqlDateFormat(expiryDate)
                            currentPayment.securityCode = securityCode
                            userProfile.updateUserSpecificPayment(currentUser, currentPayment)
                            showProgressBarForSomeTime()
                        }
                    }

                    2 -> {
                        if (cardOwner.isEmpty() || birthDate.isEmpty() || adress.isEmpty() || postalCode.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Füllen Sie bitte alle Daten aus!!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            currentPayment.accountOwner = cardOwner
                            currentPayment.birthDate =
                                userProfile.convertDateStringToSqlDateFormat(birthDate)
                            currentPayment.adress = adress
                            currentPayment.pLZ = postalCode
                            userProfile.updateUserSpecificPayment(currentUser, currentPayment)
                            showProgressBarForSomeTime()
                        }
                    }

                    3 -> {
                        if (cardOwner.isEmpty() || cardNumber.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Füllen Sie bitte alle Daten aus!!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            currentPayment.accountOwner = cardOwner
                            currentPayment.cardNumber = cardNumber
                            userProfile.updateUserSpecificPayment(currentUser, currentPayment)
                            showProgressBarForSomeTime()
                        }
                    }

                    4 -> {
                        if (cardOwner.isEmpty() || birthDate.isEmpty() || adress.isEmpty() || postalCode.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Füllen Sie bitte alle Daten aus!!",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {
                            currentPayment.accountOwner = cardOwner
                            currentPayment.birthDate =
                                userProfile.convertDateStringToSqlDateFormat(birthDate)
                            currentPayment.adress = adress
                            currentPayment.pLZ = postalCode
                            userProfile.updateUserSpecificPayment(currentUser, currentPayment)
                            showProgressBarForSomeTime()
                        }
                    }

                }
            }
        }
        binding.bottomNavViewUserPayment.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home->{
                    /**
                    Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
                     */
                    val lastInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailPayment)
                    val lastPayment = userProfile.getLastUserPaymentByEmail(userEmailPayment)
                    if (lastInvoice != null) {
                        if (lastPayment != null) {
                            userProfile.deleteUserSpecificPaymentById(
                                currentUser,
                                lastPayment.paymentId,
                                lastInvoice
                            )
                        }
                    }
                    /**
                    Die letzte erstellte Rechnung wird gelöscht und neue Rechnung wird erstellt.
                    [Die Aktion,die neue Rechnung erstellt, wird im Fragment Subscription innerhalb dem
                    Block onViewCreated ausgeführt]
                     */
                    userProfile.deleteLastAddedInvoice(userEmailPayment)
                    /**
                    Die Fächer, die im Fragment Subscription schon ausgewählt sind werden gelöscht.
                     */
                    currentUserSubjects.let {
                        for (i in currentUserSubjects.lastIndex downTo 2) {
                            currentUserSubjects[i].isMonthlySubscribed = false
                            currentUserSubjects[i].isYearlySubscribed = false
                            userProfile.deleteUserSubjectAtIndex(currentUser, i)

                        }
                    }

                    findNavController().navigate(
                        PaymentFragmentDirections.actionPaymentFragmentToHomeFragment(

                        )
                    )
                    true
                }
                R.id.logout->{
                    /**
                    Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
                     */
                    val lastInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailPayment)
                    val lastPayment = userProfile.getLastUserPaymentByEmail(userEmailPayment)
                    if (lastInvoice != null) {
                        if (lastPayment != null) {
                            userProfile.deleteUserSpecificPaymentById(
                                currentUser,
                                lastPayment.paymentId,
                                lastInvoice
                            )
                        }
                    }
                    /**
                    Die letzte erstellte Rechnung wird gelöscht und neue Rechnung wird erstellt.
                    [Die Aktion,die neue Rechnung erstellt, wird im Fragment Subscription innerhalb dem
                    Block onViewCreated ausgeführt]
                     */
                    userProfile.deleteLastAddedInvoice(userEmailPayment)
                    /**
                    Die Fächer, die im Fragment Subscription schon ausgewählt sind werden gelöscht.
                     */
                    currentUserSubjects.let {
                        for (i in currentUserSubjects.lastIndex downTo 2) {
                            currentUserSubjects[i].isMonthlySubscribed = false
                            currentUserSubjects[i].isYearlySubscribed = false
                            userProfile.deleteUserSubjectAtIndex(currentUser, i)

                        }
                    }

                    activity?.finishAffinity()
                    true
                }


                else->{
                    false
                }
            }
        }
    }


}
