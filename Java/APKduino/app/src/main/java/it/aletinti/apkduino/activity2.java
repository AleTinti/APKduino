package it.aletinti.apkduino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class activity2 extends AppCompatActivity {

    private static final UUID btUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView stateText, maxText, minText;
    SeekBar maxBar, minBar;
    int minTemp, maxTemp;
    String h, t;
    BluetoothSocket btSocket = null;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        maxBar = this.findViewById(R.id.maxBar);
        minBar = this.findViewById(R.id.minBar);
        updateButton = this.findViewById(R.id.updateButton);
        maxText = this.findViewById(R.id.maxText);
        minText = this.findViewById(R.id.minText);
        stateText = this.findViewById(R.id.stateText);

        maxBar.setProgress(3);
        minBar.setProgress(3);

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    maxTemp = 24 + maxBar.getProgress();
                    minTemp = 20 - minBar.getProgress();
                    minText.setText("Min: " + minTemp);
                    maxText.setText("Max: " + maxTemp);
                    System.out.print(activity1.toSend);
                    BluetoothDevice btDevice = btAdapter.getRemoteDevice(activity1.toSend);
                    btSocket = btDevice.createRfcommSocketToServiceRecord(btUUID);
                    btSocket.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
                        public void run() {
                while (!isDestroyed()) {
                    try {
                        byte[] buffer = new byte[1024];
                        int bytes = btSocket.getInputStream().read(buffer, 0, buffer.length);
                        String readMessage = new String(buffer, 0, bytes);
                        System.out.println(readMessage);
                        t = readMessage.split("/")[0];
                        h = readMessage.split("/")[1];
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stateText.setText("Temperature: " + t + "°" + "\nHumidity: " + h + "%");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        minBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minTemp = 20 - minBar.getProgress();
                minText.setText("Min: " + minTemp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        maxBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxTemp = 24 + maxBar.getProgress();
                maxText.setText("Max: " + maxTemp);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    maxTemp = 24 + maxBar.getProgress();
                    minTemp = 20 - minBar.getProgress();
                    String msg = (maxTemp + "/" + minTemp);
                    btSocket.getOutputStream().write(msg.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
