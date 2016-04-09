/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package village.user.details;

import java.io.FileWriter;
import java.io.IOException;

/**
 * http://164.100.181.28/edistrict/showAppDetails.aspx?xzatx=16580040000997
 * http://164.100.181.28/edistrict/showStatushome.aspx?application_no=16580040000997
 *
 * @author deepak
 */
public class ApplicationStatusProcess implements Processable {

    @Override
    public void process(String file, WritableRecord writable) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeRecord(String url, FileWriter writer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRecordCount() throws IOException {
        return 100;
    }

    @Override
    public String getPageLink(int recordNo) throws IOException {
        return "http://164.100.181.28/edistrict/showStatushome.aspx?application_no=16580040000997";
    }

}
