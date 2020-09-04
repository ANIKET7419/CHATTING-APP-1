package com.example.anichat2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    ServerSocket so = null;
    Socket s = null;
    TextView t = null;
    Button send = null;
    EditText message = null;
    EditText received = null;
    DataOutputStream output = null;
    DataInputStream input = null;
    Handler handle=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = findViewById(R.id.waiting);
        handle=new Handler();
        send = findViewById(R.id.send);
        message = findViewById(R.id.message);
        received = findViewById(R.id.received);
        send.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        thread1 thread = new thread1();
        thread.start();

    }

    @Override
    public void onClick(View view) {
        thread3 thread = new thread3();
        thread.start();
    }

    class thread3 extends Thread {
        public void run() {
            try {
                output.writeUTF(message.getText().toString());
                message.setText("");
            } catch (Exception e) {
                message.setText(e.toString());
            }
        }
    }

    class thread1 extends Thread {

        public void run() {
            t.setText("Waiting for connection");
            try {

                so = new ServerSocket(2001);
                s = so.accept();
                t.setText("Connection Has Beeen Established....,,,,,Wait for a moment");


                input = new DataInputStream(s.getInputStream());
                output = new DataOutputStream(s.getOutputStream());
                Thread.sleep(1000);
                handle.post(new Runnable() {
                    @Override
                    public void run() {


                            t.setVisibility(View.INVISIBLE);
                            send.setVisibility(View.VISIBLE);
                            message.setVisibility(View.VISIBLE);
                            received.setVisibility(View.VISIBLE);
                            send.setClickable(true);


                    }
                });
                while(!Thread.currentThread().isInterrupted())
                {
                    String update=input.readUTF();
                    handle.post(new thread5(update));
                }


            } catch (Exception e) {
                t.setText(e.toString());
            }


        }

    }




    class thread5 implements Runnable
    {
        String update=null;
        public thread5(String update)
        {
            this.update=update;

        }
        @Override
        public  void run(){
            try {
            received.append(" "+update);
                    }
            catch (Exception e)
            {
                try {

                    received.append(e.toString());
                }
                catch (Exception e1)
                {

                }
            }
                }
            }

        }






