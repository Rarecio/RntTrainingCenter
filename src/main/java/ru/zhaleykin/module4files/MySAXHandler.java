package ru.zhaleykin.module4files;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MySAXHandler extends DefaultHandler {

    Set<String> uniqueWordsInHamletReplicas = new HashSet<>();
    Map<String, Integer> tagPopularity = new HashMap<>();
    private boolean isSpeaker = false;
    private boolean isHamlet = false;
    private boolean isLine = false;
    private boolean isStageDir = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("SPEAKER")) isSpeaker = true;
        if (qName.equals("LINE")) isLine = true;
        if (qName.equals("STAGEDIR")) isStageDir = true;


        tagPopularity.merge(qName, 1, Integer::sum);
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("SPEAKER")) isSpeaker = false;
        if (qName.equals("LINE")) isLine = false;
        if (qName.equals("STAGEDIR")) isStageDir = false;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (isSpeaker) {
            isHamlet = String.copyValueOf(ch, start, length).equals("HAMLET");
        }
        if (isHamlet && isLine && !isStageDir) {
            String line = String.copyValueOf(ch, start, length).trim();
            String[] words = line.split("(?U)(?<!\\p{L})'|'(?!\\p{L})|[^\\p{L}\\p{N}']+");
            List<String> res = Stream.of(words)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            uniqueWordsInHamletReplicas.addAll(res);

        }
    }
}
