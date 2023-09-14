package com.example.zuhauselernen.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zuhauselernen.data.local.model.Payment
import com.examples.zuhauselernen.R

class PaymentRepository {
    private val _payment = MutableLiveData<List<Payment>>()
    val payment: LiveData<List<Payment>>
        get() = _payment

    init {
        loadPayments()
    }

    private fun loadPayments() {
        val paymentList = listOf(
            Payment(
                1,
                R.string.credit_card.toString(),
                R.drawable.credit_card_old,
                false,
                "",
                "",
                null,
                "",
                null,
                "",
                "",
            1),
            Payment(
                2,
                R.string.klarna.toString(),
                R.drawable.klarna,
                false,
                "",
                "",
                null,
                "",
                null,
                "",
                "",1

            ),
            Payment(
                3,
                R.string.bank_account.toString(),
                R.drawable.bank,
                false,
                "",
                "",
                null,
                "",
                null,
                "",
                "",1

            ),
            Payment(
                4,
                R.string.bill.toString(),
                R.drawable.invoice,
                false,
                "",
                "",
                null,
                "",
                null,
                "",
                "",1

            ),
            Payment(
                5,
                R.string.paypal.toString(),
                R.drawable.paypal,
                false,
                "",
                "",
                null,
                "",
                null,
                "",
                "",1

            )
        )
        _payment.value = paymentList
    }

    fun getPaymentById(paymentId: Int): Payment? {
        return payment.value?.firstOrNull { it.paymentId == paymentId }
    }
}