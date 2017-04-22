package com.tomergoldst.networkrequestdispatcherdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tomergoldst.networkrequestdispatcher.Request;
import com.tomergoldst.networkrequestdispatcher.RequestDispatcher;
import com.tomergoldst.networkrequestdispatcher.RequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

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

        Request request = new Request.Builder()
                    .url("https://httpbin.org/post")
                    .method("POST")
                    .data(jsonObject)
                    .addHeader("charset", "UTF-8")
                    .addHeader("Content-Type", "Application/JSON")
                    .build();
        Log.d("MainActivity", request.toString());

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
            RequestResponse response = RequestDispatcher.dispatch(requests[0], true);
            Log.d("MainActivity", response.toString());
            return null;
        }

    }


}
