package speechtext.com.speechtotext.entry;

import com.google.gson.Gson;

/**
 * Created by eespinor on 20/06/2017.
 */

public class Intent {
    public static final int BORRAR =1;
    public static final int CAMBIAR =2;
    public static final int INGRESAR =3;
    private String accion;
    private String alias;
    private int tipo;

    public static Intent getEntryToJson( String patron){
        return (new Gson()).fromJson(patron, Intent.class);
    }


    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
