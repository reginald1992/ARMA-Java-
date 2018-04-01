package arima;

import java.util.Vector;

public class MaModel
{
	private double [] data;
	private int q;
	
	public MaModel(double [] data, int q)
	{
		this.data = data;
		this.q = q;
	}
	
	public Vector<double []> solveCoeOfMA()
	{
		Vector<double []>vec = new Vector<>();
		double [] maCoe = new ArmaMethod().computeMACoe(this.data, this.q);
		
		vec.add(maCoe);
		
		return vec;
	}
}
