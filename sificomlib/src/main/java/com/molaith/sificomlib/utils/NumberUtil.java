package com.molaith.sificomlib.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 数字处理相关工具
 * 
 * @author molaith
 * 
 */
public class NumberUtil {
	//节权位
	public static String[] chnUnitSection = {"","万","亿","万亿"};
	//权位
	public static String[] chnUnitChar = {"","十","百","千"};
	private static String StringSections="百千万亿";
	private static String StringNumSections="一二三四五六七八九";
	public static HashMap intList = new HashMap();
	private static String[] numbers=new String[]{"零","一","二","三","四","五","六","七","八","九"};
	public static char[] chnNumChinese = {'零','一','二','三','四','五','六','七','八','九'};
	private static String[] Arabnumbers=new String[]{"1","2","3","4","5","6","7","8","9"};

	public static boolean isNumber(String singleNumString){
		if (singleNumString.length()>1){
			for (int i = 0; i < singleNumString.length(); i++) {
				if (i + 1 < singleNumString.length()) {
					String cNumber = singleNumString.substring(i, i + 1);
					if (!isNumber(cNumber)) {
						return false;
					}
				}
			}
			return true;
		}else if (singleNumString.length()==1){
			for (String Arab: Arabnumbers){
				if (Arab.equals(singleNumString)){
					return true;
				}
			}
//			for (String number: numbers) {
//				if (number.equals(singleNumString)){
//					return true;
//				}
//			}
		}
		return false;
	}

	private static String isAbridgeNumber(String numberString){
		String section1=numberString.substring(numberString.length()-1,numberString.length());
		String section2="";
		if (numberString.length()>=2){
			section2=numberString.substring(numberString.length()-2,numberString.length()-1);
		}
		if (StringSections.contains(section2)&&StringNumSections.contains(section1)){
				if (section2.equals("百")){
					return numberString+"十";
				}else if (section2.equals("千")){
					return numberString+"百";
				}else if (section2.equals("万")){
					return numberString+"千";
				}else if (section2.equals("亿")){
					return numberString+"千万";
				}
		}
		return numberString;
	}

	/**
	 * 将汉字数字转换成阿拉伯数字，不带权位
	 * @param chinese
	 * @return
	 */
	public static int chineseToAbridge(String chinese){
		if (!TextUtils.isEmpty(chinese)){
			String number="";
			for (int j=0;j<chinese.length();j++){
				for (int i=0;i<numbers.length;i++){
					if (numbers[i].equals(chinese.substring(j,j+1))){
						number=number+ String.valueOf(i);
						break;
					}
				}
			}
			if (!TextUtils.isEmpty(number)){
				return Integer.parseInt(number);
			}
		}
		return 0;
	}

	/**
	 * 将汉字数字转换成阿拉伯数字，带权位，汉字也需带权位
	 * @param str
	 * @return
	 */
	public static int ChineseToNumber(String str){
		for(int i=0;i<numbers.length;i++){
			intList.put(chnNumChinese[i], i);
		}

		intList.put('两',2);
		intList.put('十',10);
		intList.put('百',100);
		intList.put('千', 1000);

		str=isAbridgeNumber(str);
		String str1 = new String();
		String str2 = new String();
		String str3 = new String();
		int k = 0;
		boolean dealflag = true;
		for(int i=0;i<str.length();i++){//先把字符串中的“零”除去
			if('零' == (str.charAt(i))){
				str = str.substring(0, i) + str.substring(i+1);
			}
		}
		String chineseNum = str;
		for(int i=0;i<chineseNum.length();i++){
			if(chineseNum.charAt(i) == '亿'){
				str1 = chineseNum.substring(0,i);//截取亿前面的数字，逐个对照表格，然后转换
				k = i+1;
				dealflag = false;//已经处理
			}
			if(chineseNum.charAt(i) == '万'){
				str2 = chineseNum.substring(k,i);
				str3 = str.substring(i+1);
				dealflag = false;//已经处理
			}
		}
		if(dealflag){//如果没有处理
			str3 =  chineseNum;
		}
		int result = sectionChinese(str1) * 100000000 +
				sectionChinese(str2) * 10000 + sectionChinese(str3);
		return result;
	}

