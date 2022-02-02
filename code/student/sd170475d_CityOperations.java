/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

/**
 *
 * @author Danilo Stefanovic
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;
import student.db.DB;
public class sd170475d_CityOperations implements CityOperations{
    
    private Connection conn;

    @Override
    public int insertCity(String name, String postalCode) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO City (name, postalCode) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS);
                )
            {
               
                ps.setString(1, name);
                ps.setString(2, postalCode);
                int res = ps.executeUpdate();
                if(res==1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if(rs.next())return rs.getInt(1);
                    else return -1;
                }
                else{
                    
                    return -1;
                }
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int deleteCity(String... names) {
        int x=0;
        for(String name:names){
            conn = DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("DELETE FROM City WHERE name=?");
                 )
                {
                    ps.setString(1, name);
                    int res = ps.executeUpdate();
                    if(res==1) x++;
                   
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return x;
    }

    @Override
    public boolean deleteCity(int idCity) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("DELETE FROM City WHERE idCity=?");
                )
            {
                ps.setInt(1, idCity);
                
                return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT idCity FROM City");
                )
            {
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                l.add(rs.getInt(1));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
