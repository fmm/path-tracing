
public class Luz extends Objeto {

	public double intensidade;
	
	public Luz(String arquivo, Cor cor, double i) throws Exception {
		super(arquivo, cor);
		this.intensidade = i;
	}
	
	public Ponto pontoAleatorio() {
		int face = Util.random.nextInt(faces.size());
		Ponto p[] = faces.get(face).p;
		
		double v0, v1, v2, tot;
		
		do {
			v0 = Util.random.nextDouble();
			v1 = Util.random.nextDouble();
			v2 = Util.random.nextDouble();
			tot = v0 + v1 + v2;
		} while( Util.cmp(tot) == 0);
		
		v0 /= tot;
		v1 /= tot;
		v2 /= tot;
		
		Vetor vet = new Vetor(0, 0, 0);
		vet = vet.adicao(p[0].multiplica(v0));
		vet = vet.adicao(p[1].multiplica(v1));
		vet = vet.adicao(p[2].multiplica(v2));
		
		return vet;
	}
}
