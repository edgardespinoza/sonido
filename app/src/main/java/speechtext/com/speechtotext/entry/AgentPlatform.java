package speechtext.com.speechtotext.entry;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import speechtext.com.speechtotext.R;

/**
 * Created by eespinor on 20/06/2017.
 */

public class AgentPlatform {

    private Agent agent;

    public AgentPlatform(Context context) {
        createAgent(context);
    }

    public void createAgent(Context context) {
        setAgent(new Agent());
        createEntry(context);
        createIntent(context);
    }

    public void createEntry(Context context) {
        String[] file = readInput(context.getResources().openRawResource(R.raw.entry)).split("\n");
        for (String patron : file) {
            if (!TextUtils.isEmpty(patron)) {
                Entry e = Entry.getEntryToJson(patron);
                getAgent().getEntryList().add(e);
            }
        }
    }

    public void createIntent(Context context) {
        String[] file = readInput(context.getResources().openRawResource(R.raw.intent)).split("\n");

        for (String patron : file) {
            if (!TextUtils.isEmpty(patron)){
                Intent e = Intent.getEntryToJson(patron);
                getAgent().getIntentList().add(e);
            }
        }
    }


    public Entry procesarCampo(String cadena) {
        //obtenemos la entidad de la cadena
        Entry e = getAgent().procesarEntry(cadena);

        //OJO si no existe reemplazamos la entidad actual
        if (e != null) {
            getAgent().setCurrentEntry(e);
        }else{
            e =agent.getCurrentEntry();
        }

        if(e!=null) {
            Intent i = getAgent().procesarIntent(cadena);
            getAgent().procesarEntrywithIntent(e, i, cadena);
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
