package speechtext.com.speechtotext;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import speechtext.com.speechtotext.entry.AgentPlatform;
import speechtext.com.speechtotext.entry.Entry;
import speechtext.com.speechtotext.util.RecognitionService;
import speechtext.com.speechtotext.util.SpeechListener;


public class SpeechActivity extends AppCompatActivity
        implements SpeechListener {

    public static final String TAG = SpeechActivity.class.getName();

    private RecognitionService aiService;
    private ProgressBar progressBar;
    private ImageView recIndicator;
    private TextView resultTextView;

    AgentPlatform agentPlatform;
    //     private EditText contextEditText;
    private EditText dni;
    private EditText direccion;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speech_to_text);
        initTexttoSpeech(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recIndicator = (ImageView) findViewById(R.id.recIndicator);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
//        contextEditText = (EditText) findViewById(R.id.contextEditText);
        dni = (EditText) findViewById(R.id.dni);
        direccion = (EditText) findViewById(R.id.direccion);
        agentPlatform = new AgentPlatform(this);
        initService();

        //   aiService.startListening();
    }

    private void initService() {

        aiService = new RecognitionService(this);
        aiService.setListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // use this method to disconnect from speech recognition service
        // Not destroying the SpeechRecognition object in onPause method would block other apps from using SpeechRecognition service
        if (aiService != null) {
            aiService.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // use this method to reinit connection to recognition service
        if (aiService != null) {
            aiService.resume();
        }

//        procesarCamposVoz();
    }

    public void startRecognition(final View view) {

        procesarAudio();
    }

    private void procesarAudio(){
        procesarCamposVoz();
        try {
             Thread.sleep(2000);
        } catch (Exception e) {
        }
        aiService.startListening();
    }

    public void stopRecognition(final View view) {
        aiService.stopListening();
    }

    public void cancelRecognition(final View view) {
        aiService.cancel();
    }

    boolean flagActivarAduido = false;

    @Override
    public void onResult(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onResult");

                resultTextView.setText((response));

                Log.i(TAG, "Received success response");

//                // this is example how to get different parts of result object
//                final Status status = response.getStatus();
//                Log.i(TAG, "Status code: " + status.getCode());
//                Log.i(TAG, "Status type: " + status.getErrorType());
//
//                final Result result = response.getResult();
//                Log.i(TAG, "Resolved query: " + result.getResolvedQuery());
//
//                Log.i(TAG, "Action: " + result.getAction());
//
//                final String speech = result.getFulfillment().getSpeech();
//                Log.i(TAG, "Speech: " + speech);
                //  textToSpeech(response);
//                flagActivarAduido = false;
                procesarCampos(response);
//                flagActivarAduido = true;
//                if (flagActivarAduido) {
//                    procesarAudio();
//                }
//                final Metadata metadata = result.getMetadata();
//                if (metadata != null) {
//                    Log.i(TAG, "intent id: " + metadata.getIntentId());
//                    Log.i(TAG, "intent name: " + metadata.getIntentName());
//                }

//                final HashMap<String, JsonElement> params = result.getParameters();
//                if (params != null && !params.isEmpty()) {
//                    Log.i(TAG, "Parameters: ");
//                    for (final Map.entry<String, JsonElement> entry : params.entrySet()) {
//                        Log.i(TAG, String.format("%s: %s", entry.getKey(), entry.getValue().toString()));
//                    }
//                }
            }

        });
    }

    private void procesarCamposVoz() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(dni.getText().toString()))
                    textToSpeech("Buen día Edgard, por favor dime tu D N I");
                else
                    textToSpeech("Ahora por último ingresa tu dirección");
            }
        });


    }

    private void procesarCampos(String cad) {
        Entry e = agentPlatform.procesarCampo(cad);
        if (e == null) {

        };

        if (dni.getHint().toString().toUpperCase().contains(e.getEntidadPatron())) {
            dni.setText(e.getValorEntidad());
            textToSpeech("TU D N I es" + e.getValorEntidad().replaceAll("(?<=\\d)(?=\\d)", " "));
        }

        if (direccion.getHint().toString().toUpperCase().contains(e.getEntidadPatron())) {
            direccion.setText(e.getValorEntidad());
            textToSpeech("TU Dirección es" + e.getValorEntidad());
        }


    }

    @Override
    public void onError(final Exception error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resultTextView.setText(error.toString());
            }
        });
    }

    @Override
    public void onAudioLevel(final float level) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float positiveLevel = Math.abs(level);

                if (positiveLevel > 100) {
                    positiveLevel = 100;
                }
                progressBar.setProgress((int) positiveLevel);
            }
        });
    }

    @Override
    public void onListeningStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onListeningCanceled() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.INVISIBLE);
                resultTextView.setText("");
            }
        });
    }

    @Override
    public void onListeningFinished() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        final LanguageConfig selectedLanguage = (LanguageConfig) parent.getItemAtPosition(position);
//        initService(selectedLanguage);
//    }

//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    @Override
    public void onStopAudio() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (flagActivarAduido) {
                    try {

//                        Thread.sleep(1000);
                        aiService.cancel();
//                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
//                    procesarAudio();

                }
            }
        });
    }

    private TextToSpeech textToSpeech;

    public void initTexttoSpeech(final Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {

                }
            });
        }
    }

    public void textToSpeech(final String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAudioRecordPermission();
    }

    protected void checkAudioRecordPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        33);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        12);
            }
        }
    }
}
