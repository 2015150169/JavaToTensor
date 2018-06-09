package com.szu.edu.parseTensor.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.szu.edu.function.Tensor;

public class ParseTensorUtil {

	/*
	 * expressionList:�������ı��ʽ����
	 * expressionMap: ���ڱ������ı���״̬��֮�������Ҫ�õ�
	 * tensorValue: ���ڱ���tensor��������ʱ����������ļ���
	 */
	
	public static void parseTensorPragrammer(ArrayList<String> expressionList,HashMap expressionMap,HashMap tensorValue,ArrayList<String> result,ArrayList<String> fasleReasonList)
	{
		
		for(int i=0;i<expressionList.size();i++)
		{
			String expression = expressionList.get(i);
			String falseReason = "";
			boolean expressionStatus = true;
			if(expression.indexOf("=")!=-1)//������ʽ���еȺţ�֤���Ǹ�ֵ���
			{
				String[] strs=expression.split("=");
				//variableName������
				String variableName = strs[0];
				String variableValue = strs[1];
				if(variableValue.indexOf("[")!=-1&&variableValue.indexOf("slice")==-1)//ͨ��ָ���Ľṹ����Tesnor
				{
					Tensor tensor = new Tensor();
					tensor = tensor.tensorSpecify(variableValue);
					if(tensor==null)//�������ָ���ṹ����
					{
						falseReason="������Ϊ"+variableName+"����ʱ�������ָ���ṹ���ڴ��󣬸ñ�������ʧ��\r\n"
									+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else//tensor���������ɹ�����������
					{
						expressionStatus=true;
						//�������Ľ������������
						expressionMap.put(i+1, expressionStatus);
						tensorValue.put(variableName, tensor);
					}
				}else if(variableValue.indexOf("((")!=-1)//ͨ����Ӧ�ĺ�������tensor����
				{
					ArrayList<String> matchers=new ArrayList<String>();
					String reg="([\\s\\S]*?)\\(\\(([\\s\\S]*?)\\)\\)";
					matchers=getMatchers(variableValue,reg);
					if(matchers.size()==0)//��ȡ����list����Ϊ0��֤��û��ƥ�䣬˵������ĺ����ṹ�д���
					{
						falseReason="������Ϊ"+variableName+"����ʱ�����뺯���ṹ���ڴ��󣬸ñ�������ʧ��\r\n"
									+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else//�ɹ���ȡ��������function
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
								//�жϲ����ǲ��Ǵ�����
								boolean resultArg = arg.matches("[0-9]+");
								if(resultArg==false)
								{
									falseReason="������Ϊ"+variableName+"����ʱ�����뺯�������д��󣬸ñ�������ʧ��\r\n"
												+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									//�����˴Σ����ⴴ������tensor,����ά�Ȳ������
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
							//�жϲ����ǲ��Ǵ�����
							boolean resultArg = argString.matches("[0-9]+");
							if(resultArg==false)
							{
								falseReason="������Ϊ"+variableName+"����ʱ�����뺯�������д��󣬸ñ�������ʧ��\r\n"
											+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								//�����˴Σ����ⴴ������tensor
								continue;
							}else
							{
								Integer arg=Integer.parseInt(argString);			
								dimension.add(arg);
							}
						}
						//�����ж����ж�������������������Ҫ�ж�dimension��size
						if(dimension.size()!=0)
						{
							if(functionName.equals("rand"))//����rand��������tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorRand(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//�������Ľ������������
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="������Ϊ"+variableName+"�ı���ʱ������ֲ�������\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else if(functionName.equals("one"))//����one��������tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorOne(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//�������Ľ������������
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="������Ϊ"+variableName+"�ı���ʱ������ֲ�������\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else if(functionName.equals("zero"))//����zero��������tensor
							{
								Tensor tensor = new Tensor();
								tensor = tensor.tensorZero(dimension);
								if(tensor!=null)
								{
									expressionStatus=true;
									//�������Ľ������������
									expressionMap.put(i+1, expressionStatus);
									tensorValue.put(variableName, tensor);
								}else
								{
									falseReason="������Ϊ"+variableName+"�ı���ʱ������ֲ�������\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else//û���ҵ���Ӧ���������ķ������﷨����
							{
								falseReason="������Ϊ"+variableName+"�ı���ʱû���ҵ���Ӧ�Ĵ������������Գ�������(rand,one����zero)\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}
					}
				}else if(variableValue.indexOf("+")!=-1)//ͨ������x=a+b��ֵ
				{
					String[] args = variableValue.split("\\+");
					String argLeftName = args[0];
					String argRightName = args[1];
					Object argLeft = tensorValue.get(argLeftName);
					Object argRight = tensorValue.get(argRightName);
					if(argLeft==null)
					{
						falseReason="��Ϊ"+argLeftName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="��Ϊ"+argRightName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"�ı������Ͳ�ͬ���������\r\n"
								+"�������䣺"+expression;
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
							//�ع���ֹ���ݱ��޸�
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
								falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"��ά��ֵ��ͬ��������չ���\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//�������Ľ������������
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
						falseReason="��Ϊ"+argLeftName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="��Ϊ"+argRightName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"�ı������Ͳ�ͬ�����ܵ��\r\n"
								+"�������䣺"+expression;
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
							//�ع���ֹ���ݱ��޸�
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
								falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"��ά��ֵ��ͬ��������չ���\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//�������Ľ������������
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
						falseReason="��Ϊ"+argLeftName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argRight==null)
					{
						falseReason="��Ϊ"+argRightName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					if(argLeft.getClass()!=argRight.getClass())
					{
						falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"�ı������Ͳ�ͬ�����ܲ��\r\n"
								+"�������䣺"+expression;
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
								falseReason="��Ϊ"+argLeftName+"�ı�������Ϊ"+argRightName+"��ά��ֵ��ͬ��������չ��˼�\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else
							{
								expressionStatus=true;
								//�������Ľ������������
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
							//ȷ����ʹ��reshape������ֵ��ȡ��reshape�����Ĳ���
							//��ȡ����
							if(variableValue.indexOf(",")!=-1)
							{
								String[] argStrs = variableNameString.split(",");
								//��ȡ��һ������tensor�ı���
								String tensorVariableName = argStrs[0];
								Object obj = tensorValue.get(tensorVariableName);
								if(obj == null)
								{
									falseReason="��Ϊ"+tensorVariableName+"�ı���������\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}else if(obj instanceof Tensor)
								{
									Tensor tensor = (Tensor) obj;//���tensor����
									ArrayList dimension = new ArrayList();
									for(int j=1;j<argStrs.length;j++)
									{
										String numStr = argStrs[j];
										//�жϲ����ǲ��Ǵ�����
										boolean resultArg = numStr.matches("[0-9]+");
										if(resultArg==false)
										{
											falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������Ǵ����֣��ñ�������ʧ��\r\n"
														+"�������䣺"+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//�����˴Σ����ⴴ������tensor
											dimension.clear();
											break;
										}else
										{
											//�жϴ����ֻ᲻�ᳬ�����ͷ�Χ
											boolean judgeIntMax = judgeOverMaxInteger(numStr);
											if(judgeIntMax==false)
											{
												falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������ֳ������ͷ�Χ���ñ�������ʧ��\r\n"
														+"�������䣺"+expression;
												saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
												//�����˴Σ����ⴴ������tensor
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
											falseReason="��Ϊ"+tensorVariableName+"�ı���������ȷ����reshape�����ܵ�ԭ���Ǵ����ά�Ȳ���������\r\n"
													+"�������䣺"+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										}else
										{
											expressionStatus=true;
											//�������Ľ������������
											expressionMap.put(i+1, expressionStatus);
											tensorValue.put(variableName, tensorCopy);
										}
									}else
									{
										falseReason="��Ϊ"+tensorVariableName+"�ı���ʱ�����������\r\n"
												+"�������䣺"+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										continue;
									}
									
								}else
								{
									falseReason="��Ϊ"+tensorVariableName+"�ı�������tensor���ͣ����ܽ���reshape����\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
							}else
							{
								
								falseReason="��Ϊ"+functionName+"�ķ�������Ĳ�������\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}						
						}else
						{
							System.out.println("A"+"������"+functionName);
							falseReason="��Ϊ"+functionName+"�ķ�����������\r\n"
									+"�������䣺"+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
					}else
					{
						falseReason="reshape���ṹ����\r\n"
								+"�������䣺"+expression;
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
								falseReason="��Ϊ"+variableNameString+"�ı���������\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								expressionStatus=true;
								//�������Ľ������������
								expressionMap.put(i+1, expressionStatus);
								Tensor tensor = (Tensor) obj;
								String shapeResult = tensor.tensorShape(tensor);
								tensorValue.put(variableName, shapeResult);
								continue;
							}else
							{
								falseReason="��Ϊ"+variableNameString+"�ı�������tensor���Ͳ��ܽ���shape����\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							System.out.println(functionName);
							falseReason="��Ϊ"+functionName+"�ķ�����������\r\n"
									+"�������䣺"+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}					
					}else
					{
						falseReason="shape���ṹ����\r\n"
								+"�������䣺"+expression;
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
								falseReason="��Ϊ"+variableNameString+"�ı���������\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								expressionStatus=true;
								//�������Ľ������������
								expressionMap.put(i+1, expressionStatus);
								Tensor tensor = (Tensor) obj;
								Integer sizeResult = tensor.tensorSize(tensor);
								tensorValue.put(variableName, sizeResult);
								continue;
							}else
							{
								falseReason="��Ϊ"+variableNameString+"�ı�������tensor���Ͳ��ܽ���shape����\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							
							falseReason="��Ϊ"+functionName+"�ķ�����������\r\n"
									+"�������䣺"+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}			
					}else
					{
						falseReason="size���ṹ����\r\n"
								+"�������䣺"+expression;
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
								falseReason="��Ϊ"+variableNameString+"�ı���������\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}else if(obj instanceof Tensor)
							{
								Tensor tensor = (Tensor) obj;//���tensor����
								ArrayList begin=new ArrayList();
								ArrayList size=new ArrayList();
								String[] beginStrs=beginStr.split(",");
								String[] sizeStrs=sizeStr.split(",");
								for(int j=0;j<beginStrs.length;j++)
								{
									String numStr = beginStrs[j];
									//�жϲ����ǲ��Ǵ�����
									boolean resultArg = numStr.matches("[0-9]+");
									if(resultArg==false)
									{
										falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������Ǵ����֣��ñ�������ʧ��\r\n"
													+"�������䣺"+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										//�����˴Σ����ⴴ������tensor
										begin.clear();
										break;
									}else
									{
										//�жϴ����ֻ᲻�ᳬ�����ͷ�Χ
										boolean judgeIntMax = judgeOverMaxInteger(numStr);
										if(judgeIntMax==false)
										{
											falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������ֳ������ͷ�Χ���ñ�������ʧ��\r\n"
													+"�������䣺"+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//�����˴Σ����ⴴ������tensor
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
									//�жϲ����ǲ��Ǵ�����
									boolean resultArg = numStr.matches("[0-9]+");
									if(resultArg==false)
									{
										falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������Ǵ����֣��ñ�������ʧ��\r\n"
													+"�������䣺"+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
										//�����˴Σ����ⴴ������tensor
										size.clear();
										break;
									}else
									{
										//�жϴ����ֻ᲻�ᳬ�����ͷ�Χ
										boolean judgeIntMax = judgeOverMaxInteger(numStr);
										if(judgeIntMax==false)
										{
											falseReason="������Ϊ"+variableName+"����ʱ�����뺯���������ֳ������ͷ�Χ���ñ�������ʧ��\r\n"
													+"�������䣺"+expression;
											saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
											//�����˴Σ����ⴴ������tensor
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
										falseReason="��Ϊ"+variableName+"�ı���������ȷ����slice�����ܵ�ԭ���Ǵ����ά�Ȳ���������\r\n"
												+"�������䣺"+expression;
										saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									}else
									{
										expressionStatus=true;
										//�������Ľ������������
										expressionMap.put(i+1, expressionStatus);
										tensorValue.put(variableName, tensorCopy);
									}
								}else
								{
									falseReason="��Ϊ"+variableName+"�ı���ʱ�����������\r\n"
											+"�������䣺"+expression;
									saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
									continue;
								}
								
							}else
							{
								falseReason="��Ϊ"+variableNameString+"�ı�������tensor���Ͳ��ܽ���slice����\r\n"
										+"�������䣺"+expression;
								saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
								continue;
							}
						}else
						{
							falseReason="��Ϊ"+functionName+"�ķ�����������\r\n"
									+"�������䣺"+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
					}else
					{
						falseReason="slice���ṹ����\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
					
				}else
				{
					//ֱ�Ӹ�ֵ���ͣ���Ҫ�ж��Ƿ��Ǵ�����
					boolean resultToNumStr=variableValue.matches("[0-9]+");
					if(resultToNumStr==true)//֤���Ǵ�����
					{
						boolean judgeOverMaxInteger=judgeOverMaxInteger(variableValue);
						if(judgeOverMaxInteger==true)
						{
							Integer variableValueNum = Integer.parseInt(variableValue);
							tensorValue.put(variableName, variableValueNum);
						}else
						{
							falseReason="��Ϊ"+variableName+"�ı�������������Χ\r\n"
									+"�������䣺"+expression;
							saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
							continue;
						}
						
					}else{
						falseReason="��Ϊ"+variableName+"�ı������Ǵ�����\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
						continue;
					}
					
				}
			}else if(expression.indexOf("print")!=-1)
			{
				String reg="([\\s\\S]*?)\\(([\\s\\S]*?)\\)";
				ArrayList<String> matchers=getMatchers(expression, reg);
				if(matchers.size()!=0)//�����õ���Ӧ�Ĳ���
				{
					String variableName=matchers.get(1);
					Object obj = tensorValue.get(variableName);
					if(obj==null)
					{
						falseReason="��Ϊ"+variableName+"�ı���������\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}else if(obj instanceof Tensor)
					{
						expressionStatus=true;
						//�������Ľ������������
						expressionMap.put(i+1, expressionStatus);
						Tensor tensor = (Tensor) obj;
						String printfResult = tensor.tensorPrintf();
						printfResult +="   �ñ���������Ϊ��"+tensor.getClass();
						result.add(printfResult);
					}else if(obj instanceof String)
					{
						expressionStatus=true;
						//�������Ľ������������
						expressionMap.put(i+1, expressionStatus);
						String objStr = (String) obj;
						objStr +="   �ñ���������Ϊ��"+objStr.getClass();
						result.add(objStr);
					}else if(obj instanceof Integer)
					{
						expressionStatus=true;
						//�������Ľ������������
						expressionMap.put(i+1, expressionStatus);
						String objStr =  obj.toString();
						objStr +="   �ñ���������Ϊ��"+obj.getClass();
						result.add(objStr);
					}else
					{
						falseReason="��Ϊ"+variableName+"�ı������ʹ���\r\n"
								+"�������䣺"+expression;
						saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
					}
				}else{
					falseReason="print���ṹ����\r\n"
							+"�������䣺"+expression;
					saveFalseStatusAndResult(expressionMap, fasleReasonList, i,falseReason);
				}
			}
			
			
		}
	}

	public static void saveFalseStatusAndResult(HashMap expressionMap, ArrayList<String> fasleReasonList, int i,String falseReason) {
		boolean expressionStatus;
		expressionStatus=false;
		//�������Ľ������������
		expressionMap.put(i+1, expressionStatus);
		fasleReasonList.add(falseReason);
	}
	
	//���ĳ�private,������ʱ����ڲ���
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
	
	//���ĳ�private,������ʱ����ڲ���
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
		//���жϳ��ȣ�Ȼ����2147483647�Ƚ�
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
