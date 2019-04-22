package ru.myteas.teaclient_a;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import ru.myteas.teaclient_a.Teas.Tea;
import ru.myteas.teaclient_a.Teas.TeaList;

public class TeaApp  extends Application {

    public  interface SocketListener {
        void GetUser();
    }

    public final boolean DEV = true;

    private SocketListener mListener;
    private HttpURLConnection conn;
    private Thread threadForReq;
    public final String url = DEV ? "http://192.168.43.254:3100" : "http://104.40.154.103:3100";


    private  int fetchStatus = 0;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public Socket getSocket(){
        return mSocket;
    }

    public int getFethcStatus() {
        if(fetchStatus == 1) threadForReq.interrupt();
        return fetchStatus;
    }
    public void setFetchStatus(int fetchStatus) {
        this.fetchStatus = fetchStatus;
    }

    public void  MainSocketEventsOn(final Activity activity) {
        mListener = (SocketListener) activity;
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(
                    new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "EVENT_CONNECT", Toast.LENGTH_SHORT).show();
                        }
                    }

                );
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "EVENT_CONNECT_ERROR", Toast.LENGTH_LONG).show();
                            }
                        }

                );

            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "EVENT_ERROR", Toast.LENGTH_LONG).show();
                            }
                        }

                );

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(
                        new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "EVENT_DISCONNECT", Toast.LENGTH_LONG).show();
                            }
                        }

                );

            }
        }).on("getUser", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject obj = (JSONObject) args[0];
                        try {
                            String error = obj.getString("error");

                            if(!error.equals("null")){
                                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                                mListener.GetUser();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.e("_DEV",e.toString());
                        }
                        try {
                            JSONObject _user = obj.getJSONObject("user");
                            if (_user.toString().equals("null")){
                                mListener.GetUser();
                                return;
                            }
                            String name = _user.getString("user");
                            String id = _user.getString("id");
                            User.name = name;
                            User.id = id;
                            mListener.GetUser();
                        }catch (JSONException e){
                            Log.e("_DEV",e.toString());
                        }

                    }
                });
            }
        }).on("allTeas", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("_DEV-TeaApp","SocketOnAllTeas");
                        JSONObject obj = (JSONObject) args[0];
                        try {
                            String error = obj.getString("error");

                            if(!error.equals("null")){
                                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                                mListener.GetUser();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.e("_DEV-TeaApp",e.toString());
                        }
                        try {
                            JSONArray teas = obj.getJSONArray("teas");

                            for (int i = 0; i < teas.length(); i++) {
                                JSONObject _tea = teas.getJSONObject(i);
                                String id = _tea.getString("_id");
                                String name = _tea.getString("name");
                                String type = _tea.getString("type");
                                Double rating = _tea.getDouble("reating");//Поменять поле у схемы на сервере!
                                JSONObject parent = _tea.getJSONObject("parent");
                                String parentName = parent.getString("name");
                                String parentId = parent.getString("id");

                                Tea tea = new Tea(name,type,rating,new Tea.Parent(parentName,parentId));
                                if (TeaList.List == null) TeaList.List = new ArrayList<>();
                                TeaList.List.add(tea);
                            }

                        }catch (JSONException e){
                            Log.e("_DEV-TeaApp",e.toString());
                        }

                    }
                });
            }
        }).on("addTea", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.i("_DEV-TeaApp","SocketOnAddTea");
                JSONObject obj = (JSONObject) args[0];
                try {
                    String error = obj.getString("error");

                    if(!error.equals("null")){
                        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
                        mListener.GetUser();
                        return;
                    }
                } catch (JSONException e) {
                    Log.e("_DEV-TeaApp",e.toString());
                }

                try {
                    JSONObject _tea = obj.getJSONObject("tea");


                    String id = _tea.getString("_id");
                    String name = _tea.getString("name");
                    String type = _tea.getString("type");
                    Double rating = _tea.getDouble("reating");//Поменять поле у схемы на сервере!
                    JSONObject parent = _tea.getJSONObject("parent");
                    String parentName = parent.getString("name");
                    String parentId = parent.getString("id");

                    Tea tea = new Tea(name,type,rating,new Tea.Parent(parentName,parentId));
                    if (TeaList.List == null) TeaList.List = new ArrayList<>();
                    TeaList.List.add(tea);

                }catch (JSONException e){
                    Log.e("_DEV-TeaApp",e.toString());
                }

            }
        });
    }

    public void MainSocketEventsOff(){
        mSocket.disconnect();
        mSocket.off();
    }


    public void ReqForServer(){
        threadForReq = new Thread(
            new Runnable() {
                String link = url+"/api";
                @Override
                public void run() {
                    try {
                        Log.i("_DEV","+ FoneService --------------- ОТКРОЕМ СОЕДИНЕНИЕ");

                        conn = (HttpURLConnection) new URL(link).openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                        conn.setDoInput(true);
                        conn.connect();

                    } catch (Exception e) {
                        Log.i("_DEV", "+ FoneService ошибка запроса: " + e);
                        e.printStackTrace();
                    }
                    try {
                        InputStream is = conn.getInputStream();

                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        StringBuilder sb = new StringBuilder();
                        String bfr_st;
                        while ((bfr_st = br.readLine()) != null) {sb.append(bfr_st);}

                        Log.i("_DEV", "+ FoneService - полный ответ сервера:" + sb.toString().trim());
                        if(sb.toString().trim().equals("OK")){
                            fetchStatus = 1;
                        }
                        // сформируем ответ сервера в string
                        // обрежем в полученном ответе все, что находится за "]"
                        // это необходимо, т.к. json ответ приходит с мусором
                        // и если этот мусор не убрать - будет невалидным

                        is.close(); // закроем поток
                        br.close(); // закроем буфер

                    } catch (Exception e) {
                        Log.i("_DEV", "+ FoneService ошибка ответа: " + e);
                        e.printStackTrace();
                        fetchStatus = -1;

                    } finally {
                        conn.disconnect();
                        Log.i("_DEV","+ FoneService --------------- ЗАКРОЕМ СОЕДИНЕНИЕ");

                    }
                }
            });
        threadForReq.setDaemon(true);
        threadForReq.start();
    }

}
