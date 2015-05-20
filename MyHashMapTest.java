package lab9;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;

/**
 * Created by viacheslav on 15.05.15.
 */
public class MyHashMapTest {
    MyHashMap hashMapMap;

    @Before
    public void setUp() throws Exception {
        hashMapMap = new MyHashMap();

    }
    @Test
    public void testMap() throws Exception {
        //Check parametrs after init - size ==16 and is Empty ==true;
        assertEquals(hashMapMap.size(), 0);
        assertEquals(hashMapMap.isEmpty(), true);

        // Check state after put 1 element;
        hashMapMap.put("String", 456);
        assertEquals(hashMapMap.isEmpty(), false);
        assertEquals(hashMapMap.size(), 1);

        // Try to get element back;
        assertEquals(hashMapMap.get("String"), 456);

        // Try to remove and check state;
        assertEquals(hashMapMap.remove("String"),456);
        assertEquals(hashMapMap.size(), 0);
        assertEquals(hashMapMap.isEmpty(), true);
        // Try to get unexisting element from map;

        assertEquals(hashMapMap.get("String"), null);

        // Try to clean map and check state
        hashMapMap.clear();
        assertEquals(hashMapMap.size(),0);
        assertEquals(hashMapMap.isEmpty(), true);

        // Puting 10 different elements and check size;
        for (int i=0; i<10; i++){
            hashMapMap.put(("String"+i), 456+i);
        }
        assertEquals(hashMapMap.size(), 10);
        assertEquals(hashMapMap.isEmpty(), false);

        // Try to find key and  value in the map
        assertEquals(hashMapMap.containsKey("String8"), true);
        assertEquals(hashMapMap.containsValue(464), true);

        // Try to modify value
        hashMapMap.put(("String8"), 10000);
        assertEquals(hashMapMap.size(), 10);
        assertEquals(hashMapMap.containsValue(10000) ,true);
        assertEquals(hashMapMap.containsValue(464), false);

        // Try to remove key
        assertEquals(hashMapMap.remove("String8"), 10000);
        assertEquals(hashMapMap.size(), 9);

        assertEquals(hashMapMap.containsValue(10000) ,false);
        assertEquals(hashMapMap.containsKey("String8"), false);

        //Try to use an iterator;
        Iterator iter = hashMapMap.entryIterator();
        while (iter.hasNext()){
            System.out.println(iter.next());
        }

        //Try to clean map and check state;
        hashMapMap.clear();
        assertEquals(hashMapMap.size(),0);
        assertEquals(hashMapMap.isEmpty(), true);

        // Adding 10 times elements with the same key  - and checking map state
        for (int i=0; i<10; i++){
            hashMapMap.put(("String"), 456+i);
        }
        assertEquals(hashMapMap.get("String"), 456 + 9);
        assertEquals(hashMapMap.containsKey("String"), true);
        assertEquals(hashMapMap.containsValue(456 + 9) ,true);
        assertEquals(hashMapMap.size(), 1);

    }

}