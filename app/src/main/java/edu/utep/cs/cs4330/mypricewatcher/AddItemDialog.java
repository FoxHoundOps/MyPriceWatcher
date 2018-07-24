package edu.utep.cs.cs4330.mypricewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.EditText;

public class AddItemDialog extends AppCompatDialogFragment {
    private AddItemDialogListener listener;      /* The internal listener for this AddItemDialog */

    /**
     *
     * @param savedInstanceState The saved instance state from the activity that invoked the dialog
     * @return  A AddItemDialog..
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        EditText input = new EditText(getContext());
        builder.setMessage(R.string.add_item_prompt)
                .setView(input)
                .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> listener.onUserInput(this, true, input.getText().toString()))
                .setNegativeButton(R.string.no_dialog_button_label, (dialogInterface, i) -> listener.onUserInput(this, false, ""));
        setCancelable(false);
        return builder.create();
    }

    /**
     * Attach the implemented AddItemDialogListener from the activity interested in the response from
     * the DeleteDialog dialog.
     *
     * @param context The Context whose implemented AddItemDialogListener will be attached to the AddItemDialog dialog.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (AddItemDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Need to implement AddItemDialogListener.");
        }
    }
}
