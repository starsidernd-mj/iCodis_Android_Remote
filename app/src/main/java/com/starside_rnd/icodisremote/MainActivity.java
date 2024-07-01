package com.starside_rnd.icodisremote;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private IRController _irController;

    private ImageButton _buttonPowerAll;

    private final LinearLayout [] _button = new LinearLayout[17];

    private final LinearLayout [] _rows = new LinearLayout[5];

    private Vibrator _vibrator = null;

    private long _lastBurstTime = 0;     // Microsecconds
    private long _waitTime = 315000;     // Microsecconds
    
    private final int bits = 16;        // 16 bits with MSB for iCodis

    private IRMessages _irMessages;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _irMessages = new IRMessages();

        setContentView(R.layout.activity_main);

        _vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);

        _irController = new IRController(getApplicationContext());
        _irController.start_work();

        getRowReference();

        _buttonPowerAll = findViewById(R.id.id_Button_ALL);

        //row 0 = vol-, mute, vol+
        //row 1 = menu, settings, source
        //row 2 = freeze, aspect, rotate, picture, color
        //row 3 = back, ok
        //row 4 = left, up, down, right

        if(bits == 8) {
            _button[0] = createIRButton(R.drawable.vol_down,        _rows[0],   new IRMessageRequest(IRNECFactory.create(0xb7, 0xef, 0, "VOL -"   , bits)));
            _button[1] = createIRButton(R.drawable.mute,            _rows[0],   new IRMessageRequest(IRNECFactory.create(0xd7, 0xef, 0, "MUTE"    , bits)));
            _button[2] = createIRButton(R.drawable.vol_up,          _rows[0],   new IRMessageRequest(IRNECFactory.create(0x37, 0xef, 0, "VOL +"   , bits)));
            
            _button[3] = createIRButton(R.drawable.menu_burger,     _rows[1],   new IRMessageRequest(IRNECFactory.create(0xcf, 0xef, 0, "MENU"    , bits)));
            _button[4] = createIRButton(R.drawable.settings_sliders,_rows[1],   new IRMessageRequest(IRNECFactory.create(0x17, 0xef, 0, "SETTINGS", bits)));
            _button[5] = createIRButton(R.drawable.source,          _rows[1],   new IRMessageRequest(IRNECFactory.create(0x4f, 0xef, 0, "SOURCE"  , bits)));
            
            _button[6] = createIRButton(R.drawable.freeze,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0x8f, 0xef, 0, "FREEZE"  , bits)));
            _button[7] = createIRButton(R.drawable.aspect,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0xa7, 0xef, 0, "ASPECT"  , bits)));
            _button[8] = createIRButton(R.drawable.rotate,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0x0f, 0xef, 0, "ROTATE"  , bits)));
            _button[9] = createIRButton(R.drawable.picture,         _rows[2],   new IRMessageRequest(IRNECFactory.create(0x87, 0xef, 0, "PICTURE" , bits)));
            _button[10] = createIRButton(R.drawable.rad,            _rows[2],   new IRMessageRequest(IRNECFactory.create(0x0d, 0xef, 0, "COLOR"   , bits)));
            
            _button[11] = createIRButton(R.drawable.back,           _rows[3],   new IRMessageRequest(IRNECFactory.create(0x57, 0xef, 0, "BACK"    , bits)));
            _button[12] = createIRButton(R.drawable.ok,             _rows[3],   new IRMessageRequest(IRNECFactory.create(0xc7, 0xef, 0, "OK"      , bits)));
            
            _button[13] = createIRButton(R.drawable.left,           _rows[4],   new IRMessageRequest(IRNECFactory.create(0x75, 0xef, 0, "LEFT"    , bits)));
            _button[14] = createIRButton(R.drawable.up,             _rows[4],   new IRMessageRequest(IRNECFactory.create(0xf5, 0xef, 0, "UP"      , bits)));
            _button[15] = createIRButton(R.drawable.down,           _rows[4],   new IRMessageRequest(IRNECFactory.create(0x35, 0xef, 0, "DOWN"    , bits)));
            _button[16] = createIRButton(R.drawable.right,          _rows[4],   new IRMessageRequest(IRNECFactory.create(0xb5, 0xef, 0, "RIGHT"   , bits)));
        } else if(bits == 16) {
            _button[0] = createIRButton(R.drawable.vol_down,        _rows[0],   new IRMessageRequest(IRNECFactory.create(0x48b7, 0x10ef, 0, "VOL -"   , bits)));
            _button[1] = createIRButton(R.drawable.mute,            _rows[0],   new IRMessageRequest(IRNECFactory.create(0x28d7, 0x10ef, 0, "MUTE"    , bits)));
            _button[2] = createIRButton(R.drawable.vol_up,          _rows[0],   new IRMessageRequest(IRNECFactory.create(0xc837, 0x10ef, 0, "VOL +"   , bits)));
            
            _button[3] = createIRButton(R.drawable.menu_burger,     _rows[1],   new IRMessageRequest(IRNECFactory.create(0x30cf, 0x10ef, 0, "MENU"    , bits)));
            _button[4] = createIRButton(R.drawable.settings_sliders,_rows[1],   new IRMessageRequest(IRNECFactory.create(0xe817, 0x10ef, 0, "SETTINGS", bits)));
            _button[5] = createIRButton(R.drawable.source,          _rows[1],   new IRMessageRequest(IRNECFactory.create(0xb04f, 0x10ef, 0, "SOURCE"  , bits)));
            
            _button[6] = createIRButton(R.drawable.freeze,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0x708f, 0x10ef, 0, "FREEZE"  , bits)));
            _button[7] = createIRButton(R.drawable.aspect,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0x58a7, 0x10ef, 0, "ASPECT"  , bits)));
            _button[8] = createIRButton(R.drawable.rotate,          _rows[2],   new IRMessageRequest(IRNECFactory.create(0xf00f, 0x10ef, 0, "ROTATE"  , bits)));
            _button[9] = createIRButton(R.drawable.picture,         _rows[2],   new IRMessageRequest(IRNECFactory.create(0x7887, 0x10ef, 0, "PICTURE" , bits)));
            _button[10] = createIRButton(R.drawable.rad,            _rows[2],   new IRMessageRequest(IRNECFactory.create(0xf20d, 0x10ef, 0, "COLOR"   , bits)));
            
            _button[11] = createIRButton(R.drawable.back,           _rows[3],   new IRMessageRequest(IRNECFactory.create(0xa857, 0x10ef, 0, "BACK"    , bits)));
            _button[12] = createIRButton(R.drawable.ok,             _rows[3],   new IRMessageRequest(IRNECFactory.create(0x38c7, 0x10ef, 0, "OK"      , bits)));
            
            _button[13] = createIRButton(R.drawable.left,           _rows[4],   new IRMessageRequest(IRNECFactory.create(0x8a75, 0x10ef, 0, "LEFT"    , bits)));
            _button[14] = createIRButton(R.drawable.up,             _rows[4],   new IRMessageRequest(IRNECFactory.create(0x0af5, 0x10ef, 0, "UP"      , bits)));
            _button[15] = createIRButton(R.drawable.down,           _rows[4],   new IRMessageRequest(IRNECFactory.create(0xca35, 0x10ef, 0, "DOWN"    , bits)));
            _button[16] = createIRButton(R.drawable.right,          _rows[4],   new IRMessageRequest(IRNECFactory.create(0x4ab5, 0x10ef, 0, "RIGHT"   , bits)));
        }

        _lastBurstTime = System.nanoTime();

        for(int a = 0; a < 17; a++) {
            _button[a].setOnTouchListener(this);
        }
        _buttonPowerAll.setOnTouchListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("iCodis", "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("iCodis","onRestart");
    }

    @Override
    protected void onDestroy() {
        Log.d("iCodis", "onDestroy");
        _irController.stop_work();
        super.onDestroy();
    }

    private void getRowReference() {
        _rows[0] = findViewById(R.id.id_Layout_Row_01);
        _rows[1] = findViewById(R.id.id_Layout_Row_02);
        _rows[2] = findViewById(R.id.id_Layout_Row_03);
        _rows[3] = findViewById(R.id.id_Layout_Row_04);
        _rows[4] = findViewById(R.id.id_Layout_Row_05);
    }

    @SuppressLint("SetTextI18n")
    private LinearLayout createIRButton(int imageResource, LinearLayout row, IRMessageRequest request) {
        View child = getLayoutInflater().inflate(R.layout.widget_button, findViewById(android.R.id.content), false);

        RelativeLayout mainLayout = child.findViewById(R.id.layout_main);
        LinearLayout buttonLayout = child.findViewById(R.id.layout_button);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;
        mainLayout.setLayoutParams(lp);

        ImageView imageView = child.findViewById(R.id.image_main);
        TextView titleView  = child.findViewById(R.id.text_title);

        imageView.setImageResource(imageResource);
        titleView.setText(request._req._name);

        row.addView(child);

        buttonLayout.setTag(request);

        Log.d("iCodis", "New button: "+request._req._cmd+" "+request._req._addr);

        return buttonLayout;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // Microsecconds
        long _waitTimeMin = 300000;
        _waitTime = Math.max(_waitTime, _waitTimeMin);
        // Microsecconds
        long _waitTimeMax = 1000000;
        _waitTime = Math.min(_waitTime, _waitTimeMax);

        if((System.nanoTime() - _lastBurstTime) > (_waitTime * 1000)) {
            int ev = motionEvent.getActionMasked();
            if(ev == MotionEvent.ACTION_MOVE) {
                view.performClick();
                _lastBurstTime = System.nanoTime();
                if(view == _buttonPowerAll) {
                    _waitTime = sendIRMessage(new IRMessageRequest(IRNECFactory.create(0xd02f, 0x10ef, 0, "POWER", 16)));
                } else {
                    IRMessageRequest tmp = (IRMessageRequest)view.getTag();
                    Log.d("iCodis", tmp._req._name.toString()+", cmd: "+tmp._req._cmd+", addr: "+tmp._req._addr);
                    _waitTime = sendIRMessage(tmp);
                }
                return true;
            }
        }
        return false;
    }

    public long sendIRMessage(IRMessageRequest request) {
        _vibrator.vibrate(60);
        return _irController.sendMessage(request);
    }
}