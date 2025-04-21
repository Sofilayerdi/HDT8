package java;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VectorHeapTest {
    private VectorHeap<Paciente> heap;

    @Before
    public void setUp() {
        heap = new VectorHeap<>();
    }

    @Test
    public void testAdd() {
        Paciente p1 = new Paciente("Juan Perez", "fractura de pierna", 'C');
        heap.add(p1);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test
    public void testRemove() {
        Paciente p1 = new Paciente("Maria Ramirez", "apendicitis", 'A');
        Paciente p2 = new Paciente("Carmen Sarmientos", "dolores de parto", 'B');
        
        heap.add(p2);
        heap.add(p1);  
        
        assertEquals(p1, heap.remove());
        assertEquals(1, heap.size());
    }

    @Test
    public void testPeek() {
        assertNull(heap.peek());
        
        Paciente p = new Paciente("Test", "test", 'A');
        heap.add(p);
        assertEquals(p, heap.peek());
        assertEquals(1, heap.size()); 
    }

    @Test
    public void testIsEmpty() {
        assertTrue(heap.isEmpty());
        heap.add(new Paciente("Test", "test", 'D'));
        assertFalse(heap.isEmpty());
    }

    @Test
    public void testOrder() {
        Paciente p1 = new Paciente("A", "sintoma", 'C');
        Paciente p2 = new Paciente("B", "sintoma", 'A');
        Paciente p3 = new Paciente("C", "sintoma", 'B');
        
        heap.add(p1);
        heap.add(p2);
        heap.add(p3);
        
        assertEquals(p2, heap.remove()); 
        assertEquals(p3, heap.remove());  
        assertEquals(p1, heap.remove()); 
    }

    @Test
    public void testAddRemoveSequence() {
        for (char c = 'A'; c <= 'E'; c++) {
            heap.add(new Paciente("Paciente-" + c, "sintoma", c));
        }
        
        assertEquals('A', heap.remove().getCodigoEmergencia());
        assertEquals('B', heap.remove().getCodigoEmergencia());
        
        heap.add(new Paciente("Nuevo", "sintoma", 'A'));
        assertEquals('A', heap.remove().getCodigoEmergencia());
    }
}