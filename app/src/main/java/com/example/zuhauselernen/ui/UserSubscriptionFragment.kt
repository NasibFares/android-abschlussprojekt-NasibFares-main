package com.example.zuhauselernen.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.adapter.PaymentAdapter
import com.example.zuhauselernen.data.local.adapter.UserSubscriptionAdapter
import com.example.zuhauselernen.data.local.model.Invoice
import com.example.zuhauselernen.data.local.model.Payment
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentUserSubscriptionBinding
import java.sql.Date


class UserSubscriptionFragment : Fragment() {
    private lateinit var binding: FragmentUserSubscriptionBinding
    private var userEmailUserSubscription = ""
    private var userPayment=""
    private var subjectId:Int=0
    private var paymentId=0
    private lateinit var userProfile: UserProfile

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailUserSubscription = it.getString("userEmailUserSubscription")!!
            subjectId=it.getInt("subjectId")
            paymentId=it.getInt("paymentId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_subscription, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfile = UserProfile(requireContext().applicationContext)
        userEmailUserSubscription.also { binding.tvEmailUserSubscription.text = it }
        binding.tvUserPaymentUserSubscription.text = userPayment
        val currentUser = userProfile.getUserProfileByEmail(userEmailUserSubscription)
        println("currentUser is:$currentUser")
        println("userImage:${currentUser!!.userPhoto}")

        val userProfilePhoto=binding.ivUserProfileImageSubscription


        if (currentUser?.userPhoto != null) {
            val userPhotoUri = Uri.parse(currentUser.userPhoto)

            val requestOptions = RequestOptions().transform(CircleCrop()).error(R.drawable.baseline_person_24)

            Glide.with(requireContext())
                .load(userPhotoUri)
                .apply(requestOptions)
                .into(userProfilePhoto)
        } else {
            userProfilePhoto.setImageResource(R.drawable.baseline_person_24)
        }
        /**
        Eine Variable definieren, die sich auf die aktuelle Zahlungsmethode bezieht.
         */
        var currentPayment= Payment(0,"",0,false,"",
            "",null,"",null,"","",0)

        /**
        Die letzte Rechnung des aktuellen Nutzers abrufen.
         */
        var lastUserInvoice = userProfile.getLastInvoiceNumberByEmail(userEmailUserSubscription)
        println("Last invoice number is:$lastUserInvoice")
        /**
        Eine Variable definieren, die sich auf die aktuelle Rechnung bezieht.
         */

        var currentInvoice= Invoice(0,Date(System.currentTimeMillis()),mutableListOf(),currentPayment,0.0)
        if (lastUserInvoice == null) {
            currentInvoice.invoiceNumber=1
            userProfile.createNewInvoice(userEmailUserSubscription, currentInvoice.invoiceNumber,
                Date(System.currentTimeMillis()).toString(),0.0)
            Toast.makeText(context, "Neue Rechnung wurde erstellt Rechnungsnummer:${currentInvoice.invoiceNumber} ", Toast.LENGTH_SHORT).show()
        }else{
            currentInvoice.invoiceNumber=lastUserInvoice+1
            userProfile.createNewInvoice(userEmailUserSubscription, currentInvoice.invoiceNumber,
                Date(System.currentTimeMillis()).toString(),0.0)
            Toast.makeText(context, "Neue Rechnung wurde erstellt Rechnungsnummer:${currentInvoice.invoiceNumber} ", Toast.LENGTH_SHORT).show()
        }
        if (currentUser != null) {
            binding.tvEmailUserSubscription.text = userEmailUserSubscription
        }

        var currentUserSubjects = userProfile.getUserSubjectsByEmail(userEmailUserSubscription)

        var currentUserPayments = userProfile.getUserPaymentsByEmail(userEmailUserSubscription)
        /**
         Eine Liste von aktuellen verfügbaren Fächer abrufen.
         */
        viewModel.getRestList(userEmailUserSubscription,requireContext())
        viewModel.restAvailableSubjectList.observe(viewLifecycleOwner){
                restAvailableSubjectList ->
            var finalAmount = 0.0

            binding.rvAvailableSubjects.adapter = currentUser?.let {
                UserSubscriptionAdapter (
                    requireContext(),
                    restAvailableSubjectList,
                    { subject, isChecked ->
                        viewModel.onRemainingSubjectChecked(subject, isChecked)
                        if (isChecked) {
                            /**
                             Die tempList spielt die Rolle einer Vermittler ,um zu wissen -wenn
                             zurück zu diesem Fragment- welche SubjectsList soll ausgenommen werden.
                             */
                            viewModel.tempList.add(subject)
                            println("tempList: ${viewModel.tempList}")
                            currentUserSubjects.add(subject)
                            userProfile.addNewSubjectToUserProfile(currentUser, subject)
                            /**
                            Abonnement in userProfile aktualisieren
                             */
                            userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)

                        } else {
                            viewModel.tempList.remove(subject)
                            println("tempList: ${viewModel.tempList}")
                            currentUserSubjects.remove(subject)
                            userProfile.deleteUserSpecificSubjectById(
                                currentUser,
                                subject.subjectId
                            )
                            userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)
                            /**
                             Der Abonnementpreis wird Subtrahiert, wenn diese Option deaktiviert ist
                              */
                            val subTotal = userProfile.getSubscriptionPrice(subject)
                            finalAmount -= subTotal
                            /**
                            Die Benutzeroberfläche wird mit dem neuen finalAmount aktualisiert
                             */
                            binding.tvSubTotalUserSubscription.text = finalAmount.toString()
                            /**
                            Der Abonnementstatus des Betreffs wird zurück gesetzt.
                             */
                            subject.isMonthlySubscribed = false
                            subject.isYearlySubscribed = false

                        }

                    },
                    { subject ->
                        viewModel.onRemainingSubjectSubscription(subject)
                        currentUser?.let {
                            var subTotal: Double
                            finalAmount = 0.0
                            for (i in restAvailableSubjectList.indices) {
                                if (restAvailableSubjectList[i].isChecked) {
                                    subTotal = userProfile.getSubscriptionPrice(restAvailableSubjectList[i])
                                    finalAmount += subTotal
                                }
                                currentInvoice.invoiceSubtotal=finalAmount
                            }
                            binding.tvSubTotalUserSubscription.text = finalAmount.toString()
                            /**
                            Abonnement im Benutzerprofil wird aktualisiert.
                             */
                            userProfile.updateUserSpecificSubjectSubscription(
                                currentUser,
                                subject
                            )
                        }
                    }, it, userProfile
                )
            }
        }
        userProfile.updateUserSpecificInvoiceSubtotal(currentUser!!,currentInvoice)

        viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
            binding.rvPayments.adapter =
                PaymentAdapter(requireContext(), paymentList) { payment, isChecked ->
                    viewModel.onPaymentChecked(payment, isChecked)
                    currentUser?.let {
                        /**
                        Wenn eine Zahlungsmethode ausgewählt ist
                         */
                        if (isChecked) {
                            /**
                            Die vorherige gewählte Zahlungsmethode wird gelöscht und eine neue
                            Zahlungsmethode wird erstellt.
                             */
                            if (paymentId != 0) {
                                userProfile.deleteUserSpecificPaymentById(currentUser, paymentId, currentInvoice.invoiceNumber)
                            }

                            userProfile.createNewPayment(
                                userEmailUserSubscription,
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
                            binding.tvUserPaymentUserSubscription.text =
                                context?.getText(payment.paymentName.toInt())
                            userPayment = context?.getString(payment.paymentName.toInt()) ?: ""
                        } else {
                            /**
                            Wenn die Zahlungsmethode abgewählt ist.
                             */
                            if (paymentId == payment.paymentId) {
                                userProfile.deleteUserSpecificPaymentById(currentUser, paymentId, currentInvoice.invoiceNumber)
                                paymentId = 0
                                userPayment = ""
                            }
                        }
                    }
                }
        }

        binding.btnAddSubjectsAndPayUserSubscription.setOnClickListener {
            var isValidSubscriptionType = false
            var isSubjectsSelected = true
            var isPaymentSelected = false
            val userSubjects = userProfile.getUserSubjectsByEmail(userEmailUserSubscription)
            val filteredList = userSubjects.filter { subject ->
                viewModel.tempList.any { tempSubject ->
                    tempSubject.subjectId == subject.subjectId
                }
            }
            if (filteredList.isEmpty()) {
                isSubjectsSelected = false
            } else {
                for (i in filteredList.indices) {
                    isValidSubscriptionType =
                        filteredList[i].isMonthlySubscribed || filteredList[i].isYearlySubscribed
                    if (!isValidSubscriptionType) {

                    } else {
                        isValidSubscriptionType = true
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
            if (isSubjectsSelected && isValidSubscriptionType && isPaymentSelected) {
                for (j in filteredList.indices) {
                    userProfile.updateUserProfileSubjectsInvoice(
                        currentUser,
                        filteredList[j],
                        currentInvoice.invoiceNumber
                    )
                }
                /**
                Hier werden die ausgewählten Fächer dem Profil des aktuellen Nutzers hinzugefügt.
                 */
                userProfile.updateUserSpecificInvoiceSubtotal(currentUser, currentInvoice)

                findNavController().navigate(
                    UserSubscriptionFragmentDirections
                        .actionUserSubscriptionFragmentToUserPaymentFragment(
                            userEmailUserPayment = userEmailUserSubscription,
                            paymentMethodUserPayment = userPayment,
                            paymentIdUserPayment = paymentId
                        )
                )

            } else {
                if (!isSubjectsSelected) {
                    Toast.makeText(
                        context,
                        "Sie haben keine Fächer ausgewählte Fächer!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (!isValidSubscriptionType) {
                    Toast.makeText(
                        context,
                        "Sie haben keinen gültigen Abonnementtyp für ausgewählte Fächer!!",
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
        binding.bottomNavigationView.setOnItemSelectedListener {item->
            val lastInvoice=userProfile.getLastInvoiceNumberByEmail(userEmailUserSubscription)
            val userSubjects=userProfile.getUserSubjectsByEmail(userEmailUserSubscription)
            val filteredList = userSubjects.filter { subject ->
                viewModel.tempList.any { tempSubject ->
                    tempSubject.subjectId == subject.subjectId
                }
            }
            when (item.itemId) {
                R.id.back -> {
                   userProfile.deleteListOfSubjectsFromUserProfile(currentUser,filteredList)
                    /**
                    Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
                     */
                    viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
                        for (payment in paymentList) {
                            if (payment.isSelected) {
                                userProfile.deleteUserSpecificPaymentById(currentUser,payment.paymentId,lastInvoice!!)
                            }
                        }
                    }
                    /**
                    Die letzte erstellte Rechnung wird gelöscht.
                     */
                    userProfile.deleteLastAddedInvoice(userEmailUserSubscription)
                    viewModel.tempList.clear()
                    findNavController().navigate(
                        UserSubscriptionFragmentDirections.actionUserSubscriptionFragmentToUserSubjectsFragment(
                            userEmailUserSubjects = userEmailUserSubscription
                        )
                    )
                }
                R.id.home -> {
                    userProfile.deleteListOfSubjectsFromUserProfile(currentUser,filteredList)
                    /**
                    Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
                     */
                    if (lastInvoice != null) {
                        viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
                            for (payment in paymentList) {
                                if (payment.isSelected) {
                                    userProfile.deleteUserSpecificPaymentById(currentUser,payment.paymentId,lastInvoice)
                                }
                            }
                        }
                    }
                    /**
                    Die letzte erstellte Rechnung wird gelöscht.
                     */
                    userProfile.deleteLastAddedInvoice(userEmailUserSubscription)
                    viewModel.tempList.clear()
                    /**
                     es wird zum UserSubjectsFragment navigiert.
                     */
                    findNavController().navigate(
                        UserSubscriptionFragmentDirections.actionUserSubscriptionFragmentToHomeFragment(
                        )
                    )
                }
                R.id.logout-> {
                    userProfile.deleteListOfSubjectsFromUserProfile(currentUser,filteredList)
                    /**
                    Die Zahlungsmethode, dei zur aktuellen Rechnung gehört, wird gelöscht
                     */
                    if (lastInvoice != null) {
                        viewModel.paymentList.observe(viewLifecycleOwner) { paymentList ->
                            for (payment in paymentList) {
                                if (payment.isSelected) {
                                    userProfile.deleteUserSpecificPaymentById(currentUser,payment.paymentId,lastInvoice!!)
                                }
                            }
                        }
                    }
                    /**
                    Die letzte erstellte Rechnung wird gelöscht.
                     */
                    userProfile.deleteLastAddedInvoice(userEmailUserSubscription)
                    viewModel.tempList.clear()
                    /**
                     Das Programm wird beendet
                     */
                    requireActivity().finishAffinity()
                }
            }
         true
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.tempList.clear()
        viewModel.restAvailableSubjectList.observe(viewLifecycleOwner) { subjects ->
            for (i in subjects.indices) {
                subjects[i].isChecked = false
                subjects[i].isMonthlySubscribed=false
                subjects[i].isYearlySubscribed=false
            }
        }
        viewModel.paymentList.observe(viewLifecycleOwner){payment->
            for(i in payment.indices){
                payment[i].isSelected=false
            }
        }
    }

}