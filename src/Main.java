import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;

import static java.lang.Math.*;

public class Main {

	public static String input, output;
	public static Ponto eye;
	public static double x[];
	public static double y[];
	public static int W, H;
	public static Cor background;
	public static double ambient;
	public static int path;
	public static double tonemapping;
	public static long seed = System.currentTimeMillis();
	public static Vector<Luz> luzes = new Vector<Luz>();
	public static Vector<Material> objeto = new Vector<Material>();

	public static void read() throws Exception {

		Scanner scanner = new Scanner(new File(input));

		while (scanner.hasNext()) {
			String linha = scanner.nextLine();
			if (linha.contains("#")) {
				continue;
			} else if (linha.contains("output")) {
				String split[] = linha.split(" ");
				output = split[1];				
			} else if (linha.contains("eye")) {
				String split[] = linha.split(" ");
				double x = Double.valueOf(split[1]);
				double y = Double.valueOf(split[2]);
				double z = Double.valueOf(split[3]);
				eye = new Ponto(x, y, z);
			} else if (linha.contains("ortho")) {
				String split[] = linha.split(" ");
				x = new double[] { Double.valueOf(split[1]), Double.valueOf(split[3]) };
				y = new double[] { Double.valueOf(split[2]), Double.valueOf(split[4]) };
			} else if (linha.contains("size")) {
				String split[] = linha.split(" ");
				W = Integer.valueOf(split[1]);
				H = Integer.valueOf(split[2]);
			} else if (linha.contains("background")) {
				String split[] = linha.split(" ");
				double r = Double.valueOf(split[1]);
				double g = Double.valueOf(split[2]);
				double b = Double.valueOf(split[3]);
				background = new Cor(r, g, b);
			} else if (linha.contains("ambient")) {
				String split[] = linha.split(" ");
				ambient = Double.valueOf(split[1]);
			} else if (linha.contains("npaths")) {
				String split[] = linha.split(" ");
				path = Integer.valueOf(split[1]);
			} else if (linha.contains("tonemapping")) {
				String split[] = linha.split(" ");
				tonemapping = Double.valueOf(split[1]);
			} else if (linha.contains("seed")) {
				String split[] = linha.split(" ");
				seed = Long.valueOf(split[1]);
			} else if (linha.contains("light")) {
				String split[] = linha.split(" ");
				String arquivo = split[1];
				double r = Double.valueOf(split[2]);
				double g = Double.valueOf(split[3]);
				double b = Double.valueOf(split[4]);
				double i = Double.valueOf(split[5]);
				Luz luz = new Luz(arquivo, new Cor(r, g, b), i);
				luzes.add(luz);
			} else if (linha.contains("objectquadric")) {
				String split[] = linha.split(" ");
				double A = Double.valueOf(split[1]);
				double B = Double.valueOf(split[2]);
				double C = Double.valueOf(split[3]);
				double D = Double.valueOf(split[4]);
				double E = Double.valueOf(split[5]);
				double F = Double.valueOf(split[6]);
				double G = Double.valueOf(split[7]);
				double H = Double.valueOf(split[8]);
				double J = Double.valueOf(split[9]);
				double K = Double.valueOf(split[10]);
				double r = Double.valueOf(split[11]);
				double g = Double.valueOf(split[12]);
				double b = Double.valueOf(split[13]);
				double ka = Double.valueOf(split[14]);
				double kd = Double.valueOf(split[15]);
				double ks = Double.valueOf(split[16]);
				double kt = Double.valueOf(split[17]);
				double n = Double.valueOf(split[18]);
				double ir = (split.length > 19 ? Double.valueOf(split[19]) : 0);
				ObjetoQuadrico obj = new ObjetoQuadrico(A, B, C, D, E, F, G, H, J, K, new Cor(r, g, b), ka, kd, ks, kt, n, ir);
				objeto.add(obj);
			} else if (linha.contains("object")) {
				String split[] = linha.split(" ");
				String arquivo = split[1];
				double r = Double.valueOf(split[2]);
				double g = Double.valueOf(split[3]);
				double b = Double.valueOf(split[4]);
				double ka = Double.valueOf(split[5]);
				double kd = Double.valueOf(split[6]);
				double ks = Double.valueOf(split[7]);
				double kt = Double.valueOf(split[8]);
				double n = Double.valueOf(split[9]);
				double ir = (split.length > 10 ? Double.valueOf(split[10]) : 0);
				Objeto obj = new Objeto(arquivo, new Cor(r, g, b), ka, kd, ks, kt, n, ir);
				objeto.add(obj);
			}
		}
	}
	
