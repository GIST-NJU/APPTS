package combination;

import java.util.ArrayList;

import global.Main;
import struct.CombinationPosition;
import struct.Entry;

public class CoveredCombination {
	private int [][] array;				 
	private	int rowSize;				
//	private Integer unCoveredCount;			 
	private int [] pValue;				
	private int t_way;					
//	private HashMap<Integer,LinkedList<CombinationPosition>> unCoveredMap;  
//	private HashMap<Integer,LinkedList<CombinationPosition>> onlyOnceCoveredMap; 
//	private LinkedList<CombinationPosition> allUncoveredList;
	private TupleSet unCoveredTuples;
//	private LineTupleSet onlyOnceCoveredTuples;
	private VarTupleSet oneCoveredTuples;

	public	CoveredCombination(int rowSize, int [] pValue, int t_way){
		int i,j;
		this.rowSize = rowSize;
		this.pValue = pValue;				
		this.t_way = t_way;
		
		int [] tuple = new int[t_way];

		array = new int[rowSize][];
		for(i=0; i<rowSize; i++){
			Main.paraCombination.gett_tuple(i,tuple);
			int columnSize = 1;
			for(j=0; j <t_way; j++)
				columnSize *= Main.pValue[tuple[j]];
			array[i] = new int[columnSize];
		}
		
//		unCoveredMap = new HashMap<Integer,LinkedList<CombinationPosition>>();
//		onlyOnceCoveredMap = new HashMap<Integer,LinkedList<CombinationPosition>>();
//		for(i=0; i<Main.paraNum; i++){
//			unCoveredMap.put(i,new LinkedList<CombinationPosition>());
//			onlyOnceCoveredMap.put(i, new LinkedList<CombinationPosition>());
//		}
//		allUncoveredList = new LinkedList<CombinationPosition>();
		unCoveredTuples = new TupleSet(rowSize,t_way);
		oneCoveredTuples = new VarTupleSet(rowSize, t_way);
		
		
//		unCoveredCount = 0;
	}
	
	public	void clear(){
		int i,j;
		
		for( i=0; i<Main.paraNum; i++){
//			unCoveredMap.get(i).clear();
//			onlyOnceCoveredMap.get(i).clear();
		}
//		allUncoveredList.clear();
		unCoveredTuples.clear();
		oneCoveredTuples.clear();
		
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
		int combinationColumn;
//		unCoveredCount = 0;
		int[] checkedArray = new int[t_way*2];			
		for(i=0; i<rowSize; i++)
		{
			Main.paraCombination.gett_tuple(i,tuple);
			for(j=0; j<t_way; j++){
				value_tuple[j] = 0;
				checkedArray[2*j] = tuple[j];
			}
			while(true)
			{
				for(int m=t_way-1; m>0; m--)					
				{
					if(value_tuple[m] == pValue[tuple[m]])
					{
						value_tuple[m-1]++;
						value_tuple[m] = 0;
					}
					else
						break;
				}
				if(value_tuple[0] == pValue[tuple[0]])			
						break;
				combinationColumn = Main.paraValueCombination.getColumnNum(tuple,value_tuple);
				
				for(j=0; j<t_way; j++)
					checkedArray[2*j+1] = value_tuple[j];
				if(Main.checker.isValid(checkedArray)){
					array[i][combinationColumn] = 0;
//					unCoveredCount++;	
				}
				else
					array[i][combinationColumn] = -1;				
				
				value_tuple[t_way-1]++;
			}
		}
	}
	
