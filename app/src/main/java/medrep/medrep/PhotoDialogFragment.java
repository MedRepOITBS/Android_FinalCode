package medrep.medrep;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by masood on 8/15/15.
 */
public class PhotoDialogFragment extends DialogFragment {

    PhotoSelectedListener photoSelectedListener;

    public interface PhotoSelectedListener{
        void selectedGalleryApp();
        void selectedCameraApp();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            photoSelectedListener = (PhotoSelectedListener)activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString() + " must implement PhotoSelectedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Photo Dialog")
                .setItems(R.array.list_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /* User clicked so do some stuff */
                        String[] items = getResources().getStringArray(R.array.list_items);
                        if (items[which].equalsIgnoreCase("Gallery")) {
                            photoSelectedListener.selectedGalleryApp();


                        } else if (items[which].equalsIgnoreCase("Camera")) {
                            photoSelectedListener.selectedCameraApp();
                        }
                        /*new AlertDialog.Builder(getActivity())
                                .setMessage("You selected: " + which + " , " + items[which])
                                .show();*/
                    }
                })
                .create();
    }
}