	public static Material root;
	public final static int REC = 3;

	public static Cor trace(Ray ray) {
		
		// end
		if(ray.level > REC) {
			return new Cor(0, 0, 0);
		}
		
		// Colisao com algum objeto ?
		double dist = Double.MAX_VALUE;
		Material material = null;
		Interseccao interseccao = null;
		int id = -1;
		
		for(int k = 0; k < objeto.size(); k++) {
			Interseccao local = objeto.get(k).interseccao(ray);
			if(local.P != null) {
				double Z = local.P.distancia(ray.origem);
				if(Util.cmp(Z,dist) < 0) {
					dist = Z;
					material = objeto.get(k);
					interseccao = local;
					id = k;
				}
			}
		}
		
		// colide em alguma coisa no level 0 ?
		if(root == null && ray.level == 0) {
			root = material;
		}
		
		// Colisao com a luz ?
		boolean eita = false;
		for(int k = 0; k < luzes.size(); k++) {
			Interseccao local = luzes.get(k).interseccao(ray);
			if(local.P != null) {
				double Z = local.P.distancia(ray.origem);
				if(Util.cmp(Z,dist) < 0) {
					dist = Z;
					material = luzes.get(k);
					eita = true;
				}
			}
		}
		
		if(eita) { // atinge luz
			return material.cor;
		}
		
		Cor cor = new Cor(0, 0, 0);
		
		if(material != null) {
			Cor phong = new Cor(0, 0, 0);
				
			// ambiente
			Cor ambiente = material.cor.multiplica(ambient * material.ka);
			phong = phong.adiciona(ambiente);
			
			for(int l = 0; l < luzes.size(); l++) {
				Luz luz = luzes.get(l);
				Ponto ponto = luz.pontoAleatorio();
				
				// material mais proximo
				Ray ray2 = new Ray(ponto,interseccao.P,-1);
				int indice = -1;
				dist = Double.MAX_VALUE;
				for(int k = 0; k < objeto.size(); k++) {
					Interseccao local = objeto.get(k).interseccao(ray2);
					if(local.P != null) {
						double Z = local.P.distancia(ponto);
						if(Util.cmp(Z,dist) < 0) {
							dist = Z;
							indice = k;					
						}
					}
				}
				
				if(id != indice) {
					continue;
				}
				
				// difusa
				Cor difusa = new Cor(0, 0, 0);
				Vetor L = ponto.subtracao(interseccao.P);
				Vetor N = interseccao.N;
				double alfa = L.angulo(N);
				if(Util.cmp(alfa, PI / 2) <= 0) {
					double fatorDifuso = material.kd * cos(alfa) * luz.intensidade;
					difusa = difusa.adiciona(material.cor.multiplica(fatorDifuso));
				}
				phong = phong.adiciona(difusa);
				
				// especular
				Cor especular = new Cor(0, 0, 0);
				Vetor V = ray.vetor().multiplica(-1);
				Vetor R = (N.multiplica(2 * N.norma() * L.norma() * cos(N.angulo(L)))).subtracao(L);
				double beta = V.angulo(R);
				if(Util.cmp(beta, PI / 2) <= 0) {
					double fatorEspecular = material.ks * pow(cos(beta), material.n);
					especular = especular.adiciona(luz.cor.multiplica(fatorEspecular));
				}
				phong = phong.adiciona(especular);
			}
			
			cor = cor.adiciona(phong);
			
			// raio secundario
			double ktot = material.kd + material.ks + material.kt;
			double r = Util.random.nextDouble() * ktot;
			
			if(Util.cmp(r,material.kd) <= 0) { // difuso
				double alfa = PI * Util.random.nextDouble(); // TODO
				double beta = 2.0 * PI * Util.random.nextDouble();
				Vetor direcao = new Vetor(sin(alfa) * cos(beta), sin(alfa) * sin(beta), cos(alfa));
				
				if(Util.cmp(direcao.angulo(interseccao.N), PI / 2) > 0) {
					direcao = direcao.multiplica(-1.0);
				}
				direcao = interseccao.P.adicao(direcao);
				Ray secundario = new Ray(interseccao.P,direcao,ray.level+1);
				cor = cor.adiciona( trace(secundario).multiplica(material.kd) );
			}
			else if(Util.cmp(r,material.kd + material.ks) <= 0) { // especular				
				Vetor V = ray.vetor().multiplica(-1.0);
				Vetor N = interseccao.N;
				Vetor R = (N.multiplica(2 * N.norma() * V.norma() * cos(N.angulo(V)))).subtracao(V);
				Ray secundario = new Ray(interseccao.P,interseccao.P.adicao(R),ray.level+1);
				cor = cor.adiciona( trace(secundario).multiplica(material.ks) );
			}
			else { // transmissao
			}
		}
		
		return cor;
	}

	
	public static void process() throws Exception {
		BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);

