/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;
import student.db.DB;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_GeneralOperations implements GeneralOperations{
    
    private Connection conn;

    @Override
    public void eraseAll() {
        conn = DB.getInstance().getConnection();
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM TransportOffer");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM Package");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM Drive");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM Administrator");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM Courier");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM District");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM Vehicle");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM [User]");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try(
                PreparedStatement ps=conn.prepareStatement("DELETE FROM City");
                ){
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
