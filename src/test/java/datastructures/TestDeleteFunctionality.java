package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.interfaces.IList;

import static org.junit.Assert.fail;


/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {

   
    @Test(timeout=SECOND)
    public void testDeleteBasic() {
        IList<String> list = makeBasicList();
        
        list.delete(1);
        
        assertEquals(list.size(), 2);
        this.assertListMatches(new String[] {"a", "c"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteFront() {
        IList<String> list = makeBasicList();
        list.delete(0);
        this.assertListMatches(new String[] {"b", "c"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteEnd() {
        IList<String> list = makeBasicList();
        list.delete(2);
        this.assertListMatches(new String[] {"a", "b"}, list);
    }
    @Test(timeout=SECOND)
    public void testDeleteMultiple(){
        IList<String> list = makeBasicList();
        list.add("d");
        list.add("e");
        list.add("f");     
        list.delete(0);
        list.delete(4);
        list.delete(1);
        list.delete(1);
        this.assertListMatches(new String[] {"b", "e"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testDeleteOutOfBounds(){
        IList<String> list = this.makeBasicList();

        try {
            list.delete(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }

        try {
            list.delete(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // This is ok: do nothing
        }
    }
    

    @Test(timeout=SECOND)
    public void testInsertAddAndDelete() {
        IList<String> list = makeBasicList();
        
        list.insert(1, "d");
        list.delete(1);
        list.insert(0, "e");
        list.delete(0);
        list.add("zz");
        list.delete(3);
        
        this.assertListMatches(new String[] {"a", "b", "c"}, list);
    }
    
    @Test(timeout=SECOND)
    public void testSHITSHITSHIT() {
        IList<String> list = makeBasicList();
        
        assertEquals("b", list.get(1));
        assertEquals("b", list.delete(1));
        assertEquals("c", list.get(1));
    }
}
    
        


        
        
    



