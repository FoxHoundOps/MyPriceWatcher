package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the AddItemDialog dialog. This listener allows
 *  for the AddItemDialog to send a response describing what button was entered by a user
 *  on the dialog.
 *
 * @author Damian Najera
 * @version 1.0
 */

public interface AddItemDialogListener {
    /**
     * The method to be implemented by whichever activity invokes the AddItemDialog dialog, so that
     * the activity can receive a response describing what a user entered from the dialog.
     *
     * @param proceed A boolean describing whether a user selected the positive or negative button
     */
    void onUserInput(AddItemDialog d, boolean proceed, String input);
}
