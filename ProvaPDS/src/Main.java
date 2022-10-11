import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		Produto produto = new Produto();
		boolean loop = true;
		
		ArrayList<Produto> lista_produto = new ArrayList<>();
		lista_produto.add(new Produto(1,"Mouse", 50, 10));
		lista_produto.add(new Produto(2,"Mesa", 200, 10));
		lista_produto.add(new Produto(3,"Celular", 1000, 10));
		lista_produto.add(new Produto(4,"Cadeira", 300, 10));
		lista_produto.add(new Produto(5,"PC", 3000, 10));
		
		do {
			
			System.out.println("1. Cadastrar produto");
			System.out.println("2. Consultar produto");
			System.out.println("3. Vender");
			System.out.println("4. Consultar recibos");
			System.out.println("5. Sair");
			
			int opcao = Integer.parseInt(scanner.next());
			
			switch (opcao) {
			
			case 1: 
				
				System.out.println("Código Produto: ");
				produto.setCodigo(scanner.nextInt());
				System.out.println("Nome Produto: ");
				produto.setNome(scanner.next());
				System.out.println("Preço Produto: ");
				produto.setPreco(scanner.nextFloat());
				System.out.println("Estoque: ");
				produto.setEstoque(scanner.nextInt());
				
				lista_produto.add(produto);
				
				break;
				
				
			case 2:
				
				System.out.println("Qual o código do produto");
				produto.setCodigo(scanner.nextInt());
				
				for (int i=0; i < lista_produto.size(); i++) {
					if(lista_produto.get(i).codigo == produto.codigo) {
						System.out.println("-------------\n");
						System.out.println("Nome: " + lista_produto.get(i).getNome());
						System.out.println("Código: " + lista_produto.get(i).getCodigo());
						System.out.println("Preço: " + lista_produto.get(i).getPreco());
						System.out.println("Qtd. Estoque: " + lista_produto.get(i).getEstoque());
						System.out.println("\n-------------");
					}
				}
				
				break;
				
			case 3:
				System.out.println("Código do produto a ser vendido: ");
				produto.setCodigo(scanner.nextInt());
				System.out.println("Quantidade: ");
				int quantidade = scanner.nextInt();
				
				for (int i=0; i < lista_produto.size(); i++) {
					if(lista_produto.get(i).codigo == produto.codigo) {
						if(quantidade > lista_produto.get(i).getEstoque()) {
							System.out.println("Quantidade maior que estoque");
						}
						else {
							int novo_estoque = lista_produto.get(i).getEstoque() - quantidade;
							lista_produto.get(i).setEstoque(novo_estoque); 
							
							try {
								
								Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/prova", "root", "aluno");
								System.out.println("Conectado a base de dados com sucesso.");
								PreparedStatement ps = conexao.prepareStatement("insert into recibos values(?,?,?,?,?)");
								ps.setDate(1, getCurrentDate());
								ps.setString(2, lista_produto.get(i).getNome());
								ps.setInt(3, quantidade);
								ps.setFloat(4, lista_produto.get(i).getPreco());
								ps.setFloat(5, quantidade * lista_produto.get(i).getPreco());
								
								
								ps.executeUpdate();
								System.out.println("Venda Realizada!");
								
								} catch(Exception e) {
									System.out.println("Erro ao inserir:" + e);
								}
						}
					}
				}
				
				
				
				break;
					
				
				
			case 4:
				consultarRecibos();
				
				break;
				
				
			case 5:
				loop = false;
				break;
				
				default: 
					System.out.println("Digite um valor Correto!");
			}
			
			
			
		} while(loop);
		
	}
	
	private static java.sql.Date getCurrentDate(){
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}
	
	
	public static void consultarRecibos() {
		
		try {
		Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/prova", "root", "aluno");
		System.out.println("Conectado a base de dados com sucesso.");
		PreparedStatement ps;
		
	    ps = conexao.prepareStatement("select * from recibos");
	    ResultSet rs = ps.executeQuery();
	    
	    while (rs.next()) {
	      
	           Date data = 		rs.getDate("data");
	           String nome = 	rs.getString("nome");
	           int qtd =        rs.getInt("quantidade");
	           float preco = 	rs.getFloat("preco");
	           float total =    rs.getFloat("valorTotal");
	           
	           System.out.println("Data Venda: " + data +"\n" + "Produto: " + nome+"\n" + "Qtd. Vendida: " + qtd+"\n" + "Preço Unitário: " + preco+"\n" + "Valor Total: " + total+"\n");
	    }
	      
		}catch(Exception e) {
  	  
    	}
	}
	
}
