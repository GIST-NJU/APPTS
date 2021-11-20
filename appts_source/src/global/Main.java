package global;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import struct.CombinationPosition;
import struct.Element;
import tabulist.TabuList;
import combination.CoveredCombination;
import combination.ParaCombination;
import combination.ParaValueCombination;
import constraint.ForbiddenTuples;
import evalue.EvalMove;
import ftchecker.IFTChecker;
import ftchecker.MFTChecker;
import ftchecker.MFTinCA;
import ftchecker.Tuple;

public class Main {
	
	public static int L =200;	
	public static int [][] coverageArray;	
	public static int t_way;
	public static int paraNum;
	public static int[] pValue;	
	public static int rowNum;
	
	public static CoveredCombination  coveredCombination;
	public static ParaCombination paraCombination;
	public static ParaValueCombination paraValueCombination;
	
	//the checher of forbidden tuples
	public static IFTChecker checker;
	
	//the number of MFT of parameter
	public static int[] numMFTParam;
	
	public static int [] descIndexMFT;
	
	public static MFTinCA mft_ca;
	
	public static TabuList  tabuList=null;			
	
	public static EvalMove evalMove=null;
	
	public static double lamd;
	public static double beta1,beta2;
	public static double MINLAMD=0.01;			
	public static double MAXLAMD=50.0;			
	public static double INILAMD=4.0;			
	public static double BETA1=1.5;				
	public static double BETA2=1.2;				

