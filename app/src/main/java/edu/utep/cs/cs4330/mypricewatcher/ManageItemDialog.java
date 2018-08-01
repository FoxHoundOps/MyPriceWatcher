package edu.utep.cs.cs4330.mypricewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  A custom AlertDialog to display when a user wants to add/edit an item. This dialog will allow
 *  a user to confirm whether or not they actually want to proceed with adding/editing
 *  the item from their list.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class ManageItemDialog extends AppCompatDialogFragment {
    private EditText itemName;                  /* Input field for item's name */
    private EditText itemURL;                   /* Input field for item's URL */
    private ManageItemDialogListener listener;  /* The internal listener for this EditURLDialog */

    /**
     * Constructor for the dialog that allows an Item to be passed in (for editing).
     *
     * @param item  The Item to edit
     * @return      The dialog
     */
    public static ManageItemDialog newInstance(Item item) {
        ManageItemDialog d = new ManageItemDialog();
        Bundle args = new Bundle();
        args.putString("name", item.getName());
        args.putString("url", item.getURL());
        args.putString("action", "edit");
        d.setArguments(args);
        return d;
    }

    /**
     * Constructor for the dialog when no Item is passed in (for new adding items).
     *
     * @return      The dialog
     */
    public static ManageItemDialog newInstance() {
        ManageItemDialog d = new ManageItemDialog();
        Bundle args = new Bundle();
        args.putString("name", "");
        args.putString("url", "");
        args.putString("action", "add");
        d.setArguments(args);
        return d;
    }

    /**
     * Create the dialog that allows a user to add/edit an item.
     *
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String action = getArguments().getString("action");
        View fields = getActivity().getLayoutInflater().inflate(R.layout.manage_item_fields, null);
        itemName = (EditText) fields.findViewById(R.id.item_name_edit);
        itemURL = fields.findViewById(R.id.item_url_edit);

        if (action.equals("edit")) {
            String name = getArguments().getString("name");
            String url = getArguments().getString("url");

            if (savedInstanceState != null) {
                itemName.setText(savedInstanceState.getString("name"));
                itemURL.setText(savedInstanceState.getString("url"));
            }
            else {
                itemName.setText(name, TextView.BufferType.EDITABLE);
                itemURL.setText(url, TextView.BufferType.EDITABLE);
            }
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Edit Item")
                    .setView(fields)
                    .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> {
                        if (!itemName.getText().toString().equals("") && !itemURL.getText().toString().equals(""))
                            listener.onItemManaged((ManageItemDialog) getParentFragment(), itemName.getText().toString(), itemURL.getText().toString(), true, false);
                        else
                            Toast.makeText(getContext(), "Invalid Fields. Unable to edit item.", Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton(R.string.no_dialog_button_label, ((dialogInterface, i) -> listener.onItemManaged(this, "", "", false, false)))
                    .create();
        }
        else
            return new AlertDialog.Builder(getActivity())
                    .setTitle("Add Item")
                    .setView(fields)
                    .setPositiveButton(R.string.yes_dialog_button_label, (dialogInterface, i) -> {
                        if (!itemName.getText().toString().equals("") && !itemURL.getText().toString().equals(""))
                            listener.onItemManaged(this, itemName.getText().toString(), itemURL.getText().toString(), true, true);
                        else
                            Toast.makeText(getContext(), "Invalid Fields. Unable to add item.", Toast.LENGTH_LONG).show();
                    })
                    .setNegativeButton(R.string.no_dialog_button_label, ((dialogInterface, i) -> listener.onItemManaged(this, "", "", false, false)))
                    .create();

    }

    /**
     * Attach the implemented ManageItemDialog from the activity interested in the response from
     * the ManageItemDialog dialog.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ManageItemDialogListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString() + "Need to implement ManageDialogListener.");
        }
    }

    /**
     * Save the text entered in the EditText fields, so that they can be restored.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("name", itemName.getText().toString());
        outState.putString("url", itemURL.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
