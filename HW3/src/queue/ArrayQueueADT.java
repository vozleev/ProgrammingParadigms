package queue;

import java.util.Arrays;

//INV:
// ((q.size == 0 && q.start == q.end) || (q.size > 0 && q.start != q.end)) && q.size < q.elements.length
// elements from start to end in the case of orded
public class ArrayQueueADT {
    private static int size;
    private static int start = 4;
    private static int end = 4;
    private static Object[] elements = new Object[5];

    //Pre: element != null
    //Post: 
    //      ((forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i]) || 
    //      (forall i = q.start, j = q.elements'.length - 1; i != q.end; i = getNextPos(q, i), j-- : q.elements'[j] == q.elements[i])) && 
    //      q.elements'.length == q.elements.length * 2 &&
    //      (q.end' == getNextPos(q, q.end) || q.end' == getNextPos(q, q.start' - q.size)) && 
    //      (q.start' == q.start || q.start == q.elements'.length - 1) && q.size' == q.size + 1
    //      q.elements[end] == q.element
    public static void enqueue(ArrayQueueADT q, Object element) {
        assert element != null;

        ensureCapacity(q, size + 1);
        q.elements[end] = element;
        q.end = getNextPos(q, end);
        q.size++;
    }

    //Pre: 
    //Post: 
    //      ((forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i]) || 
    //      (forall i = q.start, j = q.elements'.length - 1; i != q.end; i = getNextPos(q, i), j-- : q.elements'[j] == q.elements[i])) && 
    //      q.elements'.length == q.elements.length * 2 &&
    //      (q.end' == q.end || q.end' == q.start' - q.size) && 
    //      (q.start' == q.start || q.start == q.elements'.length - 1) && q.size' == q.size
    private static void ensureCapacity(ArrayQueueADT q, int capacity) {
        if (capacity < q.elements.length) {
            return;
        }
        int newCapacity = 2 * capacity;
        Object[] newElements = new Object[newCapacity];
        for (int i = q.start, j = newCapacity - 1; i != q.end; i = getNextPos(q, i), j--) {
            newElements[j] = q.elements[i];
        }
        q.elements = newElements;
        q.start = newCapacity - 1;
        q.end = q.start - size;
    }

    //Pre: 0 <= pos < q.elements.length
    //Post: R = ((pos - 1 + q.elements.length) % q.elements.length) &&
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    private static int getNextPos(ArrayQueueADT q, int pos) {
        return (pos - 1 + q.elements.length) % q.elements.length;
    }

    //Pre: q.size > 0
    //Post: R = q.elements[q.start] &&
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size' - 1
    public static Object dequeue(ArrayQueueADT q) {
        assert size > 0;

        Object value = element(q);
        q.size--;
        start = getNextPos(q, start);
        return value;
    }

    //Pre: q.size > 0
    //Post: R = q.elements[q.start] && forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    public static Object element(ArrayQueueADT q) {
        assert size > 0;

        return q.elements[q.start];
    }

    //Pre: 
    //Post: R = q.size &&
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    public static int size(ArrayQueueADT q) {
        return q.size;
    }

    //Pre: 
    //Post: R = (q.size == 0) 
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    public static boolean isEmpty(ArrayQueueADT q) {
        return q.size == 0;
    }

    //Pre: 
    //Post: R = instance of ArrayQueueADT && 
    //      R.start == q.start && R.end == q.end && R.size == q.size && R.elements.length == q.elements.length && 
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == R.elements[i] 
    //
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    public static ArrayQueueADT makeCopy(ArrayQueueADT q) {
        final ArrayQueueADT copy = new ArrayQueueADT();
        copy.size = q.size;
        copy.elements = Arrays.copyOf(q.elements, q.size);
        copy.start = q.start;
        copy.end = q.end;
        return copy;
    }

    //Pre: 
    //Post: 
    //      forall i = 0...elements.length - 1 : elements[i] == elements'[i] &&
    //      start' == q.elements.length - 1 && end' == start && size == 0
    public static void clear(ArrayQueueADT q) {
        q.start = q.elements.length - 1;
        q.end = q.start;
        q.size = 0;
    }


    //Pre: 
    //Post: R = Object[size] && for i = q.start, j = 0; i != q.end; i = getNextPos(q, i), j++ : res[j] == q.elements[i]
    //      forall i = 0...q.elements.length - 1 : q.elements[i] == q.elements'[i] &&
    //      q.start == q.start' && q.end == q.end' && q.size == q.size'
    public static Object[] toArray(ArrayQueueADT q) {
        Object[] res = new Object[q.size];
        for (int i = q.start, j = 0; i != q.end; i = getNextPos(q, i), j++) {
            res[j] = q.elements[i];
        }
        return res;
    }
}
