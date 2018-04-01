package arima;

import java.util.Vector;

public class ArModel
{
	private double [] data;
	private int p;
	
	public ArModel(double [] data, int p)
	{
		this.data = data;
		this.p = p;
	}
	
	public Vector<double []> solveCoeOfAR()
	{
		Vector<double []>vec = new Vector<>();
		double [] arCoe = new ArmaMethod().computeARCoe(this.data, this.p);
		
		vec.add(arCoe);
		
		return vec;
	}
}