		double dx = (x[1] - x[0]) / W;
		double dy = (y[1] - y[0]) / H;

		int ct = 0;
		double tot = W * H / 100.0;
		
		double Y = y[0] + dy / 2;
		for (int i = 0; i < H; i++) {
			double X = x[0] + dx / 2;
			for (int j = 0; j < W; j++) {
				Cor cor = new Cor(0,0,0);
				for(int k = 0; k < path; k++) {
					double xx = (0.5 - Util.random.nextDouble()) * dx;
					double yy = (0.5 - Util.random.nextDouble()) * dy;
					
					Ray ray = new Ray(eye, new Ponto(X + xx,Y + yy,0),0);
					cor = cor.adiciona(trace(ray));
				}
				cor = cor.divide(path).toneMapping(tonemapping);
				if(root == null) {
					image.setRGB(j, H - 1 - i, background.RGB());
				}
				else {
					image.setRGB(j, H - 1 - i, cor.RGB());
				}
				X += dx;
				++ct;
				System.out.println( (ct/tot) + "%");
			}
			Y += dy;
		}
		
		output = new String("Output/bones300.png");
		
		File file = new File(output);
		ImageIO.write(image, "png", file);
	}

	public static void main(String[] args) throws Exception {

		input = new String("Input/config/cornellroom.sdl");
		//input = new String("Input/config/oneplanesphere.sdl");
		
		read();
		process();
	}
}

// ETAPA 1 : Desenhar os vertices, ligar com retas ^^ (OK)
// ETAPA 2 : Iniciar iluminacao, iniciar o phong

// //////////////////////////////////////////////////////////////////////
// ////// TESTES ////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////

// # Teste Luz
/*
 * Luz luz = new Luz("Input/luzcornell.obj");
 * 
 * for(int k = 0; k < (1<<20); k++) { Ponto p = luz.pontoAleatorio();
 * System.out.println(p); }
 */

// # Teste Desenhar/Salvar
/*
 * static String arq[] = { "back", "ceiling", "cube1", "cube2", "floor",
 * "leftwall", "luzcornell", "rightwall" }; Random random = new
 * Random(System.currentTimeMillis()); int N = arq.length; int cores[] = new
 * int[N]; Objeto objeto[] = new Objeto[N]; for(int i = 0; i < cores.length;
 * i++) { cores[i] = random.nextInt(); objeto[i] = new Objeto("Input/" + arq[i]
 * + ".obj"); }
 * 
 * Ponto eye = new Ponto(0.0, 0.0, 5.7);
 * 
 * double x0 = -1.0, y0 = -1.0; double x1 = +1.0, y1 = +1.0; int W = 200, H =
 * 200;
 * 
 * BufferedImage image = new BufferedImage(W,H,BufferedImage.TYPE_INT_RGB);
 * 
 * double dx = (x1 - x0) / W; double dy = (y1 - y0) / H;
 * double y = y0 + dy/2; for(int i = 0; i < H; i++) { double x = x0 + dx/2;
 * for(int j = 0; j < W; j++) { double dist = Double.MAX_VALUE; int cor = -1;
 * for(int k = 0; k < N; k++) { double nd = objeto[k].interseccao(eye, new
 * Ponto(x,y,0)); if( Util.cmp(nd,dist) < 0) { dist = nd; cor = cores[k]; } }
 * if( Util.cmp(dist, Double.MAX_VALUE) < 0 ) { image.setRGB(j, H - 1 - i, cor);
 * } x += dx; } y += dy; }
 * 
 * File file = new File("opa.png"); ImageIO.write(image, "png", file );
 */

// //////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////
