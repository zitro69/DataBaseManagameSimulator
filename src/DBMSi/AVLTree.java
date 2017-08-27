package DBMSi;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jorti on 20/07/2017.
 */
public class AVLTree extends TableDataStructure {

    /**
     * Tabla a la que pertenecen las filas que almacena esta estructura
     */
    private Table table;

    /**
     * Cantidad de filas almacenadas en esta estructura de datos
     */
    private long size;

    /**
     * Raíz del árbol AVL
     */
    private AVLNode root;

    /**
     * Tipo de dato de la columna índice de la tabla
     */
    private DataType indexType;

    /**
     * Construye un arbol binario AVL para registros de una tabla
     */
    public AVLTree(){
        this.size = 0;
    }

    /**
     * Metodo que indica a la tabla de hash a qué Tabla de la base de datos pertenece.
     * Solo podra asignarse una vez.
     * @param table Tabla a la que pertenece esta estructura de datos
     */
    public void setTable(Table table){
        this.table = this.table == null? table : this.table;
    }

    @Override
    protected boolean add(TableRow tableRow){
        if(indexType == null){
            indexType = table.getColumnType(index);
        }

        long oldSize = size;

        //Insercion  de la fila en el arbol
        root = insert(root, tableRow, tableRow.getContent().get(index));

        return oldSize < size;
    }

    @Override
    protected void select(TableRowRestriction restrictions){

        System.out.println(table.toString());

        selectPreorder(root, restrictions);

    }

    @Override
    protected TableRow getTableRow (TableRowRestriction res){
        return getPreoorder (root, res);
    }

    private TableRow getPreoorder (AVLNode root, TableRowRestriction res){
        if(root != null){
            if(res == null || res.test(root.element)) {
                return (root.element);
            }
            selectPreorder(root.leftChild, res);
            selectPreorder(root.rightChild, res);
        }
        return null;
    }

    @Override
    protected boolean update(String field, TableRow row, Table t){

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No puede actualizarse ninguna fila.");
            return false;
        }

        AVLNode aux;

        if(field.equals(index)){
            aux = searchNodeByIndex(root, row.getContent().get(index));

        }else{
            aux = searchNode(root, field, row.getContent().get(field));
        }

