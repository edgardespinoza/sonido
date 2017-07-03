package speechtext.com.speechtotext.entry;

import android.text.TextUtils;

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
    private Entry entryActual;
    private List<Entry> entryList;
    private List<Intent> intentList;
   // private Stack<Entry> sEntry;

    public void setCurrentEntry(Entry e) {
        entryActual = e;
    }

    public Entry getCurrentEntry() {

        if( entryActual != null && TextUtils.isEmpty(entryActual.getValor())){
            return entryActual;
        }
        for (Entry e : entryList) {
            if (TextUtils.isEmpty(e.getValor())) {
                entryActual =e;
                return entryActual;
            }
        }
        return null;
    }

//    public void setCurrentEntryToList() {
//        for (Entry e : entryList) {
//            if (e.getEntidad().equals(entryActual.getEntidad())) {
//                e.setValor(entryActual.getValor());
//                return;
//            }
//        }
//    }


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
            Pattern p = Pattern.compile("(" + i.getAccion() + ")");
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
        if(i==null ){

        }
        if (e.getTipo() == Entry.TIPO_ENTERO) {
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(cadena.replace(" ", ""));
            while (m.find()) {
                String value = m.group();
                e.setValor(getValue(value, 0, e.getMaximoCaracter()));
            }
        } else {
            e.setValor(cadena.replaceAll(".*(" + e.getEntidad() + ")", ""));
        }

    }

    private String getValue(String v, int start, int end) {
        int length = v.length();
        if (length > 0 && length > end && end>0)
            return v.substring(start, end);
        else return v;
    }


}

