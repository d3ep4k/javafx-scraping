/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrape;

import java.io.FileWriter;

/**
 *
 * @author deepak
 */
public interface WritableRecord {

    public void write(FileWriter writer);
}
