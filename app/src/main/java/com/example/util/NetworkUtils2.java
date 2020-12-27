package com.example.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils2 {

    private NetworkUtils2() {

    }

    private static class Singleton {
        private static final NetworkUtils2 network = new NetworkUtils2();
    }

    public static NetworkUtils2 getInstance() {
        return Singleton.network;
    }

    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public void OkhttpGet(Handler handler, String url, int what) {
        Message message = Message.obtain();
        Request request = new Request.Builder().get().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                message.obj = response.body().string();
                message.what = what;
                handler.sendMessage(message);
            }
        });
    }

    public void send(String url, Handler handler, int what) throws IOException {
        Log.i("TAG", "send: " + url);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                .addHeader("Referer", "https://manhua.dmzj.com/tongzhenmiejueliedao/99325.shtml")
                .build();
        Call call = okHttpClient.newCall(request);
        Message message = Message.obtain();
        message.obj = Objects.requireNonNull(call.execute().body()).bytes();
        message.what = what;
        handler.sendMessage(message);
    }

    public byte[] returnSend(String url) throws IOException {
        Log.i("TAG", "send: " + url);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                .addHeader("Referer", "https://manhua.dmzj.com/tongzhenmiejueliedao/99325.shtml")
                .build();
        Call call = okHttpClient.newCall(request);
        return Objects.requireNonNull(call.execute().body()).bytes();
    }

    public byte[] getReturn(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        return Objects.requireNonNull(call.execute().body()).bytes();
    }

    public String getRetStr(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        String s=call.execute().body().string();
        if (s.equals("")) return "";
        return s;
    }

    public void OKHttpPost(FormBody formBody, Handler handler) {
        Message message = Message.obtain();
        String u = "http://www.eggvod.cn/music/";
        Request request = new Request.Builder()
                .url(u)
                .method("POST", formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36")
                .addHeader("Host", "www.eggvod.cn")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Content-Length", "42")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("TAG", "请求错误: " + e.toString());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                message.what = 1;
                message.obj = response.body().string();
                handler.sendMessage(message);
            }
        });
    }
}
