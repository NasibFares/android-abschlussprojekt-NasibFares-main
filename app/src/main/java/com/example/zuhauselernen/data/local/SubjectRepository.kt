package com.example.zuhauselernen.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zuhauselernen.data.local.model.Category
import com.example.zuhauselernen.data.local.model.Subject
import com.examples.zuhauselernen.R

class SubjectRepository {

    private val _subject = MutableLiveData<List<Subject>>()
    val subject: LiveData<List<Subject>>
        get() = _subject

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        val subjectList = listOf(
            Subject(
                1,
                R.string.subjectTitel_1.toString(),
                R.drawable.subject_image_1,
                false,
                1.99,
                19.10,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                2,
                R.string.subjectTitel_2.toString(),
                R.drawable.subject_image_2,
                false,
                1.99,
                19.10,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                3,
                R.string.subjectTitel_3.toString(),
                R.drawable.subject_image_3, false,
                1.99,
                19.10,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                4,
                R.string.subjectTitel_4.toString(),
                R.drawable.subject_image_4,
                false,
                1.99,
                19.10,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                5,
                R.string.subjectTitel_5.toString(),
                R.drawable.subject_image_5,
                false,
                2.10,
                20.16,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                6,
                R.string.subjectTitel_6.toString(),
                R.drawable.subject_image_6,
                false,
                2.10,
                20.16,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                7,
                R.string.subjectTitel_7.toString(),
                R.drawable.subject_image_7,
                false,
                2.10,
                20.16,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                8,
                R.string.subjectTitel_8.toString(),
                R.drawable.subject_image_8,
                false,
                2.80,
                26.88,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                9,
                R.string.subjectTitel_9.toString(),
                R.drawable.subject_image_9,
                false,
                2.80,
                26.88,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                10,
                R.string.subjectTitel_10.toString(),
                R.drawable.subject_image_10,
                false,
                1.80,
                17.28,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                11,
                R.string.subjectTitel_11.toString(),
                R.drawable.subject_image_11,
                false,
                1.30,
                12.48,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                12,
                R.string.subjectTitel_12.toString(),
                R.drawable.subject_image_12,
                false,
                1.1,
                10.56,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                13,
                R.string.subjectTitel_13.toString(),
                R.drawable.subject_image_13,
                false,
                1.1,
                10.56,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
            Subject(
                14,
                R.string.subjectTitel_14.toString(),
                R.drawable.subject_image_14,
                false,
                2.0,
                19.20,
                isMonthlySubscribed = false,
                isYearlySubscribed = false,
                0,
                0.0
            ),
        )
        _subject.value = subjectList
    }

    private val _category = MutableLiveData<List<Category>>()
    val category: LiveData<List<Category>>
        get() = _category

    init {
        loadCategories()
    }

    private fun loadCategories() {
        val categoryList = listOf(
            Category(1, R.string.category_1_1.toString(),R.string.categoryExplain_1_1.toString(),R.drawable.category_1_1_image,1),
            Category(2, R.string.category_1_2.toString(),R.string.categoryExplain_1_2.toString(),R.drawable.category_1_2_image,1),
            Category(3, R.string.category_1_3.toString(),R.string.categoryExplain_1_3.toString(),R.drawable.category_1_3_image,1),
            Category(4, R.string.category_1_4.toString(),R.string.categoryExplain_1_4.toString(),R.drawable.category_1_4_image,1),
            Category(5, R.string.category_1_5.toString(),R.string.categoryExplain_1_5.toString(),R.drawable.category_1_5_image,1),
            Category(6, R.string.category_1_6.toString(),R.string.categoryExplain_1_6.toString(),R.drawable.category_1_6_image,1),
        )
        _category.value = categoryList
    }
}