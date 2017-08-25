package com.sonelli.juicessh.performancemonitor.controllers;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.sonelli.juicessh.performancemonitor.R;
import com.sonelli.juicessh.performancemonitor.util.JavascriptEvaluator;
import com.sonelli.juicessh.pluginlibrary.exceptions.ServiceNotConnectedException;
import com.sonelli.juicessh.pluginlibrary.listeners.OnSessionExecuteListener;
import com.sonelli.juicessh.performancemonitor.controllers.actions.*;

import java.io.IOException;

/**
 * Created by rafaelsouzaf on 8/23/2017.
 */

public class JsonDynamicallyController extends BaseController {

    private ActionBean action;

    public JsonDynamicallyController(Context context, ActionBean action) {
        super(context);
        this.action = action;
    }

    @Override
    public BaseController start() {

        super.start();

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

            try {

                getPluginClient().executeCommandOnSession(getSessionId(), getSessionKey(), action.getCommandShell(), new OnSessionExecuteListener() {
                    @Override
                    public void onCompleted(int exitCode) {
                        switch(exitCode){
                            case 127:
                                setText(getString(R.string.error));
                                Log.d(action.getTitle(), "Tried to run a command but the command was not found on the server");
                                break;
                        }
                    }
                    @Override
                    public void onOutputLine(String result) {
                        String eval = JavascriptEvaluator.evaluate(action.getJsFunctionFilter(), result);
                        setText(eval);
                        System.out.println("========================" + eval);
                    }
                    @Override
                    public void onError(int error, String reason) {
                        toast(reason);
                    }
                });
            } catch (ServiceNotConnectedException e){
                Log.d(action.getTitle(), "Tried to execute a command but could not connect to JuiceSSH plugin service");
            }

            if(isRunning()) {
                handler.postDelayed(this, action.getTtl() * 1000L);
            }

            }

        });

        return this;

    }



}