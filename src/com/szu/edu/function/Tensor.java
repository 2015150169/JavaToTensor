package com.szu.edu.function;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tensor implements TensorFunction{
	
	private ArrayList dimensionList;
	//保留tensor数据的多维矩阵
	private ArrayList tensorList;
	
	//保留tensor原始数据的一维数组
	private ArrayList tensorDataList;
	
	private String tensorShape;
	
	//保留tensor原始数据的个数
	private int tensorDataSize;
	
	private int pos;
	
	public Tensor() {
		dimensionList=new ArrayList();
		tensorList=new ArrayList();
	}

	public ArrayList getDimensionList() {
		return dimensionList;
	}

	public void setDimensionList(ArrayList dimensionList) {
		this.dimensionList = dimensionList;
	}

	public ArrayList getTensorList() {
		return tensorList;
	}

	public void setTensorList(ArrayList tensorList) {
		this.tensorList = tensorList;
	}
	
	public ArrayList getTensorDataList() {
		return tensorDataList;
	}

	
	public void setTensorDataList(ArrayList tensorDataList) {
		this.tensorDataList = tensorDataList;
	}

	

	public String getTensorShape() {
		return tensorShape;
	}

	

	public void setTensorShape(String tensorShape) {
		this.tensorShape = tensorShape;
	}

	
	
	public int getTensorDataSize() {
		return tensorDataSize;
	}

	public void setTensorDataSize(int tensorDataSize) {
		this.tensorDataSize = tensorDataSize;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public Tensor tensorRand(ArrayList dimension) {
		this.dimensionList=dimension;
		ArrayList dimensionValue=new ArrayList();
		String funcString="rand";
		if(dimension.size()==0)
		{
			System.out.println("输入出错");
			return null;
		}
		else
		{
			if(dimension.size()!=1)
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				for(int i=0;i<length;i++)
				{
					ArrayList element=initElement(1,dimension,funcString,dimensionValue);
					this.tensorList.add(element);
				}
			}else
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				Random random=new Random();
				int element=0;
				for(int i=0;i<length;i++)
				{
					element=random.nextInt(100);
					this.tensorList.add(element);
				}
			}			
		}	
		return this;
	}

	@Override
	public Tensor tensorOne(ArrayList dimension) {
		this.dimensionList=dimension;
		ArrayList dimensionValue=new ArrayList();
		String funcString="one";
		if(dimension.size()==0)
		{
			System.out.println("输入出错");
			return null;
		}
		else
		{
			if(dimension.size()!=1)
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				for(int i=0;i<length;i++)
				{
					ArrayList element=initElement(1,dimension,funcString,dimensionValue);
					this.tensorList.add(element);
				}
			}else
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				Random random=new Random();
				int element=0;
				for(int i=0;i<length;i++)
				{
					element=random.nextInt(100);
					this.tensorList.add(element);
				}
			}
		}	
		return this;
	}

	@Override
	public Tensor tensorZero(ArrayList dimension) {
		this.dimensionList=dimension;
		ArrayList dimensionValue=new ArrayList();
		String funcString="zero";
		if(dimension.size()==0)
		{
			System.out.println("输入出错");
			return null;
		}
		else
		{
			if(dimension.size()!=1)
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				for(int i=0;i<length;i++)
				{
					ArrayList element=initElement(1,dimension,funcString,dimensionValue);
					this.tensorList.add(element);
				}
			}else
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				Random random=new Random();
				int element=0;
				for(int i=0;i<length;i++)
				{
					element=random.nextInt(100);
					this.tensorList.add(element);
				}
			}
		}	
		return this;
	}

	@Override
	public Tensor tensorSpecify(String specifyDimension) {
		ArrayList dimensionValue=new ArrayList();
		this.dimensionList=initSpecifyDimensionList(specifyDimension, dimensionValue);
		if(dimensionList==null)
		{
			return null;
		}
		int size=tensorSize(this.dimensionList);
		if(size!=dimensionValue.size())
		{
			System.out.println("输入的指定结构不合法");
			return null;
		}else
		{
			String funcString="specify";
			if(dimensionList.size()==0)
			{
				System.out.println("输入出错");
			}
			else
			{
				if(dimensionList.size()!=1)
				{
					//获取最高维度的维度值
					int length=(int) dimensionList.get(0);
					for(int i=0;i<length;i++)
					{
						ArrayList element=initElement(1,dimensionList,funcString,dimensionValue);
						this.tensorList.add(element);
					}
				}else
				{
					//获取最高维度的维度值
					int length=(int) dimensionList.get(0);
					Random random=new Random();
					int element=0;
					for(int i=0;i<length;i++)
					{
						element=(int) dimensionValue.get(i);
						this.tensorList.add(element);
					}
				}
			}	
			return this;
		}
		
	}
	
	@Override
	public Tensor tensorReShape(Tensor tensor, ArrayList dimension) {
		int tensorSize = Tensor.tensorSize(tensor);
		int dimensionSize = Tensor.tensorSize(dimension);
		if(tensorSize!=dimensionSize)
		{
			System.out.println("error  不能转换");
			return null;
		}else
		{
			Tensor reShapeTensor=new Tensor();
			reShapeTensor.setDimensionList(dimension);		
			String funcString="reshape";
			tensor.tensorChangeToTensorDataList();
			ArrayList tensorDatas=tensor.getTensorDataList();
			reShapeTensor.setTensorDataList(tensorDatas);
			reShapeTensor.setTensorDataSize(tensorSize);
			if(dimension.size()!=1)
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				for(int i=0;i<length;i++)
				{
					ArrayList element=initElement(1,dimension,funcString,tensorDatas);
					reShapeTensor.tensorList.add(element);
				}
			}else
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				Random random=new Random();
				int element=0;
				for(int i=0;i<length;i++)
				{
					element=(int) tensorDatas.get(i);
					reShapeTensor.tensorList.add(element);
				}
			}
			return reShapeTensor;
		}
		
	}
	
	@Override
	public String tensorPrintf() {
		ArrayList tensorList=this.getTensorList();
		if(tensorList==null)
		{
			System.out.println("该Tensor数据还没有初始化");
		}else
		{
			//System.out.println(tensorList.toString());
			//System.out.println(this.dimensionList.toString());
		}
		return tensorList.toString();
	}
	
	@Override
	public Tensor tensorAdd(Tensor tensorLeft, Tensor tensorRight) {
		ArrayList dimensionLeft = tensorLeft.getDimensionList();
		ArrayList dimensionRight = tensorRight.getDimensionList();
		int leftDimensionSize = dimensionLeft.size();
		int rigthDimensionSize = dimensionRight.size();
		if(leftDimensionSize==rigthDimensionSize)
		{
			//相同维度判断可不可以相加
			boolean expandToAdd=judgeToSameDimension(dimensionLeft, dimensionRight);
			if(expandToAdd==true)
			{
				expandToSameDimension(tensorLeft, tensorRight);
				tensorLeft.tensorChangeToTensorDataList();
				tensorRight.tensorChangeToTensorDataList();
				ArrayList temp=new ArrayList();
				ArrayList tensorLeftDateList=tensorLeft.getTensorDataList();
				ArrayList tensorRightDateList=tensorRight.getTensorDataList();
				int length=tensorLeft.getTensorDataList().size();
				for(int i=0;i<length;i++)
				{
					int element=(int)tensorLeftDateList.get(i)+(int)tensorRightDateList.get(i);
					temp.add(i, element);
				}
				tensorLeft.tensorList.clear();
				tensorLeft.setTensorDataList(temp);
				tensorLeft.setTensorDataSize(temp.size());
				tensorLeft.AddTranToTensor(tensorLeft);
			}else
			{
				return null;
			}
			
		}else
		{
			//不同维度判断可不可以相加
			boolean expandToAdd=judgeToDifferentDimension(dimensionLeft, dimensionRight);
			if(expandToAdd==true)
			{
				expandToDifferentDimension(tensorLeft, tensorRight);
				tensorLeft.tensorChangeToTensorDataList();
				tensorRight.tensorChangeToTensorDataList();
				ArrayList temp=new ArrayList();
				ArrayList tensorLeftDateList=tensorLeft.getTensorDataList();
				ArrayList tensorRightDateList=tensorRight.getTensorDataList();
				int length=tensorLeft.getTensorDataList().size();
				for(int i=0;i<length;i++)
				{
					int element=(int)tensorLeftDateList.get(i)+(int)tensorRightDateList.get(i);
					temp.add(i, element);
				}
				tensorLeft.tensorList.clear();
				tensorLeft.setTensorDataList(temp);
				tensorLeft.setTensorDataSize(temp.size());
				tensorLeft.AddTranToTensor(tensorLeft);
				
			}else
			{
				return null;
			}
		}
		return tensorLeft;
	}
	
	//点乘
	@Override
	public Tensor tensorPointMul(Tensor tensorLeft, Tensor tensorRight) {
		ArrayList dimensionLeft = tensorLeft.getDimensionList();
		ArrayList dimensionRight = tensorRight.getDimensionList();
		int leftDimensionSize = dimensionLeft.size();
		int rigthDimensionSize = dimensionRight.size();
		if(leftDimensionSize==rigthDimensionSize)
		{
			//相同维度判断可不可以相加
			boolean expandToAdd=judgeToSameDimension(dimensionLeft, dimensionRight);
			if(expandToAdd==true)
			{
				expandToSameDimension(tensorLeft, tensorRight);
				tensorLeft.tensorChangeToTensorDataList();
				tensorRight.tensorChangeToTensorDataList();
				ArrayList temp=new ArrayList();
				ArrayList tensorLeftDateList=tensorLeft.getTensorDataList();
				ArrayList tensorRightDateList=tensorRight.getTensorDataList();
				int length=tensorLeft.getTensorDataList().size();
				for(int i=0;i<length;i++)
				{
					int element=(int)tensorLeftDateList.get(i)*(int)tensorRightDateList.get(i);
					temp.add(i, element);
				}
				tensorLeft.tensorList.clear();
				tensorLeft.setTensorDataList(temp);
				tensorLeft.setTensorDataSize(temp.size());
				tensorLeft.AddTranToTensor(tensorLeft);
			}else
			{
				System.out.println("不能相加");
				return null;
			}
			
		}else
		{
			//不同维度判断可不可以相加
			boolean expandToAdd=judgeToDifferentDimension(dimensionLeft, dimensionRight);
			if(expandToAdd==true)
			{
				expandToDifferentDimension(tensorLeft, tensorRight);
				tensorLeft.tensorChangeToTensorDataList();
				tensorRight.tensorChangeToTensorDataList();
				ArrayList temp=new ArrayList();
				ArrayList tensorLeftDateList=tensorLeft.getTensorDataList();
				ArrayList tensorRightDateList=tensorRight.getTensorDataList();
				int length=tensorLeft.getTensorDataList().size();
				for(int i=0;i<length;i++)
				{
					int element=(int)tensorLeftDateList.get(i)*(int)tensorRightDateList.get(i);
					temp.add(i, element);
				}
				tensorLeft.tensorList.clear();
				tensorLeft.setTensorDataList(temp);
				tensorLeft.setTensorDataSize(temp.size());
				tensorLeft.AddTranToTensor(tensorLeft);
				
			}else
			{
				return null;
			}
		}
		return tensorLeft;
	}
	
	@Override
	public Tensor tensorDot(Tensor tensorLeft, Tensor tensorRight) {
		Tensor dotTensor=new Tensor();
		ArrayList dotTensorDataList = new ArrayList();
		ArrayList dimensionLeft=tensorLeft.getDimensionList();
		ArrayList dimensionRight=tensorRight.getDimensionList();
		int dimensionLeftSize = dimensionLeft.size();
		int dimensionRightSize = dimensionRight.size();
		int leftLast=(int) dimensionLeft.get(dimensionLeftSize-1);
		int rightFirst=(int) dimensionRight.get(0);
		if(leftLast==rightFirst)
		{
			int leftSize = tensorSize(dimensionLeft);
			int rightSize = tensorSize(dimensionRight);
			int leftLength = leftSize/leftLast;
			int rightLength = rightSize/rightFirst;
			tensorLeft.tensorChangeToTensorDataList();
			tensorRight.tensorChangeToTensorDataList();
			ArrayList tensorLeftData = tensorLeft.getTensorDataList();
			ArrayList tensorRightDate = tensorRight.getTensorDataList();
			//拷贝dimension防止修改操作数的数据
			ArrayList dimensionCopyToLeft = new ArrayList();
			dimensionCopyToLeft.addAll(dimensionLeft);
			dimensionCopyToLeft.remove(dimensionLeftSize-1);
			ArrayList dimensionCopyToRight = new ArrayList();
			dimensionCopyToRight.addAll(dimensionRight);
			dimensionCopyToRight.remove(0);
			ArrayList dotDeminsion = new ArrayList();
			dotDeminsion.addAll(dimensionCopyToLeft);
			dotDeminsion.addAll(dimensionCopyToRight);
			System.out.println(dotDeminsion.toString());
			dimensionCopyToLeft=null;
			dimensionCopyToRight=null;
			dotTensor.setDimensionList(dotDeminsion);
			for(int i=0;i<leftLength;i++)
			{
				for(int j=0;j<rightLength;j++)
				{
					int value=0;
					for(int k=0;k<leftLast;k++)
					{
						value+=(int)tensorLeftData.get(i*leftLast+k)*(int)tensorRightDate.get(k*rightLength+j);
					}
					dotTensorDataList.add(value);
				}
			}
			dotTensor.setTensorDataList(dotTensorDataList);
			int dotSize=tensorSize(dotDeminsion);
			dotTensor.setTensorDataSize(dotSize);
			AddTranToTensor(dotTensor);
			return dotTensor;
		}else
		{
			System.out.println("输入的两个tensor数据不能够进行叉乘");
			return null;
		}
	}

	@Override
	public Tensor tensorDotWithTree(Tensor tensorLeft, Tensor tensorRight) {
		Tensor dotTensor=new Tensor();
		ArrayList dotTensorDataList = new ArrayList();
		ArrayList dimensionLeft=tensorLeft.getDimensionList();
		ArrayList dimensionRight=tensorRight.getDimensionList();
		int dimensionLeftSize = dimensionLeft.size();
		int dimensionRightSize = dimensionRight.size();
		int leftLast=(int) dimensionLeft.get(dimensionLeftSize-1);
		int rightFirst=(int) dimensionRight.get(0);
		if(leftLast==rightFirst)
		{
			//拷贝dimension防止修改操作数的数据
			ArrayList dimensionCopyToLeft = new ArrayList();
			dimensionCopyToLeft.addAll(dimensionLeft);
			dimensionCopyToLeft.remove(dimensionLeftSize-1);
			ArrayList dimensionCopyToRight = new ArrayList();
			dimensionCopyToRight.addAll(dimensionRight);
			dimensionCopyToRight.remove(0);
			ArrayList dotDeminsion = new ArrayList();
			dotDeminsion.addAll(dimensionCopyToLeft);
			dotDeminsion.addAll(dimensionCopyToRight);
			System.out.println(dotDeminsion.toString());
			dimensionCopyToLeft=null;
			dimensionCopyToRight=null;
			dotTensor.setDimensionList(dotDeminsion);
			int dotSize=tensorSize(dotDeminsion);
			dotTensor.setTensorDataSize(dotSize);
			Object dotObj=dot(tensorLeft.getTensorList(), tensorRight.getTensorList());
			if(dotObj instanceof List)
			{
				ArrayList tensorList = (ArrayList) dotObj;
				dotTensor.setTensorList(tensorList);
				dotTensor.tensorChangeToTensorDataList();
			}
			return dotTensor;
		}
		else
		{
			System.out.println("输入的两个tensor数据不能够进行叉乘");
			return null;
		}
	}

	@Override
	public Tensor tensorSlice(Tensor tensor, ArrayList begin, ArrayList size) {
		ArrayList dimension = tensor.getDimensionList();
		ArrayList tensorList = tensor.getTensorList();
		//对数据进行拷贝，防止修改原始的tensor
		ArrayList dimensionCopy = new ArrayList();
		dimensionCopy.addAll(dimension);
		ArrayList tensorListCopy = new ArrayList();
		tensorListCopy.addAll(tensorList);
		Tensor sliceTensor = new Tensor();
		boolean judgeSlice = judgeToSlice(dimensionCopy, begin, size);
		if(judgeSlice==true)
		{
			ArrayList sliceTensorList=slice(tensorListCopy, begin, size, 0);
			sliceTensor.setDimensionList(size);
			sliceTensor.setTensorList(sliceTensorList);
			sliceTensor.tensorChangeToTensorDataList();
			return sliceTensor;
		}else
		{
			System.out.println("不能正确切片");
			return null;
		}
		
	}
	
	public static String tensorShape(Object tensor)
	{
		StringBuffer tensorShape=new StringBuffer("(");
		if(tensor instanceof Tensor)
		{
			Object tensorList=((Tensor) tensor).getTensorList();
			ArrayList dimensionList=((Tensor) tensor).getDimensionList();
			if(dimensionList!=null)
			{
				if(dimensionList.size()!=1)
				{
					int i=0;
					for(;i<dimensionList.size()-1;i++)
					{
						tensorShape.append(dimensionList.get(i)+",");
					}
					//System.out.println(i);
					tensorShape.append(dimensionList.get(i));
				}
				else
				{
					tensorShape.append(dimensionList.get(0));
				}
			}else
			{
				while(tensorList instanceof List)
				{
					ArrayList tensors=(ArrayList) tensorList;
					int dimensionSize=tensors.size();
					dimensionList.add(dimensionSize);
					tensorList=tensors.get(0);
				}
				if(dimensionList.size()!=1)
				{
					int i=0;
					for(;i<dimensionList.size()-1;i++)
					{
						tensorShape.append(dimensionList.get(i)+",");
					}
					//System.out.println(i);
					tensorShape.append(dimensionList.get(i));
				}
				else
				{
					tensorShape.append(dimensionList.get(0));
				}
			}
			((Tensor) tensor).setTensorShape(tensorShape.toString());
		}else
		{
			
			System.out.println("传入的数据不是tensor类型");
		}
		tensorShape.append(")");
		String tensorShapeStr=tensorShape.toString();
		return tensorShapeStr;
	}
	
	public static int tensorSize(Object tensor)
	{
		int tensorSize=0;
		if(tensor instanceof Tensor)
		{
			ArrayList dimension=((Tensor) tensor).getDimensionList();
			if(dimension!=null&&dimension.size()!=0)
			{
				tensorSize = 1;
				int length = dimension.size();
				for(int i=0;i<length;i++)
				{
					int temp=(int) dimension.get(i);
					tensorSize=tensorSize*temp;
				}
				((Tensor) tensor).setTensorDataSize(tensorSize);
			}else
			{
				System.out.println("传入tensor数据维度参数有误");
			}
		}else if(tensor instanceof List)
		{
			ArrayList tensorList=(ArrayList) tensor;
			if(tensor!=null&&tensorList.size()!=0)
			{
				tensorSize = 1;
				int length = tensorList.size();
				for(int i=0;i<length;i++)
				{
					int temp=(int) tensorList.get(i);
					tensorSize=tensorSize*temp;
				}
			}
		}else
		{
			System.out.println("传入的数据不是tensor类型");	
		}		
		return tensorSize;
	}
	
	public ArrayList initElement(int index,ArrayList dimension,String funcString,ArrayList dimensionValue)
	{
		ArrayList result=new ArrayList();
		if(index != dimension.size()-1)
		{
			int length=(int) dimension.get(index);
			for(int i=0;i<length;i++)
			{
				ArrayList element=initElement(index+1, dimension, funcString,dimensionValue);
				result.add(element);
			}
			return result;
		}else
		{
			Random rand = new Random();
			int length=(int)dimension.get(index);		
			for(int i=0;i<length;i++)
			{
				int element=0;
				if(funcString.equals("rand"))
				{
					element=rand.nextInt(100);
				}else if(funcString.equals("one"))
				{
					element=1;
				}else if(funcString.equals("zero"))
				{
					element=0;
				}else if(funcString.equals("specify"))
				{	
					element=(int) dimensionValue.get(pos);
					pos++;
				}else if(funcString.equals("reshape"))
				{	
					element=(int) dimensionValue.get(pos);
					pos++;
				}					
				result.add(element);
			}
			return result;
		}
	}

	public ArrayList initSpecifyDimensionList(String specifyDimension,ArrayList dimensionValue)
	{
		ArrayList dimension=new ArrayList();
		//用来保存各个维度的维度值
		Map dimensionMap=new LinkedHashMap();
		int dimensionNum=0;
		int size=specifyDimension.length();
		int value=-1;
		for(int i=0;i<size;i++)
		{			
			char flagChar=specifyDimension.charAt(i);
			if(flagChar=='[')
			{
				dimensionNum++;
			}else if(flagChar==']')
			{
				if(value!=-1)
				{
					dimensionValue.add(value);
					value=-1;
				}
				Integer indexValue=(Integer) dimensionMap.get(dimensionNum);
				if(indexValue==null)
				{
					indexValue=1;
					dimensionMap.put(dimensionNum, indexValue);
				}else
				{
					indexValue++;					
					dimensionMap.put(dimensionNum, indexValue);
				}
				dimensionNum--;
			}else if(flagChar>='0'&&flagChar<='9')
			{
				if(value==-1)
				{
					value=0;
				}
				value=value*10+(flagChar-'0');
			}else if(flagChar==',')
			{
				if(value!=-1)
				{
					dimensionValue.add(value);
					value=-1;
				}
				Integer indexValue=(Integer) dimensionMap.get(dimensionNum);
				if(indexValue==null)
				{
					indexValue=1;
					dimensionMap.put(dimensionNum, indexValue);
				}else
				{
					indexValue++;
					dimensionMap.put(dimensionNum, indexValue);
				}
			}else
			{
				return null;
			}
		}
		for(int i=0;i<dimensionMap.size();i++)
		{
			if(i==0)
			{
				dimension.add(dimensionMap.get(i+1));
			}else
			{
				dimension.add(((Integer)dimensionMap.get(i+1))/((Integer)dimensionMap.get(i)));
			}
		}
		dimensionMap=null;
		return dimension;
	}

	public void tensorChangeToTensorDataList()
	{
		ArrayList tensorDatas=new ArrayList();
		ArrayList tensorList=this.getTensorList();
		int length=tensorList.size();
		for(int i=0;i<length;i++)
		{
			searchList(tensorList.get(i),tensorDatas);
		}
		this.tensorDataList=tensorDatas;
	}
	
	private void searchList(Object tensor,ArrayList tensorDatas)
	{
		if(tensor instanceof List)
		{
			ArrayList tensorList=((ArrayList) tensor);
			int length=tensorList.size();
			for(int i=0;i<length;i++)
			{
				searchList(tensorList.get(i), tensorDatas);
			}
			
		}else if(tensor instanceof Integer)
		{
			tensorDatas.add(tensor);
		}
	}

	

	private boolean judgeToSameDimension(ArrayList dimensionLeft,ArrayList dimensionRight)
	{
		int length=dimensionLeft.size();
		for(int i=0;i<length;i++)
		{
			int tempLeft=(int) dimensionLeft.get(i);
			int tempRight=(int) dimensionRight.get(i);
			if(tempLeft==tempRight||tempLeft==1||tempRight==1)
			{
				continue;
			}else
			{
				return false;
			}
		}
		return true;
	}
	
	private boolean judgeToDifferentDimension(ArrayList dimensionLeft,ArrayList dimensionRight)
	{
		int i=dimensionLeft.size()-1;
		int j=dimensionRight.size()-1;
		for(;i>=0&&j>=0;i--,j--)
		{
			int tempLeft=(int) dimensionLeft.get(i);
			int tempRight=(int) dimensionRight.get(j);
			if(tempLeft==tempRight||tempLeft==1||tempRight==1)
			{
				continue;
			}else
			{
				return false;
			}
		}
		return true;
	}

	private void expandToSameDimension(Tensor tensorLeft,Tensor tensorRight)
	{
		ArrayList dimensionLeft = tensorLeft.getDimensionList();
		ArrayList dimensionRight = tensorRight.getDimensionList();
		ArrayList tensorLeftList = tensorLeft.getTensorList();
		ArrayList tensorRightList = tensorRight.getTensorList();
		int temp=dimensionLeft.size()-1;
		for(;temp>=0;temp--)
		{
			int dimensionLeftValue=(int) dimensionLeft.get(temp);
			int dimensionRightValue=(int) dimensionRight.get(temp);
			if(dimensionLeftValue==dimensionRightValue)
			{
				//两个相等该维度下不需要扩展
				continue;
			}else if(dimensionLeftValue==1)
			{
				dimensionLeft.set(temp, dimensionRightValue);
				if(temp==0)
					temp=100;
				copy(1, temp, tensorLeftList, dimensionLeft, dimensionRightValue);	
				if(temp==100)
				temp=0;
			}else if(dimensionRightValue==1)
			{
				dimensionRight.set(temp, dimensionLeftValue);
				if(temp==0)
					temp=100;
				copy(1, temp, tensorRightList, dimensionRight, dimensionLeftValue);
				if(temp==100)
				temp=0;
			}
		}
		tensorLeft.tensorPrintf();
		tensorRight.tensorPrintf();
	}

	private void expandToDifferentDimension(Tensor tensorLeft,Tensor tensorRight)
	{
		ArrayList dimensionLeft = tensorLeft.getDimensionList();
		ArrayList dimensionRight = tensorRight.getDimensionList();
		ArrayList tensorLeftList = tensorLeft.getTensorList();
		ArrayList tensorRightList = tensorRight.getTensorList();
		int tempLeft=dimensionLeft.size()-1;
		int tempRight=dimensionRight.size()-1;
		for(;tempLeft>=0&&tempRight>=0;tempLeft--,tempRight--)
		{
			int dimensionLeftValue=(int) dimensionLeft.get(tempLeft);
			int dimensionRightValue=(int) dimensionRight.get(tempRight);
			if(dimensionLeftValue==dimensionRightValue)
			{
				//两个相等该维度下不需要扩展
				continue;
			}else if(dimensionLeftValue==1)
			{
				dimensionLeft.set(tempLeft, dimensionRightValue);
				if(tempLeft==0)
					tempLeft=100;
				copy(1, tempLeft, tensorLeftList, dimensionLeft, dimensionRightValue);	
				if(tempLeft==100)
				tempLeft=0;
			}else if(dimensionRightValue==1)
			{
				dimensionRight.set(tempRight, dimensionLeftValue);
				if(tempRight==0)
					tempRight=100;
				copy(1, tempRight, tensorRightList, dimensionRight, dimensionLeftValue);
				if(tempRight==100)
				tempRight=0;
			}
		}
		if(tempLeft>tempRight)
		{
			int tempResult=tempLeft-tempRight-1;
			for(;tempResult>=0;tempResult--)
			{
				int dimensionLeftValue=(int) dimensionLeft.get(tempResult);
				ArrayList temp=new ArrayList();
				tensorRightList=tensorRight.getTensorList();
				for(int i=0;i<dimensionLeftValue;i++)
				{
					temp.add(tensorRightList);
				}
				tensorRightList=temp;
				tensorRight.setTensorList(tensorRightList);
				tensorRight.setDimensionList(dimensionLeft);
			}
			tensorLeft.tensorPrintf();
			tensorRight.tensorPrintf();
		}else
		{
			int tempResult=tempRight-tempLeft-1;
			for(;tempResult>=0;tempResult--)
			{
				int dimensionRightValue=(int) dimensionRight.get(tempResult);
				ArrayList temp=new ArrayList();
				tensorLeftList=tensorLeft.getTensorList();
				for(int i=0;i<dimensionRightValue;i++)
				{
					temp.add(tensorLeftList);
				}
				tensorLeftList=temp;
				tensorLeft.setTensorList(tensorLeftList);
				tensorLeft.setDimensionList(dimensionRight);
			}
			tensorLeft.tensorPrintf();
			tensorRight.tensorPrintf();
		}
		
	}

	public void AddTranToTensor(Tensor tensor)
	{
		ArrayList dimension = tensor.getDimensionList();
		int tensorSize = Tensor.tensorSize(tensor);
		int dimensionSize = Tensor.tensorSize(dimension);
		if(tensorSize!=dimensionSize)
		{
			System.out.println("error  不能转换");
			
		}else
		{		
			String funcString="reshape";
			ArrayList tensorDatas=tensor.getTensorDataList();
			if(dimension.size()!=1)
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				for(int i=0;i<length;i++)
				{
					ArrayList element=initElement(1,dimension,funcString,tensorDatas);
					tensor.tensorList.add(element);
				}
			}else
			{
				//获取最高维度的维度值
				int length=(int) dimension.get(0);
				Random random=new Random();
				int element=0;
				for(int i=0;i<length;i++)
				{
					element=(int) tensorDatas.get(i);
					tensor.tensorList.add(element);
				}
			}
		}
		
	}

	private ArrayList copy(int pos,int tempLeft,ArrayList tensorList,ArrayList dimension,int num)
	{
		ArrayList result=new ArrayList();
		if(tempLeft==100)
		{
			int length=(int) dimension.get(pos);
			int size=tensorList.size();
			for(int i=0;i<size;i++)
			{
				if(tempLeft==100)
				{
					result=(ArrayList) tensorList;
				}					
				else
				result=(ArrayList) tensorList.get(i);
				for(int j=1;j<num;j++)
				{
					result.add(result.get(0));
				}
			}
			return tensorList;
		}
		else if(pos!=tempLeft)
		{
			int length=(int) dimension.get(pos);
			for(int i=0;i<tensorList.size();i++)
			{
				ArrayList temp=new ArrayList();
				temp=(ArrayList) tensorList.get(i);
				ArrayList element=copy(pos+1, tempLeft, temp, dimension,num);
				result.add(element);
			}
			return result;
		}else
		{
			int length=(int) dimension.get(pos);
			int size=tensorList.size();
			ArrayList temp=new ArrayList();
			for(int i=0;i<size;i++)
			{
				
				temp=(ArrayList) tensorList.get(i);
				for(int j=1;j<num;j++)
				{
					temp.add(temp.get(0));
				}
			}
			return tensorList;
		}
	}

	
	private Object dot(ArrayList tensorLeft,ArrayList tensorRight)
	{
		if(tensorLeft.get(0) instanceof List)
		{
			ArrayList result = new ArrayList();
			Object temp;
			for(int i=0;i<tensorLeft.size();i++)
			{
				temp=dot((ArrayList)tensorLeft.get(i), tensorRight);
				result.add(temp);
			}
			return result;
		}else if(tensorRight.get(0) instanceof List)
		{
			ArrayList columnList = new ArrayList();
			ArrayList tensorRight0=(ArrayList) tensorRight.get(0);
			for(int i=0;i<tensorRight0.size();i++)
			{
				ArrayList b=new ArrayList();
				for(int j=0;j<tensorRight.size();j++)
				{
					ArrayList tempList=(ArrayList) tensorRight.get(j);
					Object temp=tempList.get(i);
					b.add(temp);
				}
				columnList.add(b);
			}
			ArrayList result=new ArrayList();
			Object temp;
			for(int i=0;i<columnList.size();i++)
			{
				temp=dot(tensorLeft, (ArrayList)columnList.get(i));
				result.add(temp);
			}
			return result;
		}else
		{
			Integer temp=new Integer(0);
			for(int i=0;i<tensorLeft.size();i++)
			{
				temp+=(int)tensorLeft.get(i)*(int)tensorRight.get(i);
			}
			return temp;
		}
	}

	private ArrayList slice(ArrayList tensor,ArrayList begin,ArrayList size,int index)
	{
		ArrayList result=new ArrayList();
		if(index != begin.size()-1)
		{
			int beginNum=(int) begin.get(index);
			int sizeNum=(int) size.get(index);
			for(int i=beginNum;i<beginNum+sizeNum;i++)
			{
				ArrayList temp=slice((ArrayList)tensor.get(i), begin, size, index+1);
				result.add(temp);
			}
			return result;
		}else
		{
			int beginNum=(int) begin.get(index);
			int sizeNum=(int) size.get(index);
			for(int i=beginNum;i<beginNum+sizeNum;i++)
			{
				result.add(tensor.get(i));
			}
			return result;
		}
	}
	
	private boolean judgeToSlice(ArrayList dimension,ArrayList begin,ArrayList size)
	{
		if(dimension.size()!=begin.size()||dimension.size()!=size.size()||begin.size()!=size.size())
		{
			return false;
		}
		for(int i=0;i<begin.size();i++)
		{
			int beginValue=(int) begin.get(i);
			if(beginValue<0)
			{
				return false;
			}
		}
		for(int i=0;i<begin.size();i++)
		{
			int beginValue=(int) begin.get(i);
			int sizeValue = (int) size.get(i);
			int dimensionValue = (int) dimension.get(i);
			if(dimensionValue<beginValue+sizeValue)
			{
				return false;
			}
		}
		return true;
	}
	
}
