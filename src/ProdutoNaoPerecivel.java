public class ProdutoNaoPerecivel extends Produto {


    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro) {
        super(descricao, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto) {
        super(descricao, precoCusto);
    }

    // Não entendi a lógica de implementação desse override
    @Override
    public double valorDeVenda() {
        return getPrecoCusto() * (1 + getMargemLucro());
    }

    @Override
    public String gerarDadosTexto() {
        return "1;" + getDescricao() + ";" + getPrecoCusto() + ";" + getMargemLucro();
    }

}