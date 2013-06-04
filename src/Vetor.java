

public class Vetor extends Ponto {
	public Vetor() {
		super();
	}
	public Vetor(double x, double y, double z) {
		super(x,y,z);
	}
	
	public Vetor produtoVetorial(Vetor v) {
		return new Vetor(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x);
	}
	
	public double produtoInterno(Vetor v) {
		return (x * v.x + y * v.y + z * v.z);
	}
	
	public Vetor multiplica(double v) {
		return new Vetor(x * v, y * v, z * v);
	}
	
	public Vetor divide(double v) {
		return this.multiplica(1.0 / v);
	}
	
	public double norma() {
		return Math.sqrt( x * x + y * y + z * z );
	}
	
	public Vetor normaliza() {
		return this.divide(norma());
	}
	
	public double angulo(Vetor v) {
		Vetor w = this.subtracao(v);
		double a = w.norma(), b = this.norma(), c = v.norma();
		double val = (b*b + c*c - a*a) / (2*b*c);
		if(Util.cmp(val,1.0) > 0) {
			throw new RuntimeException();
		}
		return Math.acos(val);
	}
}
