import java.time.LocalDate;
import java.text.NumberFormat;

public class Pedido {
	private static final int MAX_PRODUTOS = 10;
	private static final double DESCONTO_PG_A_VISTA = 0.15;
	private ItemDePedido[] itens;
	private LocalDate dataPedido;
	private int quantItens;
	private int formaDePagamento;
	
	public Pedido(LocalDate dataPedido, int formaDePagamento) {
		this.dataPedido = dataPedido;
		this.formaDePagamento = formaDePagamento;
		this.itens = new ItemDePedido[MAX_PRODUTOS];
		this.quantItens = 0;
	}
	
	public boolean incluirProduto(Produto novo, int quantidade, double precoVenda) {
		if (quantItens >= MAX_PRODUTOS) {
			return false;
		}
		itens[quantItens] = new ItemDePedido(novo, quantidade, precoVenda);
		quantItens++;
		return true;
	}
	
	public double valorFinal() {
		double total = 0.0;
		for (int i = 0; i < quantItens; i++) {
			if (itens[i] != null) {
				total += itens[i].getPrecoVenda() * itens[i].getQuantidade();
			}
		}
		if (formaDePagamento == 1) {
			total = total * (1 - DESCONTO_PG_A_VISTA);
		}
		return total;
	}
	
	@Override
	public String toString() {
		NumberFormat moeda = NumberFormat.getCurrencyInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("Pedido de ").append(dataPedido.toString()).append("\n");
		sb.append("Forma de pagamento: ").append(formaDePagamento == 1 ? "À vista" : "A prazo").append("\n");
		sb.append("Itens:\n");
		for (int i = 0; i < quantItens; i++) {
			if (itens[i] != null) {
				sb.append("  - ").append(itens[i].toString()).append("\n");
			}
		}
		sb.append("Valor final: ").append(moeda.format(valorFinal()));
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Pedido pedido = (Pedido) obj;
		return dataPedido.equals(pedido.dataPedido) && formaDePagamento == pedido.formaDePagamento;
	}
	
	public ItemDePedido[] getItens() {
		return itens;
	}
	
	public int getQuantItens() {
		return quantItens;
	}
	
	public LocalDate getDataPedido() {
		return dataPedido;
	}
	
	public int getFormaDePagamento() {
		return formaDePagamento;
	}
	
	public ItemDePedido buscarItemPorProduto(Produto produto) {
		for (int i = 0; i < quantItens; i++) {
			if (itens[i] != null && itens[i].getProduto().equals(produto)) {
				return itens[i];
			}
		}
		return null;
	}
}

