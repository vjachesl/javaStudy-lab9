package lab9;

import java.util.Iterator;

/**
 * Created by viacheslav on 12.05.15.
 */
public interface MyMap {
    public void clear(); //removes all of the mappings from this map.
    public boolean containsKey(Object key); // - returns true if this map contains a mapping for the specified key.
    public boolean containsValue(Object value); // - returns true if this map maps one or more keys to the specified value.
    public Object get(Object key); // - returns the value to which the specified key is mapped, or null if this map contains nomapping for the key.
    public boolean isEmpty();//- returns true if this map contains no key-value mappings.
    public Object put(Object key, Object value);  //- associates the specified value with the specified key in this map.
    public Object remove(Object key); //- removes the mapping for the specified key from this map if present.
    public int size();  //- returns the number of key-value mappings in this map.
    public Iterator entryIterator();
    //- returns an iterator over the elements (MyMap.Entry) in proper sequence
    interface Entry {
      public  boolean equals(Object o);// - сompares the specified object with this entry for equality.
      public Object getKey(); // - returns the key corresponding to this entry.
      public int hashCode(); // - returns the hash code value for this map entry.
      public  Object setValue(Object value); // - replaces the value corresponding to this entry with the specified value.
    }
}
