import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {
    private static double DESCONTO = 0.25;
    private static int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;
    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate data){
        super(desc, precoCusto, margemLucro);
        if (data.isAfter(LocalDate.now())){
            dataDeValidade = data;
        }else{
            throw new IllegalArgumentException("Data de validade inválida!!!");
        }
    }

    @Override public double valorVenda(){
        if (LocalDate.now().until(dataDeValidade).getDays() <= PRAZO_DESCONTO) {
            return (precoCusto + (precoCusto * margemLucro)) * (1 - DESCONTO);
        }else{
           return super.valorVenda();
        }
    }

    @Override public String toString(){
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataDeValidade.format(formatador);
        return super.toString().concat(" produto com data de validade em "+ dataFormatada);
    }

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * Data de validade vai no formato dd/mm/aaaa
    * @return Uma string no formato "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
    */
    @Override
    public String gerarDadosTexto() {
        String precoFormatado = String.format("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format("%.2f", margemLucro).replace(",", ".");
        DateTimeFormatter Formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = dataDeValidade.format(Formatador);
        return String.format("2;%s;%s;%s;%s", descricao, precoFormatado, margemFormatada, dataFormatada);
    }
}

