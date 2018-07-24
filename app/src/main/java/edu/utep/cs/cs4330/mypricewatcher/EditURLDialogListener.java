package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the EditURLDialog dialog. This listener allows
 *  for the EditURLDialog to send a response describing what button was selected by a user
 *  on the dialog, and what input was entered.
 *
 * @author Damian Najera
 * @version 1.0
 */
public interface EditURLDialogListener {
    /**
     * The method to be implemented by whichever activity invokes the EditURLDialog dialog, so that
     * the activity can receive a response describing what a user entered on the dialog.
     *
     * @param d       The EditURLDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param newURL  The new URL of the item
     */
    void onItemURL(EditURLDialog d, boolean proceed, String newURL);

}
