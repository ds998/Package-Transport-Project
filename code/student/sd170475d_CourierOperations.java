/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierOperations;
import student.db.DB;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_CourierOperations  implements CourierOperations{
    
    private Connection conn;

    @Override
    public boolean insertCourier(String courierUserName, String licencePlateNumber) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Courier (courierUserName, licencePlateNumber,statusOfCourier,numberOfDeliveries,profit) VALUES (?,?,?,?,?)");
                )
            {
               
                ps.setString(1, courierUserName);
                ps.setString(2, licencePlateNumber);
                ps.setInt(3,0);
                ps.setInt(4,0);
                ps.setBigDecimal(5,new BigDecimal(0));
                return ps.executeUpdate()==1;
                
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteCourier(String courierUserName) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Courier WHERE courierUserName=?");
                )
            {
                ps.setString(1, courierUserName);
                
                return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<String> getCouriersWithStatus(int statusOfCourier) {
        conn = DB.getInstance().getConnection();
        LinkedList<String> l = new LinkedList<String>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT courierUserName FROM Courier WHERE statusOfCourier=?");
                )
            {
            ps.setInt(1,statusOfCourier);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                l.add(rs.getString(1));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String> getAllCouriers() {
        conn = DB.getInstance().getConnection();
        LinkedList<String> l = new LinkedList<String>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT courierUserName FROM Courier ORDER BY profit DESC");
                )
            {
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                l.add(rs.getString(1));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int numberOfDeliveries) {
        BigDecimal x = new BigDecimal(0.0);
        int y=0;
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT profit FROM Courier WHERE numberOfDeliveries >= ?");
                )
            {
            ps.setInt(1, numberOfDeliveries);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                x=x.add(rs.getBigDecimal(1));
                y++;
            }
            return x.divide(new BigDecimal(y));
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
        
        
    }

    
    
}
