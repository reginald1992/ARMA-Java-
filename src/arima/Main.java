package arima;
/**
 * 在Java实现中，主要给出了AR、MA以及ARMA的参数估计方法，并未对其平稳性以及模型的最佳阶数进行严格性证明，
 * 只是通过遍历模型参数列表的方式由AIC准则或者BIC准则确定最佳p、q阶数。
 * 同时在参数估计的过程中，主要是利用Yule-Walker方法进行求解；同时为了避免在求解过程中进行逆矩阵的计算，
 * 采用Levinson递推公式求解Y-W方程，得到模型的参数。*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main
{
	public static void main(String args[])
	{
		Path path = Paths.get("./data/", "test2.csv");
		File file = path.toFile();
		try
		(
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))
		)
		{
			String line = null;
			ArrayList<Double> al=new ArrayList<Double>();
			while ((line = br.readLine()) != null)
			{
				al.add(Double.parseDouble(line));
			}
			double [] data = new double[al.size()];
			for (int i = 0; i < data.length; ++i)
			{
				data[i] = al.get(i);
			}
//针对小数的预测，输入数据放大1000000倍，转化成int型变量；对预测结果缩小10000倍，转化成double型变量；实现小数预测
			for (int i = 0; i < data.length; i++){
			    data[i] = (int)(data[i]*1000000);
            }

            ArimaModel arima = new ArimaModel(data);//输入原始数据

			ArrayList<int []> list = new ArrayList<>();
			int period = 7;// 差分阶数
			int modelCnt = 10, cnt = 0;			//通过多次预测的平均值作为预测值
			int [] tmpPredict = new int [modelCnt];
			for (int k = 0; k < modelCnt; ++k)			//控制通过多少组参数进行计算最终的结果
			{
				int [] bestModel = arima.getARIMAModel(period, list, (k == 0) ? false : true);
				if (bestModel.length == 0)
				{
					tmpPredict[k] = (int)data[data.length - period];
					cnt++;
					break;
				}
				else
				{
					int predictDiff = arima.predictValue(bestModel[0], bestModel[1], period);
					tmpPredict[k] = arima.aftDeal(predictDiff, period);
					cnt++;
				}
				System.out.println("BestModel is " + bestModel[0] + " " + bestModel[1]);
				list.add(bestModel);
			}
			al.clear();
			double sumPredict = 0.0;
			for (int k = 0; k < cnt; ++k)
			{
				sumPredict += (double)tmpPredict[k] / (double)cnt;
			}
			int predict = (int)Math.round(sumPredict);
			double predictResult = (double)predict/1000000.0;
			System.out.println("Predict value="+predictResult);// 输出多次预测均值
		}
		catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}