	public static void main(String[] args) throws IOException{
			int i,j,k;
			int maxRowNum;
			int minRowNum;
			int iter;							
			int uncoveredCount;					
			int mft_ca_count;					
			double fitness;						
			double minFitness;								
			int[][] bestArray;					
			
			long  finish;						
			
			if(args.length!=3){
				System.out.println("Please provide modelfile constraintfile and cutofftime");
				System.exit(0);
			}
			
			String modelFileName=args[0];
			String constraintsFileName=args[1];
			int cutofftime =Integer.parseInt(args[2]);
						
			checker = new MFTChecker();
			
			//get the domain size of parameters and the coverage strength
			pValue = ForbiddenTuples.getDomains(modelFileName);
			
			
			//get the map of index to parameter and value
		    ArrayList<Integer> indexToParamMap = ForbiddenTuples.getIndexToParamMap(pValue);
		    ArrayList<Integer> indexToValueMap = ForbiddenTuples.getIndexToValueMap(pValue);
		    
		    //get forbiddenTuples
		    ArrayList<Tuple> forbiddenTuples = ForbiddenTuples.getForbiddenTuples(constraintsFileName, indexToParamMap, indexToValueMap);
		    
		    //initialize the checker
		    checker.init(pValue);
		    
		    //add forbidden tuples
		    checker.addForbiddenTuples(forbiddenTuples);
		    
			// get all minimum forbidden tuples
		    List<Tuple> ts = checker.getMinimumForbiddenTuples();
		    
		    // get number of minimum forbidden tuples of parameters
		    numMFTParam = checker.getNumofMFTbyParam();
		    //maximum of minimum forbidden tuples of parameters
		    int max_numMFTParam = numMFTParam[0];
		    for(i=1; i<paraNum; i++)
		    	if(max_numMFTParam < numMFTParam[i])
		    		max_numMFTParam = numMFTParam[i];
		    
		    
			int rowSize;
			int num=1;
			int denum = 1;
			int para = paraNum;
			int way = t_way;
			for(i=0; i<t_way; i++)
			{
				num *= (para--);
				denum *= (way--);
			}
			rowSize = num/denum;
			
			
			paraCombination = new ParaCombination(rowSize,t_way);
	
			paraValueCombination = new ParaValueCombination(t_way);
	
			coveredCombination = new CoveredCombination(rowSize,  pValue, t_way);
			
			evalMove = new EvalMove(t_way);
			
			tabuList = new TabuList();
	
			System.out.println("Wait......");
			
			double effect;							
			double bestEffect;						
			ArrayList<Element>moveSet;						
			ArrayList<Element> bestMoveSet;					
			ArrayList<ArrayList<Element>>  allBestMove ;	
	
			
			moveSet = new ArrayList<Element>();	
			bestMoveSet = new ArrayList<Element>();	
			allBestMove = new ArrayList<ArrayList<Element>>();
			
//			int rowNum;										
			boolean isSuccess = false;						
			int [] tuple = new int[t_way];					
			int [] value_tuple = new int[t_way];			
			Element changedElement = new Element();   		
			
			long wholeStart = System.currentTimeMillis();				
			
			maxRowNum = initCoverageArrayByIpog(modelFileName,constraintsFileName,forbiddenTuples.size(),t_way);
			bestArray = new int [maxRowNum][paraNum];
			rowNum = maxRowNum;	
			minRowNum = maxRowNum;
			System.out.println("The inital CA with "+rowNum+" test cases");
	//		for( i=0; i<rowNum; i++){
	//			for( j=0; j<paraNum; j++)
	//				System.out.print(coverageArray[i][j]+" ");
	//			System.out.println();
	//		}
			for( i=0; i<rowNum; i++)
				for( j=0; j<paraNum; j++)
					bestArray[i][j]=coverageArray[i][j];
			
			
			coveredCombination.init(rowNum);
			
			while((System.currentTimeMillis()-wholeStart)/1000<cutofftime)						
			{
				
				isSuccess = false;
				
				
				Random rand = new Random();
				int deletRowNum = rand.nextInt(rowNum);
				coveredCombination.delete_row(deletRowNum);//modified on 2020.10.10
				for(j=0; j<paraNum; j++)		
					coverageArray[deletRowNum][j] = coverageArray[rowNum-1][j];
				rowNum--;
				
				System.out.println();
				System.out.println();
				
				uncoveredCount = coveredCombination.getUncoveredCount();	
				mft_ca = new MFTinCA(rowNum,checker,numMFTParam);	
				mft_ca_count = 0; 							
				
				tabuList.clear();
				
				
				if(uncoveredCount==0 && mft_ca_count==0){
					isSuccess = true;
					minRowNum = rowNum;
					for(i=0; i<rowNum; i++)
						for(j=0; j<paraNum; j++)
							bestArray[i][j] = coverageArray[i][j];
				}
	
				lamd = INILAMD;
				beta1 = BETA1;
				beta2 = BETA2;
				fitness = uncoveredCount+lamd*mft_ca_count;
				minFitness = fitness;
				boolean islastfeasible=false;      
				boolean isfeasible;   				
				int nf=0;							
				boolean isfeasiblebegin=false;		
				int nnotimpr=0;						
				iter = 0;
				
				Random rand1 = new Random();
				Random rand3 = new Random();
				Random rand4 = new Random();
				while(!isSuccess && (System.currentTimeMillis()-wholeStart)/1000<cutofftime)  //
				{
					moveSet.clear();	
					bestMoveSet.clear();
					allBestMove.clear();
					
					tabuList.getTabuList();
					
					mft_ca_count = mft_ca.getCount(); 
					uncoveredCount = coveredCombination.getUncoveredCount();
					
					if(mft_ca_count>0){
						int rp = rand1.nextInt(mft_ca_count);
						
						int[] row_mft=new int[mft_ca_count];			
						ArrayList<Tuple>tuple_mfts = mft_ca.getMFTs(row_mft);
						
						bestEffect = rowSize+lamd*max_numMFTParam;				
						for(int p=0; p<mft_ca_count;p++){
							if(p!=rp)					
								continue;
							allBestMove.add(new ArrayList<Element>());
							
							Tuple tuple_mft = tuple_mfts.get(p);
							int size = tuple_mft.size;
							int[] tuple_param = new int[size];
							int[] tuple_value = new int[size];
							for(i=0; i<size; i++){
								tuple_param[i] = tuple_mft.getParam(i);
								tuple_value[i] = tuple_mft.getValue(i);
							}
							
							Element[][] neighbors = new Element[size][];
							for(int z=0; z<size; z++){
								neighbors[z] = new Element[pValue[tuple_param[z]]];
								for(int v=0; v<pValue[tuple_param[z]]; v++)
									neighbors[z][v] = null;
							}
							ArrayList<Neighbor1Parallel> neighborlist = new ArrayList<Neighbor1Parallel>();
							for(int z=0; z<size; z++){
								for(int v=0; v<pValue[tuple_param[z]]; v++){
									if(v == tuple_value[z])
										continue;
									neighborlist.add(new Neighbor1Parallel(row_mft[p],tuple_param[z],v,neighbors,z));
								}
								
							}
							neighborlist.parallelStream().forEach(Neighbor1Parallel::caculEffcAndTabu);
							
							double localBestEffect = rowSize+lamd*max_numMFTParam;	
							for(i=0; i<size; i++){
								
								for(j=0; j<pValue[tuple_param[i]]; j++){
									Element elem = neighbors[i][j];
									if(elem == null)
										continue;
									
									int effect1 = elem.effectMFT;			
									int effect2 = elem.effectUncovered;			
									effect = effect1*lamd+effect2;
									
									Element move;
									if(effect1+mft_ca_count==0)
										 move = new Element(elem.row,elem.column,elem.value,effect,true);
									else
										 move = new Element(elem.row,elem.column,elem.value,effect,false);
									moveSet.add(move);
									
									if( (mft_ca_count+effect1>0 || uncoveredCount+effect2>0) && elem.istabu)
										continue;
									
									if(localBestEffect >= effect)
									{
										if(localBestEffect >effect)
										{
											allBestMove.get(allBestMove.size()-1).clear();
											localBestEffect = effect;
										}
										isfeasible = move.isfeasible;
										Element bestMove = new Element(elem.row,elem.column,elem.value,effect,isfeasible);
										allBestMove.get(allBestMove.size()-1).add(bestMove);
									}
									if(bestEffect >= effect)
									{
										if(bestEffect >effect)
										{
											bestMoveSet.clear();
											bestEffect = effect;
										}
										isfeasible = moveSet.get(moveSet.size()-1).isfeasible;
										Element bestMove = new Element(elem.row,elem.column,elem.value,effect,isfeasible);
										bestMoveSet.add(bestMove);
									}
								}
							}
							if(allBestMove.get(allBestMove.size()-1).size()==0)
								allBestMove.remove(allBestMove.size()-1);
						}
					}
					else{
						
						int effect1;
						int effect2;
						
						int combinationRow;
						int combinationColumn;
						ArrayList<CombinationPosition> uncoveredcombinations  = coveredCombination.getUncoveredList();
								
						bestEffect = rowSize+lamd*max_numMFTParam;				
						int rp = rand1.nextInt(uncoveredcombinations.size());
						for(int p=0; p<uncoveredcombinations.size(); p++ ){
							if(rp!=p)
								continue;
							
							allBestMove.add(new ArrayList<Element>());
							
							CombinationPosition cp = uncoveredcombinations.get(p);
							combinationColumn = cp.combinationColumn;
							combinationRow = cp.combinationRow;
							paraCombination.gett_tuple(combinationRow,tuple);
							paraValueCombination.gett_tuple(tuple,combinationColumn,value_tuple);
							
							Element[] neighbors = new Element[rowNum];
							for(int z=0; z<rowNum; z++)
								neighbors[z] = null;
							ArrayList<Neighbor2Parallel> neighborlist = new ArrayList<Neighbor2Parallel>();
							for(int z=0; z<rowNum; z++){
								int difCount=0;
								for(int m=0; m<t_way; m++)
								{	if(coverageArray[z][tuple[m]]!=value_tuple[m])
									{
										if(difCount==0)
										{
											changedElement.row=z;
											changedElement.column=tuple[m];
											changedElement.value = value_tuple[m]; 
										}
										difCount++;
									}
									if(difCount >= 2)				
										break;
								}
								if(difCount >= 2)				
										continue;
								neighborlist.add(new Neighbor2Parallel(z,changedElement.column,changedElement.value,neighbors));
							}
							neighborlist.parallelStream().forEach(Neighbor2Parallel::caculEffcAndTabu);
							
							double localBestEffect = rowSize+lamd*max_numMFTParam;	
							for(int z=0; z<rowNum; z++)
							{
								Element elem = neighbors[z];
								if(elem==null)
									continue;
								effect1 = elem.effectMFT;
								effect2 = elem.effectUncovered;
								
								effect = effect1*lamd+effect2;
								
								Element move;
								if(effect1+mft_ca_count==0)
									move = new Element(elem.row,elem.column,elem.value,effect,true);
								else
									move = new Element(elem.row,elem.column,elem.value,effect,false);
								moveSet.add(move);
								if( (mft_ca_count+effect1>0 || uncoveredCount+effect2>0) && elem.istabu)
									continue;
								
								if(localBestEffect >= effect){
									if(localBestEffect >effect){
										allBestMove.get(allBestMove.size()-1).clear();
										localBestEffect = effect;
									}
									isfeasible =move.isfeasible;
									Element bestMove= new Element(elem.row,elem.column,elem.value,effect,isfeasible);
									allBestMove.get(allBestMove.size()-1).add(bestMove);
									
								}	
								if(bestEffect >= effect){
									if(bestEffect >effect){
										bestMoveSet.clear();
										bestEffect = effect;
									}
									isfeasible =move.isfeasible;
									Element bestMove= new Element(elem.row,elem.column,elem.value,effect,isfeasible);
									bestMoveSet.add(bestMove);
								}		
							}
							if(allBestMove.get(allBestMove.size()-1).size()==0)
								allBestMove.remove(allBestMove.size()-1);
						}
							
						if(moveSet.size()==0 )
						{					
							iter++;
							continue;
						}
					}
					
					int movep ;
					Element moveElement;
					if(bestEffect<=0 && bestMoveSet.size()>0 )		
					{
						movep = rand3.nextInt(bestMoveSet.size()); 
						moveElement=bestMoveSet.get(movep);
						
					}
					else if(allBestMove.size()>0){					
						int index = rand3.nextInt(allBestMove.size());
						movep =rand4.nextInt((allBestMove.get(index).size()));
						moveElement=allBestMove.get(index).get(movep);
						
					}
					else{				
						 movep= rand4.nextInt(moveSet.size());
						 moveElement=moveSet.get(movep);
						
					}
					if(iter==0){						
						nf=1;
						islastfeasible = moveElement.isfeasible;
						
					}
					else{
						if(moveElement.isfeasible){
							if(islastfeasible){
								nf++;
								if(nf>=10*rowNum){
									lamd = lamd/beta2;
									if(lamd<MINLAMD)
										lamd = MINLAMD;
									nf=0;
								}
							}
							else{
								nf=1;
								islastfeasible = true;
							}
						}
						else{
							if(!islastfeasible){
								nf++;
								if(nf>=10*rowNum){
									lamd = lamd*beta1;
									if(lamd>MAXLAMD)
										lamd = MAXLAMD;
									nf=0;
								}
							}
							else{
								nf=1;
								islastfeasible = false;
							}
						}
						
					}
					
					tabuList.updateUndoList(moveElement.row,moveElement.column,coverageArray[moveElement.row][moveElement.column]);
					
					
					if(numMFTParam[moveElement.column]>0){
						if(mft_ca_count>0 ){
							int []rowTuple_old = new int[paraNum*2];
							int []rowTuple_new = new int[paraNum*2];
							for(j=0; j<paraNum; j++){
								rowTuple_old[2*j] = j;
								rowTuple_new[2*j] = j;
								rowTuple_old[2*j+1] = coverageArray[moveElement.row][j];
								rowTuple_new[2*j+1] = coverageArray[moveElement.row][j];
							}
							rowTuple_new[2*moveElement.column+1] = moveElement.value;
							mft_ca.deletMFT(rowTuple_old, moveElement.column, moveElement.row);
							mft_ca.addMFT(rowTuple_new,moveElement.column, moveElement.row);
						}
						else{
							int []rowTuple_new = new int[paraNum*2];
							for(j=0; j<paraNum; j++){
								rowTuple_new[2*j] = j;
								rowTuple_new[2*j+1] = coverageArray[moveElement.row][j];
							}
							rowTuple_new[2*moveElement.column+1] = moveElement.value;
							mft_ca.addMFT(rowTuple_new, moveElement.column, moveElement.row);
						}
						
					}
					
					evalMove.move(moveElement.row, moveElement.column, moveElement.value);
	
					
					mft_ca_count = mft_ca.getCount(); 
					uncoveredCount = coveredCombination.getUncoveredCount();
					if(	mft_ca_count==0 && uncoveredCount==0)
					{
						minRowNum = rowNum;
						for(i=0; i<rowNum; i++)
							for(j=0; j<paraNum; j++)
								bestArray[i][j] = coverageArray[i][j];
	
						isSuccess = true;
					}
					else {
						if(!isfeasiblebegin){
							minFitness = uncoveredCount;
							isfeasiblebegin = true;
							nnotimpr = 0;
						}
						else{
							if(minFitness>uncoveredCount){
								minFitness = uncoveredCount;
								nnotimpr = 0;
	//							for(i=0; i<rowNum; i++)
	//								for(j=0; j<paraNum; j++)
	//									bestArray[i][j] = coverageArray[i][j];
							}
							else
								nnotimpr++;
						}
						
					}
					
					if(nnotimpr>5000 && uncoveredCount>0){
						
						nnotimpr = 0;
						
						tabuList.clear();
						
						Random rand6 = new Random();
						int changeRowNo = rand6.nextInt(rowNum);
						int[] newrow = generateOneRowCover( rand6.nextInt(uncoveredCount));
						
						int[] oldrow = new int[paraNum];
						for(j=0; j<paraNum; j++)
							 oldrow[j]=coverageArray[changeRowNo][j];
						
						mft_ca.updateMFT_row(changeRowNo, newrow);
						
						coveredCombination.update_row(oldrow, newrow,changeRowNo);
						
						for(j=0; j<paraNum; j++)
							coverageArray[changeRowNo][j] = newrow[j];
						
						mft_ca_count = mft_ca.getCount(); 
						uncoveredCount = coveredCombination.getUncoveredCount();
						if(	mft_ca_count==0 && uncoveredCount==0)
						{	
							minRowNum = rowNum;
							for(i=0; i<rowNum; i++)
								for(j=0; j<paraNum; j++)
									bestArray[i][j] = coverageArray[i][j];
	
							isSuccess = true;
						}
			
					}
										
					iter++;
				}
				
				finish = System.currentTimeMillis();				
				
				if(isSuccess)
				{
					double wholeDuration = (double)(finish-wholeStart)/1000;
					
					System.out.println("Success to construct a CA with "+rowNum+" test cases in "+wholeDuration+" seconds");
	//				System.out.println("Test Set:");
	//				
	//				for( i=0; i<rowNum; i++){
	//					for( j=0; j<paraNum; j++)
	//						System.out.print(coverageArray[i][j]+" ");
	//					System.out.println();
	//				}
					
				}
				else
				{
	//				System.out.println("Fail to construct a CA with "+rowNum+" test cases");
	//				System.out.println("End");
					break;									
				}
				
			}
			
			System.out.println("Test set with "+minRowNum+" test cases:");
			
			for( i=0; i<minRowNum; i++){
				for( j=0; j<paraNum; j++)
					System.out.print(bestArray[i][j]+" ");
				System.out.println();
			}
			
			System.out.println("\nEnd");
	
	}
	
