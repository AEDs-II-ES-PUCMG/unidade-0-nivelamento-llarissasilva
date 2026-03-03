import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
     * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
     * N  (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] vetorProdutos = new Produto[0];
        try {
            File arquivo = new File(nomeArquivoDados);
            if (!arquivo.exists()) {
                quantosProdutos = 0;
                return new Produto[MAX_NOVOS_PRODUTOS];
            }
            
            Scanner leitorArquivo = new Scanner(arquivo, Charset.forName("ISO-8859-2"));
            int quantidade = Integer.parseInt(leitorArquivo.nextLine().trim());
            vetorProdutos = new Produto[quantidade + MAX_NOVOS_PRODUTOS];
            
            for (int i = 0; i < quantidade; i++) {
                String linha = leitorArquivo.nextLine();
                vetorProdutos[i] = Produto.criarDoTexto(linha);
            }
            
            quantosProdutos = quantidade;
            leitorArquivo.close();
        } catch (FileNotFoundException e) {
            quantosProdutos = 0;
            return new Produto[MAX_NOVOS_PRODUTOS];
        } catch (Exception e) {
            quantosProdutos = 0;
            return new Produto[MAX_NOVOS_PRODUTOS];
        }
        return vetorProdutos;
    }

    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS CADASTRADOS:");
        for (int i = 0; i < quantosProdutos; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    /** Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus dados. 
     *  A busca não é sensível ao caso.  Em caso de não encontrar o produto, imprime mensagem padrão */
    static void localizarProdutos(){
        cabecalho();
        System.out.print("\nDigite o nome do produto a procurar: ");
        String nomeProcurado = teclado.nextLine();
        
        Produto produtoProcurado = new ProdutoNaoPerecivel(nomeProcurado, 1.0, 0.1);
        boolean encontrado = false;
        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i] != null && produtosCadastrados[i].equals(produtoProcurado)) {
                System.out.println("\nPRODUTO ENCONTRADO:");
                System.out.println(produtosCadastrados[i].toString());
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            System.out.println("\nProduto não encontrado!");
        }
    }

    /**
     * Rotina de cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui no vetor. Este método pode ser feito com um nível muito 
     * melhor de modularização. As diversas fases da lógica poderiam ser encapsuladas em outros métodos. 
     * Uma sugestão de melhoria mais significativa poderia ser o uso de padrão Factory Method para criação dos objetos.
     */
    static void cadastrarProduto(){
        cabecalho();
        System.out.println("\nCADASTRO DE NOVO PRODUTO");
        System.out.println("1 - Produto Não Perecível");
        System.out.println("2 - Produto Perecível");
        System.out.print("Digite o tipo: ");
        int tipo = Integer.parseInt(teclado.nextLine());
        
        System.out.print("Digite a descrição: ");
        String descricao = teclado.nextLine();
        
        System.out.print("Digite o preço de custo: ");
        double precoCusto = Double.parseDouble(teclado.nextLine());
        
        System.out.print("Digite a margem de lucro: ");
        double margemLucro = Double.parseDouble(teclado.nextLine());
        
        try {
            if (tipo == 1) {
                produtosCadastrados[quantosProdutos] = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
                quantosProdutos++;
                System.out.println("\nProduto não perecível cadastrado com sucesso!");
            } else if (tipo == 2) {
                System.out.print("Digite a data de validade (dd/mm/aaaa): ");
                String dataStr = teclado.nextLine();
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dataValidade = LocalDate.parse(dataStr, formato);
                produtosCadastrados[quantosProdutos] = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
                quantosProdutos++;
                System.out.println("\nProduto perecível cadastrado com sucesso!");
            } else {
                System.out.println("\nTipo inválido!");
            }
        } catch (Exception e) {
            System.out.println("\nErro ao cadastrar produto: " + e.getMessage());
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo){
        try {
            FileWriter escritor = new FileWriter(nomeArquivo, Charset.forName("ISO-8859-2"));
            escritor.write(quantosProdutos + "\n");
            for (int i = 0; i < quantosProdutos; i++) {
                if (produtosCadastrados[i] != null) {
                    escritor.write(produtosCadastrados[i].gerarDadosTexto() + "\n");
                }
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
