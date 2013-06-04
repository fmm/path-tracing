
public class Face {

	Ponto p[];
	Vetor diretor;
	
	public Face(Ponto p[]) {
		this.p = p;
		Vetor v1 = p[1].subtracao(p[0]), v2 = p[2].subtracao(p[0]);
		diretor = (v1.produtoVetorial(v2)).normaliza();		
	}
	
	public boolean dentro(Ponto q) {
		double A = Util.area(p[0],p[1],p[2]);
		double B = 0;
		for(int i = 0; i < 3; i++) {
			B += Util.area(q,p[i],p[(i+1)%3]);
		}
		return (Util.cmp(A, B) == 0);
	}
}
