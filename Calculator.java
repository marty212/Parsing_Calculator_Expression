import java.util.Stack;


public class Calculator {
	
	private static final String args[] = {"asind", "asinr", "asinh", "acosd", "acosr", "acosh", "atand", "atanr",
		"atanh", "log", "ln", "fact", "powten", "cuberoot", "sqrt", "reciproc", "powe", "sind", "sinr", "sinh", "cosd", "cosr",
		"cosh", "tand", "tanr", "tanh", "negate", "sqr"};
	
	public static double eval(String str)
	{
		String work = str.replaceAll("\\s", ""); //remove all spaces
		Stack<Integer> ops = new Stack<Integer>();
		for(int i = 0; i < work.length(); i++) //evaluate math inside ()
		{
			switch(work.charAt(i))
			{
			case('(') :
				ops.push(i);
				break;
			case(')') :
				int begin = ops.pop();
				String temp1 = work.substring(0, begin);
				String temp2 = work.substring(i + 1, work.length());
				work = temp1 + evalMath(work.substring(begin + 1, i)) + temp2;
				i = begin;
				break;
			}
		}
		return Double.parseDouble(String.format("%.15f", Double.parseDouble(evalMath(work))));
	}
	
	private static String evalMath(String str)
	{
		String work = str;
		for(int i = 0; i < args.length; i++)
		{
			while(work.contains(args[i]))
			{
				int pos  = work.indexOf(args[i]);
				int rightSide = findEndRight(work.substring(pos+args[i].length()), pos + args[i].length());
				work = work.substring(0, pos) + evalOp(i, Double.parseDouble(work.substring(pos + args[i].length(), rightSide)))
						+ work.substring(rightSide);
			}
		}
		while(work.contains("yroot"))
		{
			int pos = work.indexOf("yroot");
			int pos1 = findEndLeft(work.substring(0, pos));
			int pos2 = findEndRight(work.substring(pos+5), pos + 5);
			double result = Math.pow(Math.exp (1/Double.parseDouble(work.substring(pos + 5, pos2))),Math.log(Double.parseDouble(work.substring(pos1, pos))));
			work = work.substring(0, pos1) + result + work.substring(pos2);
		}
		while(work.contains("mod"))
		{
			int pos = work.indexOf("mod");
			int pos1 = findEndLeft(work.substring(0, pos));
			int pos2 = findEndRight(work.substring(pos+3), pos + 3);
			double result = Double.parseDouble(work.substring(pos1, pos)) % 
					Double.parseDouble(work.substring(pos + 3, pos2));
			work = work.substring(0, pos1) + result + work.substring(pos2);
		}
		while(work.contains("^"))
		{
			int pos = work.indexOf("^");
			int pos1 = findEndLeft(work.substring(0, pos));
			int pos2 = findEndRight(work.substring(pos+1), pos + 1);
			double result = Math.pow(Double.parseDouble(work.substring(pos1, pos)),
					Double.parseDouble(work.substring(pos+1, pos2)));
			work = work.substring(0, pos1) + result + work.substring(pos2);
		}
		while(work.contains("*") || work.contains("/"))
		{
			int pos  = 0;
			int mpos = work.indexOf("*");
			int dpos = work.indexOf("/");
			if(mpos >= dpos && dpos != -1 || mpos == -1)
			{
				pos = dpos;
			}
			else
			{
				pos = mpos;
			}
			int pos1 = findEndLeft(work.substring(0, pos));
			int pos2 = findEndRight(work.substring(pos+1), pos + 1);
			double result = 0.0;
			if(mpos != pos)
			{
				result =  Double.parseDouble(work.substring(pos1, pos)) / Double.parseDouble(work.substring(pos+1, pos2));
			}
			else
			{
				result =  Double.parseDouble(work.substring(pos1, pos)) * Double.parseDouble(work.substring(pos+1, pos2));
			}
			if(pos1 == 0)
			{
				work = result + work.substring(pos2);
			}
			else
			{
				work = work.substring(0, pos1+1) + result + work.substring(pos2);
			}		
		}
		while(work.contains("+") || (work.contains("-") && !work.contains("E-") && work.lastIndexOf("-") != 0))
		{
			int pos  = 0;
			int aPos = work.indexOf("+");
			int sPos = work.lastIndexOf("-");
			if(aPos >= sPos && sPos > 0 || aPos == -1)
			{
				pos = sPos + 1;
			}
			else
			{
				pos = aPos;
			}
			int pos1 = findEndLeft(work.substring(0, pos - 1));
			int pos2 = findEndRight(work.substring(pos+1), pos + 1);
			double result = 0.0;
			if(pos != aPos)
			{
				result = Double.parseDouble(work.substring(pos1, pos - 1)) - Double.parseDouble(work.substring(pos, pos2));
			}
			else
			{
				result = Double.parseDouble(work.substring(pos1, pos)) + Double.parseDouble(work.substring(pos+1, pos2));
			}
			work = work.substring(0, pos1) + result + work.substring(pos2);
		}
		return work;
	}
	
