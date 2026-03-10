public class ItemDePedido {
	private Produto produto;
	private int quantidade;
	private double precoVenda;
	
	public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
		this.produto = produto;
		this.quantidade = quantidade;
		this.precoVenda = precoVenda;
	}
	
	public int getQuantidade() {
		return quantidade;
	}
	
	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
	
	public double getPrecoVenda() {
		return precoVenda;
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	@Override
	public String toString() {
		return produto.toString() + " - Quantidade: " + quantidade + " - Preço unitário: R$ " + String.format("%.2f", precoVenda);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		ItemDePedido item = (ItemDePedido) obj;
		return produto.equals(item.produto);
	}
}

