package com.szu.edu.tensorTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.szu.edu.function.Tensor;
import com.szu.edu.parseTensor.util.ParseTensorUtil;

public class Test {

	@org.junit.Test
	public void testRand() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(2);
		dimension.add(3);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getDimensionList());
		System.out.println(tensor.getTensorList().toString());
	}
	
	@org.junit.Test
	public void testOne() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(1);
		dimension.add(2);
		dimension.add(3);
		tensor=tensor.tensorOne(dimension);
		System.out.println(tensor.getDimensionList());
		System.out.println(tensor.getTensorList().toString());
	}
	
	@org.junit.Test
	public void testZero() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(1);
		dimension.add(2);
		dimension.add(3);
		tensor=tensor.tensorZero(dimension);
		System.out.println(tensor.getDimensionList());
		System.out.println(tensor.getTensorList().toString());
	}

	@org.junit.Test
	public void testSpecify() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(2);
		dimension.add(2);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
	//	String specify=tensor.getTensorList().toString();
		String specify="[[87,93,95],[94,2,93]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试shape函数
		//String tensorShape = Tensor.tensorShape(new Object());
		String tensorShape2 = Tensor.tensorShape(tensor2);
		tensor2.tensorPrintf();
		//System.out.println(tensorShape);
		System.out.println(tensorShape2);
	}
	
	@org.junit.Test
	public void testSize() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(10);
		dimension.add(5);
		dimension.add(4);
		dimension.add(6);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
		String specify=tensor.getTensorList().toString();
//		String specify="[[[[1, 46, 95], [24, 63, 89]], [[29, 30, 87], [54, 42, 57]], [[48, 21, 27], [99, 89, 24]], [[11, 1, 20], [94, 23, 58]]], [[[9, 32, 18], [83, 87, 95]], [[6, 57, 5], [58, 81, 28]], [[89, 52, 93], [7, 10, 75]], [[71, 31, 95], [24, 78, 34]]]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试size函数
		int tensorSize = Tensor.tensorSize(tensor);
		int tensorSize2 = Tensor.tensorSize(tensor2);
		System.out.println(tensorSize);
		System.out.println(tensorSize2);
	}
	
	@org.junit.Test
	public void testTensorData() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(1);
		dimension.add(4);
		dimension.add(3);
		dimension.add(3);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
		String specify=tensor.getTensorList().toString();
//		String specify="[[[[1, 46, 95], [24, 63, 89]], [[29, 30, 87], [54, 42, 57]], [[48, 21, 27], [99, 89, 24]], [[11, 1, 20], [94, 23, 58]]], [[[9, 32, 18], [83, 87, 95]], [[6, 57, 5], [58, 81, 28]], [[89, 52, 93], [7, 10, 75]], [[71, 31, 95], [24, 78, 34]]]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		System.out.println(tensor.getTensorDataList().toString());
		tensor2.tensorChangeToTensorDataList();
		System.out.println(tensor2.getTensorDataList().toString());
	}
	
	@org.junit.Test
	public void testTensorReshape() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(3);
		dimension.add(2);
		dimension.add(2);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
		String specify=tensor.getTensorList().toString();
//		String specify="[[[[1, 46, 95], [24, 63, 89]], [[29, 30, 87], [54, 42, 57]], [[48, 21, 27], [99, 89, 24]], [[11, 1, 20], [94, 23, 58]]], [[[9, 32, 18], [83, 87, 95]], [[6, 57, 5], [58, 81, 28]], [[89, 52, 93], [7, 10, 75]], [[71, 31, 95], [24, 78, 34]]]]";
//		Tensor tensor2=new Tensor();
//		tensor2=tensor2.tensorSpecify(specify);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		System.out.println(tensor.getTensorDataList().toString());
//		tensor2.tensorChangeToTensorDataList();
//		System.out.println(tensor2.getTensorDataList().toString());
		
		//测试reshape函数
		Tensor reshapeTensor = new Tensor();
		ArrayList reShapeDimension=new ArrayList();
		reShapeDimension.add(2);
		reShapeDimension.add(6);
//		reShapeDimension.add(3);
		reshapeTensor = reshapeTensor.tensorReShape(tensor, reShapeDimension);
		System.out.println(reshapeTensor.getTensorList().toString());
		System.out.println(reshapeTensor.getDimensionList().toString());
	}
	
	@org.junit.Test
	public void testTensorPrintf() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(2);
		dimension.add(2);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
		String specify=tensor.getTensorList().toString();
//		String specify="[[[[1, 46, 95], [24, 63, 89]], [[29, 30, 87], [54, 42, 57]], [[48, 21, 27], [99, 89, 24]], [[11, 1, 20], [94, 23, 58]]], [[[9, 32, 18], [83, 87, 95]], [[6, 57, 5], [58, 81, 28]], [[89, 52, 93], [7, 10, 75]], [[71, 31, 95], [24, 78, 34]]]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		tensor.tensorPrintf();
		tensor2.tensorChangeToTensorDataList();
		tensor2.tensorPrintf();		
		//测试reshape函数
		Tensor reshapeTensor = new Tensor();
		ArrayList reShapeDimension=new ArrayList();
		reShapeDimension.add(4);
		reshapeTensor = reshapeTensor.tensorReShape(tensor, reShapeDimension);
		reshapeTensor.tensorPrintf();
		System.out.println(reshapeTensor.getDimensionList().toString());
	
	}
	
	@org.junit.Test
	public void testTensorAdd() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(2);
		dimension.add(2);
		dimension.add(2);
		dimension.add(2);
		dimension.add(1);
		dimension.add(1);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
