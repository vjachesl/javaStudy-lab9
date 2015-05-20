package lab9;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by viacheslav on 16.05.15.
 */
public class MyTreeMap implements MyMap {
    private SimpleEntry root;

    //Null node for technical purposes - parent in the root and left and right nodes in the end;
    final SimpleEntry NULL_NODE = new SimpleEntry(null,null);

    // quantity of total elements in the tree;
    private int elementsQuantity;

    // variables declaration for iterator;
    private int currentPositionForIterator;
    private Vector<SimpleEntry> iterableElem;

    // Construct the new empty map;
    public MyTreeMap() {
        this.root = NULL_NODE;
        this.elementsQuantity =0;
    }
    // Clear all data in the map;
    @Override
    public void clear() {
        this.root = NULL_NODE;
        this.elementsQuantity =0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (get(key)!=null) return true;
        return false;
    }

    // put all nodes into vector and search within it for value;
    @Override
    public boolean containsValue(Object value) {
        Vector<SimpleEntry> arrayToReturn = treeMapToArray();
        for (SimpleEntry si : arrayToReturn)
        if (si.getValue().equals(value) && (!si.isDeleted())) return true;
        return false;
    }

    // returns value for key
    @Override
    public Object get(Object key) {
        SimpleEntry toReturn = getNode(key);
        if (toReturn!=NULL_NODE && (!toReturn.isDeletedNode)) return toReturn.getValue();
        return null;
    }

    // additional private method returns node with the spesific key;
    private SimpleEntry getNode(Object key) {
        SimpleEntry workPosition = root;
        int keyHash = keyHashCode(key);
        int workKey = -1;
        while (workPosition!=NULL_NODE){
            if (workPosition!=NULL_NODE) workKey = workPosition.getKeyHashCode();
                else break;
            if (keyHash>workKey) workPosition=workPosition.getRightNode();
            if (keyHash<workKey) workPosition=workPosition.getLeftNode();
            if (keyHash==workKey) return workPosition;
        }
        return NULL_NODE;
    }

    // return if the map is empty
    @Override
    public boolean isEmpty() {
       return (elementsQuantity==0 ? true : false );
    }

    // general method for put - check all cases and check tree after puting
    @Override
    public Object put(Object key, Object value) {
        isNullCheck(key);
        if (isEmpty()) {
            root = new SimpleEntry(key, value);
            root.markBlack();
            root.setLeftNode(NULL_NODE);
            root.setRightNode(NULL_NODE);
            root.setParentNode(NULL_NODE);
            elementsQuantity++;
            return null;
        }
        SimpleEntry foundNode = getNode(key);
        if (foundNode !=NULL_NODE) {
                if (foundNode.isDeleted()) {
                    foundNode.unDelete();
                    elementsQuantity++; }
                    return  foundNode.setValue(value);
        }
        SimpleEntry newNode = new SimpleEntry(key, value);
        newNode.markRed();
        newNode.setRightNode(NULL_NODE);
        newNode.setLeftNode(NULL_NODE);
        put(newNode);
        root = treeFixUpAfterAdd(root, newNode);
        elementsQuantity++;
        return null;
    }

    // additional private method for finding place and put new node
    private void put(SimpleEntry newNode) {
        SimpleEntry workPosition = root;
        SimpleEntry parent = root;
        int keyHash = newNode.getKeyHashCode();
        int workHash = -1;
        while (workPosition!=NULL_NODE){
            parent = workPosition;
            workHash = workPosition.getKeyHashCode();
            if (keyHash>workHash) workPosition=workPosition.getRightNode();
            if (keyHash<workHash) workPosition=workPosition.getLeftNode();
            }
        if (keyHash>parent.getKeyHashCode()) parent.setRightNode(newNode);
        if (keyHash<parent.getKeyHashCode()) parent.setLeftNode(newNode);
        newNode.setParentNode(parent);
    }

