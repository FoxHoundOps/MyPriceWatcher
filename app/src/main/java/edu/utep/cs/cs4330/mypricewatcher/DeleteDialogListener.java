package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the DeleteDialog dialog. This listener allows
 *  for the DeleteDialog to send a response describing what button was selected by a user
 *  on the dialog.
 *
 * @author Damian Najera
 * @version 1.0
 */
public interface DeleteDialogListener {
    /**
     * The method to be implemented by whichever activity invokes the DeleteDialog dialog, so that
     * the activity can receive a response describing what a user selected from the dialog.
     *
     * @param d       The DeleteItemDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     */
    void onResponse(DeleteDialog d, boolean proceed);
}
