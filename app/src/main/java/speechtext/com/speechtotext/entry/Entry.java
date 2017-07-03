package speechtext.com.speechtotext.entry;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by eespinor on 20/06/2017.
 */

public class Entry
{
    public static final int TIPO_ENTERO=1;
    public static final int TIPO_STRING=2;
    private String entidad;
    private String entidadHablado;
    private String alias;
    private String valor;
    private int tipo;
    private int maximoCaracter;

    public static Entry getEntryToJson( String patron){
       return (new Gson()).fromJson(patron, Entry.class);
    }

    public Entry(String e, int ptipo){
        this.setEntidad(e); this.setTipo(ptipo);
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getValor() {
        return valor;
    }
    public String getValorHablado(){
        return tipo==TIPO_ENTERO && valor!=null? valor.replaceAll("(?<=\\d)(?=\\d)", " "):valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getMaximoCaracter() {
        return maximoCaracter;
    }

    public void setMaximoCaracter(int maximoCaracter) {
        this.maximoCaracter = maximoCaracter;
    }

    public String getEntidadHablado() {
        return TextUtils.isEmpty(entidadHablado)?entidad:entidadHablado;
    }

    public void setEntidadHablado(String entidadHablado) {
        this.entidadHablado = entidadHablado;
    }
}
