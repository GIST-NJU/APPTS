package combination;

import global.Main;

public class ParaCombination {
	private	int []array;				
	private	int [][]tuple_array;		
	private	int t_way;			   		
	
	public	ParaCombination(int rowSize, int t_way){
		int i,j,k,l,m,n,u,count;
		this.t_way = t_way;

		int maxSize=1 ;					
		for(i=0; i<t_way; i++)
			maxSize *= Main.paraNum;

		array= new int [maxSize];
		tuple_array = new int[rowSize][t_way];


		u=count=0;
		if(t_way == 2)
		{
			for(i=0; i<Main.paraNum; i++)
				for(j=0; j<Main.paraNum; j++)
				{
					if(i<j)
					{	array[u] = count; 
						tuple_array[count][0]=i;tuple_array[count][1]=j;
						count++;
					}
					else
						array[u] = -1;
					u++;
				}

		}
		else if(t_way == 3)
		{
			for(i=0; i<Main.paraNum; i++)
				for(j=0; j<Main.paraNum; j++)
					for(k=0; k<Main.paraNum; k++)
					{
						if(i<j && j<k)
						{	array[u] = count; 
							tuple_array[count][0]=i;tuple_array[count][1]=j;tuple_array[count][2]=k;
							count++;
						}	
						else
							array[u] = -1;
						u++;
					}

		}
		else if(t_way == 4)
		{
			for(i=0; i<Main.paraNum; i++)
				for(j=0; j<Main.paraNum; j++)
					for(k=0; k<Main.paraNum; k++)
						for(l=0; l<Main.paraNum; l++)
						{
							if(i<j && j<k && k<l)
							{	array[u] = count;
								tuple_array[count][0]=i;tuple_array[count][1]=j;tuple_array[count][2]=k;
								tuple_array[count][3]=l;
								count++;
							}
							else
								array[u] = -1;
							u++;
						}

		}
		else if(t_way == 5)
		{
			for(i=0; i<Main.paraNum; i++)
				for(j=0; j<Main.paraNum; j++)
					for(k=0; k<Main.paraNum; k++)
						for(l=0; l<Main.paraNum; l++)
							for(m=0; m<Main.paraNum; m++)
							{
								if(i<j && j<k && k<l && l<m)
								{	array[u] = count;
									tuple_array[count][0]=i;tuple_array[count][1]=j;tuple_array[count][2]=k;
									tuple_array[count][3]=l;tuple_array[count][4]=m;
									count++;
								}
								else
									array[u] = -1;
								u++;
							}

		}
		else if(t_way == 6)
		{
			for(i=0; i<Main.paraNum; i++)
				for(j=0; j<Main.paraNum; j++)
					for(k=0; k<Main.paraNum; k++)
						for(l=0; l<Main.paraNum; l++)
							for(m=0; m<Main.paraNum; m++)
								for(n=0; n<Main.paraNum; n++)
								{
									if(i<j && j<k && k<l && l<m && m<n)
									{	array[u] = count;
										tuple_array[count][0]=i;tuple_array[count][1]=j;tuple_array[count][2]=k;
										tuple_array[count][3]=l;tuple_array[count][4]=m;tuple_array[count][5]=n;
										count++;
									}
									else
										array[u] = -1;
									u++;
								}

		}
		else
		{
			System.out.println("Coverage strength is wrong!");
			System.exit(1);
		}
	}
	
	public	int getRowNum(int [] tuple){
		int i,count=0;
		for(i=0; i<t_way; i++)
			count = count*Main.paraNum+tuple[i];
		return array[count];
	}
	
	public	void gett_tuple(int row, int []tuple){
		int i;
		for(i=0; i<t_way; i++)
			tuple[i] = tuple_array[row][i];
	}

}
