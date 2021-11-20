package struct;

//覆盖表中的一个元素
public class Element {
	
	public int row;				
	public int column;			
	public int value;			
	public int column2;			
	public int value2;			
	public boolean isTwo;		
	public double effect;			
	public boolean isfeasible;	
	
	public int effectMFT;				//2020.9.29
	public int effectUncovered;			//2020.9.29
	public boolean istabu;				//2020.9.29
	
	public Element(){
	}
	public Element(int row, int column, int value){
		this.row = row;
		this.column = column;
		this.value = value;
		this.isTwo = false;
	}
	public Element(int row, int column, int value, double effect, boolean isfeasible){
		this.row = row;
		this.column = column;
		this.value = value;
		this.effect = effect;
		this.isfeasible = isfeasible;
		this.isTwo = false;
	}
	//2020.9.29
	public Element(int row, int column, int value, int effectMFT, int effectUncovered, boolean istabu){
		this.row = row;
		this.column = column;
		this.value = value;
		this.effectMFT = effectMFT;
		this.effectUncovered = effectUncovered;
		this.istabu = istabu;
		this.isTwo = false;
	}
	public Element(int row, int column, int value,int column2, int value2){
		this.row = row;
		this.column = column;
		this.value = value;
		this.column2 = column2;
		this.value2 = value2;
		this.isTwo = true;
	}
	public Element(int row, int column, int value,int column2, int value2,double effect, boolean isfeasible){
		this.row = row;
		this.column = column;
		this.value = value;
		this.column2 = column2;
		this.value2 = value2;
		this.effect = effect;
		this.isfeasible = isfeasible;
		this.isTwo = true;
	}

}
