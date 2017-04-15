class List<L>
{
    L element;
    L next;
    L prev;

    Entry(L element, L next, L prev)
    {
        this.element = element;
        this.next = next;
        this.prev = prev;
    }
    public String toString ()
    {
        String s = "";
        int i;
        for (i = 0; i < L/*длина*/; i++)
        {
            s += /*не знаю как тебе выводить нужно*/ ;
        }
        return s;
    }
    public int Operation(List L)
    {
        int res = 0;
        //реализация операции
        return res;
    }
}
public class Test3
{
    public static void main(String[] args)
    {
        List L = new List (/*добавь сюда что нужно;) всякие там параметры*/);
        double res = L.Operation(L);
        System.out.println(L);
        System.out.println(res);
    }
}
