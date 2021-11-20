package combination;

import java.util.ArrayList;

import global.Main;
import struct.CombinationPosition;
import struct.Entry;

//��������δ��������ϣ�2020.10.9
public class TupleSet {
	private int t_way;					//���Ǳ�ά��
	private ArrayList<CombinationPosition> tupleList;			//���δ����������б�
	private int [][] mapping;				//δ�����������tuplelist�е�λ�ã�-1��ʾ�����ڸ����
//	private ArrayList<ArrayList<ArrayList<Entry>>> varTupleList; //ÿ������ÿ��ȡֵ�����δ������һ�ε����
//	private int [][][] varMapping;				//δ�����������varTupleList�е�λ�ã�-1��ʾ�����ڸ����
	
	public TupleSet(int rowSize, int t_way){
		this.t_way = t_way;
		int [] tuple = new int[t_way];
		
		tupleList = new ArrayList<CombinationPosition>();
		mapping = new int[rowSize][];
		for(int i=0; i<rowSize; i++){
			Main.paraCombination.gett_tuple(i,tuple);
			int columnSize = 1;
			for(int j=0; j <t_way; j++)
				columnSize *= Main.pValue[tuple[j]];
			mapping[i] = new int[columnSize];
		}
		
//		varTupleList = new ArrayList<ArrayList<ArrayList<Entry>>>();
//		for(int i=0; i<Main.paraNum; i++){
//			varTupleList.add(new ArrayList<ArrayList<Entry>>());
//		}
//		for(int i=0; i<Main.paraNum; i++){
//			ArrayList<ArrayList<Entry>> tmp = varTupleList.get(i);
//			for(int j=0; j<Main.pValue[i]; j++)
//				tmp.add(new ArrayList<Entry>());
//		}
//		varMapping = new int[rowSize][][];
//		for(int i=0; i<rowSize; i++){
//			Main.paraCombination.gett_tuple(i,tuple);
//			int columnSize = 1;
//			for(int j=0; j <t_way; j++)
//				columnSize *= Main.pValue[tuple[j]];
//			varMapping[i] = new int[columnSize][];
//		}
//		for(int i=0; i<varMapping.length; i++)
//			for(int j=0; j<varMapping[i].length; j++)
//				varMapping[i][j] = new int[t_way];
	}
	
	public void clear(){
		tupleList.clear();
//		for(int i=0; i<varTupleList.size(); i++)
//			for(int j=0; j< varTupleList.get(i).size(); j++)
//					varTupleList.get(i).get(j).clear();
	}
	
	public void push(int combinationRow, int combinationColumn){
		tupleList.add(new CombinationPosition(combinationRow, combinationColumn));
		mapping[combinationRow][combinationColumn] = tupleList.size()-1;
		
//		int [] tuple = new int[t_way];	
//		int [] value_tuple = new int[t_way];
//		Main.paraCombination.gett_tuple(combinationRow,tuple);
//		Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
//		for(int i=0; i<t_way; i++){
//			varTupleList.get(tuple[i]).get(value_tuple[i])
//			.add(new Entry(combinationRow,combinationColumn,i));
//			varMapping[combinationRow][combinationColumn][i] = 
//					varTupleList.get(tuple[i]).get(value_tuple[i]).size()-1;
//		}
	}
	
	public void pop(int combinationRow, int combinationColumn){
		//��δ����������б��е����һ��Ԫ���ƶ�����ɾ����δ�������������λ��
		CombinationPosition lastElement = tupleList.get(tupleList.size()-1);
		int pos = mapping[combinationRow][combinationColumn];
		tupleList.set(pos, lastElement );
		mapping[lastElement.getCombinationRow()][lastElement.getCombinationColumn()] = pos;
		tupleList.remove(tupleList.size()-1);
		
//		int [] tuple = new int[t_way];	
//		int [] value_tuple = new int[t_way];
//		Main.paraCombination.gett_tuple(combinationRow,tuple);
//		Main.paraValueCombination.gett_tuple(tuple, combinationColumn, value_tuple);
//		for(int i=0; i<t_way; i++){
//			ArrayList<Entry> list = varTupleList.get(tuple[i]).get(value_tuple[i]);
//			Entry elem = list.get(list.size()-1);
//			int pos2 = varMapping[combinationRow][combinationColumn][i];
//			list.set(pos2, elem);
//			varMapping[elem.combinationRow][elem.combinationColumn][elem.column_index]=pos2;
//			list.remove(list.size()-1);
//		}
	}
	
	public ArrayList<CombinationPosition> getTupleList(){
		return tupleList;
	}
	
//	public ArrayList<Entry> getUncoveredListByVar(int para, int value){
//		return varTupleList.get(para).get(value);
//	}
	
	
	public int getSize(){
		return tupleList.size();
	}

}
