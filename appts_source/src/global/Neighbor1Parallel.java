package global;

import java.io.Serializable;

import struct.Element;

//并行计算邻域1（即含约束）的各个移动的代价以及是否被禁忌
public class Neighbor1Parallel implements Serializable{
	private static final long serialVersionUID = 1L;
	private int row;				
	private int column;			
	private int newvalue;
	private Element[][] neighbors;
	private int no;		//该参数是禁止元组中的第几个参数，编号从0开始
	
	public Neighbor1Parallel(int row, int column, int newvalue,  Element[][] neighbors,int no){
		this.row = row;
		this.column = column;
		this.newvalue = newvalue;
		this.neighbors = neighbors ;
		this.no = no;
	}
	
	public void caculEffcAndTabu(){
		int effect1,effect2;
		double effect;
		int[] test = new int[Main.paraNum*2];
		for(int j=0; j<Main.paraNum; j++ )		
		{
			test[2*j] = j;
			test[2*j+1] = Main.coverageArray[row][j];
		}
		if(Main.numMFTParam[column]>0)
			effect1 = Main.checker.evalMove_ParaMFS(test, column, Main.coverageArray[row][column], newvalue);
			
		else
			effect1 = 0;

		effect2 = Main.evalMove.evaluate_fast(row,column,newvalue);	//2019.10.31
		boolean istabu = Main.tabuList.isTabu(row,column,newvalue);
		neighbors[no][newvalue] = new Element(row,column,newvalue,effect1,effect2,istabu);
		
	}

}
