package evalue;

import java.util.ArrayList;
import global.Main;
import struct.CombinationPosition;
import struct.Entry;

public class EvalMove {
	private	int [][]tuple_array;	  
	private	int [][][]tuple_arrays;			//存放每个参数所参与的组合,2020.10.8
	private	int [][]tuple_array2;	    
	private	int subRowSize;				
	private	int subRowSize2;			
	private	int t_way;					
	
	public EvalMove(int t_way){
		int i;
		this.t_way = t_way;
		int num=1;
		int denum = 1;
		int para = Main.paraNum-1;
		int way = t_way-1;
		for(i=0; i<t_way-1; i++)
		{
			num *= (para--);
			denum *= (way--);
		}
		subRowSize = num/denum;
		num=1;
		denum = 1;
		para = Main.paraNum-2;
		way = t_way-2;
		for(i=0; i<t_way-2; i++)
		{
			num *= (para--);
			denum *= (way--);
		}
		subRowSize2 = num/denum;

		tuple_array = new int [subRowSize][t_way];
		tuple_arrays = new int [Main.paraNum][subRowSize][t_way];
		tuple_array2 = new int [subRowSize2][t_way];
		caculArrays();
		
	}
	
	public void caculArrays(){
		for(int k=0; k<Main.paraNum; k++){
			int i,j;
			int r = 0;					
			int []tuple = new int[t_way-1];		
			for(i=0; i<=t_way-2; i++)
				tuple[i] = i;
			while(true)
			{
				for(i=t_way-2; i>0; i--)					
				{
					if(tuple[i] == i-(t_way-2)+(Main.paraNum-1))
					{
						tuple[i-1]++;
						for(j=i; j<=t_way-2; j++)
							tuple[j] = tuple[j-1]+1;
					}
					else
						break;
				}
				if(tuple[0] == 0-(t_way-2)+(Main.paraNum-1))			
					break;

				for(i=0; i<=t_way-2 && tuple[i] < k; i++)
						tuple_arrays[k][r][i] = tuple[i];
				tuple_arrays[k][r][i] = k;
				for(j=i; j<=t_way-2; j++)
					tuple_arrays[k][r][j+1] = tuple[j]+1;
				r++;
				tuple[t_way-2]++;
			}
		}
	}

	public void caculArray(int column){

//		int i,j;
//		int r = 0;					
//		int []tuple = new int[t_way-1];		
//		for(i=0; i<=t_way-2; i++)
//			tuple[i] = i;
//		while(true)
//		{
//			for(i=t_way-2; i>0; i--)					
//			{
//				if(tuple[i] == i-(t_way-2)+(Main.paraNum-1))
//				{
//					tuple[i-1]++;
//					for(j=i; j<=t_way-2; j++)
//						tuple[j] = tuple[j-1]+1;
//				}
//				else
//					break;
//			}
//			if(tuple[0] == 0-(t_way-2)+(Main.paraNum-1))			
//				break;
//
//			for(i=0; i<=t_way-2 && tuple[i] < column; i++)
//					tuple_array[r][i] = tuple[i];
//			tuple_array[r][i] = column;
//			for(j=i; j<=t_way-2; j++)
//				tuple_array[r][j+1] = tuple[j]+1;
//			r++;
//			tuple[t_way-2]++;
//		}
		tuple_array=tuple_arrays[column];
		
	}
	
	public void caculArray2(int column,int column2){
			
		if(column>column2)
		{int t=column;column=column2;column2=t;}
		
		if(t_way==2){
			tuple_array2[0][0]=column;
			tuple_array2[0][1]=column2;
			return;
		}

		int i,j,k;
		int r = 0;					
		int []tuple = new int[t_way-2];		
		for(i=0; i<=t_way-3; i++)
			tuple[i] = i;
		while(true)
		{
			for(i=t_way-3; i>0; i--)					
			{
				if(tuple[i] == i-(t_way-3)+(Main.paraNum-2))
				{
					tuple[i-1]++;
					for(j=i; j<=t_way-3; j++)
						tuple[j] = tuple[j-1]+1;
				}
				else
					break;
			}
			if(tuple[0] == 0-(t_way-3)+(Main.paraNum-2))			
				break;

			for(i=0; i<=t_way-3 && tuple[i] < column; i++)
					tuple_array2[r][i] = tuple[i];
			tuple_array2[r][i] = column;
			for(j=i; j<=t_way-3; j++)
				tuple_array2[r][j+1] = tuple[j]+1;
			for(k=t_way-2;tuple_array2[r][k]>=column2;k--)
			{
				tuple_array2[r][k+1]=tuple_array2[r][k]+1;
			}
			tuple_array2[r][k+1] = column2;
			r++;
			tuple[t_way-3]++;
		}
		
	}

