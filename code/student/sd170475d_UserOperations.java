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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.UserOperations;
import student.db.DB;

/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_UserOperations implements UserOperations {
    
    private Connection conn;

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        boolean fn = Character.isUpperCase(firstName.charAt(0));
        boolean ln = Character.isUpperCase(lastName.charAt(0));
        boolean p =false;
        int n_num=0;
        int l_num=0;
        if(password.length()>=8){
            for(int i=0;i<password.length();i++){
                if(Character.isDigit(password.charAt(i))) n_num++;
                if(Character.isLetter(password.charAt(i))) l_num++;
                if(l_num>=1 && n_num>=1){
                    p=true;
                    break;
                }
            }
        }
        
        if(fn && ln && p){
            conn=DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("INSERT INTO [User] (userName, firstName, lastName, password,numberOfSentPackages) VALUES (?,?,?,?,?)");
                )
            {
               
                ps.setString(1, userName);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, password);
                ps.setInt(5,0);
                return ps.executeUpdate()==1;
                
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return false;
    }

    @Override
    public int declareAdmin(String userName) {
        
        conn=DB.getInstance().getConnection();
        try (
                PreparedStatement ps = conn.prepareStatement("SELECT userName from [User] WHERE userName=?");
                )
            {
               
                ps.setString(1, userName);
                
                ResultSet rs=ps.executeQuery();
                if(rs.next()){
                    
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("SELECT * FROM Administrator WHERE adminName=?");
                    )
                    {
               
                        ps2.setString(1, userName);
                        ResultSet rs2 = ps2.executeQuery();
                        if(!rs2.next()){
                            try (
                                PreparedStatement ps3 = conn.prepareStatement("INSERT INTO Administrator (adminName) VALUES (?)");
                            )
                            {
               
                                ps3.setString(1, userName);
                                if(ps3.executeUpdate()==1) return 0;
                                else return 2;
                                
                
                
                
                            } 
                            catch (SQLException ex) {
                                 Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else return 1;
              
                
                    }   catch (SQLException ex) {
                         Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else return 2;
                
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        return -1;
    }

    @Override
    public Integer getSentPackages(String... userNames) {
        int x=0;
        int l=0;
        for(String userName:userNames){
            conn = DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM [User] WHERE userName=?");
                 )
                {
                    ps.setString(1, userName);
                    ResultSet rs=ps.executeQuery();
                    if(rs.next()){
                        l++;
                        x+=rs.getInt("numberOfSentPackages");
                    }
                   
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(l==0) return null;
        return x;
    }

    @Override
    public int deleteUsers(String... userNames) {
        int x=0;
        for(String userName:userNames){
            conn = DB.getInstance().getConnection();
            try (
                PreparedStatement ps = conn.prepareStatement("DELETE FROM [User] WHERE userName=?");
                 )
                {
                    ps.setString(1, userName);
                    int res = ps.executeUpdate();
                    if(res==1) x++;
                   
            } catch (SQLException ex) {
                Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return x;
    }

    @Override
    public List<String> getAllUsers() {
        conn = DB.getInstance().getConnection();
        LinkedList<String> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT userName FROM [User]");
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
    
}
