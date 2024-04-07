import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

public class Spartacus {
    public static void main(String[] args) {
        port(4567);

       
        get("/", (req, res) -> {
            List<String> produtos = obterProdutosDoBanco();
            StringBuilder produtosHTML = new StringBuilder();
            for (String produto : produtos) {
                produtosHTML.append("<li>").append(produto).append("</li>");
            }
            String htmlContent = new String(new FileInputStream("src/main/resources/index.html").readAllBytes());
            htmlContent = htmlContent.replace("<!-- Os produtos serão adicionados aqui -->", produtosHTML.toString());
            return htmlContent;
        });

        
        post("/cadastrar-produto", (req, res) -> {
            String nome = req.queryParams("nome");
            double preco = Double.parseDouble(req.queryParams("preco"));
            cadastrarProdutoNoBanco(nome, preco);
            res.redirect("/");
            return null;
        });
    }

    
    private static List<String> obterProdutosDoBanco() {
        List<String> produtos = new ArrayList<>();
        String url = "jdbc:postgresql://localhost:5432/TI2-Ex3";
        String user = "postgres";
        String password = "Pedro";
        String SQL = "SELECT nome, preco FROM produtos";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                produtos.add(nome + " - R$" + preco);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao obter produtos do banco de dados: " + ex.getMessage());
        }

        return produtos;
    }

   
    private static void cadastrarProdutoNoBanco(String nome, double preco) {
        String url = "jdbc:postgresql://localhost:5432/TI2-Ex3";
        String user = "postgres";
        String password = "Pedro";
        String SQL = "INSERT INTO produtos (nome, preco) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, nome);
            pstmt.setDouble(2, preco);
            pstmt.executeUpdate();

            System.out.println("Produto cadastrado com sucesso no banco de dados!");

        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar produto no banco de dados: " + ex.getMessage());
        }
    }
}
