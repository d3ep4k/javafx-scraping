/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scrape;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * http://164.100.181.28/edistrict/showAppDetails.aspx?xzatx=16580040000997
 * http://164.100.181.28/edistrict/showStatushome.aspx?application_no=16580040000997
 *
 * @author deepak
 */
public class ApplicationStatusProcess implements Processable {

    int start, end, year, districtCode, applicationCode;

    public ApplicationStatusProcess(String range) {
        String limits[] = range.split("-");
        Pattern p = Pattern.compile("\\d+(\\d{7})");
        Matcher m = p.matcher(limits[0]);
        if (m.find()) {
            start = new Integer(m.group(1));
        }

        m = p.matcher(limits[1]);
        if (m.find()) {
            end = new Integer(m.group(1));
        }
    }

    @Override
    public void process(String file, WritableRecord record) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            //headers
            writer.append("Seva Name,");
            writer.append("Application Number,");
            writer.append("Applicant Name,");
            writer.append("Husband/Father Name,");
            writer.append("Application Status");
            writer.append("Process report");
            writer.append('\n');

            record.write(writer);
        }
    }

    @Override
    public void writeRecord(String url, FileWriter writer) {
        try {
            Document doc = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .get();
            Elements tableData = doc.select("#DetailsView1 td");
            //odd loop
            for (int i = 2; i < tableData.size(); i = i + 2) {
                Element e = tableData.get(i);
                writer.append(e.text());
                writer.append(',');
            }
            writer.append(doc.select("#status").first().text());
            doc = Jsoup.connect("http://164.100.181.28/edistrict/showAppDetails.aspx?xzatx=" + url.split("=")[1])
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .get();

            writer.append(',');
            writer.write(doc.select("#GridAllComp").first().text());
            writer.append('\n');
        } catch (IOException ex) {
            Logger.getLogger(ApplicationStatusProcess.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public int getRecordCount() throws IOException {
        return (int) (end - start + 1);
    }

    /**
     * Get page links for iterations
     *
     * @param recordNo
     * @return
     * @throws IOException
     */
    @Override
    public String getPageLink(int recordNo) throws IOException {
        return "http://164.100.181.28/edistrict/showStatushome.aspx?application_no=1658004" + String.format("%07d", (start + recordNo));
    }

}
