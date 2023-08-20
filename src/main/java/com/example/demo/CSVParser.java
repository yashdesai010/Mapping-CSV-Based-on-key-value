package com.example.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser {
    void CSVparsing() {
        String csvFile = "C:\\Users\\Admin\\Desktop\\CSV\\src\\main\\java\\com\\example\\demo\\activity_202307171549 (3).csv";
        String outputFile = "C:\\Users\\Admin\\Desktop\\CSV\\src\\main\\java\\com\\example\\demo\\output.csv";
        String line;
        Pattern keyValuePattern = Pattern.compile("\"(.*?)\"\\|\"(.*?)\"");
        Pattern nbspPattern = Pattern.compile("&nbsp;");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                Matcher matcher = keyValuePattern.matcher(line);
                if (matcher.matches()) {
                    String candidateId = matcher.group(1).trim();
                    String content = matcher.group(2).trim();
                    content = nbspPattern.matcher(content).replaceAll("");
                    Map<String, String> keyValuePairs = extractKeyValuePairs(content);

                    if (!keyValuePairs.isEmpty()) {

                        if (isFirstLine) {

                            writeHeaderRow(bw);
                            isFirstLine = false;
                        }

                        writeCandidateRow(bw, candidateId, keyValuePairs);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, String> extractKeyValuePairs(String content) {
        Map<String, String> keyValuePairs = new LinkedHashMap<>();
        Pattern pTagPattern = Pattern.compile("<p>(.*?)</p>");
        Matcher pTagMatcher = pTagPattern.matcher(content);

        while (pTagMatcher.find()) {
            String pTagContent = pTagMatcher.group(1).trim();
            pTagContent = pTagContent.replaceAll("<span[^>]+>", "");
            pTagContent = pTagContent.replaceAll("</span>", ""); 
            pTagContent = pTagContent.replaceAll("<br[^>]*>", "");
            String[] keyValue = pTagContent.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                keyValuePairs.put(key, value);
            }
        }

        return keyValuePairs;
    }

    private void writeHeaderRow(BufferedWriter bw) throws IOException {
        bw.write(
                "\"candidate_id\",\"Availability/Notice period\",\"Availability to interview\",\"Technical experience\",\"Industries/Sectors worked in\",\"Certificates\",\"Timezone working in\",\"Open to remote/onsite\",\"Location\",\"Fee\",\"Notes\",\"Immigration status\",\"Language\",\"End Client\",\"Limited company\"");
        bw.newLine();
    }

    private void writeCandidateRow(BufferedWriter bw, String candidateId, Map<String, String> keyValuePairs)
            throws IOException {
        bw.write("\"" + candidateId + "\",\"" +
                keyValuePairs.getOrDefault("Availability/Notice period", "") + "\",\"" +
                keyValuePairs.getOrDefault("Availability to interview", "") + "\",\"" +
                keyValuePairs.getOrDefault("Technical experience", "") + "\",\"" +
                keyValuePairs.getOrDefault("Industries/Sectors worked in", "") + "\",\"" +
                keyValuePairs.getOrDefault("Certificates", "") + "\",\"" +
                keyValuePairs.getOrDefault("Timezone working in", "") + "\",\"" +
                keyValuePairs.getOrDefault("Open to remote/onsite", "") + "\",\"" +
                keyValuePairs.getOrDefault("Location", "") + "\",\"" +
                keyValuePairs.getOrDefault("Fee", "") + "\",\"" +
                keyValuePairs.getOrDefault("Notes", "") + "\",\"" +
                keyValuePairs.getOrDefault("Immigration status", "") + "\",\"" +
                keyValuePairs.getOrDefault("Language", "") + "\",\"" +
                keyValuePairs.getOrDefault("End Client", "") + "\",\"" +
                keyValuePairs.getOrDefault("Limited company", "") + "\"");
        bw.newLine();
    }

    public static void main(String[] args) {
        CSVParser cp = new CSVParser();
        cp.CSVparsing();
    }
}