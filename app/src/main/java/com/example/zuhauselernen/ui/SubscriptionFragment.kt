package com.example.zuhauselernen.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.adapter.PaymentAdapter
import com.example.zuhauselernen.data.local.adapter.SubscriptionAdapter
import com.example.zuhauselernen.data.local.model.Invoice
import com.example.zuhauselernen.data.local.model.Payment
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentSubscriptionBinding
import java.sql.Date

class SubscriptionFragment : Fragment() {
    /**
    In dieser Zeile wird das FragmentSubscriptionBinding-Objekt erstellt, das für die Verbindung mit
    den in der zugehörigen XML-Datei (fragment_subscription) definierten Ansichten verwendet wird.
     */
    private lateinit var binding: FragmentSubscriptionBinding
    private var userEmailSubscription = ""
    private var userPayment = ""
    private var subjectId: Int = 0
    private var paymentId = 0
    private lateinit var userProfile: UserProfile
    private val viewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
        userEmailSubscription, subjectId und paymentId werden aus den Argumenten des Fragments extrahiert.
         */
        arguments?.let {
            userEmailSubscription = it.getString("userEmailSubscription")!!
            subjectId = it.getInt("subjectId")
            paymentId = it.getInt("paymentId")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_subscription, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfile = UserProfile(requireContext().applicationContext)
        binding.tvUserPayment.text = userPayment
        userEmailSubscription.also { binding.tvUserSubscriptionSection.text = it }

        /**
        Zahlungsmethoden und Rechnungen verwalten:
        Die Ausgewählte Zahlungsmethode und die aktuelle Rechnung werden über currentPayment und
        currentInvoice verfolgt.
        Eine neue Rechnung wird basierend auf der letzten Rechnungsnummer des Benutzers erstellt oder
        die vorhandene Rechnungsnummer wird inkrementiert.
        Bei der Auswahl oder Abwahl von Zahlungsmethoden werden die entsprechenden Aktionen im viewModel
        und im userProfile ausgeführt.
         */
        val currentPayment = Payment(
            0, "", 0, false, "",
            "", null, "", null, "", "", 0
        )

        /**
        Die letzte Rechnung des aktuellen Nutzers abrufen.
         */
        val lastUserInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailSubscription)

