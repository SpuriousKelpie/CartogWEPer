package com.kelpie.cartogweper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

// TODO: Validate user input before accepting (empty? db already exist?)

public class CreateScanDialog extends AppCompatDialogFragment {

    EditText ed;
    CreateDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create, null);

        builder.setView(view)
                .setTitle("Create Scan")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = ed.getText().toString();
                        listener.applyChange(name, true, false);
                    }
                });

        ed = view.findViewById(R.id.ed);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (CreateDialogListener) context;
        }
        catch(ClassCastException e){
            Log.i("INFO", "must implement ExampleDialogListener");
            e.printStackTrace();
        }
    }

    public interface CreateDialogListener{
        void applyChange(String name, Boolean create, Boolean delete);
    }
}
