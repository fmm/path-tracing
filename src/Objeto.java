import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class Objeto extends Material {

	Vector<Ponto> vertices = new Vector<Ponto>();
	Vector<Face> faces = new Vector<Face>();

	public Objeto(String arquivo, Cor cor) throws Exception {
		this(arquivo, cor, 0, 0, 0, 0, 0, 0);
	}

	public Objeto(String arquivo, Cor cor, double ka, double kd, double ks, double kt, double n, double ir) throws Exception {

		super(cor, ka, kd, ks, kt, n, ir);

		File file = new File(arquivo);
		Scanner scanner = new Scanner(file);

		while (scanner.hasNext()) {
			String linha = scanner.nextLine();
			if (linha.contains("#")) {
				continue;
			} else if (linha.contains("v")) {
				String v[] = linha.split(" ");
				double x = Double.valueOf(v[1]);
				double y = Double.valueOf(v[2]);
				double z = Double.valueOf(v[3]);
				vertices.add(new Ponto(x, y, z));
			} else if (linha.contains("f")) {
				String f[] = linha.split(" ");
				Ponto p[] = new Ponto[3];
				int indice[] = new int[3];
				for (int i = 0; i < 3; i++) {
					indice[i] = Integer.valueOf(f[i + 1]) - 1;
					p[i] = vertices.get(indice[i]);
				}
				faces.add(new Face(p));
			}
		}
	}

	public Interseccao interseccao(Ray ray) {

		Vetor vet = ray.vetor();
		Ponto ponto = null;
		Vetor normal = null;
		double dist = Double.MAX_VALUE;

		for (int f = 0; f < faces.size(); f++) {
			Face face = faces.get(f);
			Ponto p[] = face.p;
			Vetor diretor = face.diretor;

			double A = (vet.x * diretor.x) + (vet.y * diretor.y) + (vet.z * diretor.z);
			double B = ((ray.origem.x - p[0].x) * diretor.x) + ((ray.origem.y - p[0].y) * diretor.y) + ((ray.origem.z - p[0].z) * diretor.z);

			if (Util.cmp(A) == 0) {
				continue;
			}

			double t = -B / A;

			if (Util.cmp(t) > 0) {
				Ponto q = new Ponto(ray.origem.x + vet.x * t, ray.origem.y + vet.y * t, ray.origem.z + vet.z * t);
				if (face.dentro(q)) {
					double ndist = q.distancia(ray.origem);
					if (Util.cmp(ndist, dist) < 0) {
						dist = ndist;
						ponto = q;
						normal = face.diretor;
						if (Util.cmp(normal.angulo(vet.multiplica(-1)), Math.PI / 2) > 0) {
							normal = normal.multiplica(-1);
						}
					}
				}
			}
		}
		
		return new Interseccao(ponto, normal);
	}

	/*
	 * public double interseccao(Ponto eye, Ponto pixel) { double dist =
	 * Double.MAX_VALUE;
	 * 
	 * Vetor vet = pixel.subtracao(eye);
	 * 
	 * for(int f = 0; f < faces.size(); f++) { Face face = faces.get(f); Ponto
	 * p[] = face.p; Vetor diretor = face.diretor;
	 * 
	 * double A = (vet.x diretor.x) + ( vet.y diretor.y) + (vet.z diretor.z);
	 * double B = ((eye.x - p[0].x) diretor.x) + ((eye.y - p[0].y) diretor.y) +
	 * ((eye.z - p[0].z) diretor.z);
	 * 
	 * if( Util.cmp(A) == 0 ) { throw new RuntimeException(); }
	 * 
	 * double t = -B / A;
	 * 
	 * if(Util.cmp(t) >= 0) { Ponto q = new Ponto(eye.x + vet.x t, eye.y + vet.y
	 * t, eye.z + vet.z t); if( faces.get(f).dentro(q) ) { dist = Math.min(dist,
	 * q.distancia(eye)); } } }
	 * 
	 * return dist; }
	 */
}
