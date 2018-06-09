package com.szu.edu.parseTensor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.szu.edu.function.Tensor;

public class ParseTensorUtil {

	/*
	 * expressionList:传进来的表达式集合
	 * expressionMap: 用于保存语句的编译状态，之后可能需要用到
	 * tensorValue: 用于保存tensor程序运行时，保存变量的集合
	 */
	
	public static void parseTensorPragrammer(ArrayList<String> expressionList,HashMap expressionMap,HashMap tensorValue,ArrayList<String> result,ArrayList<String> fasleReasonList)
	{
		
		for(int i=0;i<expressionList.size();i++)
		{
			String expression = expressionList.get(i);
			String falseReason = "";
			boolean expressionStatus = true;
			if(expression.indexOf("=")!=-1)//如果表达式含有等号，证明是赋值语句
			{
				String[] strs=expression.split("=");
				//variableName变量名
				String variableName = strs[0];
				String variableValue = strs[1];
				if(variableValue.indexOf("[")!=-1&&variableValue.indexOf("slice")==-1)//通过指定的结构创建Tesnor
				{
					Tensor tensor = new Tensor();
					tensor = tensor.tensorSpecify(variableValue);
					if(tensor==null)//当输入的指定结构不是
					{
						falseReason="创建名为"+variableName+"变量时，输入的指定结构存在错误，该变量创建失败\r\n"
									+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else//tensor变量创建成功，保存起来
					{
						expressionStatus=true;
						//将该语句的结果，保存起来
						expressionMap.put(i+1, expressionStatus);
						tensorValue.put(variableName, tensor);
					}
				}else if(variableValue.indexOf("((")!=-1)//通过对应的函数创建tensor变量
				{
					ArrayList<String> matchers=new ArrayList<String>();
					String reg="([\\s\\S]*?)\\(\\(([\\s\\S]*?)\\)\\)";
					matchers=getMatchers(variableValue,reg);
					if(matchers.size()==0)//获取到的list长度为0，证明没有匹配，说明输入的函数结构有错误
					{
						falseReason="创建名为"+variableName+"变量时，输入函数结构存在错误，该变量创建失败\r\n"
									+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else//成功截取到参数和function
					{
						String functionName = matchers.get(0);
						ArrayList dimension = new ArrayList();
						String argString = matchers.get(1);
						if(argString.indexOf(",")!=-1)
						{
							String[] args = argString.split(",");
							int argsLength = args.length;
							boolean flag=false;
							for(int j=0;j<argsLength;j++)
							{
								String arg=args[j];
								//判断参数是不是纯数字
								boolean resultArg = arg.matches("[0-9]+");
								if(resultArg==false)
								{
									falseReason="创建名为"+variableName+"变量时，输入函数参数有错误，该变量创建失败\r\n"
												+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									//跳过此次，避免创建错误tensor,并把维度参数清空
									dimension.clear();
									flag=true;
									break;
								}else
								{
									Integer argInt=Integer.parseInt(arg);			
									dimension.add(argInt);
								}
							}
							if(flag==true)
							{
								continue;
							}
						}
						else
						{
							//判断参数是不是纯数字
							boolean resultArg = argString.matches("[0-9]+");
							if(resultArg==false)
							{
								falseReason="创建名为"+variableName+"变量时，输入函数参数有错误，该变量创建失败\r\n"
											+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								//跳过此次，避免创建错误tensor
								continue;
							}else
							{
								Integer arg=Integer.parseInt(argString);			
								dimension.add(arg);
							}
						}
						//参数判定后，判定函数方法名，首先需要判定dimension的size
						if(dimension.size()!=0)
						{
							if(functionName.equals("rand"))//调用rand函数创建tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorRand(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//将该语句的结果，保存起来
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="创建名为"+variableName+"的变量时程序出现不明错误\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else if(functionName.equals("one"))//调用one函数创建tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorOne(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//将该语句的结果，保存起来
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="创建名为"+variableName+"的变量时程序出现不明错误\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else if(functionName.equals("zero"))//调用zero函数创建tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorZero(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//将该语句的结果，保存起来
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="创建名为"+variableName+"的变量时程序出现不明错误\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else//没有找到对应创建函数的方法，语法错误
							{
								falseReason="创建名为"+variableName+"的变量时没有找到对应的创建方法，可以尝试输入(rand,one或者zero)\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}
					}
				}else if(variableValue.indexOf("+")!=-1)//通过类似x=a+b赋值
				{
					String[] args = variableValue.split("\\+");
					String argLeftName = args[0];
					String argRightName = args[1];
					Object argLeft = tensorValue.get(argLeftName);
					Object argRight = tensorValue.get(argRightName);
					if(argLeft==null)
					{
						falseReason="名为"+argLeftName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="名为"+argRightName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的变量类型不同，不能相加\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}else
					{
						if(argLeft instanceof Tensor)
						{
							Tensor argLeftTensor = (Tensor) argLeft;
							Tensor argRightTensor = (Tensor) argRight;
							ArrayList dimensionLeft = new ArrayList();
							ArrayList dimensionRight = new ArrayList();
							ArrayList tensorListLeft = new ArrayList();
							ArrayList tensorListRight = new ArrayList();
							ArrayList tensorDataRight = new ArrayList();
							ArrayList tensorDataLeft = new ArrayList();
							dimensionLeft.addAll(argLeftTensor.getDimensionList());
							dimensionRight.addAll(argRightTensor.getDimensionList());
							tensorListLeft.addAll(argLeftTensor.getTensorList());
							tensorListRight.addAll(argRightTensor.getTensorList());
							Tensor copyToLeft = new Tensor();
							Tensor copyToRight = new Tensor();
							copyToLeft.setDimensionList(dimensionLeft);
							copyToLeft.setTensorList(tensorListLeft);
							copyToLeft.tensorChangeToTensorDataList();
							copyToRight.setDimensionList(dimensionRight);
							copyToRight.setTensorList(tensorListRight);
							//tensorValue.put(argRightName, copyToRight);
							copyToRight.tensorChangeToTensorDataList();
							tensorDataRight=copyToRight.getTensorDataList();
							tensorDataLeft=copyToLeft.getTensorDataList();
							Tensor tensor = new Tensor();
							tensor = tensor.tensorAdd(copyToLeft, copyToRight);
							//重构防止数据被修改
							argRightTensor.getTensorList().clear();
							argRightTensor.setTensorDataList(tensorDataRight);
							argRightTensor.setPos(0);
							argRightTensor.AddTranToTensor(argRightTensor);
							argLeftTensor.getTensorList().clear();
							argLeftTensor.setTensorDataList(tensorDataLeft);
							argLeftTensor.setPos(0);
							argLeftTensor.AddTranToTensor(argLeftTensor);
							//copyToLeft=null;
							//copyToRight=null;
							if(tensor==null)
							{
								falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的维度值不同，不能扩展相加\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//将该语句的结果，保存起来
								expressionMap.put(i+1, expressionStatus);
								tensorValue.put(variableName, tensor);
							}
						}
					}
				}else if(variableValue.indexOf(".")!=-1)
				{
					String[] args = variableValue.split("\\.");
					String argLeftName = args[0];
					String argRightName = args[1];
					Object argLeft = tensorValue.get(argLeftName);
					Object argRight = tensorValue.get(argRightName);
					if(argLeft==null)
					{
						falseReason="名为"+argLeftName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="名为"+argRightName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的变量类型不同，不能点乘\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}else
					{
						if(argLeft instanceof Tensor)
						{
							Tensor argLeftTensor = (Tensor) argLeft;
							Tensor argRightTensor = (Tensor) argRight;
							ArrayList dimensionLeft = new ArrayList();
							ArrayList dimensionRight = new ArrayList();
							ArrayList tensorListLeft = new ArrayList();
							ArrayList tensorListRight = new ArrayList();
							ArrayList tensorDataRight = new ArrayList();
							ArrayList tensorDataLeft = new ArrayList();
							dimensionLeft.addAll(argLeftTensor.getDimensionList());
							dimensionRight.addAll(argRightTensor.getDimensionList());
							tensorListLeft.addAll(argLeftTensor.getTensorList());
							tensorListRight.addAll(argRightTensor.getTensorList());
							Tensor copyToLeft = new Tensor();
							Tensor copyToRight = new Tensor();
							copyToLeft.setDimensionList(dimensionLeft);
							copyToLeft.setTensorList(tensorListLeft);
							copyToLeft.tensorChangeToTensorDataList();
							copyToRight.setDimensionList(dimensionRight);
							copyToRight.setTensorList(tensorListRight);
							//tensorValue.put(argRightName, copyToRight);
							copyToRight.tensorChangeToTensorDataList();
							tensorDataRight=copyToRight.getTensorDataList();
							tensorDataLeft=copyToLeft.getTensorDataList();
							Tensor tensor = new Tensor();
							tensor = tensor.tensorPointMul(copyToLeft, copyToRight);
							//重构防止数据被修改
							argRightTensor.getTensorList().clear();
							argRightTensor.setTensorDataList(tensorDataRight);
							argRightTensor.setPos(0);
							argRightTensor.AddTranToTensor(argRightTensor);
							argLeftTensor.getTensorList().clear();
							argLeftTensor.setTensorDataList(tensorDataLeft);
							argLeftTensor.setPos(0);
							argLeftTensor.AddTranToTensor(argLeftTensor);
							//copyToLeft=null;
							//copyToRight=null;
							if(tensor==null)
							{
								falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的维度值不同，不能扩展相加\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//将该语句的结果，保存起来
								expressionMap.put(i+1, expressionStatus);
								tensorValue.put(variableName, tensor);
							}
						}
					}
				}else if(variableValue.indexOf("*")!=-1)
				{
					String[] args = variableValue.split("\\*");
					String argLeftName = args[0];
					String argRightName = args[1];
					Object argLeft = tensorValue.get(argLeftName);
					Object argRight = tensorValue.get(argRightName);
					if(argLeft==null)
					{
						falseReason="名为"+argLeftName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="名为"+argRightName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的变量类型不同，不能叉乘\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}else
					{
						if(argLeft instanceof Tensor)
						{
							Tensor argLeftTensor = (Tensor) argLeft;
							Tensor argRightTensor = (Tensor) argRight;
							ArrayList dimensionLeft = new ArrayList();
							ArrayList dimensionRight = new ArrayList();
							ArrayList tensorListLeft = new ArrayList();
							ArrayList tensorListRight = new ArrayList();
							dimensionLeft.addAll(argLeftTensor.getDimensionList());
							dimensionRight.addAll(argRightTensor.getDimensionList());
							tensorListLeft.addAll(argLeftTensor.getTensorList());
							tensorListRight.addAll(argRightTensor.getTensorList());
							Tensor copyToLeft = new Tensor();
							Tensor copyToRight = new Tensor();
							copyToLeft.setDimensionList(dimensionLeft);
							copyToLeft.setTensorList(tensorListLeft);
							copyToLeft.tensorChangeToTensorDataList();
							copyToRight.setDimensionList(dimensionRight);
							copyToRight.setTensorList(tensorListRight);
							copyToRight.tensorChangeToTensorDataList();
							Tensor tensor = copyToLeft.tensorDot(copyToLeft, copyToRight);
							copyToLeft=null;
							copyToRight=null;
							if(tensor==null)
							{
								falseReason="名为"+argLeftName+"的变量和名为"+argRightName+"的维度值不同，不能扩展点乘加\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//将该语句的结果，保存起来
								expressionMap.put(i+1, expressionStatus);
								tensorValue.put(variableName, tensor);
							}
						}
					}
				}else if(variableValue.indexOf("reshape")!=-1)
				{
					String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\)";
					ArrayList<String> matchers=getMatchers(variableValue, reg);
					if(matchers.size()!=0)
					{
						String variableNameString=matchers.get(1);
						String functionName=matchers.get(0);
						if(functionName.equals("reshape"))
						{
							//确定是使用reshape方法赋值，取得reshape方法的参数
							//获取参数
							if(variableValue.indexOf(",")!=-1)
							{
								String[] argStrs = variableNameString.split(",");
								//获取第一个参数tensor的变量
								String tensorVariableName = argStrs[0];
								Object obj = tensorValue.get(tensorVariableName);
								if(obj == null)
								{
									falseReason="名为"+tensorVariableName+"的变量不存在\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}else if(obj instanceof Tensor)
								{
									Tensor tensor = (Tensor) obj;//获得tensor变量
									ArrayList dimension = new ArrayList();
									for(int j=1;j<argStrs.length;j++)
									{
										String numStr = argStrs[j];
										//判断参数是不是纯数字
										boolean resultArg = numStr.matches("[0-9]+");
										if(resultArg==false)
										{
											falseReason="创建名为"+variableName+"变量时，输入函数参数不是纯数字，该变量创建失败\r\n"
														+"出错的语句："+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//跳过此次，避免创建错误tensor
											dimension.clear();
											break;
										}else
										{
											//判断纯数字会不会超出整型范围
											boolean judgeIntMax = judgeOverMaxInteger(numStr);
											if(judgeIntMax==false)
											{
												falseReason="创建名为"+variableName+"变量时，输入函数参数数字超过整型范围，该变量创建失败\r\n"
														+"出错的语句："+expression;
												saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
												//跳过此次，避免创建错误tensor
												dimension.clear();
												break;
											}else
											{
												Integer arg=Integer.parseInt(numStr);			
												dimension.add(arg);
											}					
										}
									}
									if(dimension.size()!=0)
									{
										Tensor tensorCopy = new Tensor();
										tensorCopy.setDimensionList(tensor.getDimensionList());
										tensorCopy.setTensorList(tensor.getTensorList());
										tensorCopy.setTensorDataSize(tensor.tensorSize(tensor));
										tensorCopy.tensorChangeToTensorDataList();		
										System.out.println(dimension.toString());
										tensorCopy=tensorCopy.tensorReShape(tensorCopy, dimension);
										if(tensorCopy==null)
										{
											falseReason="名为"+tensorVariableName+"的变量不能正确进行reshape，可能的原因是传入的维度参数不符合\r\n"
													+"出错的语句："+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										}else
										{
											expressionStatus=true;
											//将该语句的结果，保存起来
											expressionMap.put(i+1, expressionStatus);
											tensorValue.put(variableName, tensorCopy);
										}
									}else
									{
										falseReason="名为"+tensorVariableName+"的变量时参数传入错误\r\n"
												+"出错的语句："+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										continue;
									}
									
								}else
								{
									falseReason="名为"+tensorVariableName+"的变量不是tensor类型，不能进行reshape操作\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else
							{
								
								falseReason="名为"+functionName+"的方法传入的参数错误\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}						
						}else
						{
							System.out.println("A"+"方法名"+functionName);
							falseReason="名为"+functionName+"的方法名不存在\r\n"
									+"出错的语句："+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
					}else
					{
						falseReason="reshape语句结构出错\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
				}
				else if(variableValue.indexOf("shape")!=-1)
				{
					String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\)";
					ArrayList<String> matchers=getMatchers(variableValue, reg);
					if(matchers.size()!=0)
					{
						String variableNameString=matchers.get(1);
						String functionName=matchers.get(0);
						if(functionName.equals("shape"))
						{
							Object obj = tensorValue.get(variableNameString);
							if(obj==null)
							{
								falseReason="名为"+variableNameString+"的变量不存在\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								expressionStatus=true;
								//将该语句的结果，保存起来
								expressionMap.put(i+1, expressionStatus);
								Tensor tensor = (Tensor) obj;
								String shapeResult = tensor.tensorShape(tensor);
								tensorValue.put(variableName, shapeResult);
								continue;
							}else
							{
								falseReason="名为"+variableNameString+"的变量不是tensor类型不能进行shape操作\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							System.out.println(functionName);
							falseReason="名为"+functionName+"的方法名不存在\r\n"
									+"出错的语句："+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}					
					}else
					{
						falseReason="shape语句结构出错\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
				}
				else if(variableValue.indexOf("size")!=-1)
				{
					String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\)";
					ArrayList<String> matchers=getMatchers(variableValue, reg);
					if(matchers.size()!=0)
					{
						String variableNameString=matchers.get(1);
						String functionName=matchers.get(0);
						if(functionName.equals("size"))
						{
							Object obj = tensorValue.get(variableNameString);
							if(obj==null)
							{
								falseReason="名为"+variableNameString+"的变量不存在\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								expressionStatus=true;
								//将该语句的结果，保存起来
								expressionMap.put(i+1, expressionStatus);
								Tensor tensor = (Tensor) obj;
								Integer sizeResult = tensor.tensorSize(tensor);
								tensorValue.put(variableName, sizeResult);
								continue;
							}else
							{
								falseReason="名为"+variableNameString+"的变量不是tensor类型不能进行shape操作\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							
							falseReason="名为"+functionName+"的方法名不存在\r\n"
									+"出错的语句："+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}			
					}else
					{
						falseReason="size语句结构出错\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
				}else if(variableValue.indexOf("slice")!=-1)
				{
					String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\[([\\s\\S]*?)\\],\\[([\\s\\S]*?)\\]\\)";
					ArrayList<String> sliceMatchers = new ArrayList<String>();
					sliceMatchers = getSliceMatchers(variableValue, reg);
					if(sliceMatchers.size()==4)
					{
						String variableNameString=sliceMatchers.get(1);
						variableNameString=variableNameString.substring(0, variableNameString.length()-1);
						String functionName=sliceMatchers.get(0);
						String beginStr = sliceMatchers.get(2);
						String sizeStr = sliceMatchers.get(3);
						if(functionName.equals("slice"))
						{
							Object obj = tensorValue.get(variableNameString);
							if(obj==null)
							{
								falseReason="名为"+variableNameString+"的变量不存在\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								Tensor tensor = (Tensor) obj;//获得tensor变量
								ArrayList begin=new ArrayList();
								ArrayList size=new ArrayList();
								String[] beginStrs=beginStr.split(",");
								String[] sizeStrs=sizeStr.split(",");
								for(int j=0;j<beginStrs.length;j++)
								{
									String numStr = beginStrs[j];
									//判断参数是不是纯数字
									boolean resultArg = numStr.matches("[0-9]+");
									if(resultArg==false)
									{
										falseReason="创建名为"+variableName+"变量时，输入函数参数不是纯数字，该变量创建失败\r\n"
													+"出错的语句："+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										//跳过此次，避免创建错误tensor
										begin.clear();
										break;
									}else
									{
										//判断纯数字会不会超出整型范围
										boolean judgeIntMax = judgeOverMaxInteger(numStr);
										if(judgeIntMax==false)
										{
											falseReason="创建名为"+variableName+"变量时，输入函数参数数字超过整型范围，该变量创建失败\r\n"
													+"出错的语句："+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//跳过此次，避免创建错误tensor
											begin.clear();
											break;
										}else
										{
											Integer arg=Integer.parseInt(numStr);			
											begin.add(arg);
										}					
									}
								}
								for(int j=0;j<sizeStrs.length;j++)
								{
									String numStr = sizeStrs[j];
									//判断参数是不是纯数字
									boolean resultArg = numStr.matches("[0-9]+");
									if(resultArg==false)
									{
										falseReason="创建名为"+variableName+"变量时，输入函数参数不是纯数字，该变量创建失败\r\n"
													+"出错的语句："+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										//跳过此次，避免创建错误tensor
										size.clear();
										break;
									}else
									{
										//判断纯数字会不会超出整型范围
										boolean judgeIntMax = judgeOverMaxInteger(numStr);
										if(judgeIntMax==false)
										{
											falseReason="创建名为"+variableName+"变量时，输入函数参数数字超过整型范围，该变量创建失败\r\n"
													+"出错的语句："+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//跳过此次，避免创建错误tensor
											size.clear();
											break;
										}else
										{
											Integer arg=Integer.parseInt(numStr);			
											size.add(arg);
										}					
									}
								}
								if(begin.size()!=0&&size.size()!=0)
								{
									Tensor tensorCopy = new Tensor();
									tensorCopy.setDimensionList(tensor.getDimensionList());
									tensorCopy.setTensorList(tensor.getTensorList());
									tensorCopy.setTensorDataSize(tensor.tensorSize(tensor));
									tensorCopy.tensorChangeToTensorDataList();		
									tensorCopy=tensorCopy.tensorSlice(tensorCopy, begin, size);
									if(tensorCopy==null)
									{
										falseReason="名为"+variableName+"的变量不能正确进行slice，可能的原因是传入的维度参数不符合\r\n"
												+"出错的语句："+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									}else
									{
										expressionStatus=true;
										//将该语句的结果，保存起来
										expressionMap.put(i+1, expressionStatus);
										tensorValue.put(variableName, tensorCopy);
									}
								}else
								{
									falseReason="名为"+variableName+"的变量时参数传入错误\r\n"
											+"出错的语句："+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
								
							}else
							{
								falseReason="名为"+variableNameString+"的变量不是tensor类型不能进行slice操作\r\n"
										+"出错的语句："+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							falseReason="名为"+functionName+"的方法名不存在\r\n"
									+"出错的语句："+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
					}else
					{
						falseReason="slice语句结构出错\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
					
				}else
				{
					//直接赋值整型，需要判断是否是纯数字
					boolean resultToNumStr=variableValue.matches("[0-9]+");
					if(resultToNumStr==true)//证明是纯数字
					{
						boolean judgeOverMaxInteger=judgeOverMaxInteger(variableValue);
						if(judgeOverMaxInteger==true)
						{
							Integer variableValueNum = Integer.parseInt(variableValue);
							tensorValue.put(variableName, variableValueNum);
						}else
						{
							falseReason="名为"+variableName+"的变量超出整数范围\r\n"
									+"出错的语句："+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
						
					}else{
						falseReason="名为"+variableName+"的变量不是纯数字\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					
				}
			}else if(expression.indexOf("print")!=-1)
			{
				String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\)";
				ArrayList<String> matchers=getMatchers(expression, reg);
				if(matchers.size()!=0)//可以拿到对应的参数
				{
					String variableName=matchers.get(1);
					Object obj = tensorValue.get(variableName);
					if(obj==null)
					{
						falseReason="名为"+variableName+"的变量不存在\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else if(obj instanceof Tensor)
					{
						expressionStatus=true;
						//将该语句的结果，保存起来
						expressionMap.put(i+1, expressionStatus);
						Tensor tensor = (Tensor) obj;
						String printfResult = tensor.tensorPrintf();
						printfResult +="   该变量的类型为："+tensor.getClass();
						result.add(printfResult);
					}else if(obj instanceof String)
					{
						expressionStatus=true;
						//将该语句的结果，保存起来
						expressionMap.put(i+1, expressionStatus);
						String objStr = (String) obj;
						objStr +="   该变量的类型为："+objStr.getClass();
						result.add(objStr);
					}else if(obj instanceof Integer)
					{
						expressionStatus=true;
						//将该语句的结果，保存起来
						expressionMap.put(i+1, expressionStatus);
						String objStr =  obj.toString();
						objStr +="   该变量的类型为："+obj.getClass();
						result.add(objStr);
					}else
					{
						falseReason="名为"+variableName+"的变量类型错误\r\n"
								+"出错的语句："+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
				}else{
					falseReason="print语句结构出错\r\n"
							+"出错的语句："+expression;
					saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
				}
			}
			
			
		}
	}

	public static void saveFalseStatusAndResult(HashMap expressionMap, ArrayList<String> fasleReasonList, int i,String falseReason) {
		boolean expressionStatus;
		expressionStatus=false;
		//将该语句的结果，保存起来
		expressionMap.put(i+1, expressionStatus);
		fasleReasonList.add(falseReason);
	}
	
	//最后改成private,开发的时候便于测试
	public static ArrayList<String> getMatchers(String variableValue,String reg)
	{
		ArrayList<String> matchers=new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(variableValue);
		while(matcher.find())
		{
			//System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
			matchers.add(matcher.group(1));
			matchers.add(matcher.group(2));
		}
		return matchers;
	}
	
	//最后改成private,开发的时候便于测试
	public static ArrayList<String> getSliceMatchers(String variableValue,String reg)
	{
		ArrayList<String> matchers=new ArrayList<String>();
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(variableValue);
		while(matcher.find())
		{
			//System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
			System.out.println(matcher.group(2));
			System.out.println(matcher.group(3));
			System.out.println(matcher.group(4));
			matchers.add(matcher.group(1));
			matchers.add(matcher.group(2));
			matchers.add(matcher.group(3));
			matchers.add(matcher.group(4));
		}
		
		return matchers;
	}
	
	public static boolean judgeOverMaxInteger(String numStr)
	{
		boolean flag=true;
		int length=numStr.length();
		//先判断长度，然后与2147483647比较
		if(length>10)
		{
			return false;
		}else if(length<10)
		{
			return true;
		}else if(numStr.equals("2147483647"))
		{
			return true;
		}	
		else
		{
			for(int i=0;i<length;i++)
			{
				int temp=numStr.charAt(i)-'0';
				if(i==0)
				{
					if(temp<2)
					{
						break;
					}
					else if(temp!=2)
					flag=false;
				}else if(i==1)
				{
					if(temp<1)
					{
						break;
					}
					else if(temp!=1)
					flag=false;
				}else if(i==2)
				{
					if(temp<4)
					{
						break;
					}
					else if(temp!=4)
					flag=false;
				}else if(i==3)
				{
					if(temp<7)
					{
						break;
					}
					else if(temp!=7)
					flag=false;
				}else if(i==4)
				{
					if(temp<4)
					{
						break;
					}
					else if(temp!=4)
					flag=false;
				}else if(i==5)
				{
					if(temp<8)
					{
						break;
					}
					else if(temp!=8)
					flag=false;
				}else if(i==6)
				{
					if(temp<3)
					{
						break;
					}
					else if(temp!=3)
					flag=false;
				}else if(i==7)
				{
					if(temp<6)
					{
						break;
					}
					else if(temp!=6)
					flag=false;
				}else if(i==8)
				{
					if(temp<4)
					{
						break;
					}
					else if(temp!=4)
					flag=false;
				}else
				{
					if(temp>7)
					{
						return false;
					}
				}
			}
			
			return flag;
		}
	}
	
}
