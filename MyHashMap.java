package lab9;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by viacheslav on 12.05.15.
 */
public class MyHashMap implements MyMap {
    static final int DEFAULT_INIT_CAPASITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private int actualMapSize;
    private int addedElements;
    private float actualLoadFactor;

    // Stored values will be put into Array of ArrayLists for basket;
    private ArrayList<SimpleEntry> [] mapArray;

    // spesial variable for iterator;
    private int currentPositionForIterator;

    // Construct default Hash Map;
    public MyHashMap(){
        this.actualMapSize=DEFAULT_INIT_CAPASITY;
        this.actualLoadFactor=DEFAULT_LOAD_FACTOR;
        mapArray = new ArrayList [DEFAULT_INIT_CAPASITY];
        addedElements=0;
    }

    // Construct  Hash Map with specific size;
    public MyHashMap(int initialCapacity){
        if (initialCapacity<0) throw new IllegalArgumentException();
        this.actualMapSize= (initialCapacity>DEFAULT_INIT_CAPASITY ? initialCapacity : DEFAULT_INIT_CAPASITY);
        this.actualLoadFactor=DEFAULT_LOAD_FACTOR;
        mapArray = new ArrayList [actualMapSize];
        addedElements=0;
    }

    // Construct  Hash Map with specific size and spesific load factor;
    public MyHashMap(int initialCapacity, float loadFactor){
        if (initialCapacity<0 || loadFactor <0) throw new IllegalArgumentException();
        this.actualMapSize= (initialCapacity>DEFAULT_INIT_CAPASITY ? initialCapacity : DEFAULT_INIT_CAPASITY);
        this.actualLoadFactor=loadFactor;

        addedElements=0;
    }

    // additional method for not null basket counting;
    private int notNullCellsCounting(){
        int counter = 0;
        for (ArrayList arrElem : mapArray ){
            if (arrElem!=null) counter++;
        }
        return counter;
    }

    // additional method for check the current load;
    private boolean isLoadOk(){
        int currentLoad = notNullCellsCounting()/actualMapSize;
        return (currentLoad>actualLoadFactor ? false : true );
    }

    // additional method for normalizing load;
    private void normalizeLoading (){
        ArrayList<SimpleEntry> [] mapToReturn = new ArrayList [actualMapSize*2];
        ArrayList<SimpleEntry> tempEntriesArray = new ArrayList<>();
        for (int i=0; i<mapArray.length; i++ ){
            ArrayList<SimpleEntry> oneNodeArray = mapArray [i];
            if (oneNodeArray!=null) {
            for (SimpleEntry entry : oneNodeArray) {
                tempEntriesArray.add(entry);
                }
            }
        }
        for(SimpleEntry entry : tempEntriesArray){
            int index = hashKeyToPut(entry.getKey());
            if (mapToReturn[index]==null) {
                mapToReturn[index] = new ArrayList<>();
                mapToReturn[index].add(entry);
            }
            else {
                mapToReturn[index].add(entry);
            }
        }
        mapArray = mapToReturn;
        actualMapSize=actualMapSize*2;
    }

    // clearing all data in the hash map;
    @Override
    public void clear() {
        this.actualMapSize=DEFAULT_INIT_CAPASITY;
        this.actualLoadFactor=DEFAULT_LOAD_FACTOR;
        mapArray = new ArrayList [DEFAULT_INIT_CAPASITY];
        addedElements=0;
    }

    // check for instance with the specific key;
    @Override
    public boolean containsKey(Object key) {
        if (key == null) throw new IllegalArgumentException("Key can't be null");
        int index = hashKeyToPut(key);
        if (mapArray[index]==null) return false;
        else {
            ArrayList<SimpleEntry> oneNodeArray = mapArray[index];
            for (SimpleEntry entry : oneNodeArray) {
                if (entry.getKey().equals(key)) return true;
            }
            return false;
        }
    }

    // check for instance with the specific value;
    @Override
    public boolean containsValue(Object value) {
        if (value == null) throw new IllegalArgumentException("Value can't be null");
        ArrayList<SimpleEntry> tempEntriesArray = new ArrayList<>();
        for (int i=0; i<mapArray.length; i++ ){
            ArrayList<SimpleEntry> oneNodeArray = mapArray [i];
            if ( mapArray[i]!=null) {
            for (SimpleEntry entry : oneNodeArray){
                tempEntriesArray.add(entry);
                }
            }
        }
        for (SimpleEntry entry : tempEntriesArray)
            if(entry.getValue().equals(value)) return true;
        return false;
    }

