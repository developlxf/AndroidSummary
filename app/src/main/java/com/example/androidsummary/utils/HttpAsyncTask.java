package com.example.androidsummary.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.ConnectException;

/**
 * Created by 伦小丹 on 2015/12/24 0024.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    public HttpAsyncTask(Context context) {

    }

    private OnResponseListener onResponseListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
//        HttpUtils httpUtils = new HttpUtils();
//        final String[] result = {null};
//        httpUtils.send(HttpRequest.HttpMethod.GET, params[0], new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                result[0] = responseInfo.result;
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
//        return result[0];

        // 获取网页html数据
        Log.i("param",params[0]);
        HttpGet httpget = new HttpGet(params[0]);
        String strResult = null;
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        HttpConnectionParams.setSoTimeout(httpParams, 10000);

        HttpClient httpclient;
        try {
            httpclient = new DefaultHttpClient(httpParams);
//            httpget.setHeader("Cookie", cookieName + "=" + cookieValue);
            HttpResponse response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                strResult = EntityUtils.toString(response.getEntity(),
                        HTTP.UTF_8);
                // System.out.println(strResult);
                System.out.println("getFinish");
            }
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            System.out.println("Client");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO");
            e.printStackTrace();
        }
        if (strResult == null)
        Log.i("content","----");
        return strResult;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (null != onResponseListener) {
            onResponseListener.onResponse(result);
        }
    }

    public OnResponseListener getResponseListener() {
        return onResponseListener;
    }

    public void setOnResponseListener(OnResponseListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    public interface OnResponseListener {
        public void onResponse(String resultString);
    }
}