        /**
        Eine Variable definieren, die sich auf die aktuelle Rechnung bezieht.
         */
        val currentInvoice =
            Invoice(0, Date(System.currentTimeMillis()), mutableListOf(), currentPayment, 0.0)
        if (lastUserInvoice == null) {
            currentInvoice.invoiceNumber = 1
            userProfile.createNewInvoice(
                userEmailSubscription, currentInvoice.invoiceNumber,
                Date(System.currentTimeMillis()).toString(), 0.0
            )
            Toast.makeText(
                context,
                "Neue Rechnung wurde erstellt Rechnungsnummer:${currentInvoice.invoiceNumber} ",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            currentInvoice.invoiceNumber = lastUserInvoice + 1
            userProfile.createNewInvoice(
                userEmailSubscription, currentInvoice.invoiceNumber,
                Date(System.currentTimeMillis()).toString(), 0.0
            )
            Toast.makeText(
                context,
                "Neue Rechnung wurde erstellt Rechnungsnummer:${currentInvoice.invoiceNumber} ",
                Toast.LENGTH_SHORT
            ).show()
        }
        val currentUser = userProfile.getUserProfileByEmail(userEmailSubscription)
        if (currentUser != null) {
            binding.tvUserSubscriptionSection.text = userEmailSubscription
        }
        val currentUserSubjects = userProfile.getUserSubjectsByEmail(userEmailSubscription)
        var currentUserPayments = userProfile.getUserPaymentsByEmail(userEmailSubscription)
        /**
         Die SubjectsList wird beobachtet,und die geeigneten Aktionen werden ausgeführt
         */
        viewModel.remainingSubjectList.observe(viewLifecycleOwner) { remainingSubjectList ->
            /**
             Die Zwischensumme wird auf 0.0 gesetzt
             */
            var finalAmount = 0.0
            /**
             Der Adapter der recyclerView wird gesetzt
             */
            binding.rvSubjects.adapter = currentUser?.let {
                SubscriptionAdapter(
                    requireContext(),
                    remainingSubjectList,
                    { subject, isChecked ->
                        /**
                        Diese Methode wird von ViewModel abgerufen ,um die Situation des aktuellen
                        Fach zu setzen.
                         */
                        viewModel.onRemainingSubjectChecked(subject, isChecked)
                        /**
                        Wenn das Fach angehakt ist
                         */
                        if (isChecked) {
                            /**
                            Das Fach wird zur Liste des Nutzer hinzugefügt und die Liste in der
                            Datenbank wird aktualisiert
                             */
                            currentUserSubjects.add(subject)
                            userProfile.addNewSubjectToUserProfile(currentUser, subject)
                            userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)
                            /**
                            Wenn das Fach abgehakt wurde
                             */
                        } else {
                            /**
                            Das Fach wird von der Liste des Nutzer gelöscht und die Liste in der
                            Datenbank wird aktualisiert
                             */
                            currentUserSubjects.remove(subject)
                            userProfile.deleteUserSpecificSubjectById(
                                currentUser,
                                subject.subjectId
                            )
                            userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)

                            val subTotal = userProfile.getSubscriptionPrice(subject)
                            finalAmount -= subTotal
                            binding.tvSubTotal.text = finalAmount.toString()
                            subject.isMonthlySubscribed = false
                            subject.isYearlySubscribed = false
                        }
                    },
                    { subject ->
                        /**
                        Diese Methode wird von ViewModel abgerufen ,um die Situation des aktuellen
                        Abonnementtyp zu setzen.
                        */
                        viewModel.onRemainingSubjectSubscription(subject)
                        currentUser.let {
                            var subTotal: Double
                            finalAmount = 0.0
                            for (i in remainingSubjectList.indices) {
                                if (remainingSubjectList[i].isChecked) {
                                    subTotal =
                                        userProfile.getSubscriptionPrice(remainingSubjectList[i])
                                    finalAmount += subTotal
                                }
                                currentInvoice.invoiceSubtotal = finalAmount
                            }

                            binding.tvSubTotal.text = finalAmount.toString()
                            userProfile.updateUserSpecificSubjectSubscription(
                                currentUser,
                                subject
                            )
                        }
                    }, it, userProfile
                )
            }
        }
        userProfile.updateUserSpecificInvoiceSubtotal(currentUser!!, currentInvoice)
        /**
        Die Behandlung der Zahlungsmethoden
         */
        viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
            binding.rvPayments.adapter =
                PaymentAdapter(requireContext(), paymentList) { payment, isChecked ->
                    viewModel.onPaymentChecked(payment, isChecked)
                    currentUser.let {
                        /**
                        Wenn eine Zahlungsmethode ausgewählt ist
                         */
                        if (isChecked) {
                            /**
                            Die vorherige gewählte Zahlungsmethode wird gelöscht und eine neue
                            Zahlungsmethode wird erstellt.
                             */
                            if (paymentId != 0) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    paymentId,
                                    currentInvoice.invoiceNumber
                                )
                            }
                            userProfile.createNewPayment(
                                userEmailSubscription,
                                payment.paymentId.toString(),
                                payment.paymentName,
                                payment.paymentImage,
                                true,
                                "",
                                "",
                                null,
                                "",
                                null,
                                "",
                                "",
                                currentInvoice.invoiceNumber
                            )
                            paymentId = payment.paymentId
                            binding.tvUserPayment.text =
                                context?.getText(payment.paymentName.toInt())
                            userPayment = context?.getString(payment.paymentName.toInt()) ?: ""
                        } else {
                            /**
                            Wenn die Zahlungsmethode abgewählt ist.
                             */
                            if (paymentId == payment.paymentId) {
                                userProfile.deleteUserSpecificPaymentById(
                                    currentUser,
                                    paymentId,
                                    currentInvoice.invoiceNumber
                                )
                                paymentId = 0
                                binding.tvUserPayment.text = ""
                                userPayment = ""
                            }
                        }
                    }
                }
        }
        /**
         Einstellungen für (back,home,logout,add and pay) buttons
         */
        /**
         ---------------------------------------Zurück---------------------------------------------
         */
        binding.ivBackSubscription.setOnClickListener {
            userProfile.deleteLastAddedInvoice(userEmailSubscription)
            userProfile.deleteUserSpecificPaymentById(
                currentUser,
                paymentId,
                currentInvoice.invoiceNumber
            )
            paymentId = 0
            binding.tvUserPayment.text = ""
            userPayment = ""
            currentUserSubjects.let {
                userProfile.deleteUserProfileSubjects(
                    userEmailSubscription
                )
                for (i in currentUserSubjects.indices) {
                    currentUserSubjects[i].isChecked = false
                }
            }
            findNavController().navigate(
                SubscriptionFragmentDirections.actionSubscriptionFragmentToSubjectFragment(
                    userEmailSubject = userEmailSubscription
                )
            )
        }
        /**
        ---------------------------------------Home und Logout--------------------------------------
         */
        binding.bottomNavViewSubscription.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    /**
                     Die letzte erstellte Rechnung und die zugehörige Zahlungsmethode (fall gewählt)
                     werden von der Datenbank gelöscht.
                     */
                    userProfile.deleteLastAddedInvoice(userEmailSubscription)
                    userProfile.deleteUserSpecificPaymentById(
                        currentUser,
                        paymentId,
                        currentInvoice.invoiceNumber
                    )
                    /**
                     Die user's subjects list wird von der Datenbank abgerufen, dann werden die Fächer
                     -die die indizes von maximalen Index runter zum Index:2- von der Datenbank gelöscht
                     */
                    val userCurrentSubjects = userProfile
                        .getUserSubjectsByEmail(userEmailSubscription)
                    if (userCurrentSubjects.size != 0) {
                        currentUserSubjects.let {
                            for (i in currentUserSubjects.lastIndex downTo 2) {
                                userProfile.deleteUserSubjectAtIndex(currentUser, i)
                            }
                        }
                        findNavController().navigate(SubscriptionFragmentDirections
                            .actionSubscriptionFragmentToHomeFragment())
                    } else {
                        findNavController().navigate(SubscriptionFragmentDirections
                            .actionSubscriptionFragmentToHomeFragment())
                    }
                }
                R.id.logout->{
                    /**
                     Hierhin gelten die gleichen Bedienungen wie beim Home
                     */
                    userProfile.deleteLastAddedInvoice(userEmailSubscription)
                    userProfile.deleteUserSpecificPaymentById(
                        currentUser,
                        paymentId,
                        currentInvoice.invoiceNumber
                    )
                    val userCurrentSubjects = userProfile
                        .getUserSubjectsByEmail(userEmailSubscription)

                    if (userCurrentSubjects.size != 0) {
                        currentUserSubjects.let {
                            for (i in currentUserSubjects.lastIndex downTo 2) {
                                userProfile.deleteUserSubjectAtIndex(currentUser, i)
                            }
                        }
                        requireActivity().finishAffinity()
                    } else {
                        requireActivity().finishAffinity()
                    }
                }
            }
            true
        }
        /**
        ---------------------------------------Add and Pay------------------------------------------
         */
        binding.btnAddSubjectsAndPay.setOnClickListener {
            /**
             Die Situationen:
             a) Keine Fächer sind ausgewählt.
             b) Fächer sind ausgewählt ohne Abonnementtyp.
             c) Keine Zahlungsmethode ist ausgewählt.
             werden in diesem Abschnitt behandelt.
             Der Vorgang des Prozess gilt erst wenn sowohl ein Fach (mehrere Fächer) mit gültigen
             Abonnementtyp als auch eine Zahlungsmethode ausgewählt sind.
             */
            var isValidSubscriptionType = false
            var isSubjectsSelected=true
            var isPaymentSelected = false
             val userSubjects=userProfile.getUserSubjectsByEmail(userEmailSubscription)

                val actualSubjects = userSubjects.drop(2)
                if (actualSubjects.isEmpty()) {
                    isSubjectsSelected = false
                }else{
                    for (i in actualSubjects.indices) {
                        isValidSubscriptionType = actualSubjects[i].isMonthlySubscribed || actualSubjects[i].isYearlySubscribed
                        if(!isValidSubscriptionType){

                        }else{
                            isValidSubscriptionType=true
                        }
                    }
                }
                viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
                    for (payment in paymentList) {
                        if (payment.isSelected) {
                            isPaymentSelected = true
                        }
                    }
                }
                if ( isSubjectsSelected && isValidSubscriptionType && isPaymentSelected) {
                    for (j in currentUserSubjects.indices) {
                        userProfile.updateUserProfileSubjectsInvoice(
                            currentUser,
                            currentUserSubjects[j],
                            currentInvoice.invoiceNumber
                        )
                    }
                    userProfile.updateUserSpecificInvoiceSubtotal(currentUser, currentInvoice)
                    findNavController().navigate(
                        SubscriptionFragmentDirections.actionSubscriptionFragmentToPaymentFragment(
                            userEmailPayment = userEmailSubscription,
                            paymentMethod = userPayment,
                            paymentId = paymentId
                        )
                    )
                } else {
                    if(!isSubjectsSelected){
                        Toast.makeText(
                            context,
                            "Sie haben keine Fächer ausgewählte Fächer!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                     if (!isValidSubscriptionType) {
                        Toast.makeText(
                            context,
                            "Sie haben keinen gültigen Abonnementtyp für ausgewählten Fächer!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isPaymentSelected) {
                        Toast.makeText(
                            context,
                            "Sie haben keine Zahlungsmethode ausgewählt!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        override fun onResume() {
            super.onResume()
            /**
             Die Situation für Fächer, Abonnementtyp und Zahlungsmethode werden zurückgesetzt.
             */
            viewModel.remainingSubjectList.observe(viewLifecycleOwner) {
                for (i in it.indices) {
                    it[i].isChecked = false
                    it[i].isMonthlySubscribed=false
                    it[i].isYearlySubscribed=false
                }
            }
            viewModel.paymentList.observe(viewLifecycleOwner){
                for(i in it.indices){
                    it[i].isSelected=false
                }
            }
        }

    }



