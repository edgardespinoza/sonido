package speechtext.com.speechtotext.entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eespinor on 20/06/2017.
 */

public class Agent {

    private String name;

    private List<Entry> entryList;
    private List<Intent> intentList;
    private Stack<Entry> sEntry;

    public Entry procesarEntry(String cadena) {
        for (Entry e : entryList) {
            Pattern p = Pattern.compile("(" + e.getEntidad() + ")");
            Matcher m = p.matcher(cadena);
            if (m.find()) {
                return e;
            }
        }
        return null;
    }

    public Intent procesarIntent(String cadena) {
        for (Intent i : intentList) {
            Pattern p = Pattern.compile("("+i.getIntencion()+")");
            Matcher m = p.matcher(cadena);
            if (m.find()) {
                return i;
            }
        }
        return null;
    }

    public Agent() {
        entryList = new ArrayList<Entry>();
        intentList = new ArrayList<Intent>();
    }

    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public List<Intent> getIntentList() {
        return intentList;
    }

    public void setIntentList(List<Intent> intentList) {
        this.intentList = intentList;
    }

    public void procesarEntrywithIntent(Entry e, Intent i, String cadena) {
        if (i!=null && "BORRAR".equals(i.getIntencion()))
            e.setValorEntidad("");

        else {
            //TODO adicionar tipado de voz
            if("DNI".equalsIgnoreCase(e.getEntidadPatron())){
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(cadena.replace(" ", ""));
                while(m.find()) {
                    e.setValorEntidad(m.group());
                }
            }else {

                       e.setValorEntidad(cadena.replaceAll(".*("+e.getEntidadPatron()+")",""));

            }

        }
    }
}

