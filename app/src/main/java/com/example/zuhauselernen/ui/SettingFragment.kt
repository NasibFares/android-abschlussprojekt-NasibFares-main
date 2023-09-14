package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var currentUser: UserData
    private lateinit var userProfile: UserProfile
    /**
    Es werden verschiedene Variablen und Datenstrukturen für die Verwaltung der Benutzerdaten und der
    Dropdown-Optionen definiert.
    Dazu gehören 'Arrays' für die verfügbaren Länder, Städte, Gründe, Schultypen und Klassenstufen.
    Die "userEmail"-Variable wird verwendet, um die E-Mail-Adresse des aktuellen Benutzers zu speichern.
     */
    private var userEmail = ""
    /**
    Dieses Array enthält verschiedene Länderoptionen, darunter Deutschland, England und Spanien.
    Das leere String-Element am Anfang ermöglicht eine leere Auswahl.
     */
    private val landOptions = arrayOf("", "Deutschland", "England", "Spain")

    /**
    Diese Map enthält für jedes Land eine Zuordnung von Städten. Jedes Land ist ein Schlüssel, und
    das entsprechende Array von Städten ist der Wert. Zum Beispiel enthält "Deutschland" die Städte
    Berlin, Bremen und Hamburg.
     */
    val cityOptions = mapOf(

        "Deutschland" to arrayOf( "Berlin", "Bremen", "Hamburg"),
        "England" to arrayOf("London", "Manchester", "Liverpool"),
        "Spain" to arrayOf("Madrid", "Valencia", "Granada")
    )

    /**
    Dieses Array enthält verschiedene Gründe, darunter "School" (Schule), "Hochschule"
    (Fachhochschule) und "University" (Universität).
     */
    private val reasonOptions = arrayOf("","School", "Hochschule", "University")

    /**
    Diese Map enthält für jeden Grund (Schule, Hochschule, Universität) eine Zuordnung von Schultypen.
    Jeder Grund ist ein Schlüssel, und das entsprechende Array von Schultypen ist der Wert. Zum
    Beispiel enthält "School" die Schultypen Grundschule, Hauptschule, Realschule, Gesamtschule und
    Gymnasium.
     */
    val schoolOptions = mapOf(

        "School" to arrayOf(
            "Grundschule (Klasse 1-4)",
            "Hauptschule (Klasse 5–9/10)",
            "Realschule (Klasse 5–10)",
            "Gesamtschule (Klasse 5–12/13)",
            "Gymnasium (Klasse 5–12/13)"
        ),
        "Hochschule" to arrayOf(
            "Business and Business Law",
            "Mechanical Engineering",
            "Electrical Engineering and Information Technology",
            "Civil Engineering",
            "Computer Science"
        ),
        "University" to arrayOf("Medizin", "Science", "Ingenieurwesen", "Kunst")
    )

    /**
    Diese Map enthält für jeden Schultyp eine Zuordnung von Klassenstufen. Jeder Schultyp ist ein
    Schlüssel, und das entsprechende Array von Klassenstufen ist der Wert. Zum Beispiel enthält
    "Grundschule (Klasse 1-4)" die Klassenstufen 1, 2, 3 und 4.
     */
    val classOptions = mapOf(
        "Grundschule (Klasse 1-4)" to arrayOf("1", "2", "3", "4"),
        "Hauptschule (Klasse 5–9/10)" to arrayOf("5", "6", "7", "8", "9", "10"),
        "Realschule (Klasse 5–10)" to arrayOf("5", "6", "7", "8", "9", "10"),
        "Gesamtschule (Klasse 5–12/13)" to arrayOf(
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13"
        ),
        "Gymnasium (Klasse 5–12/13)" to arrayOf(
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "11",
            "12",
            "13"
        ),
        "Kunst" to arrayOf("Art 101", "Music 101", "Dance 101"),
        "Science" to arrayOf("Physics 101", "Chemistry 101", "Biology 101"),
        "School of Business" to arrayOf("Finance 101", "Marketing 101", "Management 101")
    )

    /**
    Die Methode "onCreate()" wird überschrieben, um die "userEmail" aus den übergebenen Argumenten
    zu erhalten.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmail = it.getString("userEmail")!!
        }
    }

    /**
    Die "onCreateView()"-Methode wird überschrieben, um das Layout des Fragments zu erstellen und zu
    initialisieren, einschließlich der Dropdown-Menüs (Spinner).
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        userProfile = UserProfile(requireContext().applicationContext)

        /**
        Legen die Anfangswerte für die Spinner auf die leere Zeichenfolge fest
         */
        binding.spinnerLand.setSelection(0)
        binding.spinnerCity.setSelection(0)
        binding.spinnerReason.setSelection(0)
        binding.spinnerSchoolType.setSelection(0)
        binding.spinnerLevelGrade.setSelection(0)
        binding.bottomNavViewSetting.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> findNavController().navigate(SettingFragmentDirections.actionSettingFragmentToHomeFragment())
                R.id.logout->{
                    requireActivity().finishAffinity()
                }
            }
            true
        }

        return binding.root
    }
    /**
    In dieser Methode wird die Logik für die Initialisierung und Aktualisierung der Dropdown-Menüs
    implementiert. Die Adapter für die Dropdown-Menüs werden erstellt und mit den entsprechenden
    Optionen gefüllt.
    Es wird eine Logik hinzugefügt, um bei der Auswahl eines Landes die zugehörigen Städte zu laden
    und bei der Auswahl eines Grundes die entsprechenden Schulen zu laden.
    Es wird eine "OnClickListener" für den "btnContinueSetting" hinzugefügt, um die ausgewählten
    Optionen zu speichern und zur nächsten Seite ("SubjectFragment") zu navigieren.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         BottomNavigationBar implementieren:
         And dieser Stelle ist der Benutzer schon in DatenBank erstellt, deswegen wenn der Nutzer
         wählt Ausloggen aus, wird der Benutzer von der DatenBank gelöscht.
         */
        binding.bottomNavViewSetting.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home->{
                    userProfile.deleteUserByEmail(userEmail)
                    findNavController().navigate(SettingFragmentDirections
                        .actionSettingFragmentToHomeFragment())}
                R.id.logout->{
                    userProfile.deleteUserByEmail(userEmail)
                    requireActivity().finishAffinity()
                }
            }
            true
        }
        /**
        Der Text im TextBox tvUserSetting gleich userEmail, um den Namen des Nutzers anzuzeigen.
         */
        userEmail.also { binding.tvUserEmailSetting.text = it }

        /**
        Verschiedenen Variable werden definiert ,um die Spinners zu entsprechen.
         */
        val landSpinner = binding.spinnerLand
        val citySpinner = binding.spinnerCity
        val schoolSpinner = binding.spinnerSchoolType
        val classSpinner = binding.spinnerLevelGrade
        val reasonSpinner = binding.spinnerReason
