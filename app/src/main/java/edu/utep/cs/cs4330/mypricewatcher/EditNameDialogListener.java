package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the EditNameDialog dialog. This listener allows
 *  for the EditNameDialog to send a response describing what button was selected by a user
 *  on the dialog, and what input was entered.
 *
 * @author Damian Najera
 * @version 1.0
 */
public interface EditNameDialogListener {
    /**
     * The method to be implemented by whichever activity invokes the EditNameDialog dialog, so that
     * the activity can receive a response describing what a user entered on the dialog.
     *
     * @param d       The EditNameDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param newName  The new name of the item
     */
    void onItemName(EditNameDialog d, boolean proceed, String newName);
}
