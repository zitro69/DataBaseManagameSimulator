package DBMSi;

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
    private int size;

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
        return true;
    }

    @Override
    protected void select(TableRowRestriction restrictions){

    }

    @Override
    protected boolean update(String field, TableRow row){
        return true;
    }

    @Override
    protected boolean remove(String field, Object value){
        return true;
    }

    @Override
    protected long size(){
        return this.size;
    }

    /**
     * Comprueba si la tabla contiene una fila con el indice especificado
     * @param index Indice a buscar
     * @return true si existe
     */
    private boolean indexExists(Object index){return false;}

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
     * Clase que representa el arbol propiamente dicho
     */
    private static class AVLNode{
        TableRow element;

        AVLNode leftChild;
        AVLNode rightChild;
        int height;

        AVLNode(TableRow e){
            element = e;
            height = 1;

            leftChild = null;
            rightChild = null;
        }
    }
}
