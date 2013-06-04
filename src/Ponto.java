

public class Ponto implements Comparable<Ponto> {
	
	double x, y, z;

	public Ponto() {
		x = y = z = 0.0;
	}

	public Ponto(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vetor adicao(Ponto p) {
		return new Vetor(x + p.x, y + p.y, z + p.z);
	}

	public Vetor subtracao(Ponto p) {
		return new Vetor(x - p.x, y - p.y, z - p.z);
	}

	public Ponto multiplica(double val) {
		return new Ponto(x * val, y * val, z * val);
	}
	
	public Ponto divide(double val) {
		return this.multiplica(1.0 / val);
	}
	
	public double distancia(Ponto p) {
		return Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2)
				+ Math.pow(z - p.z, 2));
	}
	
	
	public String toString() {
		return "(" + x + "," + y + "," + z + ")";
	}

	public int compareTo(Ponto p) {
		int cmpX = Util.cmp(x, p.x);
		if(cmpX != 0) {
			return cmpX;
		}
		int cmpY = Util.cmp(y, p.y);
		if(cmpY != 0) {
			return cmpY;
		}
		int cmpZ = Util.cmp(z, p.z);
		if(cmpZ != 0) {
			return cmpZ;
		}
		return 0;
	}
	
}
