package DBMSi;

/**
 * Created by jorti on 20/07/2017.
 */
public class AVLTree extends TableDataStructure {
    //TODO: implementar distintos metodos de la clase heredada
    protected String index;

    public AVLTree(){}

    /**
     * @return El nom de l'índex.
     */
    protected String getIndex() {

        return index;
    }

    /**
     * Estableix el nom del camp pel qual s'organitza l'estructura.
     *
     * @param field El nom del camp que serà l'índex.
     */
    protected void setIndex(String field) {

        this.index = field;
    }

    /**
     * Afegeix una nova fila dins de l'estructura utilitzada per una taula.
     *
     * @param tableRow La nova fila a afegir.
     *
     * @return true si s'ha pogut afegir, false en cas contrari.
     */
    protected boolean add(TableRow tableRow){
        return true;
    }

    /**
     * Visualitza el contingut de l'estructura de dades.
     *
     * @param restrictions  Restriccions per tal de filtrar files en la visualització.
     */
    protected void select(TableRowRestriction restrictions){
    }

    /**
     * Permet actualitzar una fila de l'estructura de dades.
     *
     * @param field El camp pel qual cercar la fila existent.
     * @param row   El contingut actualitzat de la fila.
     *
     * @return      true si s'ha actualitzat, false si no s'ha trobat el valor previ de la fila en l'estructura.
     */
    protected boolean update(String field, TableRow row){
        return true;
    }

    /**
     * Si existeix el valor en l'estructura, en la columna especificada, llavors
     * elimina la primera coincidència de l'estructura. És a dir, la fila sencera.
     *
     * @param field El nom del camp o columna.
     * @param value El valor que ha de tenir el camp.
     *
     * @return true si s'ha pogut eliminar la fila, false en cas contrari.
     */
    protected boolean remove(String field, Object value){
        return true;
    }

    /**
     * @return El total d'elements que hi ha guardats en l'estructura.
     */
    protected long size(){
        return 10;
    }
}
