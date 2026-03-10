public class ProdutoNaoPerecivel extends Produto{
    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro){
        super(desc, precoCusto, margemLucro);
    }
    public ProdutoNaoPerecivel(String desc, double precoCusto){
        super(desc, precoCusto);
    }
    @Override public double valorVenda(){
        return precoCusto + (precoCusto * margemLucro);
    }
    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * @return Uma string no formato "1;descrição;preçoDeCusto;margemDeLucro;quantidadeEmEstoque"
    */
    @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format("%.2f", margemLucro).replace(",", ".");
        if (quantidadeEmEstoque > 0) {
            return String.format("1;%s;%s;%s;%d", descricao, precoFormatado, margemFormatada, quantidadeEmEstoque);
        }
        return String.format("1;%s;%s;%s", descricao, precoFormatado, margemFormatada);
    }
}
