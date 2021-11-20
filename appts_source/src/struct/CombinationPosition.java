package struct;

public class CombinationPosition  implements Comparable<CombinationPosition>{
	public int combinationRow;				
	public int combinationColumn;			
	
	public CombinationPosition(){
	}
	public CombinationPosition(int combinationRow, int combinationColumn){
		this.combinationRow = combinationRow;
		this.combinationColumn = combinationColumn;
	}
	
	public int getCombinationRow() {
		return combinationRow;
	}
	public void setCombinationRow(int combinationRow) {
		this.combinationRow = combinationRow;
	}
	public int getCombinationColumn() {
		return combinationColumn;
	}
	public void setCombinationColumn(int combinationColumn) {
		this.combinationColumn = combinationColumn;
	}
	@Override
	public boolean equals(Object object){
		boolean result=false;
		if(object==this)
			return true;
		else{
				if(object!=null && object instanceof CombinationPosition){
					CombinationPosition position = (CombinationPosition) object;
					return this.combinationRow==position.combinationRow && 
							this.combinationColumn==position.combinationColumn;
			}
			return result;
		}
		
	}
	
	@Override
	public int compareTo(CombinationPosition o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