	public int evaluate(int row, int column, int newValue){
		int effect=0;
		int i,z;
		
		caculArray(column); 
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow=0,oldCombinationColumn,newCombinationColumn;
		
		for(z=0; z<subRowSize; z++)
		{
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array[z][i]];

				if(tuple_array[z][i] == column)
					newTupleValue[i] = newValue;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],newTupleValue);

			if(Main.coveredCombination.isOnlyOnce(combinationRow,oldCombinationColumn))
				effect++;
			if(Main.coveredCombination.isUncovered(combinationRow,newCombinationColumn))
				effect--;
		}
		
		return effect;
	}
	
	public int evaluate_fast(int row, int column, int newValue){
		int effect=0;
		int [] tuple = new int[t_way];					
		int [] value_tuple = new int[t_way];	
		
		ArrayList<CombinationPosition> unCoveredList = Main.coveredCombination.getUncoveredList();
//		ArrayList<Entry> unCoveredList= Main.coveredCombination.getUncoveredListByVar(column,newValue);
		for(CombinationPosition element : unCoveredList){
//		for(Entry element : unCoveredList){
			int combinationRow = element.combinationRow;
			Main.paraCombination.gett_tuple(combinationRow,tuple);
			int combinationColumn = element.combinationColumn;
			Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
			boolean flag = true;		
			for(int j=0; j<t_way && flag; j++){
				if(tuple[j]!=column)
					{if(value_tuple[j] != Main.coverageArray[row][tuple[j]])
						flag = false;}
				else
					{if(value_tuple[j] != newValue)
						flag = false;}
			}
			if(flag)				
				effect--;
			
		}
		
		ArrayList<Entry> oneCoveredList =
				Main.coveredCombination.getOnecoveredListByVar(column,Main.coverageArray[row][column]);
		for(Entry element : oneCoveredList){
			int combinationRow = element.combinationRow;
			Main.paraCombination.gett_tuple(combinationRow,tuple);
			int combinationColumn = element.combinationColumn;
			Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
			boolean flag = true;		
			for(int j=0; j<t_way && flag; j++){
				if(value_tuple[j] != Main.coverageArray[row][tuple[j]])
						flag = false;
			}
			if(flag)				
				effect++;
		}
//		effect += Main.coveredCombination.getOnlyOnceCoveredNumByVar(row, column, newValue);
		
		return effect;
	}
	
	
	public int evaluate2(int row, int column, int newValue, int othercolumn){
		int effect=0;
		int i,z;
		boolean isBoth;			
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow,oldCombinationColumn,newCombinationColumn;
		
		caculArray(column);
		
		for(z=0; z<subRowSize; z++)
		{	
			isBoth = false;
			for(i=0; i<t_way&&!isBoth; i++)
			{
				if(tuple_array[z][i]==othercolumn)
					isBoth = true;
			}
			if(isBoth)				
				continue;
			
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array[z][i]];

				if(tuple_array[z][i] == column)
					newTupleValue[i] = newValue;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],newTupleValue);

			if(Main.coveredCombination.isOnlyOnce(combinationRow,oldCombinationColumn))
				effect++;
			if(Main.coveredCombination.isUncovered(combinationRow,newCombinationColumn))
				effect--;
		}
		
		return effect;
	}
	
	
	
	public int evaluateBoth(int row, int column, int newValue, int column2, int newValue2){
		int effect=0;
		int i,z;
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow,oldCombinationColumn,newCombinationColumn;
		
		caculArray2(column,column2);
		
		for(z=0; z<subRowSize2; z++)
		{				
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array2[z][i]];

				if(tuple_array2[z][i] == column)
					newTupleValue[i] = newValue;
				else if(tuple_array2[z][i] == column2)
					newTupleValue[i] = newValue2;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array2[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array2[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array2[z],newTupleValue);

			if(Main.coveredCombination.isOnlyOnce(combinationRow,oldCombinationColumn))
				effect++;
			if(Main.coveredCombination.isUncovered(combinationRow,newCombinationColumn))
				effect--;
		}
		
		return effect;
		
	}
	
	public void move(int row, int column, int newValue){
		
		int i,z;
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow=0,oldCombinationColumn,newCombinationColumn;
		
		caculArray(column);

		for(z=0; z<subRowSize; z++)
		{
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array[z][i]];

				if(tuple_array[z][i] == column)
					newTupleValue[i] = newValue;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],newTupleValue);

			Main.coveredCombination.dec(combinationRow,oldCombinationColumn);

			Main.coveredCombination.inc(combinationRow,newCombinationColumn);
		}

		Main.coverageArray[row][column] = newValue;
	}
	
	public void move2(int row, int column, int newValue,int othercolumn){
		
		int i,z;
		boolean isBoth;			
		
		caculArray(column);
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow,oldCombinationColumn,newCombinationColumn;

		for(z=0; z<subRowSize; z++)
		{
			isBoth = false;
			for(i=0; i<t_way&&!isBoth; i++)
			{
				if(tuple_array[z][i]==othercolumn)
					isBoth = true;
			}
			if(isBoth)				
				continue;
			
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array[z][i]];

				if(tuple_array[z][i] == column)
					newTupleValue[i] = newValue;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array[z],newTupleValue);

			Main.coveredCombination.dec(combinationRow,oldCombinationColumn);

			Main.coveredCombination.inc(combinationRow,newCombinationColumn);
		}

	}
	
	
	public void moveBoth(int row, int column, int newValue, int column2, int newValue2){
		
		int i,z;
		
		int [] oldTupleValue = new int[t_way];
		int [] newTupleValue = new int[t_way];
		int combinationRow=0,oldCombinationColumn,newCombinationColumn;
		
		caculArray2(column,column2);

		for(z=0; z<subRowSize2; z++)
		{
			for(i=0; i<t_way; i++)
			{
				oldTupleValue[i]=Main.coverageArray[row][tuple_array2[z][i]];

				if(tuple_array2[z][i] == column)
					newTupleValue[i] = newValue;
				else if(tuple_array2[z][i] == column2)
					newTupleValue[i] = newValue2;
				else
					newTupleValue[i]=oldTupleValue[i];
			}

			combinationRow = Main.paraCombination.getRowNum(tuple_array2[z]);
			oldCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array2[z],oldTupleValue);
			newCombinationColumn = Main.paraValueCombination.getColumnNum(tuple_array2[z],newTupleValue);

			Main.coveredCombination.dec(combinationRow,oldCombinationColumn);

			Main.coveredCombination.inc(combinationRow,newCombinationColumn);
		}
		
		Main.coverageArray[row][column] = newValue;							
		Main.coverageArray[row][column2] = newValue2;							
	}

}
