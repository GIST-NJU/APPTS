
package ftchecker;

import java.util.*;


public final class FTUtils
{
	public static int nParams = 0;

    public FTUtils()
    {
    }


	public static ArrayList<Tuple> getAllCombinations(int param, final ArrayList<ArrayList<Tuple>>  valueGroup, int specifiedValue, Tuple specifiedTuple)
    {
        ArrayList<Tuple> tuples = new ArrayList<Tuple>();
        if(specifiedValue != -1)
            tuples.add(specifiedTuple);

        ArrayList<Integer> values = new ArrayList<Integer>();
        for(int i = 0; i < valueGroup.size(); i++)
            values.add(Integer.valueOf(i));

        Collections.sort(values, new Comparator<Integer>() {

            public int compare(Integer t1, Integer t2)
            {
                return ((ArrayList<Tuple>)valueGroup.get(t2.intValue())).size() - ((ArrayList<Tuple>)valueGroup.get(t1.intValue())).size();
            }
        	} );
        
        for(int i = 0; i < valueGroup.size(); i++)
        {
            int value = ((Integer)values.get(i)).intValue();
            if(value != specifiedValue)
            {
                ArrayList<Tuple> newTuples = new ArrayList<Tuple>();
                ArrayList<Tuple> list = (ArrayList<Tuple>)valueGroup.get(value);
                for(Iterator<Tuple> iterator = tuples.iterator(); iterator.hasNext();)
                {
                    Tuple t2 = (Tuple)iterator.next();

                    for(Iterator<Tuple> iterator1 = list.iterator(); iterator1.hasNext();)
                    {
                        Tuple t = (Tuple)iterator1.next();
                        Tuple newTuple = t2.combine(t, param);
                        if(newTuple != null && !newTuples.contains(newTuple))
                        {
                            newTuples.add(newTuple);
                        }
                    }

                }

                tuples = newTuples;
                simplify(tuples);
            }
        }

        return tuples;
    }


    public static void simplify(ArrayList<Tuple> tuples)
    {
        for(int i = 0; i < tuples.size(); i++)
        {
            for(int j = i + 1; j < tuples.size(); j++)
            {
                Tuple a = (Tuple)tuples.get(i);
                Tuple b = (Tuple)tuples.get(j);
                if(a.covers(b))
                {
                    tuples.remove(i);
                    i--;
                    break;
                }
                if(b.covers(a))
                {
                    tuples.remove(j);
                    j--;
                }
            }

        }

    }

}
