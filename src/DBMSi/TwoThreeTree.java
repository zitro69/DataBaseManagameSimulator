package DBMSi;

import Menu.Screen;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jorti on 20/07/2017.
 */
public class TwoThreeTree extends TableDataStructure{
    protected String index;
    private long size;

    private Nodo raiz;

    public TwoThreeTree (){
        raiz = new Nodo();
        size = 0;
    }
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
        gotoRaiz();
        while (raiz.hadSons()){
            raiz = raiz.nextToStudy(tableRow);
            if (raiz.sameValue(tableRow)){
                Screen.repeatValue();
                return false;
            }
        }
        if (hadValue(tableRow)) {
            Screen.repeatValue();
            return false;
        }
        if (raiz.addTableRow(tableRow)){
            size++;
            return true;
        }
        raiz = split(new Nodo(tableRow, null), raiz);
        size++;
        return true;
    }

    private Nodo split(Nodo hijo, Nodo padre) {
        //Guardamos los hijos
        ArrayList<Nodo> hijos = guardarHijos(hijo, padre);
        //Comparar y Generar Nodos
        Nodo next = ordenarNodos(hijo, padre);
        //Comprobar padre
        if (!(next.getFather() == null)){
            //Eliminar padre del abuelo
            padre.getFather().deleteSon(padre);
            if (next.getFather().getBv() == null){
                next = reasignar(next, hijos);
                next = reasignarBV(next, next.getFather());
            } else {
                next = split(next, next.getFather());
                next = reasignar(next, hijos);
            }
        } else {
            next = reasignar(next, hijos);
        }
        return next;
    }

    private Nodo reasignarBV (Nodo hijo, Nodo padre){
        if (padre.getLv().compareTo(index, hijo.getLv()) >= 1) {
            padre.setBv(padre.getLv());
            padre.setLv(hijo.getLv());
            padre.setBn(padre.getMn());
            padre.getBn().setFather(padre);
            padre.setMn(hijo.getMn());
            padre.getMn().setFather(padre);
            padre.setLn(hijo.getLn());
            padre.getLn().setFather(padre);
        } else {
            padre.setBv(hijo.getLv());
            padre.setBn(hijo.getMn());
            padre.getBn().setFather(padre);
            padre.setMn(hijo.getLn());
            padre.getMn().setFather(padre);
        }
        return padre;
    }

    private Nodo reasignar (Nodo hijo, ArrayList<Nodo> hijos){
        try {
            Collections.sort(hijos, (Nodo n1, Nodo n2) -> {
                if (n1 == null){return 1;}
                if (n2 == null){return -1;}
                if (n1.getLv().compareTo(index, n2.getLv()) >= 1) return 1;
                else return -1;
            });
            if (hijos.get(0) != null) {
                hijo.getLn().setLn(hijos.get(0));
                hijo.getLn().getLn().setFather(hijo.getLn());
            }
            if (hijos.get(1) != null) {
                hijo.getLn().setMn(hijos.get(1));
                hijo.getLn().getMn().setFather(hijo.getLn());
            }
            if (hijos.get(2) != null) {
                hijo.getMn().setLn(hijos.get(2));
                hijo.getMn().getLn().setFather(hijo.getMn());
            }
            if (hijos.get(3) != null) {
                hijo.getMn().setMn(hijos.get(3));
                hijo.getMn().getMn().setFather(hijo.getMn());
            }
        } catch (NullPointerException npe){
            //No tienen hijos, por lo tanto era una hoja, no hay que hacer nada.
        }
        return hijo;
    }

    private ArrayList<Nodo> guardarHijos (Nodo hijo, Nodo padre){
        ArrayList<Nodo> hijos = new ArrayList<>();
        hijos.add(hijo.getLn());
        hijos.add(hijo.getMn());
        hijos.add(hijo.getBn());
        hijos.add(padre.getLn());
        hijos.add(padre.getMn());
        hijos.add(padre.getBn());
        return hijos;
    }

    private Nodo ordenarNodos (Nodo hijo, Nodo padre){
        Nodo next;
        Nodo auxpadre = new Nodo();
        auxpadre.copy(padre);
        if (hijo.getLv().compareTo(index,auxpadre.getBv()) >= 1){
            next = new Nodo (auxpadre.getBv(), auxpadre.getFather());
            auxpadre.setBv(null);
            hijo.setFather(next);
            auxpadre.setFather(next);
            next.setLn(auxpadre);
            next.setMn(hijo);
        } else {
            if (hijo.getLv().compareTo(index, auxpadre.getLv()) >= 1){
                next = new Nodo (hijo.getLv(), auxpadre.getFather());
                hijo.setLv(null);
                hijo.setLv(auxpadre.getBv());
                auxpadre.setBv(null);
                hijo.setFather(next);
                auxpadre.setFather(next);
                next.setLn(auxpadre);
                next.setMn(hijo);
            } else {
                next = new Nodo (auxpadre.getLv(), auxpadre.getFather());
                auxpadre.setLv(null);
                auxpadre.setLv(auxpadre.getBv());
                auxpadre.setBv(null);
                hijo.setFather(next);
                auxpadre.setFather(next);
                next.setLn(hijo);
                next.setMn(auxpadre);
            }
        }

        return next;
    }

    private boolean hadValue (TableRow tr){
        if (raiz.getLv() != null && raiz.getLv().compareTo(index, tr) == 0){
            return true;
        } else if (raiz.getBv() != null && raiz.getBv().compareTo(index, tr) == 0){
            return true;
        } else {
            return false;
        }
    }

    private void gotoRaiz () {
        while (raiz.getFather() != null){
            raiz = raiz.getFather();
        }
    }

    /**
     * Visualitza el contingut de l'estructura de dades.
     *
     * @param restrictions  Restriccions per tal de filtrar files en la visualització.
     */
    protected void select(TableRowRestriction restrictions){
        select (restrictions, raiz);
    }

    private void select (TableRowRestriction res, Nodo raiz){
        if (raiz != null){
            if (raiz.getLv() != null) {
                if (res.test(raiz.getLv())) {
                    Screen.showTR(raiz.getLv());
                }
            }
            if (raiz.getBv() != null) {
                if (res.test(raiz.getBv())) {
                    Screen.showTR(raiz.getBv());
                }
            }
            select(res,raiz.getLn());
            select(res,raiz.getMn());
            select(res, raiz.getBn());
        }
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
        gotoRaiz();
        TableRow valuetr = new TableRow();
        valuetr.addColumn(field, value);
        Nodo delete = encontrado(raiz, field, valuetr);
        while (delete.hadSons()){
            delete = goDown(delete, valuetr);
        }
        if (delete.getBv() != null){
            if (delete.getBv().compareTo(field, valuetr) == 0){
                delete.setBv(null);
            } else {
                delete.setLv(delete.getBv());
                delete.setBv(null);
            }
        } else {
            if (delete.getFather().getBv() == null){
                delete2nodo(delete);
            } else {
                delete3nodo(delete);
            }
        }
        size--;
        gotoRaiz();
        return true;
    }

    private void delete3nodo(Nodo delete) {
        if (delete.getFather().getLn() == delete){
            if (delete.getFather().getMn().getBv() != null){
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getMn().getLv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv());
                delete.getFather().getMn().setBv(null);
            } else if (delete.getFather().getBn().getBv() != null) {
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getMn().getLv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv());
                delete.getFather().getMn().setBv(delete.getFather().getBv());
                delete.getFather().setBv(delete.getFather().getBn().getLv());
                delete.getFather().getBn().setLv(delete.getFather().getBn().getBv());
                delete.getFather().getBn().setBv(null);
            } else {
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getMn().getLv());
                delete.getFather().getMn().setLv(delete.getFather().getBv());
                delete.getFather().getMn().setBv(delete.getFather().getBn().getLv());
                delete.getFather().setBv(null);
                delete.getFather().setBn(null);
            }
        } else if (delete.getFather().getMn() == delete){
            if (delete.getFather().getLn().getBv() != null){
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getLn().getBv());
                delete.getFather().getLn().setBv(null);
            } else if (delete.getFather().getBn().getBv() != null) {
                delete.setLv(delete.getFather().getBv());
                delete.getFather().setLv(delete.getFather().getBn().getLv());
                delete.getFather().getBn().setLv(delete.getFather().getBn().getBv());
                delete.getFather().getBn().setBv(null);
            } else {
                delete.getFather().getMn().setLv(delete.getFather().getBv());
                delete.getFather().getMn().setBv(delete.getFather().getBn().getLv());
                delete.getFather().setBv(null);
                delete.getFather().setBn(null);
            }
        } else {
            if (delete.getFather().getMn().getBv() != null){
                delete.setLv(delete.getFather().getBv());
                delete.getFather().setBv(delete.getFather().getMn().getBv());
                delete.getFather().getMn().setBv(null);
            } else if (delete.getFather().getLn().getBv() != null) {
                delete.setLv(delete.getFather().getBv());
                delete.getFather().setBv(delete.getFather().getMn().getBv());
                delete.getFather().getMn().setBv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getBn().getBv());
                delete.getFather().getLn().setBv(null);
            } else {
                delete.getFather().getMn().setBv(delete.getFather().getBv());
                delete.getFather().setBv(null);
                delete.getFather().setBn(null);
            }

        }
    }

    private void delete2nodo(Nodo delete) {
        if (delete.getFather().getLn() == delete){
            if (delete.getFather().getMn().getBv() == null){
                delete.getFather().setBv(delete.getFather().getMn().getLv());
                delete.getFather().deleteSons();
            } else {
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getMn().getLv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv());
                delete.getFather().getMn().setBv(null);
            }
        } else {
            if (delete.getFather().getLn().getBv() == null){
                delete.getFather().setBv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getLn().getLv());
                delete.getFather().deleteSons();
            } else {
                delete.setLv(delete.getFather().getLv());
                delete.getFather().setLv(delete.getFather().getMn().getLv());
                delete.getFather().getLn().setBv(null);
            }
        }
    }

    private Nodo goDown(Nodo delete, TableRow valuetr) {
        TableRow aux;
        if (delete.getLv().compareTo(index,valuetr) == 0){
            aux = delete.getLv();
            if (delete.getLn().getBv() != null) {
                delete.setLv(delete.getLn().getBv());
                delete.getLn().setBv(aux);
            } else {
                delete.setLv(delete.getLn().getLv());
                delete.getLn().setLv(aux);
            }
            return delete.getLn();
        } else {
            aux = delete.getBv();
            if (delete.getMn().getBv() != null) {
                delete.setBv(delete.getMn().getBv());
                delete.getMn().setBv(aux);
            } else {
                delete.setBv(delete.getMn().getLv());
                delete.getMn().setLv(aux);
            }
            return delete.getMn();
        }
    }

    private Nodo encontrado(Nodo raiz, String field, TableRow value) {
        if (raiz.getLv().compareTo(field, value) == 0) {
            return raiz;
        } else {
            if (raiz.getLv().compareTo(field, value) <= -1) {
                if (raiz.getBv() == null){
                    return encontrado(raiz.getMn(), field, value);
                }
                if (raiz.getBv().compareTo(field, value) == 0) {
                    return raiz;
                } else if (raiz.getBv().compareTo(field, value) <= -1) {
                    return encontrado(raiz.getBn(), field, value);
                } else {
                    return encontrado(raiz.getMn(), field, value);
                }
            }
            return encontrado(raiz.getLn(), field, value);
        }
    }

    /**
     * @return El total d'elements que hi ha guardats en l'estructura.
     */
    protected long size(){
        return size;
    }

    @Override
    protected ArrayList<TableRow> getData() {
        gotoRaiz();
        ArrayList<TableRow> data = new ArrayList<>();
        if (raiz.getLv() != null) data.add(raiz.getLv());
        if (raiz.getBv() != null) data.add(raiz.getBv());
        data.addAll(getData(raiz.getLn()));
        data.addAll(getData(raiz.getMn()));
        data.addAll(getData(raiz.getBn()));
        return data;
    }

    private ArrayList<TableRow> getData (Nodo n) {
        ArrayList<TableRow> data = new ArrayList<>();
        boolean visita = false;
        if (n == null) {
            visita = true;
        }
        if (!visita){
            if (n.getLv() != null) data.add(n.getLv());
            if (n.getBv() != null) data.add(n.getBv());
            data.addAll(getData(n.getLn()));
            data.addAll(getData(n.getMn()));
            data.addAll(getData(n.getBn()));
            return data;
        }
        return data;
    }


    //Nodo de representació del arbol 2-3, clase anidad
    private class Nodo {

        //Atributos utiles
        private boolean hadsons;

        //Valores del nodo

        private TableRow lv;
        private TableRow bv;

        //Nodos padres e hijos

        private Nodo father;
        private Nodo ln;
        private Nodo mn;
        private Nodo bn;

        //Constructores

        public Nodo (){
            hadsons = false;
            lv = null;
            bv = null;
            father = null;
            ln = null;
            mn = null;
            bn = null;
        }

        public Nodo (TableRow littlevalue, Nodo father){
            hadsons = false;
            bv = null;
            this.lv = littlevalue;
            this.father = father;
            ln = null;
            mn = null;
            bn = null;
        }

        //Funciones

        public Nodo nextToStudy(TableRow tr) {
            if (bv != null) {
                if (lv.compareTo(index, tr) >= 1) {
                    return ln;
                } else if (lv.compareTo(index, tr) == 0) {
                    return this;
                } else {
                    if (bv.compareTo(index, tr) >= 1) {
                        return mn;
                    } else if (bv.compareTo(index, tr) == 0) {
                        return this;
                    } else {
                        return bn;
                    }
                }
            } else {
                if (lv.compareTo(index, tr) <= -1) {
                    return mn;
                } else if (lv.compareTo(index, tr) == 0) {
                    return this;
                } else {
                    return ln;
                }
            }
        }

        public boolean sameValue (TableRow tr){
            if (bv == null){
                return lv.compareTo(index,tr) == 0;
            }
            return (lv.compareTo(index,tr) == 0 || bv.compareTo(index,tr) == 0);
        }

        public boolean addTableRow(TableRow tr) {
            if (lv == null){
                lv = tr;
                return true;
            } else if (bv == null) {
                if (lv.compareTo(index, tr) <= -1) {
                    bv = tr;
                } else {
                    bv = lv;
                    lv = tr;
                }
                return true;
            } else {
                return false;
            }
        }

        public void copy(Nodo n) {
            this.father = n.getFather();
            this.lv = n.getLv();
            this.bv = n.getBv();
            this.ln = n.getLn();
            this.mn = n.getMn();
            this.bn = n.getBn();
        }

        public void deleteSon(Nodo n) {
            if (ln.getLv() == n.getLv()) ln = null;
            else if (mn.getLv() == n.getLv()) mn = null;
            else if (bn.getBv() == n.getBv()) bn = null;
        }

        //Getters && Setters

        public boolean hadSons() {
            return hadsons;
        }

        public void setHadsons(boolean hadsons) {
            this.hadsons = hadsons;
        }

        public TableRow getLv() {
            return lv;
        }

        public void setLv(TableRow lv) {
            this.lv = lv;
        }

        public TableRow getBv() {
            return bv;
        }

        public void setBv(TableRow bv) {
            this.bv = bv;
        }

        public Nodo getFather() {
            return father;
        }

        public void setFather(Nodo father) {
            this.father = father;
        }

        public Nodo getLn() {
            return ln;
        }

        public void setLn(Nodo ln) {
            this.ln = ln;
            if (ln != null) {
                this.hadsons = true;
            }
        }

        public Nodo getMn() {
            return mn;
        }

        public void setMn(Nodo mn) {
            this.mn = mn;
            if (mn != null) {
                this.hadsons = true;
            }
        }

        public Nodo getBn() {
            return bn;
        }

        public void setBn(Nodo bn) {
            this.bn = bn;
            if (bn != null) {
                this.hadsons = true;
            }
        }

        public void deleteSons() {
            ln = null;
            mn = null;
            bn = null;
            hadsons = false;
        }
    }
}
