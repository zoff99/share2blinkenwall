package com.zoffcc.applications.sharetoclipboard;

import android.util.Log;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static com.zoffcc.applications.sharetoclipboard.MainActivity.command_1;
import static com.zoffcc.applications.sharetoclipboard.MainActivity.command_2;
import static com.zoffcc.applications.sharetoclipboard.ShareToClipboardActivity.output;

class WSListener extends WebSocketListener
{
    private static final String TAG = "WSListener";

    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private static String YOUTUBE_URL = "";

    public WSListener(String youtube_url)
    {
        output("got URL : " + youtube_url);
        YOUTUBE_URL = youtube_url;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response)
    {
        // webSocket.send("Test");
        Log.i(TAG, "webSocket.send: " + command_1 + YOUTUBE_URL + command_2);
        webSocket.send(command_1 + YOUTUBE_URL + command_2);
        // webSocket.send(ByteString.decodeHex("deadbeef"));
        webSocket.close(NORMAL_CLOSURE_STATUS, "Exit");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text)
    {
        output("Receiving : " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes)
    {
        output("Receiving bytes : " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason)
    {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        output("Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response)
    {
        output("Error : " + t.getMessage());
    }
}