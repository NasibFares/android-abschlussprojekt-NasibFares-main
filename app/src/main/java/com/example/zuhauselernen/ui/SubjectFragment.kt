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
import com.example.zuhauselernen.data.local.adapter.SubjectAdapter
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentSubjectBinding

class SubjectFragment : Fragment() {
    private lateinit var binding: FragmentSubjectBinding
    private val maxFreeSubjects: Int = 2
    private var userEmailSubject = ""
    private lateinit var userProfile: UserProfile
    private val viewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailSubject = it.getString("userEmailSubject")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_subject, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         Die Anzahl der ausgewählten Fächer wird auf 0 gesetzt.
         */
        viewModel.selectedSubjectsCount.value = 0
        /**
         Bestimmung des userProfile.
         */
        userProfile = UserProfile(requireContext().applicationContext)
        /**
         Das Profil des aktuellen Nutzer wird von der Datenbank abgerufen.
         */
        val currentUser = userProfile.getUserProfileByEmail(userEmailSubject)
        /**
        Wenn der Benutzer in der Datenbank vorhanden ist, werden die TextBoxen auf die entsprechenden
        Werte gesetzt
         */
        if (currentUser != null) {
            binding.tvUserSubjectSection.text = userEmailSubject
            binding.tvUserSubjectsCount.text = viewModel.selectedSubjectsCount.value.toString()
        }
        /**
         Die SubjectList wird beobachtet ,und die geeigneten Aktionen werden ausgeführt
         */
        viewModel.subjectList.observe(viewLifecycleOwner) { subjectList ->
            /**
             Der Adapter der recyclerView wird eingesetzt.
             */
            binding.recyclerView.adapter =
                SubjectAdapter(requireContext(), subjectList) { subject, isChecked ->
                    /**
                     Diese Methode wird von ViewModel abgerufen ,um die Situation des aktuellen
                     Fach zu setzen.
                     */
                    viewModel.onSubjectChecked(subject, isChecked)
                    /**
                     Lambada für aktuellen Nutzer wird implementiert.
                     */
                    currentUser?.let {
                        /**
                         Die Anzahl der Fächer entspricht die Anzahl der Fächer des aktuellen Nutzer.
                         */
                        var subjectsCount = currentUser.subjects.count()
                        /**
                         Wenn das Fach angehakt ist
                         */
                        if (isChecked) {
                            /**
                             Die Anzahl der Fächer wird überprüft.
                             Fall sie kleiner als die maximale Anzahl der kostenlos Fächer ist.
                             */
                            if (subjectsCount <= maxFreeSubjects) {
                                /**
                                 Es wird überprüft, ob das Fach schon in der Liste des Nutzer ist.
                                 Wenn nicht:
                                 */
                                if (!currentUser.subjects.contains(subject)) {
                                    /**
                                     Das Fach wird zur Liste des Nutzer hinzugefügt und die Anzahl
                                     der Fächer wird aktualisiert.
                                     */
                                    currentUser.subjects.add(subject)
                                    subjectsCount++
                                    binding.tvUserSubjectsCount.text = subjectsCount.toString()
                                    userProfile.addNewSubjectToUserProfile(currentUser, subject)
                                    /**
                                     Wenn Ja (Das Fach existiert schon)
                                     */
                                } else {
                                    /**
                                     Keine Aktion wird ausgeführt.
                                     */
                                }
                            } else {
                                /**
                                 dieser code wird nicht ausgeführt, da wenn die Anzahl der Fächer
                                 mehr als die maximale kostenlos Fächer ist, wird einen DialogBox
                                 angezeigt.
                                 */
                                Toast.makeText(
                                    requireContext(),
                                    "Sie dürfen nur zwei Fächer kostenlos hinzufügen.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                subject.isChecked = false
                            }
                            /**
                             Wenn das Fach abgehakt wurde
                             */
                        } else {
                            /**
                             Wenn es in der Liste des Nutzer existiert, wird es gelöscht und die
                             Anzahl der Fächer wird entsprechend geändert
                             */
                            if (currentUser.subjects.contains(subject)) {
                                currentUser.subjects.remove(subject)
                                subjectsCount--
                                binding.tvUserSubjectsCount.text = subjectsCount.toString()
                                userProfile.updateUserProfileSubjects(currentUser)

                            } else {

                            }

                        }
                        /**
                         Diese Methode wird von viewModel aufgerufen um die (RemainingList) zu
                         aktualisieren.
                         RemainingList ist die Liste, die im nächsten Fragment (Abonnement)
                         angezeigt wird.
                         */
                        viewModel.updateRemainingSubjects(subjectList, currentUser.subjects)
                    }
                }

        }
        /**
         Hier wird die Anzahl der ausgewählten Fächer beobachtet, um die entsprechenden Aktionen
         auszuführen.
         */
        viewModel.selectedSubjectsCount.observe(viewLifecycleOwner) { count ->
            currentUser?.let { user ->
                if (count > maxFreeSubjects) {
                    binding.btnAddSubjects.visibility = View.GONE
                    showActionDialog(user)
                } else {
                    binding.btnAddSubjects.visibility = View.VISIBLE
                }
            }
        }

        binding.ivBackSubject.setOnClickListener {
            /**
            Klick-Listener für die ivBackSubject-Ansicht festlegen.
            Dies stellt eine Art „Zurück“-Schaltfläche oder ein „Zurück“-Symbol dar.
             */
            val userCurrentSubjects = userProfile.getUserSubjectsByEmail(userEmailSubject)
            /**
            Zunächst wird geprüft, ob der Benutzer über aktuelle Subjekte (userCurrentSubjects) verfügt.
            Wenn der Benutzer Subjects hat , werden sie von der Datenbank gelöscht.
             */
            if(userCurrentSubjects.size!=0) {
                userProfile.deleteUserProfileSubjects(userEmailSubject)
            }else{
                /**
                 Keine Aktion wird ausgeführt.
                 */
            }
            /**
            Benutzerprofilinformationen abrufen:
            Verschiedene Benutzerprofilinformationen werden mithilfe von Methoden wie
            getUserLandByEmail,getUserCityByEmail usw abgerufen:
            userLand,userCity, userReason, userSchoolType und userClass
             */
            val userLand = userProfile.getUserLandByEmail(userEmailSubject)
            val userCity = userProfile.getUserCityByEmail(userEmailSubject)
            val userReason = userProfile.getUserReasonByEmail(userEmailSubject)
            val userSchoolType = userProfile.getUserSchoolByEmail(userEmailSubject)
            val userClass = userProfile.getUserClassByEmail(userEmailSubject)
            /**
            Ein Bundle erstellen: Es erstellt ein Bundle mit dem Namen args und fügt diesem Bundle
            alle abgerufenen Benutzerprofilinformationen als Schlüssel-Wert-Paare hinzu.
             */
            val args = Bundle()
            args.putString("userEmail", userEmailSubject)
            args.putString("userLand", userLand)
            args.putString("userCity", userCity)
            args.putString("userReason", userReason)
            args.putString("userSchoolType", userSchoolType)
            args.putString("userClass", userClass)
            /**
            Schließlich wird mit findNavController().navigate() zu „settingFragment“ navigiert und
            die Benutzerprofilinformationen als Argumente übergibt.
            Dies ermöglicht den Zugriff auf die Benutzerprofilinformationen im „settingFragment“.
             */
            findNavController().navigate(
                R.id.action_subjectFragment_to_settingFragment,
                args
            )
        }
        /**
         BottomNavigationBar implementieren:
         An dieser Stelle ,unbeachtet ob der Nutzer logOut oder Home ausgewählt, wird er zum HomeF
         navigiert oder wird das Programm beendet , aber der Nutzer bleibt in der DatenBank, er kann
         sich später anmelden und die gewünschte Fächer auswählen.
         */
        binding.bottomNavViewSubject.setOnItemSelectedListener { item ->
            val userCurrentSubjects = userProfile.getUserSubjectsByEmail(userEmailSubject)
            when (item.itemId) {
                R.id.home -> {
                    if (userCurrentSubjects.size != 0) {
                        userProfile.deleteUserProfileSubjects(userEmailSubject)
                    } else {
                    }
                    findNavController().navigate(SubjectFragmentDirections.actionSubjectFragmentToHomeFragment())
                }
                R.id.logout -> {
                    if (userCurrentSubjects.size != 0) {
                        userProfile.deleteUserProfileSubjects(userEmailSubject)
                    } else {
                    }
                    requireActivity().finishAffinity()
                }
            }
            true
        }

        binding.btnAddSubjects.setOnClickListener {
            userProfile.updateUserProfileSubjects(currentUser!!)
            findNavController().navigate(
                SubjectFragmentDirections.actionSubjectFragmentToUserSubjectsFragment(
                    userEmailUserSubjects = userEmailSubject
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.subjectList.observe(viewLifecycleOwner) {

            for (i in it.indices) {
                it[i].isChecked = false
            }
        }

        binding.btnAddSubjects.visibility = View.VISIBLE
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateRecyclerView() {
        viewModel.subjectList.observe(viewLifecycleOwner) {
            for (i in it.indices)
                it[i].isChecked = false
        }

    }

    private fun showActionDialog(user: UserData) {
        val dialogFragment = SubjectDialogFragment()
        dialogFragment.actionListener = object : SubjectDialogFragment.ActionListener {
            override fun onTakeAction() {
                //userProfile.deleteLastAddedSubject(userEmailSubject)
                user.subjects.removeAt(user.subjects.lastIndex)
                userProfile.updateUserProfileSubjects(user)
                val lastAddedSubjectIndex = user.subjects.lastIndex
                val lastAddedSubject = user.subjects[lastAddedSubjectIndex]
                lastAddedSubject.isChecked = false
                updateRecyclerView()
                findNavController().navigate(
                    SubjectFragmentDirections.actionSubjectFragmentToSubscriptionFragment(
                        userEmailSubscription = userEmailSubject
                    )
                )
                dialogFragment.dismiss()
            }

            override fun onCancel() {
                user.subjects.removeAll(user.subjects)
                userProfile.updateUserProfileSubjects(user)
                updateRecyclerView()
                binding.btnAddSubjects.visibility = View.VISIBLE
                binding.tvUserSubjectsCount.text = "0"
                binding.recyclerView.adapter?.notifyDataSetChanged()

            }
        }
        dialogFragment.show(parentFragmentManager, "ActionDialogFragment")
    }


}
