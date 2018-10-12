package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

/**
 *  This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {
    /*
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }
    */
    
    @Test(timeout=15 * SECOND)
    public void testDeleteIsEfficientBasic() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 5000000;
        
        for (int i = 0; i < cap; i++) {
            list.add(i * i * i);
        }
                
        for (int i = 0; i < cap; i++){
            if (list.size() > 1) {
                list.delete(list.size() - 1);
            } else {
                list.delete(0);
            }
        }
        
        assertEquals(0, list.size());
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteAtFrontIsEfficient() {
        IList<String> list = makeBasicList();
        int cap = 5000000;
        
        for (int i = 0; i < cap; i++) {
            list.insert(0, "abc");
        }
        
        for (int i = 0; i < cap; i++){
            list.delete(0);
        }
        
        assertListMatches(new String[] {"a", "b", "c"}, list);
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteAtBackIsEfficient() {
        IList<String> list = makeBasicList();
        int cap = 5000000;
        
        for (int i = 0; i < cap; i++) {
            list.add("abc");
        }
        
        for (int i = 0; i < cap; i++){
            list.delete(list.size() - 1);
        }
        
        assertListMatches(new String[] {"a", "b", "c"}, list);
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteNearFrontIsEfficient() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        list.add(2);
        int cap = 5000000;
        
        for (int i = 0; i < cap; i++) {
            list.insert(1, i);
        }
        
        for (int i = 0; i < cap; i++){
            list.delete(1);
        }
        
        assertEquals(2, list.size());
    }
    
    @Test(timeout=15 * SECOND)
    public void testDeleteNearBackIsEfficient() {
        IList<String> list = makeBasicList();
        int cap = 5000000;
        
        for (int i = 0; i < cap; i++) {
            list.insert(2, "abc");
        }
        
        for (int i = 0; i < cap; i++){
            list.delete(2);
        }
        
        assertListMatches(new String[] {"a", "b", "c"}, list);
    }
}   
