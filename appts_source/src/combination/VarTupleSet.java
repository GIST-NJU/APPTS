package combination;

import java.util.ArrayList;

import global.Main;
import struct.Entry;

//用来描述某个参数某个取值只被覆盖一次的组合,2020.10.11
public class VarTupleSet {
	private int t_way;
	private ArrayList<ArrayList<ArrayList<Entry>>> varTupleList; //每个参数每个取值参与的只被覆盖一次的组合
	private int [][][] mapping;				//只被覆盖组合在varTupleList中的位置，-1表示不存在该组合
	private int oneCoveredCount;			//只被覆盖一次组合数
	
	public VarTupleSet (int rowSize, int t_way){
		oneCoveredCount = 0;
		this.t_way = t_way;
		varTupleList = new ArrayList<ArrayList<ArrayList<Entry>>>();
		for(int i=0; i<Main.paraNum; i++){
			varTupleList.add(new ArrayList<ArrayList<Entry>>());
		}
		for(int i=0; i<Main.paraNum; i++){
			ArrayList<ArrayList<Entry>> tmp = varTupleList.get(i);
			for(int j=0; j<Main.pValue[i]; j++)
				tmp.add(new ArrayList<Entry>());
		}
		
		int [] tuple = new int[t_way];
		mapping = new int[rowSize][][];
		for(int i=0; i<rowSize; i++){
			Main.paraCombination.gett_tuple(i,tuple);
			int columnSize = 1;
			for(int j=0; j <t_way; j++)
				columnSize *= Main.pValue[tuple[j]];
			mapping[i] = new int[columnSize][];
		}
		for(int i=0; i<mapping.length; i++)
			for(int j=0; j<mapping[i].length; j++)
				mapping[i][j] = new int[t_way];
	}
	
	public void clear(){
		oneCoveredCount= 0;
		for(int i=0; i<varTupleList.size(); i++)
			for(int j=0; j< varTupleList.get(i).size(); j++)
					varTupleList.get(i).get(j).clear();
	}
	
	public void push(int combinationRow, int combinationColumn){
		oneCoveredCount++;
		int [] tuple = new int[t_way];	
		int [] value_tuple = new int[t_way];
		Main.paraCombination.gett_tuple(combinationRow,tuple);
		Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
		for(int i=0; i<t_way; i++){
			varTupleList.get(tuple[i]).get(value_tuple[i])
			.add(new Entry(combinationRow,combinationColumn,i));
			mapping[combinationRow][combinationColumn][i] = 
					varTupleList.get(tuple[i]).get(value_tuple[i]).size()-1;
		}
		
	}
	
	public void pop(int combinationRow, int combinationColumn){
		oneCoveredCount--;
		int [] tuple = new int[t_way];	
		int [] value_tuple = new int[t_way];
		Main.paraCombination.gett_tuple(combinationRow,tuple);
		Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
		for(int i=0; i<t_way; i++){
			ArrayList<Entry> list = varTupleList.get(tuple[i]).get(value_tuple[i]);
			Entry elem = list.get(list.size()-1);
			int pos = mapping[combinationRow][combinationColumn][i];
			list.set(pos, elem);
			mapping[elem.combinationRow][elem.combinationColumn][elem.column_index]=pos;
			list.remove(list.size()-1);
		}
	}
	
	public ArrayList<Entry> getOnecoveredListByVar(int para, int value){
		return varTupleList.get(para).get(value);
	}
	
	public int getOnecoveredCountByVar(int para, int value){
		return varTupleList.get(para).get(value).size();
	}
	
	public int getOnecoveredCount(){
		return oneCoveredCount;
	}

}
