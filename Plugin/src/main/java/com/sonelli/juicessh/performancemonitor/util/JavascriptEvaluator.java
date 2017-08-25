package com.sonelli.juicessh.performancemonitor.util;

import android.content.Context;
import android.provider.Settings;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

/**
 * Created by rafaelsouzaf on 8/24/2017.
 */

public class JavascriptEvaluator {

    public static String evaluate(String functionToFilter, String lines) {

        V8 v8 = V8.createV8Runtime();
        v8.executeVoidScript("function filterStr(result) { " + functionToFilter + " };");
        V8Array parameters = new V8Array(v8);
        parameters.push(lines);
        Object result = v8.executeFunction("filterStr", parameters);
        System.out.println("#########################################" + result);
        parameters.release();
        v8.release();

        return result.toString();

    }

}
