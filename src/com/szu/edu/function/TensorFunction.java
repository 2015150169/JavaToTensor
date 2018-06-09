package com.szu.edu.function;

import java.util.ArrayList;

public interface TensorFunction {
	
	public Tensor tensorRand(ArrayList dimension);
	public Tensor tensorOne(ArrayList dimension);
	public Tensor tensorZero(ArrayList dimension);
	public Tensor tensorReShape(Tensor tensor,ArrayList dimension);
	public Tensor tensorSpecify(String specifyDimension);
	public Tensor tensorAdd(Tensor tensorLeft,Tensor tensorRight);
	public Tensor tensorPointMul(Tensor tensorLeft,Tensor tensorRight);
	public Tensor tensorDot(Tensor tensorLeft,Tensor tensorRight);
	public Tensor tensorDotWithTree(Tensor tensorLeft,Tensor tensorRight);
	public Tensor tensorSlice(Tensor tensor,ArrayList begin,ArrayList size);
	public String tensorPrintf();
}