//-------------------------------------------------------------------------------------------------
        val landAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            landOptions
        )
        val cityAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ArrayList<String>()
        )
        val reasonAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            reasonOptions
        )
        reasonSpinner.adapter = reasonAdapter

        val schoolAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ArrayList<String>()
        )
        schoolSpinner.adapter = schoolAdapter


        val classAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            ArrayList<String>()
        )

//-------------------------------------------------------------------------------------------------

        landSpinner.adapter = landAdapter
        citySpinner.adapter = cityAdapter
        reasonSpinner.adapter = reasonAdapter
        schoolSpinner.adapter = schoolAdapter
        classSpinner.adapter = classAdapter
//-------------------------------------------------------------------------------------------------
        landSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedLand = parent?.getItemAtPosition(position).toString()
                val citiesForLand = cityOptions[selectedLand] ?: emptyArray()
                cityAdapter.clear()
                for (city in citiesForLand) {
                    cityAdapter.add(city)
                }
                cityAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Situation behandeln, wenn nichts ausgewählt ist
            }
        }
        reasonSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedReason = parent?.getItemAtPosition(position).toString()
                val schoolsForReason = schoolOptions[selectedReason] ?: emptyArray()
                schoolAdapter.clear()
                for (school in schoolsForReason) {
                    schoolAdapter.add(school)
                }
                schoolAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Situation behandeln, wenn nichts ausgewählt ist
            }
        }

        schoolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedSchool = parent?.getItemAtPosition(position).toString()
                val classesForSchool = classOptions[selectedSchool] ?: emptyArray()
                classAdapter.clear()
                for (klasse in classesForSchool) {
                    classAdapter.add(klasse)
                }
                classAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Situation behandeln, wenn nichts ausgewählt ist
            }
        }
