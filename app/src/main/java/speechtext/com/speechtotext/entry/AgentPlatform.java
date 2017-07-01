package speechtext.com.speechtotext.entry;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import speechtext.com.speechtotext.R;

/**
 * Created by eespinor on 20/06/2017.
 */

public class AgentPlatform {

    private Agent agent;

    public AgentPlatform(Context context){
        createAgent(context);
    }

    public void createAgent(Context context) {
        agent = new Agent();
        createEntry(context);
        createIntent(context);
    }

    public void createEntry(Context context) {
        String[] file = readInput(context.getResources().openRawResource(R.raw.entry)).split("\n");
        for (String patron : file ) {
            if(!TextUtils.isEmpty(patron))
              agent.getEntryList().add(Entry.getEntryToJson(patron));
        }
    }

    public void createIntent(Context context) {
        String[] file = readInput(context.getResources().openRawResource(R.raw.intent)).split("\n");
        //TODO cambiar por json
        for (String s : file) {
            if (!TextUtils.isEmpty(s))
                agent.getIntentList().add(new Intent(s));
        }
    }


    public Entry procesarCampo(String cadena){
       Entry e =  agent.procesarEntry(cadena);
        if(e!=null){
            Intent i = agent.procesarIntent(cadena);
            agent.procesarEntrywithIntent(e, i, cadena);
        }
        return e;
    }


    private String readInput(InputStream ins) {
        BufferedReader r = new BufferedReader(new InputStreamReader(ins));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();
    }
}