	private static int findEndLeft(String str)
	{
		int ret = 0;
		for(int i = str.length() - 1; i > 0; i--)
		{
			if(isOp(str.charAt(i)))
			{
				ret = i;
				break;
			}
		}
		return ret;
	}
	
	private static int findEndRight(String str, int offset)
	{
		int ret = str.length();
		for(int i = 1; i < str.length(); i++)
		{
			if(isOp(str.charAt(i)) && i - 1 >= 0 && str.charAt(i-1) != 'E')
			{
				ret = i;
				break;
			}
		}
		return offset + ret;
	}
	
	public static boolean isOp(char c)
	{
		switch(c)
		{
		case '-':
			return true;
		case '+':
			return true;
		case '*':
			return true;
		case '/':
			return true;
		case '^':
			return true;
		case 'm':
			return true;
		}
		return false;
	}
	
	private static double evalOp(int i, Double num)
	{
		switch(i)
		{
		case 17 : 
			return Math.sin(Math.toRadians(num));
		case 18 :
			return Math.sin(num);
		case 19 :
			return Math.sinh(num);
		case 20 : 
			return Math.cos(Math.toRadians(num));
		case 21 :
			return Math.cos(num);
		case 22 :
			return Math.cosh(num);
		case 23 : 
			return Math.tan(Math.toRadians(num));
		case 24 :
			return Math.tan(num);
		case 25 :
			return Math.tanh(num);
		case 9 :
			return Math.log10(num);
		case 10 :
			return Math.log(num);
		case 11 :
			return Math.exp(logGamma(num + 1));
		case 12 :
			return Math.pow(10, num);
		case 13 :
			return Math.cbrt(num);
		case 14 :
			return Math.sqrt(num);
		case 15 :
			return 1/num;
		case 16 :
			return Math.exp(num);
		case 0 :
			return Math.toDegrees(Math.asin(num));
		case 1:
			return Math.asin(num);
		case 2 :
			return Math.log(num + Math.sqrt(num*num + 1.0));
		case 3 :
			return Math.toDegrees(Math.acos(num));
		case 4:
			return Math.acos(num);
		case 5 :
			return Math.log(num + Math.sqrt(num*num - 1.0));
		case 6 :
			return Math.toDegrees(Math.atan(num));
		case 7:
			return Math.atan(num);
		case 8 :
			return  0.5*Math.log((num + 1.0)/(num - 1.0));
		case 26 :
			return -1 * num;
		case 27 :
			return Math.pow(num, 2);
		}
		return 0.0;
	}
	
	/**
	 * Taken from http://introcs.cs.princeton.edu/java/91float/Gamma.java.html
	 * Does some fancy math to get floating point factorials.
	 * @param x
	 * @return a number I need for factorial floating points
	 */
	static double logGamma(double x) {
	      double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
	      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
	                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
	                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
	      return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
	   }
	
	
	public static void main (String args[])
	{
		System.out.println(eval("acosr(cosr3.14)"));
		//System.out.println(eval("8yroot3"));
	}
}
