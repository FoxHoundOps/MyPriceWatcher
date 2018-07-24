package edu.utep.cs.cs4330.mypricewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

/**
 *  A custom AlertDialog to display when a user tries to delete an item from their list of items
 *  that will be tracked. This dialog will allow a user to confirm whether or not they actually
 *  want to proceed with deleting the item from their list.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class DeleteDialog extends AppCompatDialogFragment {
    private DeleteDialogListener listener;      /* The internal listener for this DeleteDialog */

    /**
     * Build and return a DialogFragment that prompts the user whether or not they want to continue
     * to delete an item from their list. Initializes handling of the positive/negative buttons. If
     * the positive button is selected, communicate back through the listener with a boolean 'true'. If
     * the negative button is selected, communicate back through the listener with an boolean 'false'.
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.delete_dialog_prompt)
                .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> listener.onResponse(this, true))
                .setNegativeButton(R.string.no_dialog_button_label, (dialogInterface, i) -> listener.onResponse(this, false));
        setCancelable(false);
        return builder.create();
    }

    /**
     * Attach the implemented DeleteDialogListener from the activity interested in the response from
     * the DeleteDialog dialog.
     *
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Need to implement DeleteDialogListener.");
        }
    }
}