    // additional method for fixing RB tree after insert the node
    private SimpleEntry treeFixUpAfterAdd (SimpleEntry root, SimpleEntry node){
        SimpleEntry uncleNode;

        while (node!=root && node.getParentNode().isRedNode==true) {
            if (node.getParentNode()==node.getParentNode().getParentNode().getLeftNode()){
                // node in left tree of grandfather
                uncleNode = node.getParentNode().getParentNode().getRightNode();
                if (uncleNode.isRedNode()) {    // Case 1 - uncle is RED
                    node.getParentNode().markBlack();
                    uncleNode.markBlack();
                    node.getParentNode().getParentNode().markRed();
                    node=node.getParentNode().getParentNode();
                } else {   // Cases 2 & 3 - uncle is BLACK
                    if (node == node.getParentNode().getRightNode()) {     // Reduce case 2 to case 3
                        node = node.getParentNode();
                        root = treeRightRotate (root , node);
                    }
                    // Case 3
                    node.getParentNode().markBlack();
                    node.getParentNode().getParentNode().markRed();
                    root = treeRightRotate (root , node.getParentNode().getParentNode());
                }
            } else {          // Node in right tree of grandfather
                uncleNode = node.getParentNode().getParentNode().getLeftNode();
                if (uncleNode.isRedNode()) {     /* Case 1 - uncle is RED */
                    node.getParentNode().markBlack();
                    uncleNode.markBlack();
                    node.getParentNode().getParentNode().markRed();
                    node = node.getParentNode().getParentNode();
                } else {    // Uncle is BLACK
                    if (node == node.getParentNode().getLeftNode()) {
                        node = node.getParentNode();
                        root = treeRightRotate (root , node); }
                    node.getParentNode().markBlack();
                    node.getParentNode().getParentNode().markRed();
                    root = treeLeftRotate (root, node.getParentNode().getParentNode());
                }
            }
        }
        root.markBlack();
        root.setParentNode(NULL_NODE);
        return root;
    }

    // additional method for the subtree right rotation
    private SimpleEntry treeRightRotate (SimpleEntry root, SimpleEntry node){
        SimpleEntry leftNode = node.getLeftNode();
        node.setLeftNode(leftNode.getRightNode());
        if (leftNode.getRightNode()!=NULL_NODE) leftNode.getRightNode().setParentNode(node);
        if (leftNode !=NULL_NODE) leftNode.setParentNode(node.getParentNode());
        if (node.getParentNode()!=NULL_NODE) {
            if (node == node.getParentNode().getRightNode()) node.getParentNode().setRightNode(leftNode);
            else node.getParentNode().setLeftNode(leftNode);
        } else { root = leftNode; }
        leftNode.setRightNode(node);
        if (node!=NULL_NODE) node.setParentNode(leftNode);
        return root;
    }

    // additional method for the subtree left rotation
    private SimpleEntry treeLeftRotate (SimpleEntry root, SimpleEntry node){
        SimpleEntry rightNode = node.getRightNode();
        node.setRightNode(rightNode.getLeftNode()); /* Create node->right link */
        if (rightNode.getLeftNode()!=NULL_NODE) rightNode.getLeftNode().setParentNode(node);
        /* Create right->parent link */
        if (rightNode!=NULL_NODE) rightNode.setParentNode(node.getParentNode());
        if (node.getParentNode()!=NULL_NODE) {
            if (node == node.getParentNode().getLeftNode()) node.getParentNode().setLeftNode(rightNode);
            else node.getParentNode().setRightNode(rightNode);
        } else { root = rightNode; }
        rightNode.setLeftNode(node);
        if (node!=NULL_NODE) node.setParentNode(rightNode);
        return root;
    }

    // additional method for copying all instanses into vector - general preparing for recursion method call
    private Vector<SimpleEntry> treeMapToArray(){
        Vector<SimpleEntry> arrayToReturn = new Vector<>();
        treeMapPutToArray(arrayToReturn, root);
        return arrayToReturn;
    }

    // recursion method for tree elements getting
    private void treeMapPutToArray(Vector<SimpleEntry> arr, SimpleEntry start){
            if (start==NULL_NODE) return;
            treeMapPutToArray(arr,start.getRightNode());
            arr.add(start);
            treeMapPutToArray(arr,start.getLeftNode());
    }

    // method for removing nodes - nodes will marks as deleted, but will exist in the tree and will ignored
    @Override
    public Object remove(Object key) {
        SimpleEntry toReturn = getNode(key);
        if (toReturn!=NULL_NODE && (!toReturn.isDeleted())) {
            toReturn.markDeleted();
            elementsQuantity--;
            return toReturn.getValue();
        }
        return null;
    }

    // returns actual size of the tree map;
    @Override
    public int size() {
        return elementsQuantity;
    }

