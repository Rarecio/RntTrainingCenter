package ru.zhaleykin.module4files;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ParserTest {

    private final File xmlFile = new File("D:\\RntTrainingCenter\\hamlet.xml");

    @Test
    public void testSAXParser() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        MySAXHandler handler = new MySAXHandler();
        saxParser.parse(xmlFile, handler);


        Path csvFile = Paths.get("D:\\RntTrainingCenter\\output.csv");

        List<String> result = handler.tagPopularity.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> String.format("%10s : %s", entry.getKey(), entry.getValue()))
//                .peek(System.out::println)
                .collect(Collectors.toList());
        try {
            Files.write(csvFile, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(handler.uniqueWordsInHamletReplicas.size(), 2857);
        assertEquals(handler.tagPopularity.size(), 16);
        assertEquals(handler.tagPopularity, tagPopularity());
    }

    @Test
    public void testDOMParser() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        Map<String, Integer> map = new HashMap<>();
        recursiveNodeName(doc, map);
        assertEquals(map, tagPopularity());
        Path csvFile = Paths.get("D:\\RntTrainingCenter\\output.csv");
        List<String> result = map.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(entry -> String.format("%10s : %s", entry.getKey(), entry.getValue()))
//                .peek(System.out::println)
                .collect(Collectors.toList());
        try {
            Files.write(csvFile, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void recursiveNodeName(Node node, Map<String, Integer> result) {
        if (node == null) return;
        for (int i = 0; i < node.getChildNodes().getLength(); i++)
            recursiveNodeName(node.getChildNodes().item(i), result);
        String nodeName = node.getNodeName();
        if (!nodeName.startsWith("#"))
            result.merge(node.getNodeName(), 1, Integer::sum);
    }

    @Test
    public void testUniqueWordsInHamletReplicasCount() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);
        Set<String> uniqueWordsInHamletReplicas = new HashSet<>();
        NodeList speeches = doc.getElementsByTagName("SPEECH");
        for (int i = 0; i < speeches.getLength(); i++) {
            Element speech = (Element) speeches.item(i);
            NodeList speakers = speech.getElementsByTagName("SPEAKER");
            boolean flag = false;
            for (int j = 0; j < speakers.getLength(); j++) {
                Node speaker = speakers.item(j);
                flag = "HAMLET".equals(speaker.getTextContent());
                if (flag) break;
            }
            if (!flag)
                continue;
            NodeList lines = speech.getElementsByTagName("LINE");
            for (int j = 0; j < lines.getLength(); j++) {
                Element lineElement = (Element) lines.item(j);
                NodeList listNodes = lineElement.getChildNodes();
                for (int k = 0; k < listNodes.getLength(); k++) {
                    if (listNodes.item(k).getNodeType() == Node.TEXT_NODE) {
                        String line = listNodes.item(k).getTextContent();
                        String[] words = line.split("(?U)(?<!\\p{L})'|'(?!\\p{L})|[^\\p{L}\\p{N}']+");
                        List<String> res = Stream.of(words)
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .collect(Collectors.toList());
                        uniqueWordsInHamletReplicas.addAll(res);
                    }
                }
            }
        }
        assertEquals(uniqueWordsInHamletReplicas.size(), 2857);

    }

    public Map<String, Integer> tagPopularity() {
        Map<String, Integer> tagPopularity = new HashMap<>();
        tagPopularity.put("PLAYSUBT", 1);
        tagPopularity.put("FM", 1);
        tagPopularity.put("GRPDESCR", 2);
        tagPopularity.put("PERSONAE", 1);
        tagPopularity.put("SPEECH", 1138);
        tagPopularity.put("SPEAKER", 1150);
        tagPopularity.put("STAGEDIR", 243);
        tagPopularity.put("P", 4);
        tagPopularity.put("PLAY", 1);
        tagPopularity.put("ACT", 5);
        tagPopularity.put("LINE", 4014);
        tagPopularity.put("PERSONA", 26);
        tagPopularity.put("SCNDESCR", 1);
        tagPopularity.put("TITLE", 27);
        tagPopularity.put("SCENE", 20);
        tagPopularity.put("PGROUP", 2);
        return tagPopularity;
    }


}
