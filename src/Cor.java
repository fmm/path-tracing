
public class Cor {
	
	public double r, g, b;
	
	public Cor() {
		this(0, 0, 0);
	}
	
	public Cor(double r, double g, double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Cor adiciona(Cor cor) {
		return new Cor(r + cor.r, g + cor.g, b + cor.b);
	}
	
	public Cor multiplica(double val) {
		return new Cor(r * val, g * val, b * val);
	}
	
	public Cor multiplica(Cor c) {
		return new Cor(r * c.r, g * c.g, b * c.b);
	}
	
	public Cor divide(double val) {
		return this.multiplica(1.0 / val);
	}
	
	public Cor toneMapping(double tm) {
		return new Cor( r / (r + tm), g / (g + tm), b / (b + tm) );
	}
	
	public int RGB() {		
		int R = (int)(r * 255) & 255;
		int G = (int)(g * 255) & 255;
		int B = (int)(b * 255) & 255;		
		return (R << 16) | (G << 8) | (B << 0);
	}

}
