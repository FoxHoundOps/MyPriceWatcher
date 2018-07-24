package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the AddItemDialog dialog. This listener allows
 *  for the AddItemDialog to send a response describing what button was entered by a user
 *  on the dialog, along with the string, if one was entered.
 *
 * @author Damian Najera
 * @version 1.0
 */

public interface AddItemDialogListener {
    /**
     * The method to be implemented by whichever activity invokes the AddItemDialog dialog, so that
     * the activity can receive a response describing what a user entered from the dialog.
     *
     * @param d       The AddItemDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param input   The string input that was entered by the user, if any
     */
    void onUserInput(AddItemDialog d, boolean proceed, String input);
}
