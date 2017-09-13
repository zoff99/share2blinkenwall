package com.zoffcc.applications.sharetoclipboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import static com.zoffcc.applications.sharetoclipboard.MainActivity.Blinkenwall_WS_url_youtube;
import static com.zoffcc.applications.sharetoclipboard.MainActivity.USE_HTTPS;

public class ShareToClipboardActivity extends Activity
{

    private static final String PLAIN_TEXT_TYPE = "text/plain";
    private static OkHttpClient client = null;
    private static final String TAG = "ShareTClipbdActy";
    static Request request = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {

            client = new OkHttpClient();
            if (USE_HTTPS)
            {
                request = new Request.Builder().url("wss://" + Blinkenwall_WS_url_youtube).build();
            }
            else
            {
                request = new Request.Builder().url("ws://" + Blinkenwall_WS_url_youtube).build();
            }

            Log.i(TAG, "onCreate:001:" + client);
            Log.i(TAG, "onCreate:002:" + request);

            // Get intent, action and MIME type
            Intent intent = getIntent();
            String action = intent.getAction();
            String type = intent.getType();
            String scheme = intent.getScheme();

            if (Intent.ACTION_SEND.equals(action) && type != null)
            {
                Log.i(TAG, "onCreate:003:");
                if (PLAIN_TEXT_TYPE.equals(type))
                {
                    Log.i(TAG, "onCreate:004:");
                    if (!handleSendText(intent))
                    {
                        Log.i(TAG, "onCreate:005:");
                        showToast(getString(R.string.error_no_data));
                    }
                }
                else if (!handleSendText(intent))
                {
                    Log.i(TAG, "onCreate:006:");
                    showToast(getString(R.string.error_type_not_supported));
                }
            }
            else if ((Intent.ACTION_VIEW.equals(action) || Intent.ACTION_DIAL.equals(action)))
            {
                Log.i(TAG, "onCreate:007:");
                handleSchemeSpecificPart(intent);
            }

            Log.i(TAG, "onCreate:099:");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(TAG, "onCreate:EE1:" + e.getMessage());
        }

        finish();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Log.i(TAG, "onPause:099:");
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        Log.i(TAG, "onStop:099:");
    }

    @Override
    protected void onDestroy()
    {
        Log.i(TAG, "onDestroy:099:");

        super.onDestroy();
    }

    static void send_url_to_WS(String youtube_url)
    {
        Log.i(TAG, "send_url_to_WS:001:");
        WSListener listener = new WSListener(youtube_url);
        Log.i(TAG, "send_url_to_WS:002:");
        WebSocket ws = client.newWebSocket(request, listener);
        Log.i(TAG, "send_url_to_WS:003:");

        Log.i(TAG, "send_url_to_WS:098:");
        client.dispatcher().executorService().shutdown();
        Log.i(TAG, "send_url_to_WS:099:");
    }

    static void output(final String txt)
    {
        Log.i(TAG, "text=" + txt);
    }

    private void handleSchemeSpecificPart(Intent intent)
    {
        String dataString = "";
        Uri uri = intent.getData();

        if (uri != null)
        {
            dataString = uri.getSchemeSpecificPart();
        }

        if (dataString.length() > 0)
        {
            Log.i(TAG, "copyToClipboard:001");
            copyToClipboard(uri.getScheme() + ":" + dataString);

        }
        else
        {
            //If no scheme retrieved, try get it as send intent
            handleSendText(intent);
        }
    }

    private String arrayToString(Object[] objectArray)
    {
        String return_value = "";
        for (Object value : objectArray)
        {
            if (!value.toString().equals("pref"))
            {
                return_value += value.toString().substring(0, 1).toUpperCase() + value.toString().substring(1).toLowerCase();
            }
        }
        if (return_value.equals(""))
        {
            return_value = getString(R.string.other);
        }
        return return_value;
    }

    private void showToast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private boolean handleSendText(Intent intent)
    {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        String sharedTitle = intent.getStringExtra(Intent.EXTRA_SUBJECT);
        if (sharedText == null && sharedTitle == null)
        {
            return false;
        }
        if (sharedText != null)
        {
            if (sharedTitle != null && !sharedText.contains(sharedTitle))
            {
                sharedText = String.format("%s - %s", sharedTitle, sharedText);
            }
            Log.i(TAG, "copyToClipboard:002");
            copyToClipboard(sharedText);
        }
        else
        {
            Log.i(TAG, "copyToClipboard:003");
            copyToClipboard(sharedTitle);
        }
        return true;
    }

    @SuppressLint("NewApi")
    private void copyToClipboard(String clipboardText)
    {
        Log.i(TAG, "url=" + clipboardText);
        send_url_to_WS(clipboardText);
    }
}
