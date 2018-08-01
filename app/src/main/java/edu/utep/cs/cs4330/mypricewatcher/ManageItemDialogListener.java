package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  Listener to be implemented when using the ManageItemDialog dialog. This listener allows
 *  for the ManageItemDialog to send a response describing what button was entered by a user
 *  on the dialog.
 *
 * @author Damian Najera
 * @version 1.0
 */
public interface ManageItemDialogListener {
    /**
     *
     * @param d             The ManageItemDialog instance that is returning a response
     * @param itemName      The item name that was entered
     * @param url           The Web url that was entered
     * @param proceed       Boolean, whether or not positive or negative button was pressed
     * @param newItem       Boolean, whether the item is a new item or not
     */
    void onItemManaged(ManageItemDialog d, String itemName, String url, boolean proceed, boolean newItem);
}
