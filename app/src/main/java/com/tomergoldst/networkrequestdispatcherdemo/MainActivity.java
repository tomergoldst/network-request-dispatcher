package com.tomergoldst.networkrequestdispatcherdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.tomergoldst.networkrequestdispatcher.Request;
import com.tomergoldst.networkrequestdispatcher.RequestDispatcher;
import com.tomergoldst.networkrequestdispatcher.RequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testBtn = (Button) findViewById(R.id.button_test);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTestReqest();
            }
        });
    }

    private void sendTestReqest(){
        JSONObject jsonObject = createJsonObject();

        List<Pair<String, String>> headers = new LinkedList<>();
        headers.add(Pair.create("Authorization", "ada34sfr4jABgjdas79sfa"));
        headers.add(Pair.create("Content-Type", "text/html; charset=UTF-8"));

        Request request = new Request.Builder()
                    .url("https://httpbin.org/get")
                    .method("GET")
                    .listener(new DemoRequestListener())
                    .addHeaders(headers)
                    .build();

        new MakeNetworkRequestAsync().execute(request);
    }

    @NonNull
    private JSONObject createJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", "value");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private class MakeNetworkRequestAsync extends AsyncTask<Request, Void, Void> {
        protected Void doInBackground(Request... requests) {
            RequestResponse response = RequestDispatcher.dispatch(getBaseContext(), requests[0]);
            return null;
        }

    }


}
