import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProdutoDAO {

    private final String url = "jdbc:postgresql://localhost:5432/TI2-Ex3";
    private final String user = "postgres";
    private final String password = "Pedro";

    public void cadastrarProduto(String nome, double preco) {
        String SQL = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {
            
            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.executeUpdate();
            
            System.out.println("Produto cadastrado com sucesso!");
            
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar produto: " + ex.getMessage());
        }
    }
}
