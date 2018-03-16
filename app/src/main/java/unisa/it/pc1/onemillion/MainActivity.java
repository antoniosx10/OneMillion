package unisa.it.pc1.onemillion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private View mChatHeadView;
    private WindowManager mWindowManager;
    private Handler handler;
    private Runnable runnable;

    private int countCopy = 0;

    private TextView prova;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1000);
        }
        Intent serviceIntent = new Intent(getApplicationContext(), ProvaService.class);
        startService(serviceIntent);

        prova = findViewById(R.id.provaTesto);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(handler != null) {
                stopTimerHead();
                if (mChatHeadView != null) {
                    mWindowManager.removeView(mChatHeadView);
                    mChatHeadView = null;
                }
            }
            final String type = intent.getStringExtra("TYPE");  //get the type of message from MyGcmListenerService 1 - lock or 0 -Unlock

            if(mChatHeadView == null){
                mChatHeadView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_head, null);

                //Add the view to the window.
                final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);

                //Specify the chat head position
                //Initially view will be added to top-left corner
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                params.x = 0;
                params.y = 100;

                //Add the view to the window
                mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
                mWindowManager.addView(mChatHeadView, params);
                startTimerHead();

                mChatHeadView.setOnTouchListener(new View.OnTouchListener() {
                    private int lastAction;
                    private int initialX;
                    private int initialY;
                    private float initialTouchX;
                    private float initialTouchY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        //prova.setText(event.getAction());
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                stopTimerHead();
                                //remember the initial position.
                                initialX = params.x;
                                initialY = params.y;

                                //get the touch location
                                initialTouchX = event.getRawX();
                                initialTouchY = event.getRawY();

                                lastAction = event.getAction();
                                return true;
                            case MotionEvent.ACTION_UP:
                                //As we implemented on touch listener with ACTION_MOVE,
                                //we have to check if the previous action was ACTION_DOWN
                                //to identify if the user clicked the view or not.
                                if (lastAction == MotionEvent.ACTION_DOWN) {
                                    Intent intent = new Intent(MainActivity.this, Lista.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("messaggio",type);
                                    startActivity(intent);
                                    if (mChatHeadView != null) {
                                        mWindowManager.removeView(mChatHeadView);
                                        mChatHeadView = null;
                                    }
                                }
                                startTimerHead();
                                lastAction = event.getAction();
                                return true;
                            case MotionEvent.ACTION_MOVE:
                                stopTimerHead();
                                //Calculate the X and Y coordinates of the view.
                                //params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY + (int) (event.getRawY() - initialTouchY);

                                //Update the layout with new X & Y coordinate
                                mWindowManager.updateViewLayout(mChatHeadView, params);
                                int differenza = 0;
                                if(params.y > initialY) {
                                    differenza = params.y - initialY;
                                } else {
                                    differenza = initialY - params.y;
                                }

                                if(differenza > 0.3)
                                    lastAction = event.getAction();

                                return true;
                        }
                        return false;
                    }
                });


            }
        }
    };
    @Override
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("todashcopy"));
    }


/*
    public void prova(View v){
        ActivityOptions options = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(this, Lista.class);
            options = ActivityOptions.makeSceneTransitionAnimation(this,mChatHeadView, ViewCompat.getTransitionName(mChatHeadView));
            startActivity(intent, options.toBundle());
        }

    }
    */

    private void startTimerHead(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mChatHeadView != null) {
                    mWindowManager.removeView(mChatHeadView);
                    mChatHeadView = null;
                }

            }
        };
        handler.postDelayed(runnable,4000);

    }
    private void stopTimerHead(){
        handler.removeCallbacks(runnable);
    }


}
