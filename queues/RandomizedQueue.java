import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        resize(1);
    }

    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];

        for (int i = 0; i < size; i++)
            copy[i] = items[i];

        items = copy;
    }

    // is the queue empty?
    public boolean isEmpty()
    {
        return size == 0;
    }

    // return the number of items on the queue
    public int size()
    {
        return size;
    }

    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
            throw new java.lang.NullPointerException();

        if (size == items.length)
            resize(2 * items.length);

        int index = StdRandom.uniform(size + 1);
        Item tmp = items[index];
        items[index] = item;
        if (index != size)
            items[size] = tmp;

        size++;
    }

    // remove and return a random item
    public Item dequeue()
    {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        size--;
        Item item = items[size];
        items[size] = null;

        if (size > 0 && size == items.length / 4)
            resize(items.length / 2);

        return item;
    }

    // return (but do not remove) a random item
    public Item sample()
    {
        if (size == 0)
            throw new java.util.NoSuchElementException();

        return items[StdRandom.uniform(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int current;
        private Item[] shuffledItems;

        public RandomizedQueueIterator()
        {
            shuffledItems = (Item[]) new Object[size];
            for (int i = 0; i < size; i++)
            {
                int index = StdRandom.uniform(i + 1);
                Item tmp = shuffledItems[index];
                shuffledItems[index] = items[i];
                if (index != i)
                    shuffledItems[i] = tmp;
            }
        }

        public boolean hasNext()
        {
            return current < size;
        }

        public void remove()
        {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next()
        {
            if (current == size)
                throw new java.util.NoSuchElementException();

            return shuffledItems[current++];
        }
    }

    // unit testing
    public static void main(String[] args)
    {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
        StdOut.println("Size = " + q.size());

        q.enqueue(1);
        StdOut.println("Size = " + q.size());
        q.enqueue(2);
        StdOut.println("Size = " + q.size());
        q.enqueue(3);
        StdOut.println("Size = " + q.size());
        q.enqueue(4);
        StdOut.println("Size = " + q.size());

        for (int i : q)
            StdOut.println(i);
        StdOut.println("------");
        for (int i : q)
            StdOut.println(i);
        StdOut.println("------");
        for (int i : q)
            StdOut.println(i);
    }
}
