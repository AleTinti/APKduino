package it.aletinti.apkduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class activity1 extends AppCompatActivity {

    BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    ListView btDeviceList;
    String btDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);

        //activity2();
        Intent btEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(btEnable, 1);

        btDeviceList = findViewById(R.id.btDeviceList);

        int i = 0;
        while(i = 100){
            String btMac[];
            btMac[i] = (btAdapter.toString()).split(" /", 1);
        }

        final Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        List<String> ArrayList = new ArrayList();
        for(BluetoothDevice btAdapter : pairedDevices) ArrayList.add(btAdapter.getName());
        final ArrayAdapter btDeviceArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ArrayList);
        System.out.println()
        btDeviceList.setAdapter(btDeviceArrayAdapter);

        btDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btDevice = (btDeviceArrayAdapter.getItem(position)).toString();
                //activity2();
            }
        });
    }

    public void activity2(){
        Intent NextActivity = new Intent(this, activity2.class);
        //NextActivity.putExtra("btDevice", btDevice);
        startActivity(NextActivity);
    }
}
