package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.Subject
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.SubjectListSubscriptionBinding

class UserSubscriptionAdapter  (
private val context: Context,
private val dataset: List<Subject>,

private val onRemainingSubjectChecked: (Subject, Boolean) -> Unit,
private val onRemainingSubjectSubscription: (Subject) -> Unit,
private val currentUser: UserData,
private val userProfile: UserProfile
) : RecyclerView.Adapter<UserSubscriptionAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: SubjectListSubscriptionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSubscriptionAdapter.ItemViewHolder {
        val binding = SubjectListSubscriptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSubscriptionAdapter.ItemViewHolder, position: Int) {
        val subject = dataset[position]
        holder.binding.cbSubjectSubscription.isChecked = subject.isChecked

        holder.binding.tvSubjectSubscriptionTitel.text =
            context.getText(subject.subjectName.toInt())
        holder.binding.ivSubjectSubscriptionImage.setImageResource(subject.subjectImage)
        holder.binding.tvMonthlyPrice.text = subject.monthlySubscriptionPrice.toString()
        holder.binding.tvYearlyPrice.text = subject.yearlySubscriptionPrice.toString()



        holder.binding.rbMonthly.isEnabled = subject.isChecked
        holder.binding.rbYearly.isEnabled = subject.isChecked
        //holder.binding.rbMonthly.isSelected = subject.isMonthlySubscribed

        val layoutParams = holder.binding.ivSubjectSubscriptionImage.layoutParams
        val originalWidth = layoutParams.width
        val originalHeight = layoutParams.height
        val checkedColor = ContextCompat.getColor(context, R.color.buttons)
        val uncheckedColor = ContextCompat.getColor(context, R.color.white)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(checkedColor, uncheckedColor)
        )
        holder.binding.cbSubjectSubscription.buttonTintList = colorStateList
        /**
         * CheckBox behandeln
         */
        holder.binding.cbSubjectSubscription.setOnCheckedChangeListener { _, isChecked ->

            subject.isChecked = isChecked
            onRemainingSubjectChecked(subject, isChecked)

            if (isChecked) {
                holder.binding.rbMonthly.isEnabled = true
                holder.binding.rbYearly.isEnabled = true
                // Set the checked state of the toggle buttons
                /*holder.binding.rbMonthly.isSelected = true
                subject.isMonthlySubscribed = true*/

            } else {
                holder.binding.rgSubscription.clearCheck()

                holder.binding.rbMonthly.isSelected = false
                holder.binding.rbYearly.isSelected = false
                holder.binding.rbMonthly.isEnabled = false
                holder.binding.rbYearly.isEnabled = false

                subject.isMonthlySubscribed = false
                subject.isYearlySubscribed = false

            }
            val newWidth = if (isChecked) 185 else originalWidth
            val newHeight = if (isChecked) 185 else originalHeight
            layoutParams.width = newWidth
            layoutParams.height = newHeight
            holder.binding.ivSubjectSubscriptionImage.layoutParams = layoutParams
        }
        /**
         * RadioGroup behandeln
         */

        holder.binding.rgSubscription.setOnCheckedChangeListener { rdioButton, checkedId ->
            when (checkedId) {
                R.id.rb_monthly -> {


                    subject.isMonthlySubscribed = true
                    subject.isYearlySubscribed = false
                    subject.subscriptionPrice = subject.monthlySubscriptionPrice
                    // Update the subscription type in the database
                    userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)
                }

                R.id.rb_yearly -> {
                    subject.isMonthlySubscribed = false
                    subject.isYearlySubscribed = true
                    subject.subscriptionPrice = subject.yearlySubscriptionPrice
                    // Update the subscription type in the database
                    userProfile.updateUserSpecificSubjectSubscription(currentUser, subject)
                }
            }
            onRemainingSubjectSubscription(subject)
        }


    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun getItemId(position: Int): Long {
        return dataset[position].subjectId.toLong()
    }
}