    // returns iteretor for the map;
    @Override
    public Iterator entryIterator() {
            currentPositionForIterator = 0;
            iterableElem = treeMapToArray();
            if (iterableElem == null) iterableElem =new Vector<>();
            return new Iterator(){
                public boolean hasNext(){
                    return (currentPositionForIterator<iterableElem.size() ? true : false);
                }
                public Object next(){
                    SimpleEntry tempEntry = iterableElem.get(currentPositionForIterator);
                    while (tempEntry.isDeleted()) {
                        currentPositionForIterator++;
                        tempEntry = iterableElem.get(currentPositionForIterator);
                    }
                    currentPositionForIterator++;
                    return tempEntry;
                }
                public void remove(){
                    iterableElem.get(currentPositionForIterator-1).markDeleted();
                }
            };
    }

    private void isNullCheck(Object obj){
        if (obj==null) throw new IllegalArgumentException("Parametr can't be null");
    }

    // hash code making method for key
    public int keyHashCode(Object key) {
        int keyHash = 31 * (key!=null ? key.hashCode() : 1);
        return (keyHash>0 ? keyHash : (keyHash*(-1)));
    }

// Node store container class
    class SimpleEntry<K , V> implements MyMap.Entry {
        private int keyHashCode;
        private K entryKey;
        private V entryValue;
        private SimpleEntry leftNode;
        private SimpleEntry rightNode;
        private SimpleEntry parentNode;
        private Boolean isDeletedNode;
        private Boolean isRedNode;

        // all needed getters;
        public SimpleEntry getLeftNode() {
            return leftNode;
        }
        public SimpleEntry getRightNode() {
            return rightNode;
        }
        public SimpleEntry getParentNode() { return parentNode; }
        public boolean isDeleted(){
            return this.isDeletedNode;
        }
        public boolean isRedNode(){
            return this.isRedNode;
        }
        public int getKeyHashCode(){
        return this.keyHashCode;
    }


         // all needed setters;
        public void setLeftNode(SimpleEntry leftNode) {
            this.leftNode = leftNode;
        }
        public void setRightNode(SimpleEntry rightNode) {
            this.rightNode = rightNode;
        }
        public void setParentNode(SimpleEntry parentNode) {
            this.parentNode = parentNode;
        }
        public void markDeleted(){
            isDeletedNode =true;
        }
        public void markRed(){
            isRedNode =true;
        }
        public void markBlack(){
            isRedNode =false;
        }

        // mark node is not deleted
        public void unDelete(){
            isDeletedNode =false;
    }

        // Constrictor - if key or entry ==null - returns specific NULL_NODE
        // else returns normal node;
        public SimpleEntry(K entryKey, V entryValue){
            if ( entryKey == null || entryValue == null) {
                this.entryKey = null;
                this.entryValue = null;
                this.leftNode = null;
                this.parentNode = null;
                this.isRedNode = false;
                this.rightNode = null;
                this.isDeletedNode = false;
                this.keyHashCode = 0;

            } else {
                this.entryKey = entryKey;
                this.entryValue = entryValue;
                this.leftNode = null;
                this.parentNode = null;
                this.isRedNode = true;
                this.rightNode = null;
                this.isDeletedNode = false;
                this.keyHashCode = keyHashCode(entryKey);
            }


        }

        @Override
        public Object getKey() {
            return entryKey;
        }
        public Object getValue() {
            return entryValue;
        }

        @Override
        public Object setValue(Object value) {
            V toReturnValue = entryValue;
            entryValue = (V) value;
            return toReturnValue;
        }

        @Override
        public boolean equals (Object entry) {
            if (this == entry) return true;
            if (this == null && entry==null) return true;
            if (entry==null) return false;
            if (!(entry instanceof SimpleEntry)) return false;
            SimpleEntry<K, V> entry1 = (SimpleEntry)entry;
            if (((SimpleEntry) entry).entryKey.equals(entry1.getKey()) && ((SimpleEntry) entry).entryValue.equals(entry1.entryValue)) return true;
            return false;
        }
        @Override
        public int hashCode() {
            return 31 * entryKey.hashCode() + entryValue.hashCode();
        }

        @Override
        public String toString() {
            return "SimpleEntry{" +
                    "keyHashCode=" + keyHashCode +
                    ", entryKey=" + entryKey +
                    ", entryValue=" + entryValue +
                    ", leftNode=" + leftNode +
                    ", rightNode=" + rightNode +
                    ", isDeletedNode=" + isDeletedNode +
                    '}';
            }
        }
}
