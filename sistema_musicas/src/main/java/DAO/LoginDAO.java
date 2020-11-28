
package DAO;

import conexao.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import Models.Login;

public class LoginDAO {
    public static boolean existe (Login login) throws Exception{
        String sql = "SELECT * FROM tb_login WHERE usuario = ? AND senha = ?";
        try(Connection conexao = ConnectionFactory.obterConexao();
                PreparedStatement ps = conexao.prepareStatement(sql);){
            
            ps.setString(1, login.getUsuario());
            ps.setString(2, login.getSenha());
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        }
    }
    
    public void cadastro (Login l){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try{
            stmt = con.prepareStatement("INSERT INTO tb_login(usuario, senha) VALUES(?,?)");
            stmt.setString(1, l.getUsuario());
            stmt.setString(2, l.getSenha());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        } 
        
    }
}
