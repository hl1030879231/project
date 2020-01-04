package aPredict;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

//import org.apache.commons.io.FileUtils;  
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import sketch.geo.gPoint;

public class predictModel {

	static Session chineseSession;
	static String[] chineseLabel;


	static Session delSession;
	
	//yuyangcheng
	//特殊符号的 Session
		//目前 会存在一些 和chineselabel 相同的字符 
		static Session specialSession;
		static char[] specialCharLabel = {'Ｔ', 'ｑ', '⊕', 'ｅ', 'ｋ', 'Ｈ', '５', '＋', 'ｂ', '７', '４', 'ｗ', 'Ｙ', 'ｓ', 'Ｍ', 'ｍ', 'Ｒ', '↓', '＜', '［', '８', '２', 'ｙ', '→', 'Ｏ', '６', 'ｉ', '＝', 'Ａ', '）', 'ｎ', 'ｘ', 'Ｎ', 'Ｉ', 'Ｌ', '←', 'Ｑ', 'ｐ', '＃', '９', 'a', 'Ｆ', 'ｄ', '＊', 'ｔ', '｝', '≤', 'ｕ', 'Ｊ', 'ｆ', 'Ｇ', '＞', 'ｇ', '（', '３', '１', '↑', 'Ｄ', '÷', '？', '－', 'Ｂ', 'ｊ', 'ｌ', 'Ｕ', 'Ｅ'};
	//yuyangcheng 
		
		
	static {
		byte[] chineseModel;
		try {
		chineseModel = Files.readAllBytes(Paths.get("D://eclipse/workspace/project/model/scut_all_9459.pb"));
		Graph graph = new Graph();
		graph.importGraphDef(chineseModel);
		chineseSession = new Session(graph);
		chineseLabel = new String[3807];
		readChineseLabel();
		
		
		
		
		byte[] specialCharModel;
		//yuyangcheng
		// 特殊字符的相关模型构建
		//specialCharModel = Files.readAllBytes(Paths.get("./model/mobilenet_66.pb"));
		specialCharModel = Files.readAllBytes(Paths.get("D://eclipse/workspace/project/model/mobilenet_66.pb"));
		Graph specialCharGraph = new Graph();
		specialCharGraph.importGraphDef(specialCharModel);
		specialSession = new Session(specialCharGraph);
		System.out.println("特殊字符mobilenet_66.pb导入模型成功.");
		//yuyangcheng
		
		

		byte[] delModel = Files.readAllBytes(Paths.get("D://eclipse/workspace/project/model/dele_conv4.pb"));// 94
		Graph graph1 = new Graph();
		graph1.importGraphDef(delModel);
		delSession = new Session(graph1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void readChineseLabel() {
		String pathname = "D://eclipse/workspace/project/model/scut_all_9459";
		try (FileReader reader = new FileReader(pathname);
				BufferedReader br = new BufferedReader(reader)) {
			String line;
			int hang = 0;
			while ((line = br.readLine()) != null) {
				chineseLabel[hang++] = line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// yuce chinese
	public char predictChinese(ListIterator<gPoint> li) throws Exception {
		float my_x[][][][] = preProcess(li);
		Tensor x = Tensor.create(my_x);
		Tensor y = chineseSession.runner().feed("input_4", x).fetch("dense_4/Softmax").run().get(0);// 4 4 scut_all_9459
		float[][] result = new float[1][(int) y.shape()[1]];
		y.copyTo(result);
		int maxindex = qiuxiabiao(result[0]);
		String predictlabel = chineseLabel[maxindex];
		char[] array = predictlabel.toCharArray();
		return array[0];
	}

	
	
	/*yuyangcheng
	 * 预测特殊字符 返回预测的字符？
	 * */
	public char predictSpecialChar(ListIterator<gPoint> li) throws Exception {
		// specialChar的预测 
		float my_x_specialChar[][][][] = preProcess(li);
		Tensor x_specialChar = Tensor.create(my_x_specialChar);
		// input_* dense_* 是模型随机生成出来的，所以这里需要根据模型里去找，如果替换了模型这里就需要hard code来实现
		Tensor y_specialChar = specialSession.runner().feed("input_2_1", x_specialChar).fetch("dense_2_3/concat").run().get(0);
		float[][] resultSpecialChar = new float[1][(int) y_specialChar.shape()[1]];
		y_specialChar.copyTo(resultSpecialChar);
		int maxindexSpecialChar = qiuxiabiao(resultSpecialChar[0]);
		char testChar = specialCharLabel[maxindexSpecialChar];
//		System.out.println("test特殊char : "+testChar);
//		for(int j = 0 ; j<resultSpecialChar[0].length ; j++){
//		System.out.println("index"+j+" 对应对字符： "+specialCharLabel[j]+"  概率："+resultSpecialChar[0][j]);
//		}
//test
//char testChar = specialCharLabel[maxindex];
//System.out.println("获取到的下标 : "+maxindex+"test特殊char : "+testChar);
		return testChar;
	}
	
	//yuyangcheng
	
	

	public char predictDel(ListIterator<gPoint> li) throws Exception {
		float my_x[][][][] = preProcess1(li);
		int maxindex;
		Tensor x = Tensor.create(my_x);
		Tensor y = delSession.runner().feed("conv2d_5_input", x).fetch("dense_4/Softmax").run().get(0);
		float[][] result = new float[1][(int) y.shape()[1]];
		y.copyTo(result);
		maxindex = qiuxiabiao(result[0]);
	
		return Character.forDigit(maxindex, 10);
	}

	
	
	
	public static float[][][][] preProcess(ListIterator<gPoint> li) {
		int maxlen = 48;
		/* *3 */
		float my_x[][][][] = new float[1][maxlen][maxlen][3];
		// System.out.println(my_x[0][0][0][0]);
		int xmin = 100000000, xmax = 0, ymin = 100000000, ymax = 0;
		for (int i = 0; i < maxlen; i++) {
			for (int j = 0; j < maxlen; j++) {
				my_x[0][i][j][0] = 1;
				my_x[0][i][j][1] = 1;
				my_x[0][i][j][2] = 1;
			}
		}

		while (li.hasNext()) {
			gPoint g = li.next();
			int x = (int) g.getX();
			int y = (int) g.getY();
			if (x < xmin)
				xmin = x;
			if (x > xmax)
				xmax = x;
			if (y < ymin)
				ymin = y;
			if (y > ymax)
				ymax = y;
		}

		int xymaxlen, xyminlen;
		if ((xmax - xmin) > (ymax - ymin)) {
			xymaxlen = xmax - xmin + 1;
			xyminlen = ymax - ymin + 1;
		} else {
			xymaxlen = ymax - ymin + 1;
			xyminlen = xmax - xmin + 1;
		}

		boolean flag = true;
		if ((xymaxlen - 1) == (xmax - xmin))
			flag = false;
		int kong = (xymaxlen - xyminlen) / 2;

		while (li.hasPrevious()) {
			li.previous();
		}

		if (false){
		//if (xymaxlen >= maxlen) {
			System.out.println(" 不补点： ");
			while (li.hasNext()) {
				gPoint g = li.next();
				int x = (int) g.getX();
				int y = (int) g.getY();
				// System.out.print("("+x+","+y+')');
				int x1, y1, x2, y2;
				if (flag) {
					x1 = y - ymin;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin + kong;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				} else {
					x1 = y - ymin + kong;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				}
				if ((x2 >= maxlen) || (y2 >= maxlen))
					continue;
				my_x[0][x2][y2][0] = 0;
				my_x[0][x2][y2][1] = 0;
				my_x[0][x2][y2][2] = 0;
			}
		} else {
			// System.out.print(" 补点： ");
			int pre_x = 0, pre_y = 0;
			int i = 0;
			while (li.hasNext()) {
				gPoint g = li.next();
				int x = (int) g.getX();
				int y = (int) g.getY();
				// System.out.print("("+x+","+y+')');
				int x1, y1, x2, y2;
				if (flag) {
					x1 = y - ymin;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin + kong;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				} else {
					x1 = y - ymin + kong;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				}
				if ((x2 >= maxlen) || (y2 >= maxlen))
					continue;
				my_x[0][x2][y2][0] = 0;
				my_x[0][x2][y2][1] = 0;
				my_x[0][x2][y2][2] = 0;

				int cur_x = x2;
				int cur_y = y2;
				if (i == 0) {
					i++;
					pre_x = x2;
					pre_y = y2;
					continue;
				}

				if (Math.abs(cur_x - pre_x) > 5 || Math.abs(cur_y - pre_y) > 5) {
					pre_x = cur_x;
					pre_y = cur_y;
					continue;
				}
				int dx = cur_x - pre_x;
				int dy = cur_y - pre_y;

				ArrayList allpoint = new ArrayList<>();
				if (Math.abs(dy) > Math.abs(dx)) {
					if (dx > 0)
						allpoint = drawlineP(pre_x, pre_y, cur_x, cur_y,
								"jumpEND");
					else
						allpoint = drawlineP(cur_x, cur_y, pre_x, pre_y,
								"jumpSTA");
				} else {
					if (dy < 0)
						allpoint = drawlineP(cur_x, cur_y, pre_x, pre_y,
								"jumpSTA");
					else
						allpoint = drawlineP(pre_x, pre_y, cur_x, cur_y,
								"jumpEND");
				}

				// System.out.println(pre_x + " " + pre_y + "->" + cur_x +
				// " "+cur_y);

				for (int j = 0; j < allpoint.size();) {// System.out.print((int)
														// allpoint.get(j)+" "+(int)
														// allpoint.get(j+1)+",");
					my_x[0][(int) allpoint.get(j)][(int) allpoint.get(j + 1)][0] = 0;
					my_x[0][(int) allpoint.get(j)][(int) allpoint.get(j + 1)][1] = 0;
					my_x[0][(int) allpoint.get(j)][(int) allpoint.get(j + 1)][2] = 0;
					j += 2;

				}
				pre_x = cur_x;
				pre_y = cur_y;

			}

		}

//		 for (int i = 0; i < maxlen; i++) {
//		 for (int j = 0; j < maxlen; j++) {
//		 System.out.print((int) my_x[0][i][j][0]);
//		 }
//		 System.out.print('\n');
//		 }

		return my_x;

	}

	public static float[][][][] preProcess1(ListIterator<gPoint> li) {
		int maxlen = 30;
		/* *1 */
		float my_x[][][][] = new float[1][maxlen][maxlen][1];
		// System.out.println(my_x[0][0][0][0]);
		int xmin = 100000000, xmax = 0, ymin = 100000000, ymax = 0;
		for (int i = 0; i < maxlen; i++) {
			for (int j = 0; j < maxlen; j++) {
				my_x[0][i][j][0] = 0;

			}
		}

		while (li.hasNext()) {
			gPoint g = li.next();
			int x = (int) g.getX();
			int y = (int) g.getY();
			if (x < xmin)
				xmin = x;
			if (x > xmax)
				xmax = x;
			if (y < ymin)
				ymin = y;
			if (y > ymax)
				ymax = y;
		}

		int xymaxlen, xyminlen;
		if ((xmax - xmin) > (ymax - ymin)) {
			xymaxlen = xmax - xmin + 1;
			xyminlen = ymax - ymin + 1;
		} else {
			xymaxlen = ymax - ymin + 1;
			xyminlen = xmax - xmin + 1;
		}

		boolean flag = true;
		if ((xymaxlen - 1) == (xmax - xmin))
			flag = false;
		int kong = (xymaxlen - xyminlen) / 2;

		while (li.hasPrevious()) {
			li.previous();
		}

		if (false) {
		//if (xymaxlen >= maxlen) {
			// System.out.print(" 不补点： ");
			while (li.hasNext()) {
				gPoint g = li.next();
				int x = (int) g.getX();
				int y = (int) g.getY();
				// System.out.print("("+x+","+y+')');
				int x1, y1, x2, y2;
				if (flag) {
					x1 = y - ymin;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin + kong;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				} else {
					x1 = y - ymin + kong;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				}
				if ((x2 >= maxlen) || (y2 >= maxlen))
					continue;
				my_x[0][x2][y2][0] = 1;

			}
		} else {
			// System.out.print(" 补点： ");
			int pre_x = 0, pre_y = 0;
			int i = 0;
			while (li.hasNext()) {
				gPoint g = li.next();
				int x = (int) g.getX();
				int y = (int) g.getY();
				// System.out.print("("+x+","+y+')');
				int x1, y1, x2, y2;
				if (flag) {
					x1 = y - ymin;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin + kong;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				} else {
					x1 = y - ymin + kong;
					x2 = x1 * (maxlen - 1) / xymaxlen;
					y1 = x - xmin;
					y2 = y1 * (maxlen - 1) / xymaxlen;
				}
				if ((x2 >= maxlen) || (y2 >= maxlen))
					continue;
				my_x[0][x2][y2][0] = 1;

			}
		}
		return my_x;

	}

	// max index
	public static int qiuxiabiao(float arr[]) {
		float max = 0;
		int maxIndex = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	// insert dot
	public static ArrayList drawlineP(int p0x, int p0y, int p1x, int p1y,
			String jump) {
		if (jump.equals("jumpSTA")) {
			return drawlineNoStart(p0x, p0y, p1x, p1y);
		} else {
			return drawlineNoEnd(p0x, p0y, p1x, p1y);
		}
	}

	public static ArrayList drawlineNoStart(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = 1, sy = 1;
		if (x0 >= x1)
			sx = -1;
		if (y0 >= y1)
			sy = -1;
		int err = dx - dy;
		// int nnn[][] = null;
		// int geshu = 0;

		ArrayList list = new ArrayList();

		while (true) {
			int e2 = err * 2;
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
			if (e2 > -dy) {
				err -= dy;
				x0 += sx;
			}
			// int a[][1]={x0,y0};
			list.add(x0);
			list.add(y0);
			// nnn[geshu][0] = x0;
			// nnn[geshu][1] = y0;geshu++;
			if (x0 == x1 && y0 == y1)
				break;
		}
		return list;

	}

	public static ArrayList drawlineNoEnd(int x0, int y0, int x1, int y1) {
		int dx = Math.abs(x1 - x0);
		int dy = Math.abs(y1 - y0);
		int sx = 1, sy = 1;
		if (x0 >= x1)
			sx = -1;
		if (y0 >= y1)
			sy = -1;
		int err = dx - dy;
		ArrayList list = new ArrayList();

		while (true) {
			if (x0 == x1 && y0 == y1)
				break;

			// a=[x0,y0];
			list.add(x0);
			list.add(y0);

			int e2 = err * 2;
			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
			if (e2 > -dy) {
				err -= dy;
				x0 += sx;
			}
		}
		// System.out.println(list.size());
		return list;
	}

}