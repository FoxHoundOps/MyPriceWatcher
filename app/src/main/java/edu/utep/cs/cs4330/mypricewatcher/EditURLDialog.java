package edu.utep.cs.cs4330.mypricewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.EditText;

public class EditURLDialog extends AppCompatDialogFragment {
    private EditURLDialogListener listener;      /* The internal listener for this EditURLDialog */
    private EditText input;                      /* Input field for item's new URL */
    /**
     * A custom AlertDialog to display when a user tries to edit an item's URL from their list of items
     *  that will be tracked. This dialog will allow a user to confirm whether or not they actually
     *  want to proceed with editing an item's URL from their list.
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
        builder.setMessage(R.string.edit_item_url_prompt)
                .setView(input)
                .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> listener.onItemURL(this, true, input.getText().toString()))
                .setNegativeButton(R.string.no_dialog_button_label, (dialogInterface, i) -> listener.onItemURL(this, false, ""));
        setCancelable(false);
        return builder.create();
    }

    /**
     * Attach the implemented EditURLDialog from the activity interested in the response from
     * the EditURLDialog dialog.
     *
     * A custom AlertDialog to display when a user tries to edit an item name from their list of items
     *  that will be tracked. This dialog will allow a user to confirm whether or not they actually
     *  want to proceed with editing an item's name from their list.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditURLDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Need to implement EditURLDialogListener.");
        }
    }

    /**
     * Save the text entered in the EditText, so that it can be restored.
     *
     * A custom AlertDialog to display when a user tries to edit an item name from their list of items
     *  that will be tracked. This dialog will allow a user to confirm whether or not they actually
     *  want to proceed with editing an item's name from their list.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("input", input.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
