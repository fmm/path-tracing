
public abstract class Material {

	public Cor cor;
	public double ka, kd, ks, kt;
	public double n, ir;
	
	public Material(Cor cor, double ka, double kd, double ks, double kt, double n, double ir) {
		this.cor = cor;
		this.ka = ka;
		this.kd = kd;
		this.ks = ks;
		this.kt = kt;
		this.n = n;
		this.ir = ir;
	}
	
	public abstract Interseccao interseccao(Ray ray);

}
