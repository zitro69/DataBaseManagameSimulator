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
        if(indexType == null){
            indexType = table.getColumnType(index);
        }

        // Comprobacion de que el indice no exista ya en la tabla
        if(indexExists(tableRow.getContent().get(index))){
            System.err.println("No se puede inserir. Ya existe una fila con el indice "+tableRow.getContent().get(index).toString());
            return false;
        }

        int oldSize = size;

        //Insercion  de la fila en el arbol
        root = insert(root, tableRow, tableRow.getContent().get(index));

        return oldSize < size;
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
     * Introduce una nueva fila de la tabla en el arbol
     * @param root Nodo AVL raiz
     * @param row Fila a inserir
     * @param index indice de la fila a inserir
     * @return true si se ha inserido con exito
     */
    private AVLNode insert(AVLNode root, TableRow row, Object index){

        if (root == null){
            size++;
            return new AVLNode(row, index);
        }

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

        }else{
            System.err.println("ERROR: el indice no es de tipo int o text");
            return root;
        }

        //Actualizamos la altura del arbol
        root.height = Math.max(height(root.leftChild), height(root.rightChild)) + 1;

        //Comprobacion de necesidad de rotaciones
        int bf = balanceFactor(root);

        if(bf > 1){
            if(indexType == DataType.INT){
                if((int)index < (int)root.leftChild.indexKey){
                    //LL
                    return rotateRight(root);
                }
                if((int)index > (int)root.leftChild.indexKey){
                    //LR
                    root.leftChild = rotateLeft(root.leftChild);
                    return rotateRight(root);
                }
            }else if(indexType == DataType.TEXT){

            }else{
                System.err.println("ERROR: el indice no es de tipo int o text");
                return root;
            }
        }

        if(bf < -1){
            if(indexType == DataType.INT){
                if((int)index > (int)root.rightChild.indexKey){
                    //RR
                    return rotateLeft(root);
                }
                if((int)index < (int)root.rightChild.indexKey) {
                    //RL
                    root.rightChild = rotateRight(root.rightChild);
                    return rotateLeft(root);
                }
            }else if(indexType == DataType.TEXT){

            }else{
                System.err.println("ERROR: el indice no es de tipo int o text");
                return root;
            }
        }

        return root;
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
        Object indexKey;

        AVLNode leftChild;
        AVLNode rightChild;
        int height;

        AVLNode(TableRow e, Object index){
            element = e;
            indexKey = index;
            height = 1;

            leftChild = null;
            rightChild = null;
        }
    }
}
