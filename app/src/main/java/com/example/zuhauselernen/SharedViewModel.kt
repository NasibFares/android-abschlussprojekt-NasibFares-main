package com.example.zuhauselernen
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zuhauselernen.data.local.PaymentRepository
import com.example.zuhauselernen.data.local.SubjectRepository
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.Category
import com.example.zuhauselernen.data.local.model.Invoice
import com.example.zuhauselernen.data.local.model.Payment
import com.example.zuhauselernen.data.local.model.Subject
import com.examples.zuhauselernen.R


class SharedViewModel: ViewModel() {
    private val repository = SubjectRepository()
    private val paymentRepository = PaymentRepository()
    private lateinit var userProfile:UserProfile
    /**
     -----------------------------------Subjects Abteilung-----------------------------------------
     */
    /**
    -----------------------------------Schritt.1 SubjectList-----------------------------------------
     */
    val subjectList = repository.subject
    private val _selectedSubjectsCount = MutableLiveData(0)
    val selectedSubjectsCount: MutableLiveData<Int>
        get() = _selectedSubjectsCount
    private val _currentSubject = MutableLiveData<Subject?>()
    private val currentSubject: MutableLiveData<Subject?>
        get() = _currentSubject
    /**
    Die Funktion fun onSubjectChecked(subject: Subject, isChecked: Boolean) wird aufgerufen, wenn
    der Status eines bestimmten Subject geändert wird. Der Parameter isChecked gibt an, ob das
    Subject aktuell ausgewählt oder abgewählt ist.
     */
    fun onSubjectChecked(subject: Subject, isChecked: Boolean) {
        /**
        Wenn isChecked true ist, bedeutet dies, dass das Subject ausgewählt wurde. In diesem Fall
        wird die Variable currentSubject.value auf das ausgewählte Subject gesetzt. Dadurch wird
        das aktuelle Subject aktualisiert, um das neu ausgewählte Subject darzustellen.
         */
        if (isChecked) {
            currentSubject.value = subject
            /**
            Wenn isChecked false ist, bedeutet dies, dass das Subject abgewählt wurde.
             */
        } else {
            /**
            Es wird überprüft, ob das aktuelle Subject, das durch currentSubject.value
            repräsentiert wird, mit dem abgewählten Subject übereinstimmt.
            Wenn ja, bedeutet dies,dass das abgewählte Subject zuvor das aktuelle war
             */
            if (currentSubject.value == subject) {
                /**
                Daher wird currentSubject.value auf null gesetzt, um das aktuelle Subject zu löschen
                oder zurückzusetzen.
                 */
                currentSubject.value = null
            }
        }
        val currentCount = subjectList.value?.count { it.isChecked } ?: 0
        selectedSubjectsCount.value = currentCount
    }
    /**
    ---------------------------------Schritt.2 remainingSubjectList---------------------------------
     */
    private val _remainingSubjectList = MutableLiveData<List<Subject>>(emptyList())
    val remainingSubjectList: LiveData<List<Subject>>
        get() = _remainingSubjectList
    /**
    Die Methode updateRemainingSubjects wird von SubjectFragment aufgerufen, um gefilterte Liste
    zu bekommen (SubjectsList außer die vom Benutzer zwei ausgewählte kostenlos Fächer)
     */
    fun updateRemainingSubjects(
        fullSubjectList: List<Subject>,
        userSelectedSubject: List<Subject>
    ) {

        val firstList =
            userSelectedSubject.filter { userSelectedSubject.indexOf(it) != userSelectedSubject.lastIndex }
        _remainingSubjectList.value = fullSubjectList.minus(firstList.toSet())
    }
    /**
    Die Aktion, die ausgeführt wird, wenn der Benutzer den CheckBox eines Fach geklickt hat.
     */
    fun onRemainingSubjectChecked(subject: Subject, isChecked: Boolean) {
        if (isChecked) {
            currentSubject.value = subject
        } else {
            if (currentSubject.value == subject) {
                currentSubject.value = null
            }
        }
        val currentCount = subjectList.value?.count { it.isChecked } ?: 0
        selectedSubjectsCount.value = currentCount
    }

    fun onRemainingSubjectSubscription(subject: Subject) {
        currentSubject.value?.let { currentSubject ->
            currentSubject.isMonthlySubscribed = subject.isMonthlySubscribed
            currentSubject.isYearlySubscribed = subject.isYearlySubscribed
        }
    }

    /**
    --------------------------------Schritt.3 restAvailableSubjectList------------------------------
     */
    private val _restAvailableSubjectList = MutableLiveData<List<Subject>>()
    val restAvailableSubjectList: LiveData<List<Subject>>
        get() = _restAvailableSubjectList
    fun getRestList(email:String,context:Context){
        userProfile= UserProfile(context)
        val restList=subjectList.value!!.filter { userProfile.getUserSubjectsByEmail(email).toSet().none{
            it1->it1.subjectId==it.subjectId
        }}
        _restAvailableSubjectList .value=restList
    }
    val tempList: MutableList<Subject> = mutableListOf()

    /**
    -------------------------------------Payments Abteilung-----------------------------------------
     */
    val paymentList = paymentRepository.payment
    private val _selectedPaymentsCount = MutableLiveData(0)
    private val selectedPaymentsCount: MutableLiveData<Int>
        get() = _selectedPaymentsCount
    private val _currentPayment = MutableLiveData<Payment?>()
    private val currentPayment: MutableLiveData<Payment?>
        get() = _currentPayment

    /**
    -------------------------------------Categories Abteilung-----------------------------------------
     */
    private val categoryList = repository.category
    private val _selectedCategoriesCount = MutableLiveData(0)
    val categories = repository.category
    fun getCategoriesBySubjectId(subjectId: Int): List<Category> {
        return categoryList.value?.filter { it.categorySubject == subjectId } ?: emptyList()
    }
    /**
    Die folgenden Methoden werden derzeit nicht verwendet, aber ich werde sie später benötigen,
    wenn ich die UserSubjectsCategories behandeln möchte
     */
    private val selectedCategoriesCount: MutableLiveData<Int>
        get() = _selectedCategoriesCount
    private val _currentCategory = MutableLiveData<Payment?>()
    private val currentCategory: MutableLiveData<Payment?>
        get() = _currentCategory
    /**
    Die Funktion fun onPaymentChecked(payment: Payment, isChecked: Boolean) wird aufgerufen, wenn
    der Status einer bestimmten Zahlungsmethode geändert wird. Der Parameter isChecked gibt an, ob das
    PaymentMethod aktuell ausgewählt oder abgewählt ist.
     */
    fun onPaymentChecked(payment: Payment, isChecked: Boolean) {
        if (isChecked) {
            currentPayment.value = payment
        } else {
            if (currentPayment.value == payment) {
                /**
                Daher wird currentPayment.value auf null gesetzt, um das aktuelle paymentMethod zu
                löschen oder zurückzusetzen.
                 */
                currentPayment.value = null
            }
        }
    }

    fun getPaymentImage(paymentId: Int, context: Context): Drawable? {
        val placeholderDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.paypal
        )

        val payment: Payment? = paymentRepository.getPaymentById(paymentId)
        val imageResource = payment?.paymentImage

        return imageResource?.let {
            ContextCompat.getDrawable(context, it) ?: placeholderDrawable
        }
    }
    private val _invoices=MutableLiveData<List<Invoice>>()
    val invoices:LiveData<List<Invoice>>
        get()= _invoices
    fun getUserInvoices(email:String){
        _invoices.value= userProfile.getUserInvoices(email)
    }
}

