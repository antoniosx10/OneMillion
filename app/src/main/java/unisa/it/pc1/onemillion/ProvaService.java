package unisa.it.pc1.onemillion;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.IntentService;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by PC1 on 08/03/2018.
 */

public class ProvaService extends Service {

    private WindowManager mWindowManager;
    private View mChatHeadView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {

                Intent in = new Intent();
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString();
                Log.d("Testo",text);
                in.putExtra("TYPE", text);
                in.setAction("todashcopy");
                sendBroadcast(in);
                //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

}
