import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int size;

    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;
    }

    // construct an empty deque
    public Deque()
    {
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the deque
    public int size()
    {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
            throw new java.lang.NullPointerException();

        Node node = new Node();
        node.item = item;
        node.next = head;

        if (head != null)
            head.prev = node;

        head = node;
        if (tail == null)
            tail = node;

        size++;
    }

    // add the item to the end
    public void addLast(Item item)
    {
        if (item == null)
            throw new java.lang.NullPointerException();

        Node node = new Node();
        node.item = item;
        node.prev = tail;

        if (tail != null)
            tail.next = node;

        tail = node;
        if (head == null)
            head = node;

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (head == null)
            throw new java.util.NoSuchElementException();

        Node node = head;

        if (node.next != null)
            node.next.prev = null;
        head = node.next;

        if (tail == node)
            tail = null;

        size--;

        return node.item;
    }

    // remove and return the item from the end
    public Item removeLast()
    {
        if (tail == null)
            throw new java.util.NoSuchElementException();

        Node node = tail;

        if (node.prev != null)
            node.prev.next = null;
        tail = node.prev;

        if (head == node)
            head = null;

        size--;

        return node.item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node current = head;

        public boolean hasNext()
        {
            return current != null;
        }

        public void remove()
        {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next()
        {
            if (current == null)
                throw new java.util.NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing
    public static void main(String[] args)
    {
        Deque<Integer> d = new Deque<Integer>();

        StdOut.println("Deque size = " + d.size());

        d.addFirst(5);
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        d.addFirst(6);
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        d.addLast(4);
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        StdOut.println(d.removeFirst());
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        StdOut.println(d.removeFirst());
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        StdOut.println(d.removeFirst());
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);

        d.addLast(4);
        StdOut.println("Deque size = " + d.size());
        for (int i : d)
            StdOut.println(i);
    }
}
