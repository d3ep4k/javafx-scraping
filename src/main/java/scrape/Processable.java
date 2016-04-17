/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrape;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author deepak
 */
interface Processable {

    public void process(String file, WritableRecord writable) throws IOException;

    public void writeRecord(String url, FileWriter writer);

    public int getRecordCount() throws IOException;

    public String getPageLink(int recordNo) throws IOException;
}


