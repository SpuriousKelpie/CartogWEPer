package uk.spurious.kelpie.cartogweper;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ListviewFragment extends Fragment implements View.OnClickListener {

    Button b1;

    public ListviewFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listview, container, false);

        b1 = (Button)view.findViewById(R.id.button);
        b1.setOnClickListener(this);

        TextView tv1 = (TextView)view.findViewById(R.id.ssid2);
        TextView tv2 = (TextView)view.findViewById(R.id.mac2);
        TextView tv3 = (TextView)view.findViewById(R.id.proto2);
        TextView tv4 = (TextView)view.findViewById(R.id.lati2);
        TextView tv5 = (TextView)view.findViewById(R.id.longi2);
        TextView tv6 = (TextView)view.findViewById(R.id.rssi2);
        TextView tv7 = (TextView)view.findViewById(R.id.band2);

        Bundle bundle = this.getArguments();

        if (bundle != null){
            if (bundle.getString("ssid") != null){
                tv1.setText(bundle.getString("ssid"));
                tv2.setText(bundle.getString("mac"));
                tv3.setText(bundle.getString("proto"));
                tv4.setText(bundle.getString("lati"));
                tv5.setText(bundle.getString("longi"));
                tv6.setText(bundle.getString("rssi"));
                tv7.setText(bundle.getString("band"));
            }
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();    }
}
