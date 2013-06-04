
public class Ray {

	public Ponto origem, direcao;
	int level;

	public Ray(Ponto origem, Ponto direcao, int level) {
		this.origem = origem;
		this.direcao = direcao;
		this.level = level;
	}
	
	public Vetor vetor() {
		return direcao.subtracao(origem);
	}

}