	private static int sectionChinese(String str){
		int value = 0;
		int sectionNum = 0;
		try{
			for(int i=0;i<str.length();i++){
				int v = (int) intList.get(str.charAt(i));
				if( v == 10 || v == 100 || v == 1000 ){//如果数值是权位则相乘
					sectionNum = (i==0?v:v * sectionNum);
					value = value + sectionNum;
				}else if(i == str.length()-1){
					value = value + v;
				}else{
					sectionNum = v;
				}
			}
		}catch (Exception e){

		}
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return 将字节数格式化
	 */
	public static String[] getIntRealSize(long value){
		String unit = "M";
		StringBuilder size = new StringBuilder();
		float f = (float) value;
		if (value > 1023) {
			f = div(f, 1024, 1);
			if (f > 1023) {
				f = div(f, 1024, 1);
				if (f > 999) {
					f = div(f, 1024, 1);
					size.append(f);
					unit = "G";
				} else {
					size.append(f);
					unit = "M";
				}
			} else {
				size.append(f);
				unit = "K";
			}
		} else if (value > 0) {
			size.append(value);
			unit = "B";
		} else {
			size.append('0');
		}
		return new String[] {size.toString(), unit};
	}

	/**
	 * 两个数的除法，并精确到小数点后面scale位
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            精度
	 * @return
	 */
	public static float div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (float) b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/**
	 * 将number格式化为精确到scale位小数
	 * @param number
	 * @param scale
	 * @return
	 */
	public static double format(double number,int scale){
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		return ((double) Math.round(number* Math.pow(10,scale))/ Math.pow(10,scale));
	}

	/**
	 * 将字节数格式化，精确到小数点后一位
	 * 
	 * @param size
	 * @return
	 */
	public static String getRealSize(long size) {
		String realSize = size + "B";
		float fsize = div(size, 1024, 1);
		if (fsize > 0) {
			if (fsize < 1024) {
				realSize = fsize + "K";
			} else if (fsize >= 1024 && fsize < (1024 * 1024)) {
				if (fsize>=(1000*1024)) {
					realSize=(div(size, 1024 * 1024 * 1024, 1))+"G";
				}else {
					realSize = (div(size, 1048576, 1)) + "M";
				}
			} else if (fsize >= (1024 * 1024)) {
				realSize = (div(size, (1024 * 1024 * 1024), 1)) + "G";
			}
		}
		return realSize;
	}
//	大于100M就转换成G
	public static String getSurpOrTot(long size) {
		String realSize = size + "B";
		float fsize = div(size, 1024, 1);
		if (fsize > 0) {
			if (fsize < 1024) {
				realSize = fsize + "K";
			} else if (fsize >= 1024 && fsize < (1024 * 1024)) {
				if (fsize>=(100*1024)) {
					realSize=(div(size, 1024 * 1024 * 1024, 1))+"G";
				}else {
					realSize = (div(size, 1048576, 1)) + "M";
				}
			} else if (fsize >= (1024 * 1024)) {
				realSize = (div(size, (1024 * 1024 * 1024), 1)) + "G";
			}
		}
		return realSize;
	}
	public static String getFormatSize(long value) {
		StringBuilder size = new StringBuilder();
		float f = (float) value;
		if (value > 1023) {
			f = div(f, 1024, 1);
			if (f > 1023) {
				f = div(f, 1024, 1);
				if (f > 1023) {
					f = div(f, 1024, 1);
					size.append(f).append("G");
				} else {
					size.append(f).append("M");
				}
			} else {
				size.append(f).append("K");
			}
		} else {
			size.append(value).append('B');
		}
		return size.toString();
	}
	
	public static String[] getFormatValues(long value) {
		String unit = "M";
		StringBuilder size = new StringBuilder();
		float f = (float) value;
		if (value > 1023) {
			f = div(f, 1024, 1);
			if (f > 1023) {
				f = div(f, 1024, 1);
				if (f > 1023) {
					f = div(f, 1024, 1);
					size.append(f);
					unit = "G";
				} else {
					size.append(f);
					unit = "M";
				}
			} else {
				size.append(f);
				unit = "K";
			}
		} else if (value > 0) {
			size.append(value);
			unit = "B";
		} else {
			size.append('0');
		}
		return new String[] {size.toString(), unit};
	}

	/**
	 * 获取格式化后的金钱
	 * @param number
	 * @return
     */
	public static String getFormattedNumber(long number){
		if (number>=10000&&number<100000000){
			return div(number,10000,2)+"万元";
		}else if (number>=100000000){
			return div(number,100000000,1)+"亿元";
		}
		return number+"元";
	}
}
