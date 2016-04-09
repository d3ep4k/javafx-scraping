/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package village.user.details;

import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketTimeoutException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author VINAY
 */
public class RationProcess {

    Element tbody;

    public RationProcess(String url) throws IOException {
        tbody = getTbody(url);
    }

    public void process(String fileName, WritableRecord record) throws IOException {

        try (FileWriter writer = new FileWriter(fileName)) {
            //headers
            writer.append("Ration Card No");
            writer.append(',');
            writer.append("Name");
            writer.append(',');
            writer.append("Father/Husband Name");
            writer.append(',');
            writer.append("Mother Name");
            writer.append(',');
            writer.append("Phone No.");
            writer.append(',');
            writer.append("Voter Id No.");
            writer.append(',');
            writer.append("Adhaar No");
            writer.append(',');
            writer.append('\n');

            record.write(writer);
        }
    }

    public void writeRecord(String url, FileWriter writer) {
        try {

            User user = getUserInfo(url);
            writer.append(user.getRationId());
            writer.append(',');
            writer.append(user.getName());
            writer.append(',');
            writer.append(user.getDependentName());
            writer.append(',');
            writer.append(user.getMotherName());
            writer.append(',');
            writer.append(user.getMobile());
            writer.append(',');
            writer.append(user.getVoterId());
            writer.append(',');
            writer.append(user.getAdhaarId());
            writer.append(',');
            writer.append('\n');

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private User getUserInfo(String url) throws IOException {
        Element element;
        try {
            element = getTbody(url);
        } catch (SocketTimeoutException e) {
            element = getTbody(url);
        }
        Element rationId = (Element) element.childNode(3).childNode(2);
        Element name = (Element) element.childNode(10).childNode(2);
        Element dependentName = (Element) element.childNode(12).childNode(2);
        Element motherName = (Element) element.childNode(14).childNode(2);
        Element mobile = (Element) element.childNode(21).childNode(2);
        Element voterId = (Element) element.childNode(22).childNode(2);
        Element adhaar = (Element) element.childNode(23).childNode(2);

        User user = new User(getInfo(name), getInfo(rationId), getInfo(adhaar),
                getInfo(voterId), getInfo(mobile), getInfo(dependentName), getInfo(motherName));

        return user;
    }

    private static String getInfo(Element el) {
        String[] tokens = el.html().split(";");
        if (tokens.length == 3) {
            return (tokens[2]);
        } else {
            return "";
        }
    }

    public String getPageLink(int recordNo) throws IOException {

        Element tr = (Element) tbody.childNode(recordNo + 5).childNode(1).childNode(1);
        String link = tr.absUrl("href");
        return link;
    }

    public int getRecordCount() throws IOException {
        return tbody.childNodeSize() - 6;
//        doc.outputSettings().charset("UTF-8");
//        Elements elements = doc.select("td#tab");
//        Element tbody = (Element) elements.first().childNode(0).childNode(0);
//        Element td = (Element) tbody.childNode(tbody.childNodeSize()).childNode(0);
//        return Integer.parseInt(td.text().split("\\.")[0]);
    }

    public static Element getTbody(String url) throws IOException {

        Document doc = Jsoup.connect(url)
                .timeout(12000)
                .maxBodySize(0) //setting to 0 turns size checking off
                .get();

        return doc.select("td#tab table tbody").first();
    }

}

class User {

    private String name;
    private String rationId;
    private String adhaarId;
    private String voterId;
    private String mobile;
    private String dependentName;
    private String motherName;

    public User(String name, String rationId, String adhaarId, String voterId, String mobile, String dependentName, String motherName) {
        this.name = name;
        this.rationId = rationId;
        this.adhaarId = adhaarId;
        this.voterId = voterId;
        this.mobile = mobile;
        this.dependentName = dependentName;
        this.motherName = motherName;
    }

    public String getName() {
        return name;
    }

    public String getRationId() {
        return rationId;
    }

    public String getAdhaarId() {
        return adhaarId;
    }

    public String getVoterId() {
        return voterId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDependentName() {
        return dependentName;
    }

    public String getMotherName() {
        return motherName;
    }

}

interface WritableRecord {

    public void write(FileWriter writer);
}
