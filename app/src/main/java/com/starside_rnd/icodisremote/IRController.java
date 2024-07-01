package com.starside_rnd.icodisremote;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class IRController extends Thread {

    private final ConsumerIrManager _irManager;
    private boolean _enabled = false;

    private final Object _messageLock = new Object();
    private final Queue<IRMessageRequest> _messageQueue = new LinkedList<>();

    public IRController(Context context) {
        _irManager = (ConsumerIrManager) context.getSystemService(Context.CONSUMER_IR_SERVICE);

        if (_irManager != null) {
            if (_irManager.hasIrEmitter()) {
                _enabled = true;
            }
        }
        
        ConsumerIrManager.CarrierFrequencyRange[] frequencyRanges = _irManager.getCarrierFrequencies();
        
        if (frequencyRanges != null) {
            StringBuilder frequencies = new StringBuilder("Supported Frequencies:\n");
            for (ConsumerIrManager.CarrierFrequencyRange range : frequencyRanges) {
                frequencies.append(range.getMinFrequency())
                        .append(" - ")
                        .append(range.getMaxFrequency())
                        .append(" Hz\n");
            }
            Log.d("iCodis", frequencies.toString());
        }
    }
    
    public static String getHexCode(int[] intArray) {
        StringBuilder hexString = new StringBuilder();
        for (int num : intArray) {
            hexString.append(Integer.toHexString(num)).append(" ");
        }
        return hexString.toString().trim();
    }

    private void executeMessage(IRMessageRequest request) {
        if(_enabled) {
            ArrayList<IRMessage> messages = request.get_messages();
            for(int a = 0; a < messages.size(); a++) {
                IRMessage message = messages.get(a);

                int frequency = message.get_frequency();
                int [] codes = message.get_message();
                String code_string = getHexCode(codes);
                
                Log.d("iCodis", "Executing: f"+frequency+"Hz, Code: "+code_string);

                _irManager.transmit(frequency, codes);
                //Toast.makeText(this, "IR Signal sent!", Toast.LENGTH_SHORT).show();
                WaitPerMessage();
            }
        }
    }

    public long sendMessage(IRMessageRequest request) {
        synchronized (_messageLock) {
            int MAX_QUEUED_COUNT = 2;
            if(_messageQueue.size() < MAX_QUEUED_COUNT) {
                _messageQueue.add(request);
                _messageLock.notify();
            }
        }

        if(request != null) {
            return request.getRequestTime();
        } else {
            return 0;
        }
    }

    public void start_work() {
        this.start();
    }

    public void stop_work() {
        sendMessage(null);
        try {
            join();
        } catch(InterruptedException e) {
            Log.d("iCodis", "interrupted");
        }
    }

    @Override
    public void run() {
        Log.d("iCodis", "Worker starts");
        while(true) {
            IRMessageRequest message = null;
            synchronized (_messageLock) {
                if(!_messageQueue.isEmpty()) {
                    message = _messageQueue.poll();

                    if(message == null) {
                        Log.d("iCodis", "Closing Worker");
                        break;
                    }
                } else {
                    try {
                        _messageLock.wait();
                    } catch(InterruptedException e) {
                        Log.d("iCodis", "Interrupted");
                    }
                }
            }
            if(message != null) {
                Log.d("iCodis", "Executing message");
                executeMessage(message);
            }

            WaitPerPulse();
        }
        Log.d("iCodis", "Worker Stops");
    }

    private void WaitPerPulse() {
        try {
            int MAX_WAIT_PER_PULSE_MS = 40;
            Thread.sleep(MAX_WAIT_PER_PULSE_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void WaitPerMessage() {
        try {
            int MAX_WAIT_PER_MESSAGE_MS = 40;
            Thread.sleep(MAX_WAIT_PER_MESSAGE_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