	public void init(int rowNum){
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
		
		clear();
		
//		onlyOnceCoveredTuples = new LineTupleSet(rowNum,rowSize,t_way);
//		onlyOnceCoveredTuples.clear();
		
		//保存每一参数取值组合被哪行覆盖，只被覆盖一次该数组才有意义
//		int [][]coverByLineIndex = new int [rowSize][];
//		for(int i=0; i<rowSize; i++){
//			Main.paraCombination.gett_tuple(i,tuple);
//			int columnSize = 1;
//			for(int j=0; j <t_way; j++)
//				columnSize *= Main.pValue[tuple[j]];
//			coverByLineIndex[i] = new int[columnSize];
//		}
		for(int combinationrow=0; combinationrow<rowSize; combinationrow++)
		{
			Main.paraCombination.gett_tuple(combinationrow,tuple);
			for(int k=0; k<rowNum; k++)
			{
				for(int i=0; i<t_way; i++)
					value_tuple[i]=Main.coverageArray[k][tuple[i]];
				int combinationColumn = Main.paraValueCombination.getColumnNum(tuple,value_tuple);
				if(array[combinationrow][combinationColumn] != -1){
//					if(array[combinationrow][combinationColumn] == 0)
//						unCoveredCount--;
					array[combinationrow][combinationColumn] += 1;
//					coverByLineIndex[combinationrow][combinationColumn] = k;
				}
			}
		}
		for(int i=0; i<rowSize; i++){
			Main.paraCombination.gett_tuple(i,tuple);
			for(int j=0; j<array[i].length; j++){
				if(array[i][j]==0){
//					CombinationPosition element = new CombinationPosition(i,j);
//					for(int k=0; k<t_way; k++){
//						unCoveredMap.get(tuple[k]).add(element);}
//					allUncoveredList.add(new CombinationPosition(i,j));
					unCoveredTuples.push(i, j);
				}
				else if(array[i][j]==1){
//					CombinationPosition element = new CombinationPosition(i,j);
//					for(int k=0; k<t_way; k++){
//						onlyOnceCoveredMap.get(tuple[k]).add(element);}
//					onlyOnceCoveredTuples.push(i,j, coverByLineIndex[i][j]);
					oneCoveredTuples.push(i, j);
				}
			}
		}
	}
	
	public void printAllUnCoveredTuple(){
//		int i,j,k;
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
//		for(i=0; i<rowSize; i++){
//			Main.paraCombination.gett_tuple(i,tuple);
//			int columnSize = 1;
//			for(j=0; j <t_way; j++)
//				columnSize *= Main.pValue[tuple[j]];
//			for(k=0; k<columnSize; k++){
//				if(array[i][k]==0){
//					Main.paraValueCombination.gett_tuple(tuple, k, value_tuple);
//					for(j=0; j <t_way; j++)
//						System.out.print(tuple[j]+":"+value_tuple[j]+" ");
//					System.out.println();
//				}
//			}
//		}
		 ArrayList<CombinationPosition> list = unCoveredTuples.getTupleList();
		 for(CombinationPosition elem:list){
			 int combinationRow = elem.getCombinationRow();
			 int combinationColumn = elem.getCombinationColumn();
			 Main.paraCombination.gett_tuple(combinationRow,tuple);
			 Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
			 for(int j=0; j <t_way; j++)
					System.out.print(tuple[j]+":"+value_tuple[j]+" ");
			System.out.println();
		 }
	}
	
	public void checkTupleConst(){
		int i,j,k;
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
		int[] tempValueArray = new int[2*t_way];
		for(i=0; i<rowSize; i++){
			Main.paraCombination.gett_tuple(i,tuple);
			int columnSize = 1;
			for(j=0; j <t_way; j++)
				columnSize *= Main.pValue[tuple[j]];
			for(k=0; k<columnSize; k++){
				Main.paraValueCombination.gett_tuple(tuple, k, value_tuple);
				for(j=0; j <t_way; j++){
					tempValueArray[2*j] = tuple[j];
					tempValueArray[2*j+1] = value_tuple[j];
				}
				if(array[i][k]==-1){
					if(Main.checker.isValid(tempValueArray)){
						System.out.println("fail to check constraints");
						System.exit(0);
					}
				}
				else{
					if(!Main.checker.isValid(tempValueArray)){
						System.out.println("fail to check constraints");
						System.exit(0);
					}
				}
			}
		}
		
	}
	
	public	int getUncoveredCount(){
//		return unCoveredCount;	
		return unCoveredTuples.getSize();
	}
	
	
	public	CombinationPosition getUncoveredComb(int p){
		int i,j,k;
		
		if(p<0 || p>unCoveredTuples.getSize()-1){
			System.out.println("the value of p is unreasonable in the getUncoveredComb!");
			return null;
		}
		else{
			k=0;
			for(i=0; i<rowSize; i++)
				for(j=0; j<array[i].length; j++){
					if(array[i][j] == 0){		
						if(k == p)				
							return new CombinationPosition(i,j);
						k++;
					}
				}
			System.out.println("getUncoveredComb error"+" p="+p+" k="+k);
			return null;
		}
	}
	
