
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {

    /** 
     * Para inclusão de novos produtos no vetor
     */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /**
     * Nome do arquivo de dados. O arquivo deve estar localizado na raiz do
     * projeto
     */
    static String nomeArquivoDados;

    /**
     * Scanner para leitura do teclado
     */
    static Scanner teclado;

    /**
     * Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos
     * a cada execução
     */
    static Produto[] produtosCadastrados;

    /**
     * Quantidade produtos cadastrados atualmente no vetor
     */
    static int quantosProdutos;

    /**
     * Gera um efeito de pausa na CLI. Espera por um enter para continuar
     */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /**
     * Cabeçalho principal da CLI do sistema
     */
    static void cabecalho() {
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /**
     * Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma
     * classe Menu.
     *
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo
     * no formato N (quantidade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em
     * caso de problemas com o arquivo.
     *
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de
     * problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        File arquivoDados = new File(nomeArquivoDados);


        // Configura um vetor padrão vazio para retornos de erro/arquivo novo
        Produto[] vetorVazio = new Produto[MAX_NOVOS_PRODUTOS];

        try {
            // Verifica se o arquivo existe. Se não existir, cria um novo arquivo vazio e retorna vetor vazio
            if (!arquivoDados.exists()) {
                System.out.println("Arquivo não encontrado. Criando novo arquivo vazio.");
                arquivoDados.createNewFile();
                quantosProdutos = 0; // Zera o contador global de produtos cadastrados
                return vetorVazio;
            }

            try (Scanner leitor = new Scanner(arquivoDados, Charset.forName("UTF-8"))) {

                // Verifica se o arquivo está vazio
                if (!leitor.hasNextLine()) {
                    System.out.println("Arquivo de dados vazio.");
                    quantosProdutos = 0;
                    return vetorVazio;
                }

                // Lê a quantidade
                int quantidadeProdutos = Integer.parseInt(leitor.nextLine().trim());
                quantosProdutos = quantidadeProdutos;   // Atualiza o contador global de produtos cadastrados

                Produto[] vetorProdutos = new Produto[quantidadeProdutos + MAX_NOVOS_PRODUTOS];

                for (int i = 0; i < quantidadeProdutos; i++) {
                    if (leitor.hasNextLine()) {
                        String linha = leitor.nextLine();
                        // Tratamento para substituir vírgula por ponto
                        linha = linha.replace(",", ".");
                        vetorProdutos[i] = Produto.criarDoTexto(linha);
                    }
                }

                return vetorProdutos;
            }

        } catch (IOException e) {
            System.err.println("Erro de Entrada/Saída: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Erro ao ler número no arquivo (formato inválido): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }

        // Se cair no catch, zera o contador e retorna vetor vazio
        quantosProdutos = 0;
        return vetorVazio;
    }

    /**
     * Lista todos os produtos cadastrados, numerados, um por linha
     */
    static void listarTodosOsProdutos() {
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null) {
                System.out.println(String.format("%02d - %s", (i + 1), produtosCadastrados[i].toString()));
            }
        }
    }

    /**
     * Localiza um produto no vetor de cadastrados, a partir do nome, e imprime
     * seus dados. A busca não é sensível ao caso. Em caso de não encontrar o
     * produto, imprime mensagem padrão
     */
    static void localizarProdutos() {
        System.out.print("Digite a descrição/nome do produto a ser localizado: ");
        String descProduto = teclado.nextLine().trim();
        boolean encontrado = false;

        for (Produto produto : produtosCadastrados) {
            if (produto != null && produto.getDescricao().equalsIgnoreCase(descProduto)) {
                System.out.println("Produto encontrado:");
                System.out.println(produto.toString());
                encontrado = true;
                break; // Para de procurar após encontrar o primeiro produto correspondente
            }
        }

        if (!encontrado) {
            System.out.println("Produto não encontrado.");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do
     * produto, lê os dados correspondentes, cria o objeto adequado de acordo
     * com o tipo, inclui no vetor. Este método pode ser feito com um nível
     * muito melhor de modularização. As diversas fases da lógica poderiam ser
     * encapsuladas em outros métodos. Uma sugestão de melhoria mais
     * significativa poderia ser o uso de padrão Factory Method para criação dos
     * objetos.
     */
    static void cadastrarProduto() {

        System.out.print("Digite a descrição do produto: ");
        String descricao = teclado.nextLine().trim();

        System.out.print("Digite o preço de custo do produto: ");
        double precoCusto = Double.parseDouble(teclado.nextLine().trim());

        System.out.print("Digite a margem de lucro do produto (em decimal, ex: 0.5 para 50%): ");
        double margemLucro = Double.parseDouble(teclado.nextLine().trim());

        System.out.print("O produto é perecível? (s/n): ");
        String respostaPerecivel = teclado.nextLine().trim().toLowerCase();

        System.out.print("Digite a quantidade: ");
        int quantidadeEmEsqtoque = Integer.parseInt(teclado.nextLine().trim());

        Produto novoProduto;
        if (respostaPerecivel.equals("s")) {
            System.out.print("Digite a data de validade do produto (dd/MM/yyyy): ");
            String dataValidadeStr = teclado.nextLine().trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataValidade = LocalDate.parse(dataValidadeStr, formatter);
            novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade, quantidadeEmEsqtoque);
        } else {
            novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro, quantidadeEmEsqtoque);
        }

        if (quantosProdutos < produtosCadastrados.length) {
            produtosCadastrados[quantosProdutos] = novoProduto;
            quantosProdutos++;
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Limite de produtos cadastrados atingido. Não é possível cadastrar mais produtos.");
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado.
     * Sobrescreve todo o conteúdo do arquivo.
     *
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        // Verifica se há produtos para salvar antes de tentar abrir o arquivo
        if (produtosCadastrados == null || quantosProdutos == 0) {
            System.out.println("Não há produtos cadastrados para salvar.");
            return;
        }

        cabecalho(); // Exibe o cabeçalho para indicar que o processo de salvamento está começando
        System.out.println("Salvando produtos no arquivo: " + nomeArquivo);
        System.out.println("Total de produtos a salvar: " + quantosProdutos);

        try (java.io.PrintWriter escritor = new java.io.PrintWriter(new java.io.File(nomeArquivo), "UTF-8")) {
            escritor.println(quantosProdutos); // Escreve a quantidade de produtos no início do arquivo
            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i] != null) { // Verifica se o produto não é nulo antes de tentar salvar
                    escritor.println(produtosCadastrados[i].gerarDadosTexto());
                }
            }

            escritor.flush(); // Garante que os dados sejam escritos no arquivo
            System.out.println("Produtos salvos com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + e.getMessage());
        }

    }

    public void realizarPedido(){
        System.out.print("Digite o id do produto a ser adicionado: ");
        int idSolicitado = Integer.parseInt(teclado.nextLine());
        Produto solicitacao = produtosCadastrados[idSolicitado];
        
        
    }
        // Verificar se o produto já está presente no carrinho
        



    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 ->
                    listarTodosOsProdutos();
                case 2 ->
                    localizarProdutos();
                case 3 ->
                    cadastrarProduto();
                // case 4 ->
                //     visualizarCarrinho();
                // case 5 ->
                //     realizarPedido();
            }
            pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
