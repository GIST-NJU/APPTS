package ftchecker;

import java.util.ArrayList;
import java.util.Iterator;

public class MFTinCA {
	ArrayList<ArrayList<Tuple>>  list_mft_ca;		
	int mft_count;			
	int rowNum;				
	int []numMFTParam;		
	IFTChecker checker;		
	
	public MFTinCA(int rowNum, IFTChecker checker,int []numMFTParam){
		mft_count = 0;
		this.checker = checker;
		this.rowNum = rowNum;
		this.numMFTParam=numMFTParam;
		list_mft_ca = new ArrayList<ArrayList<Tuple>>();
		for(int i=0; i<rowNum; i++)
			list_mft_ca.add(new ArrayList<Tuple>());
	}
	
	public void clear(){
		for(int i=0; i<rowNum; i++)
			list_mft_ca.get(i).clear();
		mft_count = 0;	
	}
	
	public int getCount(){
		return mft_count;
	}
	
	public Tuple getMFT(int n, int[] row){
		int i,j,k;
		k=0;
		for(i=0; i<list_mft_ca.size(); i++){
			
			for(j=0; j<list_mft_ca.get(i).size(); j++){
				if(k == n)
					{ row[0]= i; return list_mft_ca.get(i).get(j);}
				k++;
			}
		}
		return null;			
	}
	
	public ArrayList<Tuple> getMFTs(int[] row){
		int i,j,k=0;
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		for(i=0; i<list_mft_ca.size(); i++){
			for(j=0; j<list_mft_ca.get(i).size(); j++){
					row[k++]= i;
					tuples.add(list_mft_ca.get(i).get(j));
			}
		}
		return tuples;			
	}
	
	public void caculateMFT(int [][] CA){
		int colNum = numMFTParam.length;
		int i,j,k;
		int [] rowTuple = new int[colNum*2];
		for(i=0; i<rowNum; i++){
			for(j=0; j<colNum; j++){
				rowTuple[2*j] = j;
				rowTuple[2*j+1] = CA[i][j];
			}
			for(j=0; j<colNum; j++){
				if(numMFTParam[j]==0)
					continue;		
				ArrayList<Tuple> mft_param = checker.getMFTbyParam(rowTuple, j, CA[i][j]);
				int mft_row_size = list_mft_ca.get(i).size();
				if(mft_param != null)
					for(Iterator<Tuple> iter=mft_param.iterator(); iter.hasNext();){
						Tuple t = iter.next();
						for(k=0; k<mft_row_size; k++)
							if(t.isEqual(list_mft_ca.get(i).get(k)))
								break;
						if(k==mft_row_size){
							list_mft_ca.get(i).add(t);
							mft_count++;
						}
					}
			}
		}
	}
	
	public void deletMFT(int [] rowTuple, int param, int row ){
		int k;
		boolean isfound;
		if(numMFTParam[param]==0)
			return;
		ArrayList<Tuple> mft_param = checker.getMFTbyParam(rowTuple, param, rowTuple[2*param+1]);
		if(mft_param != null)
			for(Iterator<Tuple> iter=mft_param.iterator(); iter.hasNext();){
				Tuple t = iter.next();
				isfound=false;
				for(k=0; k<list_mft_ca.get(row).size(); k++)
					if(t.isEqual(list_mft_ca.get(row).get(k))){
					list_mft_ca.get(row).remove(k);
					mft_count--;
					isfound = true;
					break;
				}
			}
	}
	
	public void addMFT(int [] rowTuple, int param, int row ){
		int k;
		boolean isfound;
		if(numMFTParam[param]==0)
			return;
		ArrayList<Tuple> mft_param = checker.getMFTbyParam(rowTuple, param, rowTuple[2*param+1]);
		if(mft_param != null)
			for(Iterator<Tuple> iter=mft_param.iterator(); iter.hasNext();){
				Tuple t = iter.next();		
				list_mft_ca.get(row).add(t);
				mft_count++;
			}
	}
	
	public void addMFT2(int [] rowTuple, int column, int column2,int row ){
		int k;
		int c1,c2;
    	if(column<column2){
			c1=column;
			c2=column2;
		}
		else{
			c1=column2;
			c2=column;
		}
		ArrayList<Tuple> mft_param = checker.getMFTbyParam(rowTuple, c1, rowTuple[2*c1+1]);
		ArrayList<Tuple> mft_param2 = checker.getMFTbyParam(rowTuple, c2, rowTuple[2*c2+1]);
		int mft_size = mft_param.size();
		for(Iterator<Tuple> iter=mft_param2.iterator(); iter.hasNext();){
			Tuple t = iter.next();
			for(k=0; k<mft_size; k++)
				if(t.isEqual(mft_param.get(k)))
					break;
			if(k==mft_size)
				mft_param.add(t);
		} 	
				
		for(Iterator<Tuple> iter=mft_param.iterator(); iter.hasNext();){
			Tuple t = iter.next();		
			list_mft_ca.get(row).add(t);
			mft_count++;
		}
	}
	
	public void updateMFT_row(int rowNo,int[] newRow){
		int colNum = numMFTParam.length;
		int i,j,k;
		int [] rowTuple = new int[colNum*2];
		
		mft_count -=list_mft_ca.get(rowNo).size();
		list_mft_ca.get(rowNo).clear();
	
		for(j=0; j<colNum; j++){
			rowTuple[2*j] = j;
			rowTuple[2*j+1] = newRow[j];
		}
		for(j=0; j<colNum; j++){
			if(numMFTParam[j]==0)
				continue;		
			ArrayList<Tuple> mft_param = checker.getMFTbyParam(rowTuple, j, newRow[j]);
			int mft_row_size = list_mft_ca.get(rowNo).size();
			if(mft_param != null)
				for(Iterator<Tuple> iter=mft_param.iterator(); iter.hasNext();){
					Tuple t = iter.next();
					for(k=0; k<mft_row_size; k++)
						if(t.isEqual(list_mft_ca.get(rowNo).get(k)))
							break;
					if(k==mft_row_size){
						list_mft_ca.get(rowNo).add(t);
						mft_count++;
					}
				}
		}
	}
}