        if(aux == null){
            System.err.println("No existe ninguna fila con "+field+"="+row.getContent().get(field));
            return false;
        }
        for (int i = 0; i < t.getColumnNames().size(); i++){
            Object o = null;
            if (row.compareTo(t.getColumnNames().get(i), o) == 0){
                row.addColumn(t.getColumnNames().get(i), aux.getElement().getContent().get(t.getColumnNames()));
            }
        }
        aux.oldData.add(aux.element);
        aux.element = row;
        aux.indexKey = row.getContent().get(index);
        hasUpdates = true;
        updateCount++;
        return true;
    }

    @Override
    protected boolean remove(String field, Object value){

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No puede eliminarse ningun elemento.");
            return false;
        }

        long oldSize = size;

        if(field.equals(index))
            root = delete(root, value);
        else
            root = delete(root, field, value);

        size = nodeCount(root);

        return oldSize > size;
    }

    @Override
    protected long size(){
        return this.size;
    }

    @Override
    protected ArrayList<TableRow> getData() {
        ArrayList<TableRow> data = new ArrayList<>();
        if(root == null)
            return data;
        data.add(root.getElement());
        data.addAll(rowsToCSV(root.leftChild));
        data.addAll(rowsToCSV(root.rightChild));
        return data;
    }

    /**
     * Concatena las filas almacenadas en el arbol, añadiendolas una por una
     * mediante exploracion por preorden
     * @param root raiz AVL
     */
    private ArrayList<TableRow> rowsToCSV(AVLNode root){
        ArrayList<TableRow> data = new ArrayList<>();
        if(root == null)
            return data;
        data.add(root.getElement());
        data.addAll(rowsToCSV(root.leftChild));
        data.addAll(rowsToCSV(root.rightChild));
        return data;
    }

    /**
     * @param root AVL tree
     * @return Cantidad de nodos en el arbol
     */
    private long nodeCount(AVLNode root){

        if(root == null){
            return 0;
        }

        return 1 + nodeCount(root.rightChild) + nodeCount(root.leftChild);
    }

    /**
     * Introduce una nueva fila de la tabla en el arbol
     * @param root Nodo AVL raiz
     * @param row Fila a inserir
     * @param index indice de la fila a inserir
     * @return la raiz del arbol
     */
    private AVLNode insert(AVLNode root, TableRow row, Object index){

        //Caso trivial: retornamos un nuevo nodo
        if (root == null){
            size++;
            return new AVLNode(row, index);
        }

        //Caso general: Descendemos por el arbol hasta encontrar donde inserir
        if(indexType == DataType.INT){

            if((int)index < (int)root.indexKey){
                root.leftChild = insert(root.leftChild, row, index);
            } else if((int)index > (int)root.indexKey){
                root.rightChild = insert(root.rightChild, row, index);
            } else{
                System.err.println("No se puede inserir. Ya existe una fila con el indice "+row.getContent().get(index).toString());
                return root;
            }

        }else if(indexType == DataType.TEXT){
            if(((String) index).compareToIgnoreCase((String)root.indexKey) > 0){
                root.leftChild = insert(root.leftChild, row, index);
            } else if(((String) index).compareToIgnoreCase((String)root.indexKey) < 0){
                root.rightChild = insert(root.rightChild, row, index);
            }else{
                System.err.println("No se puede inserir. Ya existe una fila con el indice "+row.getContent().get(index).toString());
                return root;
            }
        }else{
            System.err.println("ERROR: el indice no es de tipo int o text");
            return root;
        }

        return balanceTree(root, index);
    }

    /**
     * Muestra la seleccion de todas aquellas filas que cumplan las restricciones mediante
     * preorden
     * @param root AVL raiz
     * @param res restricciones de seleccion
     */
    private void selectPreorder(AVLNode root, TableRowRestriction res){

        if(root != null){
            if(res == null || res.test(root.element)) {
                System.out.println(root.element.toString());
            }
            selectPreorder(root.leftChild, res);
            selectPreorder(root.rightChild, res);
        }

    }

    /**
     * Elimina una fila de la tabla en el arbol mediante el valor de indice
     * @param root Nodo AVL raiz
     * @return la raiz del arbol
     */
    private AVLNode delete(AVLNode root, Object index){

        //Caso trivial, hemos llegado a una hoja
        if(root == null){
            return root;
        }

        //Caso general: descendemos por el arbol hasta encontrar el nodo a eliminar
        if(indexType == DataType.INT){

            if((int)index < (int)root.indexKey){
                root.leftChild = delete(root.leftChild, index);
            } else if((int)index > (int)root.indexKey){
                root.rightChild = delete(root.rightChild, index);
            } else{
                //Nodo actual es el nodo a eliminar
                root = killNode(root);
            }

        }else if(indexType == DataType.TEXT){
            if(((String) index).compareToIgnoreCase((String)root.indexKey) > 0){
                root.leftChild = delete(root.leftChild, index);
            } else if(((String) index).compareToIgnoreCase((String)root.indexKey) < 0){
                root.rightChild = delete(root.rightChild, index);
            }else{
                //Nodo actual es el nodo a eliminar
                root = killNode(root);
            }
        }else{
            System.err.println("ERROR: el indice no es de tipo int o text");
            return root;
        }

        //No hace falta balancear, root era un arbol de un solo nodo
        if(root == null)
            return root;

        return balanceTree(root, index);
    }

    /**
     * Elimina la primera coincidencia de fila en la que coincida el valor de busqueda.
     * Realiza dicha busqueda mediante un pre-orden.
     * @param root Nodo AVL raiz
     * @param searchField Campo por el cual buscar la fila
     * @param value Valor del campo por el que buscar
     * @return la raiz del arbol
     */
    private AVLNode delete(AVLNode root, String searchField, Object value){
        AVLNode aux;

        aux = searchNode(root, searchField, value);

        if(aux == null)
            return root;

        return delete(root, aux.indexKey);
    }

    /**
     * Elimina el nodo del arbol especificado
     * @param root Nodo a eliminar
     * @return arbol resultante, null en caso de que no tuviese hijos
     */
    private AVLNode killNode(AVLNode root){
        AVLNode aux;

        // Arbol con 1 o ningun hijo
        if(root.rightChild == null || root.leftChild == null){

            if(root.leftChild == null){
                aux = root.rightChild;
            }else{
                aux = root.leftChild;
            }

            if(aux == null){
                aux = root;
                root = null;
            }else{
                root = aux;
            }

        // Arbol con 2 hijos: ascender al sucesor por inorden del subarbol derecho
        }else{

            aux = minNode(root.rightChild);

            root.indexKey = aux.indexKey;
            root.element = aux.element;
            root.oldData = aux.oldData;

            root.rightChild = delete(root.rightChild, aux.indexKey);
        }

        return root;
    }

    /**
     * Busqueda la primera coincidencia de un nodo por valor de una columna (recomendado para usar con columnas no indice)
     * @param field Nombre de la columna
     * @param value Valor que debe tener
     * @return null si no ha encontrado ninguno. La primera coincidencia nodo si ha encontrado uno.
     */
    private AVLNode searchNode(AVLNode root, String field, Object value){

        if(root == null){
            return root;
        }

        if(root.element.getContent().get(field).equals(value)) {

            return root;

        }

        AVLNode aux = searchNode(root.leftChild, field, value);

        return aux == null? searchNode(root.rightChild, field, value) : aux;

    }

    /**
     * Busca en el arbol un nodo cuyo indice de fila sea el indicado
     * @param root AVL raiz
     * @param value Valor de la columna índice
     * @return
     */
    private AVLNode searchNodeByIndex(AVLNode root, Object value){

        if(root == null)
            return null;

        if(root.indexKey.equals(value))
            return root;

        AVLNode aux;

        if(indexType == DataType.INT){
            if((int)value < (int)root.indexKey){
                aux = searchNodeByIndex(root.leftChild, value);
            } else {
                aux = searchNodeByIndex(root.rightChild, value);
            }
        } else {
            if(((String) value).compareToIgnoreCase((String)root.indexKey) > 0){
                aux = searchNodeByIndex(root.leftChild, value);
            } else{
                aux = searchNodeByIndex(root.rightChild, value);
            }
        }

        return aux;
    }

    /**
     * @param node Nodo AVL
     * @return altura del nodo arbol
     */
    private int height(AVLNode node){
        return node == null? 0 : node.height;
    }

    /**
     * @param node NodoAVL
     * @return Factor de balance del nodo arbol
     */
    private int balanceFactor(AVLNode node){
        return node == null? 0 : height(node.leftChild)-height(node.rightChild);
    }

    /**
     * @param root AVL raiz
     * @return el nodo de menor valor del arbol
     */
    private AVLNode minNode(AVLNode root){

        AVLNode aux = root;

        while(aux.leftChild != null)
            aux = aux.leftChild;

        return aux;
    }

    /**
     * Realiza una rotacion tipo L
     * @param root Nodo arbol sobre el que realizar la rotacion
     * @return arbol rotado
     */
    private AVLNode rotateLeft(AVLNode root){

        AVLNode rightChild = root.rightChild,
                leftChild = rightChild.leftChild;

        rightChild.leftChild = root;
        root.rightChild = leftChild;

        root.height = Math.max(height(root.leftChild), height(root.rightChild)) + 1;
        rightChild.height = Math.max(height(rightChild.leftChild), height(rightChild.rightChild)) + 1;

        return rightChild;
    }

    /**
     * Realiza una rotacion tipo L
     * @param root Nodo arbol sobre el que realizar la rotacion
     * @return arbol rotado
     */
    private AVLNode rotateRight(AVLNode root){

        AVLNode leftChild = root.leftChild,
                rightChild = leftChild.rightChild;

        leftChild.rightChild = root;
        root.leftChild = rightChild;

        root.height = Math.max(height(root.leftChild), height(root.rightChild)) + 1;
        leftChild.height = Math.max(height(leftChild.leftChild), height(leftChild.rightChild)) + 1;

        return leftChild;
    }

    /**
     * @param tr Fila con el valor del índice de la fila cuyo historial de cambios se desea recuperar
     * @return null si no se encuentra la fila. Coleccion de filas en caso contrario.
     */
    public  ArrayList<TableRow> getHistoricalRow(TableRow tr){

        ArrayList<TableRow> historic, auxOldData;

        if(size == 0){
            System.err.println("Tabla \""+table.getName()+"\" vacía. No hay datos históricos para la fila.");
            return null;
        }

        AVLNode aux = searchNodeByIndex(root, tr.getContent().get(index));

        if(aux == null){
            return null;
        }

        historic = new ArrayList<>();
        historic.add(aux.element);

        if(aux.oldData != null && aux.oldData.size() > 0) {
            auxOldData = new ArrayList<>(aux.oldData);
            Collections.reverse(auxOldData);
            historic.addAll(auxOldData);
        }
        return historic;
    }

    /**
     * Balancea el arbol mediante rotaciones
     * @param root Raiz nodo
     * @param index Indice del nodo
     * @return Arbol balanceado
     */
    private AVLNode balanceTree(AVLNode root, Object index){
        //Actualizamos la altura del arbol
        root.height = Math.max(height(root.leftChild), height(root.rightChild)) + 1;

        //Comprobacion de necesidad de rotaciones
        int bf = balanceFactor(root);

        if(bf > 1){
            if(indexType == DataType.INT){
                if((int)index < (int)root.leftChild.indexKey){
                    //LL enteros
                    return rotateRight(root);
                }
                if((int)index > (int)root.leftChild.indexKey){
                    //LR enteros
                    root.leftChild = rotateLeft(root.leftChild);
                    return rotateRight(root);
                }
            }else if(indexType == DataType.TEXT){
                if(((String) index).compareToIgnoreCase((String)root.leftChild.indexKey) > 0){
                    //LL strings
                    return rotateRight(root);
                }
                if(((String) index).compareToIgnoreCase((String)root.leftChild.indexKey) < 0){
                    //LR strings
                    root.leftChild = rotateLeft(root.leftChild);
                    return rotateRight(root);
                }

            }else{
                System.err.println("ERROR: el indice no es de tipo int o text");
                return root;
            }
        }

        if(bf < -1){
            if(indexType == DataType.INT){
                if((int)index > (int)root.rightChild.indexKey){
                    //RR enteros
                    return rotateLeft(root);
                }
                if((int)index < (int)root.rightChild.indexKey) {
                    //RL enteros
                    root.rightChild = rotateRight(root.rightChild);
                    return rotateLeft(root);
                }
            }else if(indexType == DataType.TEXT){
                if(((String) index).compareToIgnoreCase((String)root.rightChild.indexKey) < 0){
                    //RR strings
                    return rotateLeft(root);
                }
                if(((String) index).compareToIgnoreCase((String)root.rightChild.indexKey) > 0){
                    //RL strings
                    root.rightChild = rotateRight(root.rightChild);
                    return rotateLeft(root);
                }
            }else{
                System.err.println("ERROR: el indice no es de tipo int o text");
                return root;
            }
        }

        return root;
    }

    /**
     * Clase que representa el arbol propiamente dicho
     */
    private static class AVLNode{
        TableRow element;
        ArrayList<TableRow> oldData;

        Object indexKey;

        AVLNode leftChild;
        AVLNode rightChild;
        int height;

        AVLNode(TableRow e, Object index){
            element = e;
            indexKey = index;
            height = 1;
            oldData = new ArrayList<>();

            leftChild = null;
            rightChild = null;
        }

        public TableRow getElement() {
            return element;
        }
    }

}
