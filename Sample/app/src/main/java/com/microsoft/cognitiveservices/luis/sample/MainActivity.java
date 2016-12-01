package com.microsoft.cognitiveservices.luis.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.microsoft.cognitiveservices.luis.clientlibrary.LUISClient;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISDialog;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISEntity;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISIntent;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponse;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponseHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LUISResponse previousResponse = null;
    String chatText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPredict = (Button) findViewById(R.id.buttonPredict);
        Button btnReply = (Button) findViewById(R.id.buttonReply);
        final EditText editTextAppId = (EditText) findViewById(R.id.editTextAppId);
        final EditText editTextAppKey = (EditText) findViewById(R.id.editTextAppKey);
        final EditText editTextPredict = (EditText) findViewById(R.id.editTextPredict);

        btnPredict.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String appId = String.valueOf(editTextAppId.getText());
                String appKey = String.valueOf(editTextAppKey.getText());
                String textPredict = String.valueOf(editTextPredict.getText());
                try {
                    LUISClient client = new LUISClient(appId, appKey, true);
                    client.predict(textPredict, new LUISResponseHandler() {
                        @Override
                        public void onSuccess(LUISResponse response) {
                            processResponse(response);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            printToResponse(e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    printToResponse(e.getMessage());
                }
                editTextPredict.setText("");
            }
        });

        btnReply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (previousResponse == null || (previousResponse.getDialog() != null
                        && previousResponse.getDialog().isFinished())) {
                    printToResponse("Nothing to reply to!");
                    return;
                }
                String appId = String.valueOf(editTextAppId.getText());
                String appKey = String.valueOf(editTextAppKey.getText());
                String textPredict = String.valueOf(editTextPredict.getText());
                try {
                    LUISClient client = new LUISClient(appId, appKey, true);
                    client.reply(textPredict, previousResponse, new LUISResponseHandler() {
                        @Override
                        public void onSuccess(LUISResponse response) {
                            processResponse(response);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            printToResponse(e.getMessage());
                        }
                    });
                } catch (Exception e) {
                    printToResponse(e.getMessage());
                }
                editTextPredict.setText("");
            }
        });
    }

    public void processResponse(LUISResponse response) {
        printToResponse("-------------------");
        previousResponse = response;
        printToResponse(response.getQuery());
        LUISIntent topIntent = response.getTopIntent();
        printToResponse("Top Intent: " + topIntent.getName());
        printToResponse("Entities:");
        List<LUISEntity> entities = response.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            printToResponse(String.valueOf(i+1)+ " - " + entities.get(i).getName());
        }
        LUISDialog dialog = response.getDialog();
        if (dialog != null) {
            printToResponse("Dialog Status: " + dialog.getStatus());
            if (!dialog.isFinished()) {
                printToResponse("Dialog prompt: " + dialog.getPrompt());
            }
        }
    }

    public void printToResponse(String text) {
        TextView textViewResponse = (TextView) findViewById(R.id.textViewResponse);
        chatText += "\n" + text;
        textViewResponse.setText(chatText);
        scrollToBottom();
    }

    public void scrollToBottom(){
        final ScrollView scrollView = (ScrollView) findViewById(R.id.SV);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        }, 100);
    }
}
