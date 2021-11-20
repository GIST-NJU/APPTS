package struct;

//a integer and a array
public class IntArr {
	int value;
	int[] array;
	
	//constructor
	public IntArr(){
		
	}
	
	//constructor
	public IntArr(int value, int[] array){
		this.value = value;
		this.array = array;
	}

	//get the member of value
	public int getValue() {
		return value;
	}
	
	//get the member of array
	public int[] getArray() {
		return array;
	}

}
