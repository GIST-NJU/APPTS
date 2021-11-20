
package ftchecker;

import java.util.*;


public class MFTChecker
    implements IFTChecker
{
	
	private int mDomains[];
    private FTGroup mAllMFTs;
    private ArrayList<Tuple> mInputTuples;
    
    public MFTChecker()
    {
    }

    public void init(int domains[])
    {
        FTUtils.nParams = domains.length;
        mDomains = domains;
        mInputTuples = new ArrayList<Tuple>();
    }

    public void addForbiddenTuple(int tuple[])
    {
        mInputTuples.add(new Tuple(tuple));
    }
    
    public void addForbiddenTuples(ArrayList<Tuple> tuples)
    {
        mInputTuples.addAll(tuples);
    }

    public List<Tuple> getMinimumForbiddenTuples()
    {
        if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        return mAllMFTs.getAllTuples();
    }
    
    public int[] getNumofMFTbyParam(){
    	if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
    	return mAllMFTs.getNumofMFTbyParam();
    }

	private FTGroup deriveAll(ArrayList<Tuple> inputTuples)
    {
        long start = System.currentTimeMillis();
        FTGroup group = new FTGroup(mDomains, null);
        if(inputTuples.isEmpty())
        {
            System.out.println("No input forbidden tuples.");
            return group;
        }
        PriorityQueue<Tuple> queue = new PriorityQueue<Tuple>(inputTuples.size(), new Comparator<Tuple>() {

            public int compare(Tuple t1, Tuple t2)
            {
                if(t1.size != t2.size)
                    return t1.size - t2.size;
                for(int i = 0; i < t1.size; i++)
                {
                    int p1 = t1.getParam(i);
                    int v1 = t1.getValue(i);
                    int p2 = t2.getParam(i);
                    int v2 = t2.getValue(i);
                    if(p1 != p2)
                        return p1 - p2;
                    if(v1 != v2)
                        return v1 - v2;
                }

                return 0;
            }
        });
        queue.addAll(inputTuples);
        while(!queue.isEmpty()) 
        {
            Tuple t = (Tuple)queue.poll();
            if(group.add(t))
            {
                ArrayList<Tuple> derivedTuples = group.derive(t);
                queue.addAll(derivedTuples);
            }		
        }
        group.calcNumofMFTbyParam();							
        long time = System.currentTimeMillis() - start;
//        System.out.println((new StringBuilder("Generated ")).append(group.size()).append(" MFTs in ").append(time).append(" ms").toString());
        return group;
    }

    public boolean isValid(int test[])
    {
        if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        return !mAllMFTs.represents(tuple);
    }
    
    public boolean isValid(int test[], int parameter, int value)
    {
        if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        return !mAllMFTs.represents(tuple,parameter, value);
    }
    
    public ArrayList<Tuple> getMFTbyParam(int test[], int parameter, int value)
    {
    	if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        return mAllMFTs.getParaMFS(tuple,parameter, value);
    }
    
    public int getMFTNumbyParam(int test[], int parameter, int value)
    {
    	int num;
    	if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        num = mAllMFTs.countParaMFS(tuple, parameter, value);
        return num;
    }
    
    public int evalMove_ParaMFS(int[] test, int parameter, int oldValue, int newValue)
    {
    	int num1,num2;
    	int [] newtest = new int[test.length];
    	for(int i=0; i<test.length; i++)
    		newtest[i] = test[i];
    	newtest[2*parameter+1] = newValue;
    	if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        Tuple newtuple = new Tuple(newtest);
        num1= mAllMFTs.countParaMFS(tuple, parameter, oldValue);
        num2= mAllMFTs.countParaMFS(newtuple, parameter, newValue);
        	
        	
        return num2-num1;
    }
    
    public int evalMove_ParaMFS2(int[] test, int column, int newValue,int column2,int newValue2)
    {
    	int i,k,num;
    	int c1,c2,v1,v2;
    	if(column<column2){
			c1=column;
			v1=newValue;
			c2=column2;
			v2=newValue2;
		}
		else{
			c1=column2;
			v1=newValue2;
			c2=column;
			v2=newValue;
		}
    	int [] newtest = new int[test.length];
    	for(i=0; i<test.length; i++)
    		newtest[i] = test[i];
    	newtest[2*c1+1] = newValue;
    	newtest[2*c2+1] = newValue2;
    	
		ArrayList<Tuple> mft_param = getMFTbyParam(newtest, c1, v1);
		ArrayList<Tuple> mft_param2 = getMFTbyParam(newtest, c2, v2);
		
		int mft_size = mft_param.size();
		num = mft_size;
		for(Iterator<Tuple> iter=mft_param2.iterator(); iter.hasNext();){
			Tuple t = iter.next();
			for(k=0; k<mft_size; k++)
				if(t.isEqual(mft_param.get(k)))
					break;
			if(k==mft_size)
				num++;
		} 	
        return num;
    }
    
    public boolean isValid(ArrayList<Integer> test, int parameter, int value)
    {
        if(mAllMFTs == null)
            mAllMFTs = deriveAll(mInputTuples);
        Tuple tuple = new Tuple(test);
        return !mAllMFTs.represents(tuple,parameter, value);
    }

}
