
package ftchecker;

import java.util.ArrayList;
import java.util.List;

public interface IFTChecker
{

    public abstract void init(int ai[]);

    public abstract void addForbiddenTuple(int ai[]);
    
    public abstract void addForbiddenTuples(ArrayList<Tuple> tuples);

    public abstract List<Tuple> getMinimumForbiddenTuples();

    public abstract boolean isValid(int ai[]);
    
    public abstract boolean isValid(int ai[], int parameter, int value);
    
    public abstract boolean isValid(ArrayList<Integer> test, int parameter, int value);
    
    public abstract ArrayList<Tuple> getMFTbyParam(int test[], int parameter, int value);
    
    public int getMFTNumbyParam(int test[], int parameter, int value);
    
    public int evalMove_ParaMFS(int[] test, int parameter, int oldValue, int newValue);
    
    public int evalMove_ParaMFS2(int[] test, int column, int newValue,int column2,int newValue2);
    
    public abstract  int[] getNumofMFTbyParam();
}