//		String specify=tensor.getTensorList().toString();
		String specify="[[1,2,3,4],[2,2,3,7]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		tensor.tensorPrintf();
		tensor2.tensorChangeToTensorDataList();
		tensor2.tensorPrintf();		
		tensor2.setPos(0);
		//测试Add
		Tensor result=tensor.tensorAdd(tensor2, tensor);
		if(result!=null)
		{
			System.out.println(result.tensorPrintf());
			System.out.println(result.tensorShape(result));
		}
		
	}
	
	@org.junit.Test
	public void testTensorPointMul() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		//dimension.add(3);
		dimension.add(3);
		dimension.add(2);
		dimension.add(3);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
//		String specify=tensor.getTensorList().toString();
		//String specify="[[42,37],[9,25]]";
		ArrayList dimension2=new ArrayList();
		dimension2.add(3);
		dimension2.add(1);
		dimension2.add(2);
		dimension2.add(3);
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorRand(dimension2);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		tensor.tensorPrintf();
		tensor2.tensorChangeToTensorDataList();
		System.out.println(tensor2.tensorPrintf());
		tensor2.tensorPrintf();		
		tensor2.setPos(0);
		//测试Add
		Tensor result=tensor.tensorPointMul(tensor, tensor2);
		if(result!=null)
		System.out.println(result.tensorPrintf());
	}
	
	@org.junit.Test
	public void testTensorDot() {
		Tensor tensor=new Tensor();
		ArrayList dimension=new ArrayList();
		dimension.add(2);
		dimension.add(3);
		dimension.add(2);
		tensor=tensor.tensorRand(dimension);
		System.out.println(tensor.getTensorList().toString());
//		String specify=tensor.getTensorList().toString();
		String specify="[[[1,2],[3,4]],[[5,6],[7,8]]]";
		Tensor tensor2=new Tensor();
		tensor2=tensor2.tensorSpecify(specify);
		//测试ChangeToTensorDataList函数
		tensor.tensorChangeToTensorDataList();
		tensor.tensorPrintf();
		tensor2.tensorChangeToTensorDataList();
		tensor2.tensorPrintf();		
		tensor2.setPos(0);
		//测试Add
		Tensor result=tensor.tensorDotWithTree(tensor, tensor2);
		result.tensorPrintf();
	}
	
	@org.junit.Test
	public void testTensorSlice() {
		Tensor tensor=new Tensor();
//		String specify=tensor.getTensorList().toString();
		String specify="[[[1,1,1],[2,2,2]],[[3,3,3],[4,4,4]],[[5,5,5],[6,6,6]]]";
		tensor=tensor.tensorSpecify(specify);
		ArrayList begin=new ArrayList();
		ArrayList size=new ArrayList();
		begin.add(1);
		begin.add(0);
		begin.add(0);
		size.add(2);
		size.add(1);
		size.add(3);
		//测试Add
		Tensor result=tensor.tensorSlice(tensor, begin, size);
		System.out.println(result.tensorPrintf());
	}
	
	@org.junit.Test
	public void testRegex()
	{
		
		//String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\[([\\s\\S]*?)\\],\\[([\\s\\S]*?)\\]\\)";
		//String str = "slice(x,[1,2,3],[0,0,0])";
		//ParseTensorUtil.getSliceMatchers(str, reg);
		double k=0D;
		double j=0D;
		double i=0D;
		k=(j+13)/27;
		System.out.println(k);
		while(k<10.0D)
		{
			k=(k+1.2D);
			i=3*k-1.0D;
		}
		System.out.println(k);
		System.out.println(i);
		
	}
	
	@org.junit.Test
	public void testParseTensor()
	{
		 
		Scanner scanner=new Scanner(System.in);
		String expression;
		ArrayList<String> expressions=new ArrayList<String>();
		ArrayList<String> result = new ArrayList();
		ArrayList<String> falseReason = new ArrayList<String>();
		HashMap tensorValue = new HashMap();
		HashMap expressionMap = new HashMap();
		while(true)
		{
			expression=scanner.nextLine();
			if(expression.equals("quit"))
			{
				break;
			}else
			{
				expressions.add(expression);
			}
		}
		ParseTensorUtil.parseTensorPragrammer(expressions, expressionMap, tensorValue, result, falseReason);
		System.out.println(result.size());
		System.out.println("程序输入完成");
		if(falseReason.size()==0)
		{
			for(int i=0;i<result.size();i++)
			{
				System.out.println(result.get(i));
			}
		}else
		{
			System.out.println("程序出错，错误原因如下");
			for(int i=0;i<falseReason.size();i++)
			{
				System.out.println(falseReason.get(i));
			}
		}
	}
}
