package com.app.luberack.utility;

/**
 * Created by Abubakar on 21/08/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.app.luberack.Profile_management.SessionManager;
import com.app.luberack.R;


//for showing alert dialog
public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     *
     * @param context - application context
     * @param title   - alert dialog title
     * @param message - alert message
     * @param status  - success/failure (used to set icon)
     *                - pass null if you don't want icon
     */
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        if ((Boolean) false != null)
            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                        }
                    });
        // Showing Alert Message
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }
    //Showing alert if location is not enabled
    public void showLocationAlertDialog(final Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.gps_network_not_enabled));

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                //get gps
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });

        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

    //Showing alert if account is deleted by admin
    public void showAccountDeletedAlertDialog(Context context, String title, String message, Boolean status) {
        final SessionManager session;
        session = new SessionManager(context);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);

        if ((Boolean) false != null)
            // Setting OK Button
            alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            session.logoutUser();
                        }
                    });
        // Showing Alert Message
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.colorPrimary));
    }

}