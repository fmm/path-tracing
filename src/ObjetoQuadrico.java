public class ObjetoQuadrico extends Material {

	double A, B, C, D, E, F, G, H, J, K;

	public ObjetoQuadrico(double a, double b, double c, double d, double e, double f, double g, double h, double j, double k, Cor cor, double ka, double kd, double ks, double kt, double n, double ir) {
		super(cor, ka, kd, ks, kt, n, ir);
		A = a;
		B = b;
		C = c;
		D = d;
		E = e;
		F = f;
		G = g;
		H = h;
		J = j;
		K = k;
	}

	public Interseccao interseccao(Ray ray) {

		Vetor vet = ray.vetor();
		
		double acoef = 2 * F * vet.x * vet.z + 2 * E * vet.y * vet.z + C * vet.z * vet.z + B * vet.y * vet.y + A * vet.x * vet.x + 2 * D * vet.x * vet.y;
		double bcoef = 2 * B * ray.origem.y * vet.y + 2 * A * ray.origem.x * vet.x + 2 * C * ray.origem.z * vet.z + 2 * H * vet.y + 2 * G * vet.x + 2 * J * vet.z + 2 * D * ray.origem.x * vet.y + 2 * E * ray.origem.y * vet.z + 2 * E * ray.origem.z * vet.y + 2 * D * ray.origem.y * vet.x + 2 * F * ray.origem.x * vet.z + 2 * F * ray.origem.z * vet.x;
		double ccoef = A * ray.origem.x * ray.origem.x + 2 * G * ray.origem.x + 2 * F * ray.origem.x * ray.origem.z + B * ray.origem.y * ray.origem.y + 2 * E * ray.origem.y * ray.origem.z + 2 * D * ray.origem.x * ray.origem.y + C * ray.origem.z * ray.origem.z + 2 * H * ray.origem.y + 2 * J * ray.origem.z + K;
		double t = 0.0;
		
		if (Util.cmp(acoef) == 0) {
			if (Util.cmp(bcoef) == 0) {
				t = -1.0;
			} else {
				t = (-ccoef) / (bcoef);
			}
		} else {
			double disc = bcoef * bcoef - 4 * acoef * ccoef;
			if (Util.cmp(disc) < 0) {
				t = -1.0;
			} else {
				double root = Math.sqrt(disc);
				t = (-bcoef - root) / (acoef * 2);
				if (Util.cmp(t) < 0) {
					t = (-bcoef + root) / (acoef * 2);
				}
			}
		}

		Ponto ponto = null;
		Vetor normal = null;
		
		if (Util.cmp(t) > 0) {
			ponto = vet.multiplica(t).adicao(ray.origem);
			double vx = 2 * A * ponto.x + 2 * D * ponto.y + 2 * F * ponto.z + 2 * G;
			double vy = 2 * B * ponto.y + 2 * D * ponto.x + 2 * E * ponto.z + 2 * H;
			double vz = 2 * C * ponto.z + 2 * E * ponto.y + 2 * F * ponto.x + 2 * J;
			normal = new Vetor(vx, vy, vz);
			if( Util.cmp(normal.angulo(vet.multiplica(-1)), Math.PI / 2) > 0 ) {
				normal = normal.multiplica(-1);
			}
			normal = normal.normaliza();		
		}
		
		return new Interseccao(ponto, normal);
	}
}
