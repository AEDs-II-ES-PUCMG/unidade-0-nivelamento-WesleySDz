public class ProdutoNaoPerecivel extends Produto {


    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro, int quantidadeEmEstoque) {
        super(descricao, precoCusto, margemLucro, quantidadeEmEstoque);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto, int quantidadeEmEstoque) {
        super(descricao, precoCusto, quantidadeEmEstoque);
    }

    // Não entendi a lógica de implementação desse override
    @Override
    public double valorDeVenda() {
        return getPrecoCusto() * (1 + getMargemLucro());
    }

    @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%.2f", getPrecoCusto()).replace(',', '.');
        String margemFormatada = String.format("%.2f", getMargemLucro()).replace(',', '.');
        return "1;" + getDescricao() + ";" + precoFormatado + ";" + margemFormatada;
    }

}