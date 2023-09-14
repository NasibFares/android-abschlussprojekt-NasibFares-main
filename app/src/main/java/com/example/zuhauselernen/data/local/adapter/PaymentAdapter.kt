package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.model.Payment
import com.examples.zuhauselernen.databinding.PaymentsListBinding

class PaymentAdapter (
    private val context: Context,
    private val dataset: List<Payment>,
    /**
    Der private Wert onSubjectChecked: (Subject, Boolean) -> Unit ist ein Lambda-Ausdruck,
    der eine Funktion darstellt, die zwei Parameter der Typen Subject und Boolean
    annimmt und Unit zurückgibt (d. h. keinen Wert zurückgibt). Es wird als Rückruf verwendet, um
    den aufrufenden Code (normalerweise das Fragment, das den SubjectAdapter verwendet) zu
    benachrichtigen, wenn ein Betreff aktiviert oder deaktiviert wird.
    Dadurch kann der SubjectAdapter den aufrufenden Code (normalerweise ein Fragment oder ViewModel)
    benachrichtigen, wenn ein Betreff in der RecyclerView aktiviert oder deaktiviert wird.
     */
    private val onPaymentSelected: (Payment, Boolean) -> Unit
) : RecyclerView.Adapter<PaymentAdapter.ItemViewHolder>() {
    /**
    ItemViewHolder ist eine innere Klasse, die die einzelnen Elemente in der RecyclerView repräsentiert.
    Sie hält eine Referenz auf das SubjectListBinding-Objekt, das die Layout-Views für jedes Element enthält.
     */
    inner class ItemViewHolder(val binding: PaymentsListBinding) :
        RecyclerView.ViewHolder(binding.root)
    /**
    onCreateViewHolder wird aufgerufen, wenn ein neuer ViewHolder erstellt werden muss. Sie erstellt
    eine Instanz des ItemViewHolder und verbindet sie mit dem Layout des Listenelements mithilfe des
    SubjectListBinding.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaymentAdapter.ItemViewHolder {
        val binding = PaymentsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }
    /**
    onBindViewHolder wird aufgerufen, wenn die RecyclerView ein Listenelement mit Daten füllen muss.
    Sie bekommt die Daten für das entsprechende Element aus dem dataset und aktualisiert die
    Layout-Views mit den entsprechenden Informationen.
     */
    override fun onBindViewHolder(holder: PaymentAdapter.ItemViewHolder, position: Int) {
        val payment = dataset[position]
        
        holder.binding.rbPayment.isChecked=payment.isSelected
        holder.binding.rbPayment.setOnCheckedChangeListener { _, isChecked ->
            payment.isSelected = isChecked
            onPaymentSelected(payment, isChecked)
        }
        holder.binding.rbPayment.setOnClickListener {
            for (selectedPayment in dataset) {
                selectedPayment.isSelected = selectedPayment == payment
            }
            notifyDataSetChanged()
        }
        /**
        dieser Implementierung wird der Titel des (Payment), das (Image) und der Status des (RadioButton)
        (selected/unselected) im Layout aktualisiert.
         */
        holder.binding.tvPaymentTitel.text=context.getText(payment.paymentName.toInt())
        holder.binding.ivPaymentImage.setImageResource(payment.paymentImage)
    }
    /**
    getItemCount() gibt die Anzahl der Elemente in der RecyclerView zurück, die dem dataset entsprechen.
     */
    override fun getItemCount(): Int {
        return dataset.size
    }

}