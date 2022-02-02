/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.DistrictOperations;
import student.db.DB;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_DistrictOperations implements DistrictOperations {
    
    private Connection conn;

    @Override
    public int insertDistrict(String name, int cityId, int xCord, int yCord) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO District (name, idCity, xCord, yCord) VALUES (?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                )
            {
               
                ps.setString(1, name);
                ps.setInt(2, cityId);
                ps.setInt(3, xCord);
                ps.setInt(4, yCord);
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
    public int deleteDistricts(String... names) {
        int x=0;
        for(String name:names){
            conn = DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("DELETE FROM District WHERE name=?");
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
    public boolean deleteDistrict(int idDistrict) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("DELETE FROM District WHERE idDistrict=?");
                )
            {
                ps.setInt(1, idDistrict);
                
                return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteAllDistrictsFromCity(String nameOfTheCity) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT idCity FROM City WHERE name=?");
                )
            {
                ps.setString(1, nameOfTheCity);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    int id = rs.getInt(1);
                    try(
                            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM District WHERE idCity=?");
                            ){
                        ps2.setInt(1,id);
                        return ps2.executeUpdate();
                    }catch (SQLException ex) {
                        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return 0;
                }
                else return 0;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idCity) {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT idDistrict FROM District WHERE idCity=?");
                )
            {
            ps.setInt(1,idCity);
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

    @Override
    public List<Integer> getAllDistricts() {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT idDistrict FROM District");
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
