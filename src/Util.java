import static java.util.Arrays.deepToString;

import java.util.Random;


public class Util {

	public static final double EPS = 1e-12;
	public static Random random =  new Random(System.currentTimeMillis());
	
	public static int cmp(double x, double y) {
		if( Math.abs(x-y) <= EPS) {
			return 0;
		}
		else {
			return (x < y ? -1 : +1);
		}
	}
	
	public static int cmp(double x) {
		return cmp(x, 0.0);
	}
	
	public static double area(Ponto a, Ponto b, Ponto c) {
		double det = 0;
		det += (a.x * b.y * c.z) + (b.x * c.y * a.z) + (c.x * a.y * b.z);
		det -= (c.x * b.y * a.z) + (a.x * c.y * b.z) + (b.x * a.y * c.z);
		return Math.abs(det);
	}
	
	static void debug(Object...os) {
		System.err.println(deepToString(os));
  	}
}
