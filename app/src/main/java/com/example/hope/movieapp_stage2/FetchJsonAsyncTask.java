package com.example.hope.movieapp_stage2;

import android.os.AsyncTask;

import com.example.hope.movieapp_stage2.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Hope on 6/4/2017.
 */

public class FetchJsonAsyncTask extends AsyncTask<URL, Void, String> {

    private static final String TAG = FetchJsonAsyncTask.class.getSimpleName();
    private JsonCallBack mReturnJson;

    @Override
    protected String doInBackground(URL... params) {
        String json = null;
        URL url = params[0];
        try {
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.v(TAG, json);
        return json;
    }

    public void setCallBackContext(JsonCallBack c) {
        this.mReturnJson = c;
    }

    @Override
    protected void onPostExecute(String s) {
        if (!s.equals(null)) {
            super.onPostExecute(s);
            mReturnJson.setJson(s);
        }
    }

    public interface JsonCallBack {
        void setJson(String json);
    }
}