	public	CombinationPosition getUncoveredCombByList(int p){
		if(p<0 || p>unCoveredTuples.getSize()-1){
			System.out.println("the value of p is unreasonable in the getUncoveredComb!");
			return null;
		}
//		return allUncoveredList.get(p); 
		return unCoveredTuples.getTupleList().get(p);
	}
	
//	public ArrayList<Integer> getUncoveredValue(int combinationRow){
//		ArrayList<Integer> combinationColumns = new ArrayList<Integer> ();
//		for(int i=0; i<array[combinationRow].length; i++){
//			if(array[combinationRow][i]==0)
//				combinationColumns.add(i);
//		}
//		return combinationColumns;
//	}
	
//	public ArrayList<CombinationPosition> getUncoveredCombs(){
//		ArrayList<CombinationPosition> combinationColumns = new ArrayList<CombinationPosition> ();
//		for(int i=0; i<rowSize; i++)
//			for(int j=0; j<array[i].length; j++){
//				if(array[i][j]==0)
//					combinationColumns.add(new CombinationPosition(i,j));
//			}
//		return combinationColumns;
//	}
//	
//	public LinkedList<CombinationPosition> getUncoveredList(){
//		return allUncoveredList;
//	}
	
	public ArrayList<CombinationPosition> getUncoveredList(){
		return unCoveredTuples.getTupleList();
	}
	
	//获取只被覆盖一次组合的总数
	public int getOnlyOneCoveredCount(){
//		int count1=0;
//		for(int i=0; i<rowSize; i++)
//			for(int j=0; j<array[i].length; j++){
//				if(array[i][j]==1)
//					count1++;
//			}
//		return count1;
		return oneCoveredTuples.getOnecoveredCount();
	}
	
	//获取某个参数某个取值参与的只被覆盖一次的组合数
	public int getOnlyOneCoveredCountByVar(int para, int value){
		return oneCoveredTuples.getOnecoveredCountByVar(para, value);
	}


	public	int getCount(int row, int column){
		return array[row][column];	
	}
	

