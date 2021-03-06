import java.util.Arrays;

public class WeightedQuickUnionUF
{
    private int[] id;
    private int[] sz;

    public WeightedQuickUnionUF(int N)
    {
        id = new int[N];
        sz = new int[N];

        for (int i = 0; i < N; i++)
        {
            id[i] = i;
            sz[i] = 1;
        }
    }

    public boolean connected(int p, int q)
    {
        return root(id[p]) == root(id[q]);
    }

    public void union(int p, int q)
    {
        int i = root(p);
        int j = root(q);

        if (i == j)
            return;

        if (sz[i] < sz[j])
        {
            id[i] = j;
            sz[j] += sz[i];
        }
        else
        {
            id[j] = i;
            sz[i] += sz[j];
        }
    }

    public String toString()
    {
        return "id: " + Arrays.toString(id) + "; sz: " + Arrays.toString(sz);
    }

    private int root(int p)
    {
        while (p != id[p])
            p = id[p];

        return p;
    }
}
