package com.game.pad;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Joystick.Joystick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private Button button;
    private ListView listView;
    private ImageView imageView;
    private Joystick joystick;
    Rect rect_canvas = new Rect(0, 0, 600, 600);
    int Rayon = rect_canvas.width() / 8;
    private Vibrator vibrator;
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        button = findViewById(R.id.button);
        listView = findViewById(R.id.listview);
        imageView = findViewById(R.id.imageView);
        joystick = new Joystick(imageView, rect_canvas.width(), rect_canvas.height());
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        bluetooth = new Bluetooth(this);
        button.setBackgroundColor(Color.BLACK);
        button.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        imageView.setOnTouchListener(this);

    }

    @SuppressLint("MissingPermission")
    public void onClick(View view) {
        vibrator.vibrate(100);
        switch (view.getId()) {
            case R.id.button:
                bluetooth.refresh();
                ArrayList <String> nameDevices = new ArrayList<>();
                if(bluetooth.pairedDevices.size() > 0)
                    for(BluetoothDevice device : bluetooth.pairedDevices)
                        nameDevices.add(device.getName());
                listView.setAdapter(new ArrayAdapter <> (this, android.R.layout.simple_list_item_1, nameDevices));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
       if(bluetooth.connect((BluetoothDevice) bluetooth.pairedDevices.toArray()[i]))
            adapterView.setAdapter(null);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_UP:
                joystick.circTouch(rect_canvas.centerX(), rect_canvas.centerY(), Rayon);
                break;

            case MotionEvent.ACTION_DOWN:
                if(
                        Math.abs(Math.abs(motionEvent.getX()) - rect_canvas.centerX()) > Rayon / 1.5 ||
                                Math.abs(Math.abs(motionEvent.getY()) - rect_canvas.centerY()) > Rayon / 1.5
                )

                    joystick.enabled = false;
                else {
                    vibrator.vibrate(100);
                    joystick.enabled = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                joystick.circTouch(motionEvent.getX(), motionEvent.getY(), Rayon);
                String str =  joystick.x + "," + joystick.y + ":" + joystick.angle;
                System.out.println(" x: "+ joystick.x + " y: " + joystick.y + " a: " + joystick.angle);
                bluetooth.send(str.getBytes());
                break;
            default:
                break;
        }
        return true;
    }

}










