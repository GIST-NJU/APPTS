package global;

import java.io.Serializable;

import struct.Element;
//���м�������2��������Լ�����ĸ����ƶ��Ĵ����Լ��Ƿ񱻽���
public class Neighbor2Parallel implements Serializable{
	private static final long serialVersionUID = 1L;
	private int row;				
	private int column;			
	private int newvalue;
	private Element[] neighbors;
	
	public Neighbor2Parallel(int row, int column, int newvalue,  Element[] neighbors){
		this.row = row;
		this.column = column;
		this.newvalue = newvalue;
		this.neighbors = neighbors ;
	}
	
	public void caculEffcAndTabu(){
		int effect1,effect2;
		double effect;
		int[] checkedArray = new int[2*Main.paraNum];
		for(int k=0; k<Main.paraNum; k++){
			checkedArray[2*k] = k;
			if(k!=column)
				checkedArray[2*k+1] = Main.coverageArray[row][k];
			else
				checkedArray[2*k+1] = newvalue;
		}
		if(Main.numMFTParam[column]>0)
		    effect1 = Main.checker.getMFTNumbyParam(checkedArray, column, newvalue);
		else
			effect1 = 0;
		effect2 = Main.evalMove.evaluate_fast(row,column,newvalue);

		boolean istabu = Main.tabuList.isTabu(row,column,newvalue);
		
		neighbors[row] = new Element(row,column,newvalue,effect1,effect2,istabu);
		
	}
}
