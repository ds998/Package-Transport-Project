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
import rs.etf.sab.operations.VehicleOperations;
import student.db.DB;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_VehicleOperations implements VehicleOperations {
    
    private Connection conn;

    @Override
    public boolean insertVehicle(String licencePlateNumber, int fuelType, BigDecimal fuelConsumption) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Vehicle (licencePlateNumber, fuelType,fuelConsumption) VALUES (?,?,?)");
                )
            {
               
                ps.setString(1, licencePlateNumber);
                ps.setInt(2, fuelType);
                ps.setBigDecimal(3, fuelConsumption);
                
                return ps.executeUpdate()==1;
                
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public int deleteVehicles(String... licencePlateNumbers) {
        int x=0;
        for(String licencePlateNumber:licencePlateNumbers){
            conn = DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Vehicle WHERE licencePlateNumber=?");
                 )
                {
                    ps.setString(1, licencePlateNumber);
                    int res = ps.executeUpdate();
                    if(res==1) x++;
                   
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return x;
    }

    @Override
    public List<String> getAllVehichles() {
        conn = DB.getInstance().getConnection();
        LinkedList<String> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT licencePlateNumber FROM Vehicle");
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
    public boolean changeFuelType(String licencePlateNumber, int fuelType) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Vehicle WHERE licencePlateNumber=?");
                )
            {
               
                ps.setString(1, licencePlateNumber);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("UPDATE Vehicle SET fuelType=? WHERE licencePlateNumber=?");
                    )
                    {
                        
                        ps2.setInt(1,fuelType);
                        ps2.setString(2, licencePlateNumber);
                        
                        return ps2.executeUpdate()==1;
               
                
                
                    } catch (SQLException ex) {
                        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else return false;
                
                
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeConsumption(String licencePlateNumber, BigDecimal fuelConsumption) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Vehicle WHERE licencePlateNumber=?");
                )
            {
               
                ps.setString(1, licencePlateNumber);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("UPDATE Vehicle SET fuelConsumption=? WHERE licencePlateNumber=?");
                    )
                    {
                        
                        ps2.setBigDecimal(1,fuelConsumption);
                        ps2.setString(2, licencePlateNumber);
                        
                        return ps2.executeUpdate()==1;
               
                
                
                    } catch (SQLException ex) {
                        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else return false;
                
                
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
