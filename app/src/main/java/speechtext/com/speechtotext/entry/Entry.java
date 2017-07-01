package speechtext.com.speechtotext.entry;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by eespinor on 20/06/2017.
 */

public class Entry
{
    public static final int TIPO_ENTERO=1;
    public static final int TIPO_STRING=2;
    private String entidad;
    private String alias;
    private String valorEntidad;
    private int tipo;

    public static Entry getEntryToJson( String patron){
       return (new Gson()).fromJson(patron, Entry.class);
    }

    public Entry(String e, int ptipo){
        this.entidad=e; this.setTipo(ptipo);
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getValorEntidad() {
        return valorEntidad;
    }

    public void setValorEntidad(String valorEntidad) {
        this.valorEntidad = valorEntidad;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
