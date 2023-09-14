package com.example.zuhauselernen.ui

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentUserPaymentBinding
import java.util.Calendar
import java.util.Locale


class UserPaymentFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var binding: FragmentUserPaymentBinding
    private lateinit var userProfile: UserProfile

    private lateinit var currentUser: UserData
    private var userEmailUserPayment = ""
    private var paymentMethodUserPayment = ""
    private var paymentIdUserPayment = 0
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userEmailUserPayment = it.getString("userEmailUserPayment")!!
            paymentMethodUserPayment = it.getString("paymentMethodUserPayment")!!
            paymentIdUserPayment = it.getInt("paymentIdUserPayment")
        }

    }
    /**
    Diese Methode ist für die Erstellung und Rückgabe der Ansichtshierarchie des Fragments verantwortlich.
    Das Bindungsobjekt wird mithilfe der Datenbindungsbibliothek initialisiert, um das in
    R.layout.fragment_paid definierte Layout zu erweitern.
    Der Titel (tvPaymentTitle) der Zahlung wird basierend auf dem Wert von paymentMethod festgelegt.
    Die progressBar und andere UI-Elemente werden durch Verweisen auf die entsprechenden Ansichten im erweiterten Layout initialisiert.
    Für verschiedene UI-Elemente werden Click-Listener und andere Event-Handler eingerichtet.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_payment, container, false)
        binding.tvPaymentTitleUserPayment.text = paymentMethodUserPayment
        progressBar = binding.progressBarUserPayment

        binding.textInputCardExpiryDateUserPayment.setOnClickListener {
            showDatePicker()
        }
        binding.textInputCardExpiryDateUserPayment.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        binding.textInputCardExpiryDateUserPayment.doAfterTextChanged { text ->
            val isDateFormatted = isDateFormatted(text.toString())
            binding.textInputCardExpiryDateUserPayment.inputType =
                if (isDateFormatted) android.text.InputType.TYPE_NULL else android.text.InputType.TYPE_CLASS_TEXT
        }
        binding.textInputBirthDateUserPayment.setOnClickListener {
            showDatePicker()
        }
        binding.textInputBirthDateUserPayment.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
        binding.textInputBirthDateUserPayment.doAfterTextChanged { text ->
            val isDateFormatted = isDateFormatted(text.toString())
            binding.textInputBirthDateUserPayment.inputType =
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
        binding.progressBarUserPayment.visibility = View.VISIBLE
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
            binding.progressBarUserPayment.visibility = View.GONE
            binding.btnPaymentFinishUserPayment.isEnabled = true

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
                    binding.percentageTextUserPayment.text = "$progress%"
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
            UserPaymentFragmentDirections.actionUserPaymentFragmentToUserSubjectsFragment(
                userEmailUserSubjects = userEmailUserPayment
            )
        )
    }
    /**
    Diese Methode ist für die Anzeige eines Datumsauswahldialogs verantwortlich, wenn der Benutzer
    mit dem Feld 'editTextExpiryDate' interagiert.
     */
    private fun showDatePicker() {
        /**
        Der Eingabetyp von 'editTextExpiryDate' ist auf android.text.InputType.TYPE_NULL festgelegt,
        wodurch verhindert wird, dass die Softtastatur angezeigt wird, wenn auf das Feld geklickt wird.
         */
        binding.textInputCardExpiryDateUserPayment.inputType = InputType.TYPE_NULL
        binding.textInputBirthDateUserPayment.inputType = InputType.TYPE_NULL
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
                /**
                Innerhalb des Rückrufs werden die ausgewählten Datumskomponenten verwendet, um das
                Datum mithilfe von String.format im Format „TT/MM/JJJJ“ zu formatieren.
                 */
                val formattedDate =
                    String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                /**
                Dieses formatierte Datum wird dann als Text für das Feld editTextExpiryDate festgelegt.
                 */
                /**
                Dieses formatierte Datum wird dann als Text für das Feld editTextExpiryDate festgelegt.
                 */
                binding.textInputCardExpiryDateUserPayment.setText(formattedDate)
                binding.textInputBirthDateUserPayment.setText(formattedDate)
                /**
                Schließlich wird der Eingabetyp von editTextExpiryDate auf
                android.text.InputType.TYPE_CLASS_TEXT zurückgesetzt, um die Texteingabe zu ermöglichen.
                 */
                /**
                Schließlich wird der Eingabetyp von editTextExpiryDate auf
                android.text.InputType.TYPE_CLASS_TEXT zurückgesetzt, um die Texteingabe zu ermöglichen.
                 */
                binding.textInputCardExpiryDateUserPayment.inputType =
                    InputType.TYPE_CLASS_TEXT
                binding.textInputBirthDateUserPayment.inputType =
                    InputType.TYPE_CLASS_TEXT
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
        Das viewModel wird mit dem ViewModelProvider initialisiert, um auf die SharedViewModel-Klasse
        zuzugreifen.
         */
        //viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val tempList = viewModel.tempList
        println("TempList in PaymentFragment is: $tempList")
        println("TempList in ViewModel: ${viewModel.tempList}")
        /**
        Das userProfile wird mithilfe der UserProfile-Klasse initialisiert, die die benutzerbezogene
        Daten und Interaktionen verwaltet.
         */
        userProfile = UserProfile(requireContext().applicationContext)
        /**
        currentPayment wird mithilfe der Methode getLastUserPaymentByEmail aus der letzten Zahlung
        des Benutzers abgerufen.
         */
        var currentPayment = userProfile.getLastUserPaymentByEmail(userEmailUserPayment)

        /**
        Verschiedene UI-Elemente werden mit Informationen aktualisiert, z. B. dem Festlegen
        - der E-Mail-Adresse des Benutzers,
        - dem Titel der Zahlungsmethode
        - und der Anzeige eines Zahlungsbilds basierend auf der Zahlungs-ID.
         */
        binding.tvUserEmailPaymentUserPayment.text = userEmailUserPayment
        binding.tvPaymentTitleUserPayment.text = paymentMethodUserPayment
        val paymentImage = viewModel.getPaymentImage(paymentIdUserPayment, requireContext())
        binding.ivPaymentMethodUserPayment.setImageDrawable(paymentImage)
        /**
        Das Profil des aktuellen Nutzers abrufen.
         */
        currentUser = userProfile.getUserProfileByEmail(userEmailUserPayment)!!
        /**
        Die aktuelle (Subjects) Liste des aktuellen Nutzers abrufen.
         */
        userProfile.getUserSubjectsByEmail(userEmailUserPayment)

        /**
        Die Aktuelle Zahlungsmethode des Nutzer von Database abrufen.
         */
        val actualUserPayment = userProfile.getLastUserPaymentByEmail(userEmailUserPayment)
        /**
        Wenn die tatsächliche Zahlungsmethode des Benutzers (actualUserPayment) nicht null ist und
        eine bestimmte Zahlungs-ID (3) hat, wird die Benutzeroberfläche so angepasst, dass relevante
        Informationen zu dieser Zahlungsmethode angezeigt werden, z. B. das Ändern von Bezeichnungen
        und das Ausblenden sicherheitsrelevanter Ansichten.
         */
        if (actualUserPayment != null) {
            when (actualUserPayment.paymentId) {

                2 -> {
                    binding.tvCardOwnerLabelUserPayment.hint = "Konto Inhaber"
                    binding.tvCardNumberLabelUserPayment.visibility = View.GONE
                    binding.tvCardExpiryDateUserPayment.visibility = View.GONE
                    binding.tvCardSecurityCodeUserPayment.visibility = View.GONE
                    binding.tvBirthDateUserPayment.visibility = View.VISIBLE
                    binding.tvAdressUserPayment.visibility = View.VISIBLE
                    binding.tvPostalCodeUserPayment.visibility = View.VISIBLE
                }

                3 -> {
                    binding.tvCardOwnerLabelUserPayment.hint = "Bankkonto Inhaber"
                    binding.tvCardNumberLabelUserPayment.hint = "IBAN"
                    binding.tvCardExpiryDateUserPayment.visibility = View.GONE
                    binding.tvCardSecurityCodeUserPayment.visibility = View.GONE
                }

                4 -> {
                    binding.tvCardOwnerLabelUserPayment.hint="Vor-Nachname"
                    binding.tvCardNumberLabelUserPayment.visibility=View.GONE
                    binding.tvCardExpiryDateUserPayment.visibility=View.GONE
                    binding.tvCardSecurityCodeUserPayment.visibility=View.GONE
                    binding.tvBirthDateUserPayment.visibility=View.VISIBLE
                    binding.tvAdressUserPayment.visibility=View.VISIBLE
                    binding.tvPostalCodeUserPayment.visibility=View.VISIBLE
                }
            }
            /**
            Fall der Benutzer zurück zum vorherigen Fragment kommen möchte.
             */
            binding.ivBackUserPayment.setOnClickListener {
                /**
                Die letzte Rechnung der aktuellen Nutzer und zugehörige Zahlungsmethode von der
                Datenbank abrufen
                 */
                val lastInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailUserPayment)
                val lastPayment = userProfile.getLastUserPaymentByEmail(userEmailUserPayment)

                /**
                Die aktuelle SubjectsList von der Datenbank abrufen, sie wird gefiltert um die
                SubjectsList, die beim UserSubscriptionFragment hinzugefügt ist, herauszufinden.
                 */
                val userSubjects = userProfile.getUserSubjectsByEmail(userEmailUserPayment)
                val filteredList = userSubjects.filter { subject ->
                    viewModel.tempList.any { tempSubject ->
                        tempSubject.subjectId == subject.subjectId
                    }
                }
                /**
                Diese Liste wird von der Datenbank des aktuellen Nutzers gelöscht.
                 */
                userProfile.deleteListOfSubjectsFromUserProfile(currentUser, filteredList)
                /**
                Sowohl die letzte Zahlungsmethode als auch die letzte Rechnung werden gelöscht.
                 */
                if (lastPayment != null) {
                    if (lastInvoice != null) {
                        userProfile.deleteUserSpecificPaymentById(
                            currentUser,
                            lastPayment.paymentId,
                            lastInvoice
                        )
                    }
                }
                userProfile.deleteLastAddedInvoice(userEmailUserPayment)
                /**
                Die tempList wird geleert.
                 */
                viewModel.tempList.clear()
                /**
                Es wird zurück zum UserSubscriptionFragment navigiert.
                 */
                findNavController().navigate(
                    UserPaymentFragmentDirections.actionUserPaymentFragmentToUserSubscriptionFragment(
                        userEmailUserSubscription = userEmailUserPayment
                    )
                )
            }
            /**
            Fall der Benutzer Home oder Logout auswählen möchte.
             */
            binding.bottomNavViewUserPayment.setOnItemSelectedListener { item ->
                val lastInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailUserPayment)
                val lastPayment = userProfile.getLastUserPaymentByEmail(userEmailUserPayment)
                val userSubjects = userProfile.getUserSubjectsByEmail(userEmailUserPayment)
                val filteredList = userSubjects.filter { subject ->
                    viewModel.tempList.any { tempSubject ->
                        tempSubject.subjectId == subject.subjectId
                    }
                }
                when (item.itemId) {
                    R.id.home -> {
                        userProfile.deleteListOfSubjectsFromUserProfile(currentUser, filteredList)
                        if (lastPayment != null) {
                            if (lastInvoice != null) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    lastPayment.paymentId,
                                    lastInvoice
                                )
                            }
                        }
                        if (lastPayment != null) {
                            if (lastInvoice != null) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    lastPayment.paymentId,
                                    lastInvoice
                                )
                            }
                        }
                        userProfile.deleteLastAddedInvoice(userEmailUserPayment)
                        viewModel.tempList.clear()
                        findNavController().navigate(UserPaymentFragmentDirections.actionUserPaymentFragmentToHomeFragment())
                    }

                    R.id.logout -> {
                        userProfile.deleteListOfSubjectsFromUserProfile(currentUser, filteredList)
                        if (lastPayment != null) {
                            if (lastInvoice != null) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    lastPayment.paymentId,
                                    lastInvoice
                                )
                            }
                        }
                        if (lastPayment != null) {
                            if (lastInvoice != null) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    lastPayment.paymentId,
                                    lastInvoice
                                )
                            }
                        }
                        userProfile.deleteLastAddedInvoice(userEmailUserPayment)
                        viewModel.tempList.clear()
                        requireActivity().finishAffinity()
                    }
                }
                true

            }
            binding.btnPaymentFinishUserPayment.setOnClickListener {
                val cardOwner = binding.textInputCardOwnerUserPayment.text.toString()
                val cardNumber = binding.textInputCardNumberUserPayment.text.toString()
                val expiryDate = binding.textInputCardExpiryDateUserPayment.text.toString()
                val securityCode = binding.textInputCardSecurityCodeUserPayment.text.toString()
                val birthDate=binding.textInputBirthDateUserPayment.text.toString()
                val adress=binding.textInputAdressUserPayment.text.toString()
                val postalCode=binding.textInputPostalCodeUserPayment.text.toString()
                if (currentPayment != null) {

                    currentPayment.accountOwner = cardOwner
                    currentPayment.cardNumber = cardNumber
                    currentPayment.cardExpiryDate =
                        userProfile.convertDateStringToSqlDate(expiryDate)
                    currentPayment.securityCode = securityCode
                    currentPayment.birthDate=userProfile.convertDateStringToSqlDateFormat(birthDate)
                    currentPayment.adress=adress
                    currentPayment.pLZ=postalCode
                    userProfile.updateUserSpecificPayment(currentUser, currentPayment)
                }
                viewModel.tempList.clear()
                showProgressBarForSomeTime()
            }

        }
    }
}