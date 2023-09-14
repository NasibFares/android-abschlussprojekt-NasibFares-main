package com.example.zuhauselernen.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

/**
SubjectDialogFragment ist eine Unterklasse von DialogFragment, die zur Anzeige von Dialogen in
Android-Anwendungen verwendet wird.
 */
class SubjectDialogFragment : DialogFragment() {
    /**
    Innerhalb der SubjectDialogFragment-Klasse wird eine ActionListener-Schnittstelle deklariert.
     */
    interface ActionListener {
        /**
        Diese Schnittstelle definiert zwei Funktionen:
        onTakeAction():
        Diese Funktion wird aufgerufen, wenn der Benutzer eine Aktion ausführt, in diesem Fall das
        Abonnieren eines Subjects.
        onCancel():
        Diese Funktion wird aufgerufen, wenn der Benutzer die Aktion abbrechen möchte.
         */
        fun onTakeAction()
        fun onCancel()
    }
    /**
    Eine nullfähige actionListener-Eigenschaft vom Typ ActionListener wird deklariert.
    Diese Eigenschaft enthält einen Verweis auf ein Objekt, das die ActionListener-Schnittstelle
    implementiert.
    Es ermöglicht die Kommunikation zwischen dem Dialog und dem aufrufenden Code.
     */
    var actionListener: ActionListener? = null
    /**
    In der Methode onCreateDialog wird ein Dialog mithilfe eines AlertDialog.Builder erstellt und
    konfiguriert.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(requireContext())
            /**
            Der Dialog weist folgende Eigenschaften auf:
            Titel: „ABONNEMENT“
            Nachricht: „Wollen Sie abonnieren?“
             */
            builder.setTitle("ABONNEMENT")
                .setMessage("Wollen Sie abonnieren?")
                /**
                 Zwei Tasten:
                A) „abonnieren“ (subscribe):
                 Ein Klick auf diese Schaltfläche löst den onTakeAction-Callback des actionListeners
                 aus,sofern dieser nicht null ist.
                 */
                .setPositiveButton("abonnieren") { _, _ ->
                    actionListener?.onTakeAction()
                    dismiss()
                }
                /**
                B) „abbrechen“ (cancel):
                Ein Klick auf diese Schaltfläche löst den onCancel-Callback des actionListeners
                aus, sofern dieser nicht null ist.
                 */
                .setNegativeButton("abbrechen") { _, _ ->
                    actionListener?.onCancel()
                    dismiss()
                }
            /**
            Das Dialogobjekt wird mit den Einstellungen des Builders erstellt.
             */
            val dialog = builder.create()
            /**
            Der Dialog ist so konfiguriert, dass er nicht abbrechbar ist (der Benutzer kann ihn nicht
            durch Drücken der Zurück-Taste schließen) und nicht abbrechbar ist, wenn er außerhalb
            der Grenzen des Dialogs berührt wird.
             */
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            /**
            Abschließend wird das Dialog-Objekt zurückgegeben.
             */
            return dialog
        } ?: throw IllegalStateException("Activity könnte nicht null sein")
    }

}