package struct;

public class Entry {
	public int combinationRow;		//未被覆盖组合的参数组合编号
	public int combinationColumn;	//未被覆盖组合的参数取值组合编号
	public int column_index;		//某个参数在该参数组合中的索引	
	public Entry(int combinationRow, int combinationColumn, int column_index){
		this.combinationRow = combinationRow;
		this.combinationColumn = combinationColumn;
		this.column_index = column_index;
	}
	

}
