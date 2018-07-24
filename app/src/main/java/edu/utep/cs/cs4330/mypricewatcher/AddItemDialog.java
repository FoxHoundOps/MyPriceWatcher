package edu.utep.cs.cs4330.mypricewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.EditText;

/**
 *  A custom AlertDialog to display when a user wants to add an item to their list of items
 *  that will be tracked. This dialog will allow a user to confirm whether or not they actually
 *  want to proceed with adding the item from their list.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class AddItemDialog extends AppCompatDialogFragment {
    private AddItemDialogListener listener;      /* The internal listener for this AddItemDialog */
    private EditText input;                      /* Input field for item's URL */
    /**
     * Create, build, and return the dialog.
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        input = new EditText(getContext());
        if (savedInstanceState != null)
            input.setText(savedInstanceState.getString("input"));
        builder.setMessage(R.string.add_item_prompt)
                .setView(input)
                .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> listener.onUserInput(this, true, input.getText().toString()))
                .setNegativeButton(R.string.no_dialog_button_label, (dialogInterface, i) -> listener.onUserInput(this, false, ""));
        setCancelable(false);
        return builder.create();
    }

    /**
     * Override to attach the implemented AddItemDialogListener from the activity interested in
     * the response from the AddItemDialog dialog.
     *
     * {@inheritDoc}
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

    /**
     * Save the text entered in the EditText, so that it can be restored.
     *
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("input", input.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
