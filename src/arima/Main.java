package arima;
/**
 * ��Javaʵ���У���Ҫ������AR��MA�Լ�ARMA�Ĳ������Ʒ�������δ����ƽ�����Լ�ģ�͵���ѽ��������ϸ���֤����
 * ֻ��ͨ������ģ�Ͳ����б�ķ�ʽ��AIC׼�����BIC׼��ȷ�����p��q������
 * ͬʱ�ڲ������ƵĹ����У���Ҫ������Yule-Walker����������⣻ͬʱΪ�˱������������н��������ļ��㣬
 * ����Levinson���ƹ�ʽ���Y-W���̣��õ�ģ�͵Ĳ�����*/
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
//���С����Ԥ�⣬�������ݷŴ�1000000����ת����int�ͱ�������Ԥ������С10000����ת����double�ͱ�����ʵ��С��Ԥ��
			for (int i = 0; i < data.length; i++){
			    data[i] = (int)(data[i]*1000000);
            }

            ArimaModel arima = new ArimaModel(data);//����ԭʼ����

			ArrayList<int []> list = new ArrayList<>();
			int period = 7;// ��ֽ���
			int modelCnt = 10, cnt = 0;			//ͨ�����Ԥ���ƽ��ֵ��ΪԤ��ֵ
			int [] tmpPredict = new int [modelCnt];
			for (int k = 0; k < modelCnt; ++k)			//����ͨ��������������м������յĽ��
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
			System.out.println("Predict value="+predictResult);// ������Ԥ���ֵ
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
