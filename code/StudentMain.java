import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;
import student.sd170475d_CityOperations;
import student.sd170475d_CourierOperations;
import student.sd170475d_CourierRequestOperation;
import student.sd170475d_DistrictOperations;
import student.sd170475d_GeneralOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import student.db.DB;
import student.sd170475d_PackageOperations;
import student.sd170475d_UserOperations;
import student.sd170475d_VehicleOperations;



public class StudentMain {

    public static void main(String[] args) {
        CityOperations cityOperations = new sd170475d_CityOperations(); // Change this to your implementation.
        DistrictOperations districtOperations = new sd170475d_DistrictOperations(); // Do it for all classes.
        CourierOperations courierOperations = new sd170475d_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new sd170475d_CourierRequestOperation();
        GeneralOperations generalOperations = new sd170475d_GeneralOperations();
        UserOperations userOperations = new sd170475d_UserOperations();
        VehicleOperations vehicleOperations = new sd170475d_VehicleOperations();
        PackageOperations packageOperations = new sd170475d_PackageOperations();
        
        
        //System.out.println(userOperations.deleteUsers("ds998","ds999"));
        
        
        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);


        TestRunner.runTests();
        
    }
}
