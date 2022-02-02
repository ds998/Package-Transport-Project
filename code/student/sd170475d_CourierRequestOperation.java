/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CourierRequestOperation;
import student.db.DB;
import student.help.CourierRequest;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_CourierRequestOperation implements CourierRequestOperation{
    
    private Connection conn;
    private LinkedList<CourierRequest> l=new LinkedList<>();

    @Override
    public boolean insertCourierRequest(String courierUserName, String licencePlateNumber) {
        for(int i =0;i<l.size();i++){
            if(l.get(i).courierUserName.equals(courierUserName)){
                return false;
            }
        }
        CourierRequest rc = new CourierRequest();
        rc.courierUserName=courierUserName;
        rc.licencePlateNumber=licencePlateNumber;
        l.add(rc);
        return true;
    }

    @Override
    public boolean deleteCourierRequest(String courierUserName) {
        int idx=-1;
        for(int i =0;i<l.size();i++){
            if(l.get(i).courierUserName.equals(courierUserName)){
                idx=i;
                break;
            }
        }
        if(idx==-1) return false;
        l.remove(idx);
        
        return true;
    }

    @Override
    public boolean changeVehicleInCourierRequest(String courierUserName, String licencePlateNumber) {
        for(int i =0;i<l.size();i++){
            if(l.get(i).courierUserName.equals(courierUserName)){
                l.get(i).licencePlateNumber=licencePlateNumber;
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getAllCourierRequests() {
        LinkedList<String> s=new LinkedList<>();
        for(int i =0;i<l.size();i++){
            s.add(l.get(i).courierUserName);
        }
        return s;
    }

    @Override
    public boolean grantRequest(String courierUserName) {
        conn=DB.getInstance().getConnection();
        CourierRequest cr=null;
        int idx=-1;
        for(int i =0;i<l.size();i++){
            if(l.get(i).courierUserName.equals(courierUserName)){
                cr=l.get(i);
                idx=i;
                break;
            }
        }
        if(cr==null) return false;
        try (
            CallableStatement cs = conn.prepareCall("{ call grantCourierRequest(?,?) };");
                )
            {
                cs.setString(1, courierUserName);
                cs.setString(2,cr.licencePlateNumber);
                cs.execute();
                l.remove(idx);
                return true;
                
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CourierRequestOperation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        
    }
    
}
