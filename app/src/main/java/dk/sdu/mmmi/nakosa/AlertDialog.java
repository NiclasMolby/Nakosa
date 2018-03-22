package dk.sdu.mmmi.nakosa;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog.Builder;

/**
 * Created by niclasmolby on 21/03/2018.
 */

public class AlertDialog extends DialogFragment {

    AlertDialogNotifier notifier;

    public interface AlertDialogNotifier {
        void onPositiveClick(DialogFragment dialogFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            notifier = (AlertDialogNotifier) activity.getFragmentManager().findFragmentById(R.id.fragment);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        Builder builder = new Builder(getActivity());
        builder.setMessage("Are you sure, that you want to delete it?")
                .setPositiveButton("Delete it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notifier.onPositiveClick(AlertDialog.this);
                    }
                })
                .setNegativeButton("Cancel!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