//-------------------------------Land,City,Reason,School type,Class--------------------------------//
        currentUser = userProfile.getUserProfileByEmail(userEmail) ?:
                UserData("", "", userEmail, "", "", "", "",
                    "", "", mutableListOf(), mutableListOf(),"",mutableListOf()
                )

        binding.btnContinueSetting.setOnClickListener {
            currentUser?.let { user ->
                /**
                Variable für ausgewählten (land,city,reason,school,class) werden definiert.
                 */
                val selectedLand = binding.spinnerLand.selectedItem?.toString()
                val selectedCity = binding.spinnerCity.selectedItem?.toString()
                val selectedReason = binding.spinnerReason.selectedItem?.toString()
                val selectedSchool = binding.spinnerSchoolType.selectedItem?.toString()
                val selectedClass = binding.spinnerLevelGrade.selectedItem?.toString()

                if (selectedLand.isNullOrEmpty() || selectedCity.isNullOrEmpty() ||
                    selectedReason.isNullOrEmpty() || selectedSchool.isNullOrEmpty() || selectedClass.isNullOrEmpty()
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields before continuing.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    /**
                    Die neu hinzugefügten Werte werden in den Datensatz des aktuellen Benutzers eingefügt
                     */
                    user.land = selectedLand
                    user.city = selectedCity
                    user.reason = selectedReason
                    user.schoolType = selectedSchool
                    user.classLevel = selectedClass
                    /**
                    Die Methode overrideUserDataToFile wird aufgerufen, um den aktuellen user zu
                    aktualisieren.
                     */
                    userProfile.updateUserProfile(currentUser)

                    Toast.makeText(
                        requireContext(),
                        "Ihre ausgewählten Optionen wurden hinzugefügt",
                        Toast.LENGTH_SHORT
                    ).show()

                    val action =
                        SettingFragmentDirections.actionSettingFragmentToSubjectFragment(userEmailSubject= userEmail)
                    findNavController().navigate(action)
                }
            }
        }

    }
    private fun getSelectedIndexFromArray(array: Array<String>, value: String?): Int {
        if (value == null) return 0
        val index = array.indexOf(value)
        return if (index >= 0) index else 0
    }
    /**
    In dieser Methode werden die zuvor ausgewählten Optionen wiederhergestellt, wenn der Benutzer
    von anderen Seiten zurückkehrt.
     */
    override fun onResume() {
        super.onResume()
        /**
         Die Situation behandeln , wenn der Nutzer zurück vom 'SubjectFragment' kommt, werden die
         folgende angezeigten Daten  mit Hilfe von Bundle abgerufen.
         */
        arguments?.getString("userEmail") ?: ""
        val selectedLand = arguments?.getString("userLand") ?: ""
        val selectedCity = arguments?.getString("userCity") ?: ""
        val selectedReason = arguments?.getString("userReason") ?: ""
        val selectedSchoolType = arguments?.getString("userSchoolType") ?: ""
        val selectedClass = arguments?.getString("userClass") ?: ""
        /**
        Diese Daten werden als Werte für die entsprechenden Spinner festgelegt.
         */
        selectedLand?.let { land ->
            binding.spinnerLand.setSelection(getSelectedIndexFromArray(landOptions, land))
            binding.spinnerCity.setSelection(getSelectedIndexFromArray(cityOptions[land] ?: emptyArray(), selectedCity))
        }

        selectedReason?.let { reason ->
            binding.spinnerReason.setSelection(getSelectedIndexFromArray(reasonOptions, reason))
            binding.spinnerSchoolType.setSelection(getSelectedIndexFromArray(schoolOptions[reason] ?: emptyArray(), selectedSchoolType))
        }

        selectedSchoolType?.let { schoolType ->
            binding.spinnerLevelGrade.setSelection(getSelectedIndexFromArray(classOptions[schoolType] ?: emptyArray(), selectedClass))
        }
    }
}