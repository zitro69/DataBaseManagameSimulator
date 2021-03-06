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

    @Override
    protected String getIndex() {

        return index;
    }

    @Override
    protected void setIndex(String field) {

        this.index = field;
    }

    @Override
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

    /**
     * Hace un split entre los valores de hijo y padre. Devuelve el nodo con el split hecho señalando a sus hijos.
     * @param hijo      Nodo con el que se hace el split
     * @param padre     Nodo con el que se hace el split
     * @return          Nodo resultado del split
     */
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

    /**
     * Averigua que valor es mayor y reasigna los valores.
     * @param hijo      Nodo a estudiar.
     * @param padre     Nodo a estudiar
     * @return          Nodo con los valores reasignados.
     */
    private Nodo reasignarBV (Nodo hijo, Nodo padre){
        if (padre.getLv().compareTo(index, hijo.getLv()) >= 1) {
            padre.setBv(padre.getLv(), padre.getHlv());
            padre.setLv(hijo.getLv(), hijo.getHlv());
            padre.setBn(padre.getMn());
            padre.getBn().setFather(padre);
            padre.setMn(hijo.getMn());
            padre.getMn().setFather(padre);
            padre.setLn(hijo.getLn());
            padre.getLn().setFather(padre);
        } else {
            padre.setBv(hijo.getLv(), hijo.getHlv());
            padre.setBn(hijo.getMn());
            padre.getBn().setFather(padre);
            padre.setMn(hijo.getLn());
            padre.getMn().setFather(padre);
        }
        return padre;
    }

    /**
     * Averigua que nodo de hijos es mayor y reasigna los valores.
     * @param hijo      Nodo a estudiar.
     * @param hijos     Nodos a reasignar
     * @return          Nodo con los valores reasignados.
     */
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

    /**
     * Guarda los hijos antes de empezar hacer el split.
     * @param hijo      Nodo padre del que guardar los hijos.
     * @param padre     Nodo padre del que gurdar los hijos.
     * @return          Los diferentes hijos @param hijo y @param padre.
     */
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

    /**
     * Averigua que valor es mayor y reasigna los valores.
     * @param hijo      Nodo a estudiar.
     * @param padre     Nodo a estudiar
     * @return          Nodo con los valores reasignados.
     */
    private Nodo ordenarNodos (Nodo hijo, Nodo padre){
        Nodo next;
        Nodo auxpadre = new Nodo();
        auxpadre.copy(padre);
        if (hijo.getLv().compareTo(index,auxpadre.getBv()) >= 1){
            next = new Nodo (auxpadre.getBv(), auxpadre.getFather());
            auxpadre.setBv(null, new ArrayList<>());
            hijo.setFather(next);
            auxpadre.setFather(next);
            next.setLn(auxpadre);
            next.setMn(hijo);
        } else {
            if (hijo.getLv().compareTo(index, auxpadre.getLv()) >= 1){
                next = new Nodo (hijo.getLv(), auxpadre.getFather());
                hijo.setLv(null, new ArrayList<>());
                hijo.setLv(auxpadre.getBv(), auxpadre.getHbv());
                auxpadre.setBv(null, new ArrayList<>());
                hijo.setFather(next);
                auxpadre.setFather(next);
                next.setLn(auxpadre);
                next.setMn(hijo);
            } else {
                next = new Nodo (auxpadre.getLv(), auxpadre.getFather());
                auxpadre.setLv(null, new ArrayList<>());
                auxpadre.setLv(auxpadre.getBv(), auxpadre.getHbv());
                auxpadre.setBv(null, new ArrayList<>());
                hijo.setFather(next);
                auxpadre.setFather(next);
                next.setLn(hijo);
                next.setMn(auxpadre);
            }
        }

        return next;
    }

    /**
     * Comprueba si el valor esta en el nodo raiz.
     * @param tr        valor a estudiar
     * @return          cierto si tr esta en el nodo raiz.
     */
    private boolean hadValue (TableRow tr){
        if (raiz.getLv() != null && raiz.getLv().compareTo(index, tr) == 0){
            return true;
        } else if (raiz.getBv() != null && raiz.getBv().compareTo(index, tr) == 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Va a arriba del arbol.
     */
    private void gotoRaiz () {
        while (raiz.getFather() != null){
            raiz = raiz.getFather();
        }
    }

    @Override
    protected void select(TableRowRestriction restrictions){
        select (restrictions, raiz);
    }

    /**
     * Comprueba si el nodo de entrada cuple les restricciones.
     * @param res       restriciones a cumplir
     * @param raiz      nodo en el cual se miran los valores.
     */
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

    @Override
    protected TableRow getTableRow (TableRowRestriction res){
        gotoRaiz();
        return getValue(res, raiz);
    }

    private TableRow getValue (TableRowRestriction res, Nodo n){
        if (raiz != null){
            if (raiz.getLv() != null) {
                if (res.test(raiz.getLv())) {
                    return (raiz.getLv());
                }
            }
            if (raiz.getBv() != null) {
                if (res.test(raiz.getBv())) {
                    return (raiz.getBv());
                }
            }
            select(res,raiz.getLn());
            select(res,raiz.getMn());
            select(res, raiz.getBn());
        }
        return null;
    }


    @Override
    protected boolean update(String field, TableRow row, Table t){
        gotoRaiz();
        if (size == 0){
            Screen.error("The table is empty.");
            return false;
        }
        Nodo toupdate = encontrado(raiz, field, row);
        if (toupdate == null){
            return false;
        }
        if (toupdate.getLv().compareTo(field, row) == 0){
            ArrayList<TableRow> aux = toupdate.getHlv();
            aux.add(row);
            toupdate.setLv(row, aux);
        } else if (toupdate.getBv().compareTo(field,row) == 0){
            ArrayList<TableRow> aux = toupdate.getHbv();
            aux.add(row);
            toupdate.setBv(row, aux);
        } else {
            return false;
        }
        return true;
    }

    @Override
    protected boolean remove(String field, Object value){
        gotoRaiz();
        if (size == 0){
            Screen.error("The table is empty.");
            return false;
        }
        TableRow valuetr = new TableRow();
        valuetr.addColumn(field, value);
        Nodo delete = encontrado(raiz, field, valuetr);
        if (delete == null){
            Screen.error("The index doesn't exist. Impossible remove row.");
            return false;
        }
        while (delete.hadSons()){
            delete = goDown(delete, valuetr);
        }
        if (delete.getBv() != null){
            if (delete.getBv().compareTo(field, valuetr) == 0){
                delete.setBv(null, new ArrayList<>());
            } else {
                delete.setLv(delete.getBv(), delete.getHbv());
                delete.setBv(null, new ArrayList<>());
            }
        } else {
            if (delete.getFather() == null){
                delete.setLv(null, new ArrayList<>());
                size--;
                return true;
            }
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

    /**
     * Reasigna los valores si el nodo es 3 nodo
     * @param delete    Nodo al cual se reasignan los valores.
     */
    private void delete3nodo(Nodo delete) {
        if (delete.getFather().getLn() == delete){
            if (delete.getFather().getMn().getBv() != null){
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv(), delete.getFather().getMn().getHbv());
                delete.getFather().getMn().setBv(null, new ArrayList<>());
            } else if (delete.getFather().getBn().getBv() != null) {
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv(), delete.getFather().getMn().getHbv());
                delete.getFather().getMn().setBv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().setBv(delete.getFather().getBn().getLv(), delete.getFather().getBn().getHlv());
                delete.getFather().getBn().setLv(delete.getFather().getBn().getBv(), delete.getFather().getBn().getHbv());
                delete.getFather().getBn().setBv(null, new ArrayList<>());
            } else {
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().getMn().setLv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().getMn().setBv(delete.getFather().getBn().getLv(), delete.getFather().getBn().getHlv());
                delete.getFather().setBv(null, new ArrayList<>());
                delete.getFather().setBn(null);
            }
        } else if (delete.getFather().getMn() == delete){
            if (delete.getFather().getLn().getBv() != null){
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getLn().getBv(), delete.getFather().getLn().getHbv());
                delete.getFather().getLn().setBv(null, new ArrayList<>());
            } else if (delete.getFather().getBn().getBv() != null) {
                delete.setLv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().setLv(delete.getFather().getBn().getLv(), delete.getFather().getBn().getHlv());
                delete.getFather().getBn().setLv(delete.getFather().getBn().getBv(), delete.getFather().getBn().getHbv());
                delete.getFather().getBn().setBv(null, new ArrayList<>());
            } else {
                delete.getFather().getMn().setLv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().getMn().setBv(delete.getFather().getBn().getLv(), delete.getFather().getBn().getHlv());
                delete.getFather().setBv(null, new ArrayList<>());
                delete.getFather().setBn(null);
            }
        } else {
            if (delete.getFather().getMn().getBv() != null){
                delete.setLv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().setBv(delete.getFather().getMn().getBv(), delete.getFather().getMn().getHbv());
                delete.getFather().getMn().setBv(null, new ArrayList<>());
            } else if (delete.getFather().getLn().getBv() != null) {
                delete.setLv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().setBv(delete.getFather().getMn().getBv(), delete.getFather().getMn().getHbv());
                delete.getFather().getMn().setBv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getBn().getBv(), delete.getFather().getBn().getHbv());
                delete.getFather().getLn().setBv(null, new ArrayList<>());
            } else {
                delete.getFather().getMn().setBv(delete.getFather().getBv(), delete.getFather().getHbv());
                delete.getFather().setBv(null, new ArrayList<>());
                delete.getFather().setBn(null);
            }

        }
    }

    /**
     * Reasigna los valores si el nodo es 2 nodo
     * @param delete    Nodo al cual se reasignan los valores.
     */
    private void delete2nodo(Nodo delete) {
        if (delete.getFather().getLn() == delete){
            if (delete.getFather().getMn().getBv() == null){
                delete.getFather().setBv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().deleteSons();
            } else {
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().getMn().setLv(delete.getFather().getMn().getBv(), delete.getFather().getMn().getHbv());
                delete.getFather().getMn().setBv(null, new ArrayList<>());
            }
        } else {
            if (delete.getFather().getLn().getBv() == null){
                delete.getFather().setBv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getLn().getLv(), delete.getFather().getLn().getHlv());
                delete.getFather().deleteSons();
            } else {
                delete.setLv(delete.getFather().getLv(), delete.getFather().getHlv());
                delete.getFather().setLv(delete.getFather().getMn().getLv(), delete.getFather().getMn().getHlv());
                delete.getFather().getLn().setBv(null, new ArrayList<>());
            }
        }
    }

    /**
     * Baja el nodo hasta que haya hojas.
     * @param delete    Nodo el cual se esta bajando.
     * @param valuetr   Segun
     * @return          Nodo una vez que esta abajo
     */
    private Nodo goDown(Nodo delete, TableRow valuetr) {
        TableRow aux;
        if (delete.getLv().compareTo(index,valuetr) == 0){
            aux = delete.getLv();
            if (delete.getLn().getBv() != null) {
                delete.setLv(delete.getLn().getBv(), delete.getLn().getHbv());
                delete.getLn().setBv(aux, delete.getHlv());
            } else {
                delete.setLv(delete.getLn().getLv(), delete.getLn().getHlv());
                delete.getLn().setLv(aux, delete.getHlv());
            }
            return delete.getLn();
        } else {
            aux = delete.getBv();
            if (delete.getMn().getBv() != null) {
                delete.setBv(delete.getMn().getBv(), delete.getMn().getHbv());
                delete.getMn().setBv(aux, delete.getHbv());
            } else {
                delete.setBv(delete.getMn().getLv(), delete.getMn().getHlv());
                delete.getMn().setLv(aux, delete.getHbv());
            }
            return delete.getMn();
        }
    }

    /**
     * Devuele el nodo en el que esta value
     * @param raiz      raiz nodo a estudiar
     * @param field     indice (index)
     * @param value     value a comprobar si esta en el nodo
     * @return          Devuelve el nodo en el que esta la partida.
     */
    private Nodo encontrado(Nodo raiz, String field, TableRow value) {
        if (raiz == null){
            return null;
        }
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

    @Override
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

    /**
     * Hace un inorder de los nodos y guarda en una array list
     * @param n     nodo a mirar
     * @return      donde se guardan los nodos.
     */
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
    @Override
    public ArrayList<TableRow> getHistoricalRow (TableRow tr){
        gotoRaiz();
        Nodo encontrado = encontrado(raiz, index, tr);
        if (encontrado == null){
            return null;
        }
        if (encontrado.getLv().compareTo(index, tr) == 0){
            return encontrado.getHlv();
        } else {
            return encontrado.getHbv();
        }
    }


    //Nodo de representació del arbol 2-3, clase anidad
    private class Nodo {

        //Atributos utiles
        private boolean hadsons;

        //Valores del nodo

        private TableRow lv;
        private TableRow bv;

        //Valores historicos

        private ArrayList<TableRow> hlv;
        private ArrayList<TableRow> hbv;

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
            hlv = new ArrayList<>();
            hbv = new ArrayList<>();
        }

        public Nodo (TableRow littlevalue, Nodo father){
            hadsons = false;
            bv = null;
            this.lv = littlevalue;
            hlv = new ArrayList<>();
            hlv.add(lv);
            this.father = father;
            hbv = new ArrayList<>();
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
                hlv.add(tr);
                return true;
            } else if (bv == null) {
                if (lv.compareTo(index, tr) <= -1) {
                    bv = tr;
                    hbv.add(tr);
                } else {
                    hbv = hlv;
                    hlv.add(tr);
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

        public void setLv(TableRow lv, ArrayList<TableRow> hlv) {
            this.lv = lv;
            this.hlv = hlv;
        }

        public TableRow getBv() {
            return bv;
        }

        public void setBv(TableRow bv, ArrayList<TableRow> hbv) {
            this.bv = bv;
            this.hbv = hbv;
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

        public ArrayList<TableRow> getHlv() {
            return hlv;
        }

        public ArrayList<TableRow> getHbv() {
            return hbv;
        }
    }
}
