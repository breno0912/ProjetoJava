/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.PreparedStatement;
import conexao.ConnectionFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Models.Genero;
import Models.GeneroPreferido;
import Models.Musica;

/**
 *
 * @author win
 */
public class MusicaDAO {
    
    public void create(Musica m){
        
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO tb_musicas(usuario, genero1, genero2, musica)VALUES(?,?,?,?)");
            stmt.setString(1,m.getUsuario());
            stmt.setString(2,m.getGenero1());
            stmt.setString(3,m.getGenero2());
            stmt.setString(4,m.getMusica());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Salvo com sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        } 
    }
    
    public void creategenero (Genero g){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO tb_genero(usuario, genero)VALUES(?,?)");
            stmt.setString(1,g.getUsuario());
            stmt.setString(2,g.getGenero());
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
    
    public void limpagenero(){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("DELETE a FROM tb_genero AS a, tb_genero AS b WHERE a.genero=b.genero AND a.id < b.id;");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
        
    }
    
    public void limpaGeneroPreferido(){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("DELETE a FROM tb_genero_preferido AS a, tb_genero_preferido AS b WHERE a.genero=b.genero AND a.usuario=b.usuario AND a.id < b.id;");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
    
    public List<Genero> readgen(){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Genero> generos = new ArrayList<>();
        
        try {
            stmt = con.prepareStatement("SELECT * FROM tb_genero");
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Genero novogenero = new Genero();
                novogenero.setGenero(rs.getString("genero"));
                generos.add(novogenero);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectionFactory.fecharConexao(con, stmt, rs);
        }
        return generos;
    }
    
        public List<Musica> readAvaliacao(String user){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Musica> avaliar = new ArrayList<>();
        
        try {
            stmt = con.prepareStatement("SELECT musica, nota FROM tb_musicas WHERE usuario = ? ORDER BY nota ASC");
            stmt.setString(1, user);
            rs = stmt.executeQuery();
            
            stmt = con.prepareStatement("DELETE a FROM tb_musicas AS a, tb_musicas AS b WHERE a.usuario=b.usuario AND a.genero1=b.genero1 AND a.genero2=b.genero2 AND a.musica=b.musica AND a.id > b.id;");
            stmt.executeUpdate();
            
            while(rs.next()){
                Musica proxAvaliar = new Musica();
                proxAvaliar.setMusica(rs.getString("musica"));
                proxAvaliar.setNota(rs.getDouble("nota"));
                avaliar.add(proxAvaliar);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectionFactory.fecharConexao(con, stmt, rs);
        }
        return avaliar;
    }
        
    public void insertNota(double nota, String musica, String user){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("set SQL_SAFE_UPDATES=0");
            stmt.execute();
            //update tb_musicas set nota = 4.0 where musica = "teste" and usuario = "admin";
            stmt = con.prepareStatement("update tb_musicas set nota = ? where musica = ? and usuario = ?");
            stmt.setDouble(1,nota);
            stmt.setString(2, musica);
            stmt.setString(3, user);
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao avaliar música: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
        
    public List<Musica> readRecomendadas(String gen, String user){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Musica> recomenda = new ArrayList<>();
        
        try {
            stmt = con.prepareStatement("SELECT musica, AVG(nota) as media_nota FROM tb_musicas WHERE usuario != ? AND genero1 = ? AND nota IS NOT NULL OR usuario != ? AND genero2 = ? AND nota IS NOT NULL GROUP BY musica ORDER BY media_nota DESC;");
            stmt.setString(1, user);
            stmt.setString(2, gen);
            stmt.setString(3, user);
            stmt.setString(4, gen);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                Musica musicaRecomenda = new Musica();
                musicaRecomenda.setMusica(rs.getString("musica"));
                musicaRecomenda.setNota(rs.getDouble("media_nota"));
                recomenda.add(musicaRecomenda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectionFactory.fecharConexao(con, stmt, rs);
        }
        return recomenda;
    }
    
    public void adcMusica(String user, String genero, String musica, double nota){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO tb_musicas(usuario, genero1, genero2, musica, nota)VALUES(?,?,?,?,?)");
            stmt.setString(1,user);
            stmt.setString(2,genero);
            stmt.setString(3,genero);
            stmt.setString(4,musica);
            stmt.setDouble(5,nota);
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
    
    
    public void createTbGenero(Genero g){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO tb_genero_preferido(usuario, genero)VALUES(?,?)");
            stmt.setString(1,g.getUsuario());
            stmt.setString(2,g.getGenero());
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
    
    public List<GeneroPreferido> readGenPref(String g){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<GeneroPreferido> genpref = new ArrayList<>();
        
        try {
            stmt = con.prepareStatement("SELECT * FROM tb_genero_preferido WHERE usuario = ?");
            stmt.setString(1, g);
            rs = stmt.executeQuery();
            
            while(rs.next()){
                GeneroPreferido ngenpref = new GeneroPreferido();
                ngenpref.setGenero(rs.getString("genero"));
                genpref.add(ngenpref);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MusicaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            ConnectionFactory.fecharConexao(con, stmt, rs);
        }
        return genpref;
    }
    
    public void deleteGeneroPref (String u, String g){
        Connection con = ConnectionFactory.obterConexao();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("DELETE FROM tb_genero_preferido WHERE usuario = ? AND genero = ?");
            stmt.setString(1,u);
            stmt.setString(2,g);
            stmt.executeUpdate();
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Deletar: "+ex);
        }finally{
            ConnectionFactory.fecharConexao(con, stmt);
        }
    }
    
}
