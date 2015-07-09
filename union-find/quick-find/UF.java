import java.util.Scanner;

public class UF
{
    public static void main(String[] args)
    {
        Scanner s = new Scanner(System.in);

        int N = s.nextInt();
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);

        while (s.hasNext())
        {
            int p = s.nextInt();
            int q = s.nextInt();

            System.out.println(uf);
            System.out.println(p + " " + q);
            uf.union(p, q);
        }
        System.out.println(uf);
    }
}