	public  void inc(int row, int column){
		if(array[row][column] == -1)		
			return;
		if(array[row][column] == 0)		
		{
//			CombinationPosition element = new CombinationPosition(row,column);
//			for(int k=0; k<t_way; k++){
//				int index = unCoveredMap.get(tuple[k]).indexOf(element);
//				if(index==-1)
//				{
//					System.out.println("The combination is not found！");
//					return ;
//				}
//				unCoveredMap.get(tuple[k]).remove(index);
//				onlyOnceCoveredMap.get(tuple[k]).add(element);
//			}
//			int index2 = allUncoveredList.indexOf(element);
//			if(index2==-1)
//			{
//				System.out.println("The combination is not found！");
//				return ;
//			}
//			allUncoveredList.remove(index2);
//			unCoveredCount--;
			unCoveredTuples.pop(row, column);
			oneCoveredTuples.push(row, column);
		}
		else if	(array[row][column] == 1)					
		{
//			CombinationPosition element = new CombinationPosition(row,column);
//			for(int k=0; k<t_way; k++){
//				int index = onlyOnceCoveredMap.get(tuple[k]).indexOf(element);
//				if(index==-1)
//				{
//					System.out.println("The combination is not found！");
//					return ;
//				}
//				onlyOnceCoveredMap.get(tuple[k]).remove(index);
//			}
//			int [] tuple = new int[t_way];
//			int [] value_tuple = new int[t_way];
//			Main.paraCombination.gett_tuple(row,tuple);
//			Main.paraValueCombination.gett_tuple(tuple, column, value_tuple);
//			for(int lineIndex=0; lineIndex<Main.rowNum; lineIndex++){
//				if(lineIndex==oldLineIndex)
//					continue;
//				boolean isMatch = true;
//				for(int j=0; j<t_way; j++){
//					if(Main.coverageArray[lineIndex][tuple[j]]!=value_tuple[j]){
//						isMatch = false;
//						break;
//					}
//				}
//				if(isMatch){
//					onlyOnceCoveredTuples.pop(row, column, lineIndex);
//					break;
//				}
//			}
			oneCoveredTuples.pop(row, column);
		}
		array[row][column] += 1;
	}
	
	
	public void  dec(int row, int column){
		if(array[row][column] == -1)
			return;
		if(array[row][column] == 1){		
//			CombinationPosition element = new CombinationPosition(row,column);
//			for(int k=0; k<t_way; k++){
//				int index = onlyOnceCoveredMap.get(tuple[k]).indexOf(element);
//				if(index==-1)
//				{
//					System.out.println("The combination is not found!");
//					return ;
//				}
//				onlyOnceCoveredMap.get(tuple[k]).remove(index);
//				unCoveredMap.get(tuple[k]).add(element);
//			}
//			allUncoveredList.add(new CombinationPosition(row,column));
//			unCoveredCount++;
			unCoveredTuples.push(row, column);
			oneCoveredTuples.pop(row, column);
		}
		else if(array[row][column] == 2){		
//			CombinationPosition element = new CombinationPosition(row,column);
//			for(int k=0; k<t_way; k++){
//				onlyOnceCoveredMap.get(tuple[k]).add(element);
//			}
//			int [] tuple = new int[t_way];
//			int [] value_tuple = new int[t_way];
//			Main.paraCombination.gett_tuple(row,tuple);
//			Main.paraValueCombination.gett_tuple(tuple, column, value_tuple);
//			for(int lineIndex=0; lineIndex<Main.rowNum; lineIndex++){
//				if(lineIndex==oldLineIndex)
//					continue;
//				boolean isMatch = true;
//				for(int j=0; j<t_way; j++){
//					if(Main.coverageArray[lineIndex][tuple[j]]!=value_tuple[j]){
//						isMatch = false;
//						break;
//					}
//				}
//				if(isMatch){
//					onlyOnceCoveredTuples.push(row, column, lineIndex);
//					break;
//				}
//			}
			oneCoveredTuples.push(row, column);
		}
		array[row][column] -= 1;
	}
	
	
	
	public	boolean isOnlyOnce(int row, int column){
		if(array[row][column] == 1)
			return true;
		else 
			return false;
		
	}

	
	public	boolean isUncovered(int row, int column){
		if(array[row][column] == 0)
			return true;
		else 
			return false;
	}
	
	
	public void update_row(int[] oldrow, int[] newrow, int line){
		int k,i;
		int [] tuple = new int[t_way];					
		int [] oldvalue_tuple = new int[t_way];			
		int oldCombinationColumn;						
		int [] newvalue_tuple = new int[t_way];			
		int newCombinationColumn;						
		for(int combinationrow=0; combinationrow<rowSize; combinationrow++)
		{
			Main.paraCombination.gett_tuple(combinationrow,tuple);
			for(i=0; i<t_way; i++)
			{	
				oldvalue_tuple[i]=oldrow[tuple[i]];
				newvalue_tuple[i]=newrow[tuple[i]];
			}
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple,oldvalue_tuple);
			dec(combinationrow,oldCombinationColumn);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple,newvalue_tuple);
			inc(combinationrow,newCombinationColumn);
		}
	}
	
	public void delete_row(int deleteRowNum){
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
		for(int combinationrow=0; combinationrow<rowSize; combinationrow++)
		{
			Main.paraCombination.gett_tuple(combinationrow,tuple);
			
			for(int i=0; i<t_way; i++)
				value_tuple[i]=Main.coverageArray[deleteRowNum][tuple[i]];
			int combinationColumn = Main.paraValueCombination.getColumnNum(tuple,value_tuple);
			dec(combinationrow,combinationColumn);
		}
//		onlyOnceCoveredTuples.delet_row(deleteRowNum);
	}
	
//	public LinkedList<CombinationPosition> getUnCoveredList(int para)
//	{
//		return unCoveredMap.get(para);
//	}
	
	public  ArrayList<Entry> getOnecoveredListByVar(int para, int value)
	{
		return oneCoveredTuples.getOnecoveredListByVar(para,value);
	}
	
//	public  ArrayList<Entry> getUncoveredListByVar(int para, int value)
//	{
//		return unCoveredTuples.getUncoveredListByVar(para,value);
//	}
	
}