	public static int[] generateOneRowCover(int p){
		int i,j,param;
		int [] oneRow = new int[paraNum];
		int [] tuple = new int[t_way];
		int [] value_tuple = new int[t_way];
		int combinationRow;
		int combinationColumn;
		CombinationPosition uncoveredpaire;
		uncoveredpaire = coveredCombination.getUncoveredCombByList(p);
		combinationRow = uncoveredpaire.combinationRow;
		combinationColumn = uncoveredpaire.combinationColumn;
		paraCombination.gett_tuple(combinationRow,tuple);
		paraValueCombination.gett_tuple(tuple,combinationColumn,value_tuple);
		
		Random rand = new Random();
		for(j=0; j<t_way; j++){
			oneRow[tuple[j]] = value_tuple[j];
		}
		for(param=0,j=t_way; param<paraNum; param++){
			for(i=0; i<t_way; i++)
				if(param==tuple[i])
					break;
			if(i<t_way)
				continue;
			
			int value = rand.nextInt(pValue[param]);		
			oneRow[param] = value;							
		}
		return oneRow;
	}
	
	public static int initCoverageArrayByIpog(String modelFileName, String constraintsFileName, int ftcount,int t_way) throws IOException
	{
        String t_way_string = String.valueOf(t_way);
		String[] command = {"java", "-jar", "ipog-ft.jar", modelFileName,constraintsFileName,t_way_string};
		
		Process process = Runtime.getRuntime().exec(command);		//Ö´ÐÐÍâ²¿ÃüÁî
		
//		InputStream istrerror = process.getErrorStream();
//		BufferedReader brerror = new BufferedReader(new InputStreamReader(istrerror));
//		String strerror;
//		boolean isError=false;
//		System.out.println("error");
//		while((strerror=brerror.readLine())!=null){
//			System.out.println("error");
//			System.out.println(strerror);
//			isError=true;
//		}
//		if(isError)
//			System.exit(0);
//		System.out.println("no error");
		
		InputStream istr = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(istr));
		String str;
		int count=0;
		String[] paraSequence_s=null;
		int [] paraSequence = new int[paraNum];
		ArrayList<String> initarray_s=new ArrayList<String>();
		while((str=br.readLine())!=null){
			count++;
			if(ftcount>0){
				if(count==7)	
					paraSequence_s = str.split(" ");
				if(count>=9)		
				{
					initarray_s.add(str);
				}
			}
			else{
				if(count==6)	
					paraSequence_s = str.split(" ");
				if(count>=8)		
				{
					initarray_s.add(str);
				}
			}
		}
		
		//
		if(paraNum!=paraSequence_s.length){
			System.out.println("initialization fail!");
			System.exit(0);
		}
		for(int j=0; j<paraNum; j++){
			int value = Integer.parseInt(paraSequence_s[j]);
			paraSequence[j] = value;
		}	
		
		int maxRowNum = initarray_s.size()-1;
		coverageArray = new int [maxRowNum][paraNum];
		for(int i=0; i<maxRowNum; i++){
			str =initarray_s.get(i);
			String[] newStr = str.split(" ");	
			if(newStr.length!=paraNum){
				System.out.println("initialization fail!");
				System.exit(0);
			}
			for(int j=0; j<paraNum; j++){
					int value = Integer.parseInt(newStr[j]);
					coverageArray[i][paraSequence[j]] = value;
			}	
		}
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		br.close();
	
		return maxRowNum;
	}
	
	
}
