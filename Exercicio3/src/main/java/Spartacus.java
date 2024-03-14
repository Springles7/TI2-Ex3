import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static spark.Spark.*;

public class Spartacus {
    public static void main(String[] args) {
        port(4567);

        get("/", (req, res) -> {
            return new FileInputStream("src/main/resources/index.html");
        });
        post("/cadastrar-produto", (req, res) -> {
            String nome = req.queryParams("nome");
            double preco = Double.parseDouble(req.queryParams("preco"));
            cadastrarProduto(nome, preco);
            
            return "Produto cadastrado com sucesso!";
        });
    }

    private static void cadastrarProduto(String nome, double preco) {
        String url = "jdbc:postgresql://localhost:5432/TI2-Ex3";
        String user = "postgres";
        String password = "Pedro";
        
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
