import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto{
    private static final double DESCONTO = 0.25; // 25% de desconto
    private static final int PRAZO_DESCONTO = 7; // 7 Dias
    private final LocalDate dataDeValidade;

    public ProdutoPerecivel(String descricao, double precoCusto, double margemLucro, LocalDate dataDeValidade, int quantidadeEmEstoque) {

        super(descricao, precoCusto, margemLucro, quantidadeEmEstoque);

        // Verificação se a data de validade é posterior a data atual
        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de validade deve ser posterior a data atual");
        }
        
        this.dataDeValidade = dataDeValidade;
    }

    // public ProdutoPerecivel(String descricao, double precoCusto, LocalDate dataDeValidade, int quantidadeEmEstoque) {
        
    //     super(descricao, precoCusto, quantidadeEmEstoque);

    //     // Verificação se a data de validade é posterior a data atual
    //     if (dataDeValidade.isBefore(LocalDate.now())) {
    //         throw new IllegalArgumentException("A data de validade deve ser posterior a data atual");
    //     }
        
    //     this.dataDeValidade = dataDeValidade;
    // }

    @Override
    public double valorDeVenda(){
        // Verificação se o produto está vencido e retorna um aviso
        if (dataDeValidade.isBefore(LocalDate.now())){
            throw new IllegalStateException("O produto está vencido");
        }
        
        // Desconto se o produto estiver perto de vencer
        if (dataDeValidade.isBefore(LocalDate.now().plusDays(PRAZO_DESCONTO))) {
            return (getPrecoCusto() - (getPrecoCusto() * DESCONTO)) * (1 + getMargemLucro());
        }

        return getPrecoCusto() * (1 + getMargemLucro());
    }
 
        @Override
        public String toString(){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return String.format("%s (Perecível) - Preço: R$ %.2f - Validade: %s - Estoque: %d", getDescricao(), valorDeVenda(), dataDeValidade.format(formatter), getQuantidadeEmEstoque());
        }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataValidadeFormatada = dataDeValidade.format(formatter);
        String precoFormatado = String.format("%.2f", getPrecoCusto()).replace(',', '.');
        String margemFormatada = String.format("%.2f", getMargemLucro()).replace(',', '.');
        return "2;" + getDescricao() + ";" + precoFormatado + ";" + margemFormatada + ";" + dataValidadeFormatada;
    }
}