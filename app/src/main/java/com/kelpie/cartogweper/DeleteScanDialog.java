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
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class DeleteScanDialog extends AppCompatDialogFragment {

    RadioGroup rg;
    DeleteDialogListener listener;
    String item;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_load, null);

        // Create radio group
        String[]array = getActivity().databaseList();
        rg = (RadioGroup)view.findViewById(R.id.rg);
        RadioButton[] rb = new RadioButton[array.length];

        for (int i = 0; i < array.length; i++){
            if (array[i].contains("-journal")){
                // Do nothing
            }
            else{
                rb[i] = new RadioButton(getActivity());
                rb[i].setText(array[i]);
                rb[i].setId(i);
                rg.addView(rb[i]);
            }
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioButtonId = rg.getCheckedRadioButtonId();
                RadioButton radioBtn = (RadioButton) view.findViewById(checkedRadioButtonId);
                item = radioBtn.getText().toString();
            }
        });

        builder.setView(view)
                .setTitle("Load Scan")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyChange(item, false, true);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (DeleteDialogListener) context;
        }
        catch(ClassCastException e){
            Log.i("INFO", "must implement ExampleDialogListener");
            e.printStackTrace();
        }
    }

    public interface DeleteDialogListener{
        void applyChange(String name, Boolean create, Boolean delete);
    }
}
