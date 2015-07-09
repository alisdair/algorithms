import java.util.Arrays;

public class QuickFindUF
{
    private int[] id;

    public QuickFindUF(int N)
    {
        id = new int[N];

        for (int i = 0; i < id.length; i++)
            id[i] = i;
    }

    public boolean connected(int p, int q)
    {
        return id[p] == id[q];
    }

    public void union(int p, int q)
    {
        int idp = id[p];
        int idq = id[q];

        if (idp == idq)
            return;

        for (int i = 0; i < id.length; i++)
            if (id[i] == idp)
                id[i] = idq;
    }

    public String toString()
    {
        return Arrays.toString(id);
    }
}
