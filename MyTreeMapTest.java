package lab9;


import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

/**
 * Created by viacheslav on 17.05.15.
 */
public class MyTreeMapTest  {
    MyTreeMap treeMap;

    @Before
    public void setUp() throws Exception {
        treeMap = new MyTreeMap();

    }
    @Test
    public void testMap() throws Exception {
        //Check parametrs after init - size ==0 and is Empty ==true;
        assertEquals(treeMap.size(), 0);
        assertEquals(treeMap.isEmpty(), true);

        // Check state after put 1 element;
        treeMap.put("String", 456);
        assertEquals(treeMap.isEmpty(), false);
        assertEquals(treeMap.size(), 1);

        // Try to get element back;
        assertEquals(treeMap.get("String"), 456);

        // Try to remove and check state;
        assertEquals(treeMap.remove("String"),456);
        assertEquals(treeMap.size(), 0);
        assertEquals(treeMap.isEmpty(), true);
        // Try to get unexisting element from map;

        assertEquals(treeMap.get("String"), null);

        // Try to clean map and check state
        treeMap.clear();
        assertEquals(treeMap.size(),0);
        assertEquals(treeMap.isEmpty(), true);

        // Puting 10 different elements and check size;
        for (int i=0; i<10; i++){
            treeMap.put(("String"+i), 456+i);
        }
        assertEquals(treeMap.size(), 10);
        assertEquals(treeMap.isEmpty(), false);

        // Try to find key and  value in the map
        assertEquals(treeMap.containsKey("String8"), true);
        assertEquals(treeMap.containsValue(464), true);

        // Try to modify value
        treeMap.put(("String8"), 10000);
        assertEquals(treeMap.size(), 10);
        assertEquals(treeMap.containsValue(10000) ,true);
        assertEquals(treeMap.containsValue(464), false);

        // Try to remove key
        assertEquals(treeMap.remove("String8"), 10000);
        assertEquals(treeMap.size(), 9);
        assertEquals(treeMap.containsValue(10000) ,false);
        assertEquals(treeMap.containsKey("String8"), false);

        //Try to use an iterator;
        Iterator iter = treeMap.entryIterator();
        while (iter.hasNext()){
            System.out.println(iter.next());
        }

        //Try to clean map and check state;
        treeMap.clear();
        assertEquals(treeMap.size(),0);
        assertEquals(treeMap.isEmpty(), true);

        // Adding 10 times elements with the same key  - and checking map state
        for (int i=0; i<10; i++){
            treeMap.put(("String"), 456+i);
        }
            assertEquals(treeMap.get("String"), 456 + 9);
            assertEquals(treeMap.containsKey("String"), true);
            assertEquals(treeMap.containsValue(456 + 9) ,true);
            assertEquals(treeMap.size(), 1);

    }


}