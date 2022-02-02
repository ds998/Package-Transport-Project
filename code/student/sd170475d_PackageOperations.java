/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;
import student.db.DB;


/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_PackageOperations implements PackageOperations {
    
    private Connection conn;

    @Override
    public int insertPackage(int districtFrom, int districtTo, String userName, int packageType, BigDecimal weight) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Package (districtFrom, districtTo, userName, packageType, weight) VALUES (?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
                )
            {
               
                ps.setInt(1, districtFrom);
                ps.setInt(2, districtTo);
                ps.setString(3, userName);
                ps.setInt(4, packageType);
                ps.setBigDecimal(5,weight);
                int res = ps.executeUpdate();
                if(res==1) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if(rs.next()){
                        int x=rs.getInt(1);
                        PreparedStatement ps2=conn.prepareStatement("UPDATE [User] SET numberOfSentPackages=numberOfSentPackages+1  WHERE userName=?");
                        ps2.setString(1,userName);
                        ps2.executeUpdate();
                        
                        return x;
                    }
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
    public int insertTransportOffer(String courierUserName, int packageId, BigDecimal pricePercentage) {
        BigDecimal bd = null;
        if(pricePercentage==null){
            Random r = new Random();
            double randomValue = -10 + 20 * r.nextDouble();
            bd=new BigDecimal(randomValue);
        }else bd=pricePercentage;
        
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("INSERT INTO TransportOffer (courierUserName,packageId, pricePercentage) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);
                )
            {
               
                ps.setString(1, courierUserName);
                ps.setInt(2, packageId);
                ps.setBigDecimal(3,bd);
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
    public boolean acceptAnOffer(int offerId) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT packageId FROM TransportOffer WHERE offerId=?");
                )
            {
               
                ps.setInt(1, offerId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("UPDATE package SET deliveryStatus=1, acceptedTime=? WHERE packageId=?");
                    )
                    {
                        int pId=rs.getInt(1);
                        ps2.setDate(1,new Date(System.currentTimeMillis()));
                        ps2.setInt(2, pId);
                        
                        if(ps2.executeUpdate()==1){
                            try (
                                PreparedStatement ps3 = conn.prepareStatement("UPDATE TransportOffer SET accepted=1 WHERE offerId=?");
                            )
                            {
                                
                                ps3.setInt(1, offerId);
                        
                                return ps3.executeUpdate()==1;
               
                
                
                            } catch (SQLException ex) {
                                    Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                        }else return false;
               
                
                
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
    public List<Integer> getAllOffers() {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT offerId FROM TransportOffer");
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

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int packageId) {
        conn = DB.getInstance().getConnection();
        LinkedList<Pair<Integer,BigDecimal>> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT offerId,pricePercentage FROM TransportOffer WHERE packageId=?");
                )
            {
            ps.setInt(1,packageId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                l.add(new sd170475d_Pair(rs.getInt(1),rs.getBigDecimal(2)));
            }
            return l;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean deletePackage(int packageId) {
         conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Package WHERE packageId=?");
                )
            {
                ps.setInt(1, packageId);
                
                return ps.executeUpdate()==1;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeWeight(int packageId, BigDecimal newWeight) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                )
            {
               
                ps.setInt(1, packageId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("UPDATE Package SET weight=? WHERE packageId=?");
                    )
                    {
                        
                        ps2.setBigDecimal(1,newWeight);
                        ps2.setInt(2, packageId);
                        
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
    public boolean changeType(int packageId, int newType) {
        conn = DB.getInstance().getConnection();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                )
            {
               
                ps.setInt(1, packageId);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    try (
                        PreparedStatement ps2 = conn.prepareStatement("UPDATE Package SET packageType=? WHERE packageId=?");
                    )
                    {
                        
                        ps2.setInt(1,newType);
                        ps2.setInt(2, packageId);
                        
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
    public Integer getDeliveryStatus(int packageId) {
        conn = DB.getInstance().getConnection();
        
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT deliveryStatus FROM Package WHERE packageId=?");
                )
            {
            ps.setInt(1,packageId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
        
    }

    @Override
    public BigDecimal getPriceOfDelivery(int packageId) {
        conn = DB.getInstance().getConnection();
        
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                )
            {
            ps.setInt(1,packageId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int delStat=rs.getInt("deliveryStatus");
                if(delStat>=1){
                    int districtFrom=rs.getInt("districtFrom");
                    int districtTo=rs.getInt("districtTo");
                    int dfx=-1;
                    int dfy=-1;
                    int dtx=-1;
                    int dty=-1;
                    int packageType=rs.getInt("packageType");
                    BigDecimal weight=rs.getBigDecimal("weight");
                    BigDecimal base_price=null;
                    BigDecimal weight_factor=null;
                    BigDecimal price_per_kg=null;
                    switch(packageType){
                        case 0:{
                            base_price=new BigDecimal(10);
                            weight_factor=new BigDecimal(0);
                            price_per_kg=new BigDecimal(0);
                        }
                        break;
                        
                        case 1:{
                            base_price=new BigDecimal(25);
                            weight_factor=new BigDecimal(1);
                            price_per_kg=new BigDecimal(100);
                        }
                        break;
                        
                        case 2:{
                            base_price=new BigDecimal(75);
                            weight_factor=new BigDecimal(2);
                            price_per_kg=new BigDecimal(300);
                        }
                        break;
                    }
                    
                    try(
                            PreparedStatement ps2=conn.prepareStatement("SELECT xCord,yCord from District WHERE idDistrict=?");
                            PreparedStatement ps3=conn.prepareStatement("SELECT xCord,yCord from District WHERE idDistrict=?");
                            PreparedStatement ps4=conn.prepareStatement("SELECT pricePercentage from TransportOffer WHERE packageId=?")
                            ){
                        
                        ps2.setInt(1, districtFrom);
                        ResultSet rs2=ps2.executeQuery();
                        if(rs2.next()){
                            dfx=rs2.getInt(1);
                            dfy=rs2.getInt(2);
                            ps3.setInt(1, districtTo);
                            ResultSet rs3=ps3.executeQuery();
                            if(rs3.next()){
                                dtx=rs3.getInt(1);
                                dty=rs3.getInt(2);
                                BigDecimal euclid=new BigDecimal(sqrt(pow((double)dfx-dtx,2.0)+pow((double)dfy-dty,2.0)));
                                weight = weight.multiply(weight_factor);
                                weight=weight.multiply(price_per_kg);
                                weight=weight.add(base_price);
                                weight=weight.multiply(euclid);
                                
                                ps4.setInt(1,packageId);
                                ResultSet rs4=ps4.executeQuery();
                                if(rs4.next()){
                                    BigDecimal pP=rs4.getBigDecimal(1);
                                    pP=pP.divide(new BigDecimal(100));
                                    pP=pP.add(new BigDecimal(1));
                                    weight=weight.multiply(pP);
                                    return weight;
                                }else return null;
                                
                            }
                            
                        }else return null;
                        
                    }
                    catch(SQLException ex){
                        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else return null;
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Date getAcceptanceTime(int packageId) {
        conn = DB.getInstance().getConnection();
        
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                )
            {
            ps.setInt(1,packageId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int acc=rs.getInt("deliveryStatus");
                if(acc>=1){
                    return rs.getDate("acceptedTime");
                }else return null;
                
            }else return null;
            
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int packageType) {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT packageId FROM Package WHERE packageType=?");
                )
            {
            ps.setInt(1,packageType);
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
    public List<Integer> getAllPackages() {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT packageId FROM Package");
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

    @Override
    public List<Integer> getDrive(String courierUserName) {
        conn = DB.getInstance().getConnection();
        LinkedList<Integer> l = new LinkedList<>();
        try (
            PreparedStatement ps = conn.prepareStatement("SELECT driveId from Drive WHERE courierUserName=?");
                )
            {
            ps.setString(1,courierUserName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int driveId=rs.getInt(1);
                try (
                    PreparedStatement ps2 = conn.prepareStatement("SELECT * from Package WHERE driveId=?");
                )
                {
                    ps2.setInt(1,driveId);
                    ResultSet rs2 = ps2.executeQuery();
                    while(rs2.next()){
                        int pId=rs2.getInt("packageId");
                        int delStat=rs2.getInt("deliveryStatus");
                        if(delStat==1){
                            l.add(pId);
                        }
                
                    }
                    return l;
                    
                } catch (SQLException ex) {
                    Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            else return null;
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int driveNextPackage(String courierUserName) {
        conn=DB.getInstance().getConnection();
        try(
                PreparedStatement ps = conn.prepareStatement("SELECT * from Drive WHERE currentStatus=1 AND courierUserName=?");
                ){
            ps.setString(1,courierUserName);
            int driveId=-1;
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                driveId=rs.getInt("driveId");
            }else{
                List<Integer> packages = findNumberOfOurPackages(courierUserName);
                if(packages==null) return -2;
                if(packages.isEmpty()) return -1;
                PreparedStatement ps2 = conn.prepareStatement("INSERT INTO Drive (courierUserName,currentStatus,totalFuelConsumption) VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);
                ps2.setString(1,courierUserName);
                ps2.setInt(2,1);
                ps2.setBigDecimal(3,new BigDecimal(0));
                if(ps2.executeUpdate()==1){
                    ResultSet rs2=ps2.getGeneratedKeys();
                    if(rs2.next()){
                        driveId=rs2.getInt(1);
                        if(updateOurPackages(packages,driveId)!=packages.size()) return -2;
                        PreparedStatement ps3=conn.prepareStatement("UPDATE Courier SET statusOfCourier=1 WHERE courierUserName=?");
                        ps3.setString(1,courierUserName);
                        if(ps3.executeUpdate()!=1) return -2;
                    }else return -2;
                }else return -2;
                
            }
            
            List<Integer> unfinishedPackages = allUnfinishedPackages(driveId);
            if(unfinishedPackages==null) return -2;
            int retPackageId=unfinishedPackages.get(0);
            if(!deliverPackage(courierUserName,driveId,retPackageId)) return -2;
            if(unfinishedPackages.size()>1){
                int secondPackageId=unfinishedPackages.get(1);
                if(!toTheNextPackage(courierUserName,driveId,retPackageId,secondPackageId)) return -2;
                else return retPackageId;
            }
            else{
                if(!endDrive(courierUserName,driveId)) return -2;
                else return retPackageId;
            }
        } catch (SQLException ex) {
            Logger.getLogger(sd170475d_PackageOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -2;
    }
    
    
private List<Integer> findNumberOfOurPackages(String courierUserName){
    conn=DB.getInstance().getConnection();
    LinkedList<Integer> l=new LinkedList<>();
    try(
        PreparedStatement ps=conn.prepareStatement("SELECT P.packageId from Package P JOIN TransportOffer O ON(O.packageId=P.packageId) WHERE deliveryStatus=1 AND driveId IS NULL AND courierUserName=?");
        ){
        ps.setString(1,courierUserName);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            l.add(rs.getInt(1));
        }
        return l;
    }catch(SQLException ex){
        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

private int updateOurPackages(List<Integer> l,int driveId){
    conn=DB.getInstance().getConnection();
    int x=0;
    for(int i=0;i<l.size();i++){
        try(
            PreparedStatement ps=conn.prepareStatement("UPDATE Package SET driveId=? WHERE packageId=?");
        ){
            ps.setInt(1,driveId);
            ps.setInt(2,l.get(i));
            if(ps.executeUpdate()==1) x++;
        }catch(SQLException ex){
            Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    return x;
}

private List<Integer> allUnfinishedPackages(int driveId){
    conn=DB.getInstance().getConnection();
    LinkedList<Integer> l=new LinkedList<>();
    try(
        PreparedStatement ps=conn.prepareStatement("SELECT packageId from Package WHERE driveId=? AND deliveryStatus<3 ORDER BY acceptedTime");
        ){
        ps.setInt(1,driveId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            l.add(rs.getInt(1));
        }
        return l;
    }catch(SQLException ex){
        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
}

private boolean deliverPackage(String courierUserName,int driveId,int packageId){
    conn=DB.getInstance().getConnection();
    try(
        PreparedStatement ps=conn.prepareStatement("SELECT licencePlateNumber from Courier WHERE courierUserName=?");
        ){
        ps.setString(1,courierUserName);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            String lpN=rs.getString(1);
            PreparedStatement ps2=conn.prepareStatement("SELECT fuelConsumption FROM Vehicle WHERE licencePlateNumber=?");
            ps2.setString(1,lpN);
            ResultSet rs2=ps2.executeQuery();
            if(rs2.next()){
                BigDecimal fuelConsumption = rs2.getBigDecimal(1);
                PreparedStatement ps3=conn.prepareStatement("UPDATE Package SET deliveryStatus=2 WHERE packageId=?");
                PreparedStatement ps4=conn.prepareStatement("UPDATE Package SET deliveryStatus=3 WHERE packageId=?");
                ps3.setInt(1,packageId);
                ps4.setInt(1,packageId);
                if(ps3.executeUpdate()==1){
                    if(ps4.executeUpdate()==1){
                        PreparedStatement ps5=conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                        ps5.setInt(1,packageId);
                        ResultSet rs5=ps5.executeQuery();
                        if(rs5.next()){
                            int districtFrom=rs5.getInt("districtFrom");
                            int districtTo=rs5.getInt("districtTo");
                            int dfx=-1;
                            int dfy=-1;
                            int dtx=-1;
                            int dty=-1;
                            PreparedStatement ps6=conn.prepareStatement("SELECT xCord,yCord FROM District WHERE idDistrict=?");
                            PreparedStatement ps7=conn.prepareStatement("SELECT xCord,yCord FROM District WHERE idDistrict=?");
                            ps6.setInt(1,districtFrom);
                            ps7.setInt(1,districtTo);
                            ResultSet rs6=ps6.executeQuery();
                            if(rs6.next()){
                                dfx=rs6.getInt(1);
                                dfy=rs6.getInt(2);
                                ResultSet rs7=ps7.executeQuery();
                                if(rs7.next()){
                                    dtx=rs7.getInt(1);
                                    dty=rs7.getInt(2);
                                    BigDecimal euclid = new BigDecimal(sqrt(pow(dfx-dtx,2)+pow(dfy-dty,2)));
                                    euclid=euclid.multiply(fuelConsumption);
                                    PreparedStatement ps8=conn.prepareStatement("UPDATE Drive SET totalFuelConsumption=totalFuelConsumption+? WHERE driveId=?");
                                    ps8.setBigDecimal(1,euclid);
                                    ps8.setInt(2,driveId);
                                    if(ps8.executeUpdate()==1){
                                        PreparedStatement ps9=conn.prepareStatement("SELECT COALESCE(numberofDeliveries,0) FROM Courier WHERE courierUserName=?");
                                        ps9.setString(1,courierUserName);
                                        ResultSet rs9=ps9.executeQuery();
                                        if(rs9.next()){
                                            int n_of_d=rs9.getInt(1);
                                            PreparedStatement ps10=conn.prepareStatement("UPDATE Courier SET numberOfDeliveries=? WHERE courierUserName=?");
                                            ps10.setInt(1,n_of_d+1);
                                            ps10.setString(2,courierUserName);
                                            return ps10.executeUpdate()==1;
                                        }else return false;
                                    }else return false;
                                }else return false;
                            }else return false;
                            
                        }else return false;
                    }
                    else return false;
                }else return false;
                
            }
            else return false;
            
        }
        else return false;
    }catch(SQLException ex){
        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}

private boolean toTheNextPackage(String courierUserName,int driveId,int packageId1,int packageId2){
    conn=DB.getInstance().getConnection();
    try(
        PreparedStatement ps=conn.prepareStatement("SELECT licencePlateNumber from Courier WHERE courierUserName=?");
        ){
        ps.setString(1,courierUserName);
        ResultSet rs=ps.executeQuery();
        if(rs.next()){
            String lpN=rs.getString(1);
            PreparedStatement ps2=conn.prepareStatement("SELECT fuelConsumption FROM Vehicle WHERE licencePlateNumber=?");
            ps2.setString(1,lpN);
            ResultSet rs2=ps2.executeQuery();
            if(rs2.next()){
                BigDecimal fuelConsumption=rs2.getBigDecimal(1);
                PreparedStatement ps3=conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                PreparedStatement ps4=conn.prepareStatement("SELECT * FROM Package WHERE packageId=?");
                ps3.setInt(1,packageId1);
                ps4.setInt(1,packageId2);
                ResultSet rs3=ps3.executeQuery();
                if(rs3.next()){
                    int district1=rs3.getInt("districtTo");
                    ResultSet rs4=ps4.executeQuery();
                    if(rs4.next()){
                        int district2=rs4.getInt("districtFrom");
                        int dfx=-1;
                        int dfy=-1;
                        int dtx=-1;
                        int dty=-1;
                        PreparedStatement ps5=conn.prepareStatement("SELECT xCord,yCord FROM District WHERE idDistrict=?");
                        PreparedStatement ps6=conn.prepareStatement("SELECT xCord,yCord FROM District WHERE idDistrict=?");
                        ps5.setInt(1,district1);
                        ps6.setInt(1,district2);
                        ResultSet rs5=ps5.executeQuery();
                        if(rs5.next()){
                            dfx=rs5.getInt(1);
                            dfy=rs5.getInt(2);
                            ResultSet rs6=ps6.executeQuery();
                            if(rs6.next()){
                                dtx=rs6.getInt(1);
                                dty=rs6.getInt(2);
                                BigDecimal euclid = new BigDecimal(sqrt(pow(dfx-dtx,2)+pow(dfy-dty,2)));
                                euclid=euclid.multiply(fuelConsumption);
                                PreparedStatement ps7=conn.prepareStatement("UPDATE Drive SET totalFuelConsumption=totalFuelConsumption+? WHERE driveId=?");
                                ps7.setBigDecimal(1,euclid);
                                ps7.setInt(2,driveId);
                                if(ps7.executeUpdate()==1){
                                    PreparedStatement ps8=conn.prepareStatement("UPDATE Package SET deliveryStatus=2 WHERE packageId=?");
                                    ps8.setInt(1,packageId2);
                                    return ps8.executeUpdate()==1;
                                }else return false;
                            }else return false;
                        }else return false;
                    }else return false;
                }
                else return false;
            }
            else return false;
            
        }
        else return false;
    }catch(SQLException ex){
        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}

private boolean endDrive(String courierUserName,int driveId){
    conn=DB.getInstance().getConnection();
    BigDecimal profit=new BigDecimal(0);
    try(
        PreparedStatement ps=conn.prepareStatement("SELECT packageId from Package WHERE driveId=?");
        ){
        ps.setInt(1,driveId);
        ResultSet rs=ps.executeQuery();
        while(rs.next()){
            BigDecimal bd=getPriceOfDelivery(rs.getInt(1));
            if(bd!=null) profit=profit.add(bd);
        }
        PreparedStatement ps2=conn.prepareStatement("SELECT licencePlateNumber from Courier WHERE courierUserName=?");
        ps2.setString(1,courierUserName);
        ResultSet rs2=ps2.executeQuery();
        if(rs2.next()){
            String lpN=rs2.getString(1);
            PreparedStatement ps3=conn.prepareStatement("SELECT fuelType FROM Vehicle WHERE licencePlateNumber=?");
            ps3.setString(1,lpN);
            ResultSet rs3=ps3.executeQuery();
            if(rs3.next()){
                int fuelType=rs3.getInt(1);
                BigDecimal fuel_per_litre=null;
                switch(fuelType){
                    case 0:
                        fuel_per_litre=new BigDecimal(15);
                        break;
                    case 1:
                        fuel_per_litre=new BigDecimal(36);
                        break;
                    case 2:
                        fuel_per_litre=new BigDecimal(32);
                        break;
                }
                PreparedStatement ps4=conn.prepareStatement("SELECT totalFuelConsumption FROM Drive WHERE driveId=?");
                ps4.setInt(1,driveId);
                ResultSet rs4=ps4.executeQuery();
                if(rs4.next()){
                    BigDecimal totalFuelConsumption=rs4.getBigDecimal(1);
                    totalFuelConsumption=totalFuelConsumption.multiply(fuel_per_litre);
                    profit=profit.subtract(totalFuelConsumption);
                    PreparedStatement ps5=conn.prepareStatement("UPDATE Drive SET currentStatus=0 WHERE driveId=?");
                    
                    ps5.setInt(1,driveId);
                    
                    if(ps5.executeUpdate()==1){
                        PreparedStatement ps6=conn.prepareStatement("SELECT COALESCE(profit,0) FROM Courier WHERE courierUserName=?");
                        PreparedStatement ps7=conn.prepareStatement("UPDATE Courier SET profit=? WHERE courierUserName=?");
                        ps6.setString(1,courierUserName);
                        ResultSet rs6=ps6.executeQuery();
                        if(rs6.next()){
                            BigDecimal total_profit=rs6.getBigDecimal(1);
                            total_profit=total_profit.add(profit);
                            ps7.setBigDecimal(1,total_profit);
                            ps7.setString(2,courierUserName);
                            if(ps7.executeUpdate()==1){
                                PreparedStatement ps8=conn.prepareStatement("UPDATE Courier SET statusOfCourier=0 WHERE courierUserName=?");
                                ps8.setString(1,courierUserName);
                                return ps8.executeUpdate()==1;
                            }else return false;
                        }else return false;
                        
                    }else return false;
                }
                else return false;
            }else return false;
        }else return false;
        
        
    }catch(SQLException ex){
        Logger.getLogger(sd170475d_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}

    
}
