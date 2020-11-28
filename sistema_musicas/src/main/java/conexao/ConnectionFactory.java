
package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    private static final String usuario = "root";
    private static final String senha = "";
    private static final String host = "localhost";
    private static final String porta = "3306";
    private static final String db = "db_sistema_musicas";
    
    public static Connection obterConexao (){
        
        try{
           Connection c = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + porta + "/" + db + "?useTimezone=true&serverTimezone=UTC",
                usuario,
                senha
            ); 
            return c;
        }
        catch (SQLException e){
            throw new RuntimeException("Erro de conexão", e);           
        }
    }
    
    public static void fecharConexao (Connection con){
        
        try {
            if(con!=null){
                con.close();
            }                   
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public static void fecharConexao (Connection con, PreparedStatement stmt){
        
        fecharConexao(con);
        
        try {
             if(stmt != null){
                 stmt.close();
             }                     
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    public static void fecharConexao (Connection con, PreparedStatement stmt, ResultSet rs){
        
        fecharConexao(con, stmt);
        
        try {
             if(rs != null){
                 rs.close();
             }                     
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }

}
