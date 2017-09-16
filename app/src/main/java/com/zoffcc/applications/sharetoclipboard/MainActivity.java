package com.zoffcc.applications.sharetoclipboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

public class MainActivity extends Activity
{
    // ------------
    // -- CONFIG --
    // ------------
    // static  String Blinkenwall_WS_url_youtube = "echo.websocket.org";
    static final String Blinkenwall_WS_url_youtube_DEFAULT = "10.20.30.26:1337/blinkenwall";
    static final String command_1_DEFAULT = "{\"cmd\":\"video play\",\"url\":\"";
    static final String command_2_DEFAULT = "\"}";
    static final boolean USE_HTTPS_DEFAULT = false;
    // ------------
    // -- CONFIG --
    // ------------

    private static final String TAG = "MainActivity";


    static String Blinkenwall_WS_url_youtube = Blinkenwall_WS_url_youtube_DEFAULT;
    static String command_1 = command_1_DEFAULT;
    static String command_2 = command_2_DEFAULT;
    static boolean USE_HTTPS = USE_HTTPS_DEFAULT;

    static boolean dont_change_values = false;

    EditText url = null;
    EditText cmd1 = null;
    EditText cmd2 = null;
    AppCompatButton reset_ = null;
    SwitchCompat https_toggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // don't show keyboard when activity starts
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Blinkenwall_WS_url_youtube = Blinkenwall_WS_url_youtube_DEFAULT;
        command_1 = command_1_DEFAULT;
        command_2 = command_2_DEFAULT;
        USE_HTTPS = USE_HTTPS_DEFAULT;

        dont_change_values = false;

        url = (EditText) findViewById(R.id.url);
        cmd1 = (EditText) findViewById(R.id.cmd1);
        cmd2 = (EditText) findViewById(R.id.cmd2);
        https_toggle = (SwitchCompat) findViewById(R.id.https_toggle);
        reset_ = (AppCompatButton) findViewById(R.id.reset);

        setUpToolbar();

        load_prefs();

        url.setText(Blinkenwall_WS_url_youtube);
        cmd1.setText(command_1);
        cmd2.setText(command_2);
        https_toggle.setChecked(USE_HTTPS);

        https_toggle.setOnCheckedChangeListener(onCheckedChanged());
        reset_.setOnClickListener(reset_on_click());
        url.addTextChangedListener(txt_watcher);
        cmd1.addTextChangedListener(txt_watcher);
        cmd2.addTextChangedListener(txt_watcher);
    }

    private Button.OnClickListener reset_on_click()
    {
        return new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dont_change_values = true;
                try
                {

                    // Log.i(TAG, "1command_1_DEFAULT=" + command_1_DEFAULT);
                    // Log.i(TAG, "1command_2_DEFAULT=" + command_2_DEFAULT);
                    // Log.i(TAG, "1command_1=" + command_1);
                    // Log.i(TAG, "1command_2=" + command_2);

                    Blinkenwall_WS_url_youtube = Blinkenwall_WS_url_youtube_DEFAULT;
                    command_1 = command_1_DEFAULT;
                    command_2 = command_2_DEFAULT;
                    USE_HTTPS = USE_HTTPS_DEFAULT;

                    url.setText(Blinkenwall_WS_url_youtube);
                    cmd1.setText(command_1);
                    cmd2.setText(command_2);
                    https_toggle.setChecked(USE_HTTPS);

                    // Log.i(TAG, "2command_1_DEFAULT=" + command_1_DEFAULT);
                    // Log.i(TAG, "2command_2_DEFAULT=" + command_2_DEFAULT);
                    // Log.i(TAG, "2command_1=" + command_1);
                    // Log.i(TAG, "2command_2=" + command_2);

                    save_prefs();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.i(TAG, "onClick:EE01:" + e.getMessage());
                }
                dont_change_values = false;
            }
        };
    }

    private final TextWatcher txt_watcher = new TextWatcher()
    {
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        public void afterTextChanged(Editable s)
        {
            try
            {
                if (!dont_change_values)
                {
                    // Log.i(TAG, "afterTextChanged:start");
                    Blinkenwall_WS_url_youtube = url.getText().toString();
                    command_1 = cmd1.getText().toString();
                    command_2 = cmd2.getText().toString();
                    // Log.i(TAG, "afterTextChanged:mid");

                    save_prefs();
                    // Log.i(TAG, "afterTextChanged:end");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChanged()
    {
        return new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                switch (buttonView.getId())
                {
                    case R.id.https_toggle:
                        try
                        {
                            USE_HTTPS = isChecked;
                            save_prefs();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    private void save_prefs()
    {
        try
        {
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            p.edit().putBoolean("USE_HTTPS", USE_HTTPS).commit();
            p.edit().putString("Blinkenwall_WS_url_youtube", Blinkenwall_WS_url_youtube).commit();
            p.edit().putString("command_1", command_1).commit();
            p.edit().putString("command_2", command_2).commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void load_prefs()
    {
        try
        {
            SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            USE_HTTPS = p.getBoolean("USE_HTTPS", USE_HTTPS_DEFAULT);
            Blinkenwall_WS_url_youtube = p.getString("Blinkenwall_WS_url_youtube", Blinkenwall_WS_url_youtube_DEFAULT);
            command_1 = p.getString("command_1", command_1_DEFAULT);
            command_2 = p.getString("command_2", command_2_DEFAULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setUpToolbar()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            try
            {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.primary600));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
