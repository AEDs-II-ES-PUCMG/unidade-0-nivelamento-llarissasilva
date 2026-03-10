import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {
    static final int MAX_NOVOS_PRODUTOS = 10;
    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos;
    static Pedido[] pedidosCadastrados;
    static int quantosPedidos;
    static Pedido pedidoAtual;

    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    static int menu(){
        cabecalho();
        System.out.println("1 - Listar produtos\n2 - Procurar produto\n3 - Cadastrar produto\n4 - Criar pedido\n5 - Adicionar ao pedido\n6 - Finalizar pedido\n7 - Listar pedidos\n0 - Sair");
        System.out.print("Opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Produto[] produtos = new Produto[MAX_NOVOS_PRODUTOS];
        Scanner arquivo = null;
        try{
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            int numProdutos = Integer.parseInt(arquivo.nextLine());
            for (int i=0; i<numProdutos && i<MAX_NOVOS_PRODUTOS; i++) {
                produtos[i] = Produto.criarDoTexto(arquivo.nextLine());
            }
            quantosProdutos = numProdutos < MAX_NOVOS_PRODUTOS ? numProdutos : MAX_NOVOS_PRODUTOS;
        } catch (IOException e){ 
            quantosProdutos = 0;
        } finally{
            if(arquivo != null) arquivo.close();
        }
        return produtos;
    }

    static void listarTodosOsProdutos(){
        cabecalho();
        System.out.println("\nPRODUTOS:");
        for (int i = 0; i < quantosProdutos; i++) {
            if(produtosCadastrados[i]!=null)
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
        }
    }

    static void localizarProdutos(){
        cabecalho();
        System.out.print("\nNome do produto: ");
        String nome = teclado.nextLine().toLowerCase();
        for (int i = 0; i < quantosProdutos; i++) {
            if(produtosCadastrados[i] != null && produtosCadastrados[i].toString().toLowerCase().contains(nome)){
                System.out.println(String.format("%02d - %s", (i+1),produtosCadastrados[i].toString()));
                return;
            }
        }
        System.out.println("Produto não encontrado!");
    }

    static void cadastrarProduto(){
        cabecalho();
        System.out.print("\nTipo (1-Perecível 2-Não Perecível): ");
        int tipo = Integer.parseInt(teclado.nextLine());
        System.out.print("Nome: ");
        String nome = teclado.nextLine();
        System.out.print("Preço: ");
        double preco = Double.parseDouble(teclado.nextLine());
        System.out.print("Margem: ");
        double margem = Double.parseDouble(teclado.nextLine());
        System.out.print("Estoque: ");
        int estoque = Integer.parseInt(teclado.nextLine());

        Produto produto;
        if (tipo == 2) {
            produto = new ProdutoNaoPerecivel(nome, preco, margem);
        } else {
            System.out.print("Data validade (dd/MM/yyyy): ");
            produto = new ProdutoPerecivel(nome, preco, margem, LocalDate.parse(teclado.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        produto.quantidadeEmEstoque = estoque;

        if (quantosProdutos < produtosCadastrados.length) {
            produtosCadastrados[quantosProdutos++] = produto;
            System.out.println("Produto cadastrado!");
        } else {
            System.out.println("Sem espaço!");
        }
    }

    static void salvarProdutos(String nomeArquivo){
        try{
            FileWriter arquivo = new FileWriter(nomeArquivo, Charset.forName("UTF-8"));
            arquivo.append(quantosProdutos + "\n");
            for (int i = 0; i < quantosProdutos; i++) {
                arquivo.append(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }
            arquivo.close();
            System.out.println("Arquivo salvo!");
        }catch (IOException e) {
            System.out.println("Erro ao salvar!");
        }
    }
    
    static void criarNovoPedido() {
        cabecalho();
        System.out.print("\nData (dd/MM/yyyy): ");
        LocalDate data = LocalDate.parse(teclado.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        System.out.print("Pagamento (1-À vista 2-A prazo): ");
        pedidoAtual = new Pedido(data, Integer.parseInt(teclado.nextLine()));
        System.out.println("Pedido criado!");
    }
    
    static void adicionarProdutoAoPedido() {
        if (pedidoAtual == null) {
            System.out.println("Crie um pedido primeiro!");
            return;
        }
        cabecalho();
        listarTodosOsProdutos();
        System.out.print("\nNúmero do produto: ");
        int num = Integer.parseInt(teclado.nextLine()) - 1;
        if (num < 0 || num >= quantosProdutos || produtosCadastrados[num] == null) {
            System.out.println("Produto inválido!");
            return;
        }
        
        Produto produto = produtosCadastrados[num];
        System.out.print("Quantidade: ");
        int qtd = Integer.parseInt(teclado.nextLine());
        if (qtd > 0 && qtd <= produto.getQuantidade()) {
            if (pedidoAtual.incluirProduto(produto, qtd, produto.valorVenda())) {
                produto.baixarEstoque(qtd);
                System.out.println("Adicionado!");
            } else {
                System.out.println("Pedido cheio!");
            }
        } else {
            System.out.println("Quantidade inválida ou estoque insuficiente!");
        }
    }
    
    static void finalizarPedido() {
        if (pedidoAtual == null || pedidoAtual.getQuantItens() == 0) {
            System.out.println("Pedido vazio!");
            return;
        }
        cabecalho();
        System.out.println("\nPEDIDO FINALIZADO:\n" + pedidoAtual.toString());
        if (quantosPedidos < pedidosCadastrados.length) {
            pedidosCadastrados[quantosPedidos++] = pedidoAtual;
            pedidoAtual = null;
            System.out.println("\nPedido salvo!");
        }
    }
    
    static void listarPedidos() {
        cabecalho();
        System.out.println("\nPEDIDOS:");
        if (quantosPedidos == 0) {
            System.out.println("Nenhum pedido.");
        } else {
            for (int i = 0; i < quantosPedidos; i++) {
                System.out.println("\n--- Pedido " + (i + 1) + " ---\n" + pedidosCadastrados[i].toString());
            }
        }
    }
    
    static void salvarPedidos(String nomeArquivo) {
        try {
            FileWriter arquivo = new FileWriter(nomeArquivo, Charset.forName("UTF-8"));
            arquivo.append(quantosPedidos + "\n");
            for (int i = 0; i < quantosPedidos; i++) {
                Pedido p = pedidosCadastrados[i];
                arquivo.append(p.getDataPedido().toString() + ";" + p.getFormaDePagamento() + "\n");
                arquivo.append(p.getQuantItens() + "\n");
                ItemDePedido[] itens = p.getItens();
                for (int j = 0; j < p.getQuantItens(); j++) {
                    if (itens[j] != null) {
                        arquivo.append(itens[j].getProduto().getDescricao() + ";" + itens[j].getQuantidade() + ";" + String.format("%.2f", itens[j].getPrecoVenda()).replace(",", ".") + "\n");
                    }
                }
            }
            arquivo.close();
            System.out.println("Pedidos salvos!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar pedidos!");
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        pedidosCadastrados = new Pedido[100];
        quantosPedidos = 0;
        pedidoAtual = null;
        
        int opcao;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
                case 4 -> criarNovoPedido();
                case 5 -> adicionarProdutoAoPedido();
                case 6 -> finalizarPedido();
                case 7 -> listarPedidos();
            }
            if (opcao != 0) pausa();
        }while(opcao !=0);       

        salvarProdutos(nomeArquivoDados);
        salvarPedidos("pedidos.csv");
        teclado.close();    
    }
}