    // receiving value for specific key
    @Override
    public Object get(Object key) {
        if (containsKey(key)==true) {
            int index = hashKeyToPut(key);
            ArrayList<SimpleEntry> oneNodeArray = mapArray[index];
            if (oneNodeArray != null) {
                for (SimpleEntry entry : oneNodeArray) {
                    if (entry.getKey().equals(key)) return entry.getValue();
                }
            }
        }
        return null;
    }

    // check if the map is empty
    @Override
    public boolean isEmpty() {
        return (addedElements==0 ? true : false);
    }

    // putting key and value into the map
    @Override
    public Object put(Object key, Object value) {
        Object oldValue =null;
        int index = hashKeyToPut(key);
        if (mapArray[index]==null) { mapArray[index] = new ArrayList<>();  }
        ArrayList<SimpleEntry> oneNodeArray = mapArray[index];
            for (SimpleEntry entry : oneNodeArray) {
                if (entry.getKey().equals(key)) {
                    oldValue = entry.getValue();
                    entry.setValue(value);
                    return oldValue;
                }
            }
            oneNodeArray.add(new SimpleEntry(key, value));
            addedElements++;
        if(!isLoadOk()) normalizeLoading();
        return oldValue;
    }

    // remove an instance with the specific key
    @Override
    public Object remove(Object key) {
        Object toReturn=null;
        if (containsKey(key)) {
            int index = hashKeyToPut(key);
            ArrayList<SimpleEntry> oneNodeArray = mapArray[index];
            for (int i=0; i<oneNodeArray.size(); i++){
                if (oneNodeArray.get(i).getKey().equals(key)) {
                    toReturn = oneNodeArray.get(i).entryValue;
                    oneNodeArray.remove(i);
                }
            }
            mapArray [index] = oneNodeArray;
        }
        addedElements--;
        return toReturn;
    }

    @Override
    public int size() {
        return addedElements;
    }

    // additional method for putting all entries into arraylist for iterator
    private ArrayList<SimpleEntry> mapToArray(){
        ArrayList<SimpleEntry> tempEntriesArray = new ArrayList<>();
        for (int i=0; i<mapArray.length; i++ ){
            ArrayList<SimpleEntry> oneNodeArray = mapArray [i];
            if (oneNodeArray!=null) {
                for (SimpleEntry entry : oneNodeArray) {
                    tempEntriesArray.add(entry);
                }
            }
        }
        return tempEntriesArray;
    }

    // return the iterator
    @Override
    public Iterator entryIterator() {
        currentPositionForIterator = 0;
        ArrayList<SimpleEntry> iterableElem = mapToArray();
        return new Iterator(){
           public boolean hasNext(){
                return (currentPositionForIterator<iterableElem.size() ? true : false);
           }
           public Object next(){
               SimpleEntry tempEntry = iterableElem.get(currentPositionForIterator);
               currentPositionForIterator++;
               return tempEntry;
           }
           public void remove(){
                SimpleEntry nodeToReturn = iterableElem.get(currentPositionForIterator-1);
                Object result = MyHashMap.this.remove(nodeToReturn.getKey());
                if (result==null) throw new IllegalAccessError("Collection was modificated during iterable");
           }
        };
    }
    // calculating hash for index;
    private int hashKeyToPut(Object key) {
        if (key == null) throw new IllegalArgumentException("Key can't be null");
        int hash = (31* key.hashCode());
        if (hash<0) hash=hash*(-1);
        hash = hash%(actualMapSize-1);
        return hash;
    }

    // internal class for entry
    class SimpleEntry<K, V> implements MyMap.Entry{
        private K entryKey;
        private V entryValue;

    public SimpleEntry(K entryKey, V entryValue){
      if ( entryKey == null || entryValue ==null) throw new IllegalArgumentException("Key or Value cant be null");
        this.entryKey = entryKey;
        this.entryValue = entryValue;
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
        if (value !=null) entryValue = (V) value;
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
                "entryKey=" + entryKey +
                ", entryValue=" + entryValue +
                '}';
        }
    }
}
