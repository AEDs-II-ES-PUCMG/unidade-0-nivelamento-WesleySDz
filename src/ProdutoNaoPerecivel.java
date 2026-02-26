public class ProdutoNaoPerecivel extends Produto {


    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro) {
        super(descricao, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto) {
        super(descricao, precoCusto);
    }

    // Não entendi a lógica de implementação desse override
    // Qual o sentido????
    @Override
    public double valorDeVenda() {
        return getPrecoCusto() * (1 + getMargemLucro());
    }

    @Override
    public String gerarDadosTexto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}