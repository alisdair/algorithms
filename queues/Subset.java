public class Subset {
   public static void main(String[] args)
   {
       RandomizedQueue<String> queue = new RandomizedQueue<String>();
       int k = Integer.parseInt(args[0]);

       int i = 0;
       while (!StdIn.isEmpty())
       {
           String s = StdIn.readString();
           i++;
           double r = StdRandom.uniform();
           double p = (double) k / i;

           if (r < p)
           {
               if (p < 1)
                   queue.dequeue();
               queue.enqueue(s);
           }
       }

       while (!queue.isEmpty())
           StdOut.println(queue.dequeue());
   }
}
