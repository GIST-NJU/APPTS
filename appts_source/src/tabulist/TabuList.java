package tabulist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import global.Main;
import struct.Element;

public class TabuList {
	private	Element [] modifiedList;		
	private	int front;						
	private	int rear;						
	private ArrayList<Element> tablst1;      
	private ArrayList<Element> tablst2;      

	public TabuList(){
		modifiedList = new Element[Main.L+1];
		for(int i=0; i<Main.L+1; i++)
			modifiedList[i] = new Element();
		front = rear = 0;
		tablst1= new ArrayList<Element>();
		tablst2= new ArrayList<Element>();
		
	}

	public Element  getElement(int p){
		int k;
		k=(front+p)%(Main.L+1);
		
		return modifiedList[k];
	}

	public int  getLength(){
		return (rear-front+Main.L+1)%(Main.L+1);
	}

	public void clear(){
		front = rear = 0;
		tablst1.clear();
		tablst2.clear();
	}
	

	public void updateUndoList(int row, int column,int value){
		if((rear+1)%(Main.L+1) != front)
		{
			modifiedList[rear].row = row;
			modifiedList[rear].column= column;
			modifiedList[rear].value = value;
			modifiedList[rear].isTwo = false;
			rear=(rear+1)%(Main.L+1);
		}
		else
		{
			front = (front+1)%(Main.L+1);
			modifiedList[rear].row = row;
			modifiedList[rear].column= column;
			modifiedList[rear].value = value;
			modifiedList[rear].isTwo = false;
			rear=(rear+1)%(Main.L+1);
		}
	}
	
	public void updateUndoList2(int row, int column,int value,int column2,int value2){
		if((rear+1)%(Main.L+1) != front)
		{
			modifiedList[rear].row = row;
			modifiedList[rear].column= column;
			modifiedList[rear].value = value;
			modifiedList[rear].column2= column2;
			modifiedList[rear].value2 = value2;
			modifiedList[rear].isTwo = true;
			rear=(rear+1)%(Main.L+1);
		}
		else
		{
			front = (front+1)%(Main.L+1);
			modifiedList[rear].row = row;
			modifiedList[rear].column= column;
			modifiedList[rear].value = value;
			modifiedList[rear].column2= column2;
			modifiedList[rear].value2 = value2;
			modifiedList[rear].isTwo = true;
			rear=(rear+1)%(Main.L+1);
		}
	}
	
	public void getTabuList(){
		int i,j,row,column,value,column2,value2,length;
		boolean isTwo;					    
		HashMap<Integer,Integer> changes;	
		
		tablst1.clear();
		tablst2.clear();
		
		changes = new HashMap<Integer,Integer>();

		length = getLength();		         
		if(length==0) return ;

		int stateCount = 0;


		for(i=length-1; i>=0; i--)
		{
			row = getElement(i).row;
			column = getElement(i).column;
			value = getElement(i).value;

			if(Main.coverageArray[row][column] == value)
			{
				changes.remove(row*Main.paraNum+column);
				stateCount -= 1;
			}
			else
			{
				if(!changes.containsKey(row*Main.paraNum+column))
					stateCount += 1;
				changes.put(row*Main.paraNum+column, value);
			}
			
			if(getElement(i).isTwo){
				column2 = getElement(i).column2;
				value2= getElement(i).value2;

				if(Main.coverageArray[row][column2] == value2)
				{
					changes.remove(row*Main.paraNum+column2);
					stateCount -= 1;
				}
				else
				{
					if(!changes.containsKey(row*Main.paraNum+column2))
						stateCount += 1;
					changes.put(row*Main.paraNum+column2, value2);
				}
				
			}
			if(stateCount == 1)					
				addTabuElement(changes);
			else if(stateCount == 2)			
				addTabuElement2(changes);
		}
		
	}
	
	public void addTabuElement(HashMap<Integer,Integer> changes){
		Iterator<Entry<Integer,Integer>> iter = changes.entrySet().iterator();
			Entry<Integer,Integer> entry = iter.next();
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			int row = key/Main.paraNum;
			int column = key%Main.paraNum;
			tablst1.add(new Element(row,column,value));
	}
	
	public void addTabuElement2(HashMap<Integer,Integer> changes){
		Iterator<Entry<Integer,Integer>> iter = changes.entrySet().iterator();
			Entry<Integer,Integer> entry = iter.next();
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			int row = key/Main.paraNum;
			int column = key%Main.paraNum;
			entry = iter.next();
			Integer key2 = entry.getKey();
			Integer value2 = entry.getValue();
			int row2 = key2/Main.paraNum;
			int column2 = key2%Main.paraNum;
			if(row2!=row)			
				return;
			tablst2.add(new Element(row,column,value,column2,value2));
	}
	
	
	public boolean isTabu(int modiRow, int modiColumn, int modiValue){
		for(int i=0; i<tablst1.size(); i++){
			Element e = tablst1.get(i);
			if(e.row==modiRow&&e.column==modiColumn&&e.value==modiValue)
				return true;
		}
       return false;
	}
	
	public boolean isTabu2(int modiRow, int modiColumn, int modiValue,int modiColumn2, int modiValue2){
		for(int i=0; i<tablst2.size(); i++){
			Element e = tablst2.get(i);
			if(e.row==modiRow&&e.column==modiColumn&&e.value==modiValue
					&& e.column2==modiColumn2 && e.value2==modiValue2)
				return true;
		}
       return false;
	}
	
	public boolean isTabu_simple(int modiRow, int modiColumn, int modiValue){
		int length = getLength();
		for(int i=0; i<length;i++){
			Element e = getElement(i);
			if(e.row==modiRow&&e.column==modiColumn&&e.value==modiValue)
				return true;
		}
       return false;
	}
	
	public boolean isTabu2_simple(int modiRow, int modiColumn, int modiValue,int modiColumn2, int modiValue2){
		int length = getLength();
		for(int i=0; i<length; i++){
			Element e = getElement(i);
			if((e.row==modiRow&&e.column==modiColumn&&e.value==modiValue)
					||( e.row==modiRow& e.column2==modiColumn2 && e.value2==modiValue2))
				return true;
		}
       return false;
	}
}
