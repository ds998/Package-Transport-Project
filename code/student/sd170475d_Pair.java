/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.math.BigDecimal;
import rs.etf.sab.operations.PackageOperations;


/**
 *
 * @author Danilo Stefanovic
 */
public class sd170475d_Pair<Integer,BigDecimal> implements PackageOperations.Pair {
    
    private Integer first;
    private BigDecimal second;
    
    public sd170475d_Pair(Integer f,BigDecimal s){
        first=f;
        second=s;
    }

    @Override
    public Object getFirstParam() {
        return first;
    }

    @Override
    public Object getSecondParam() {
        return second;
    }
    
}
