package translation;

import java.util.*;

public class XmlPullParser {
    private String xmlData;

    public XmlPullParser(String xmlData) {
        this.xmlData = xmlData;
    }

    public static class Tag {
        String tag;
        int position;

        public Tag(String tag, int position) {
            this.tag = tag;
            this.position = position;
        }
    }

    public static class Node {
        Tag start;
        Tag end;
        String tagName;
        String srcValue;
        String data;

        public Node(Tag start, Tag end, String srcValue) {
            this.start = start;
            this.end = end;
            this.tagName = start.tag;
            this.srcValue = srcValue;
            init();
        }

        Map<String, String> keyValues;

        public boolean isVisibleChar(char c) {
            return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c == '_') || (c == '.');
        }

        public String getValue(String key) {
            return keyValues.get(key);
        }

        public void init() {
            keyValues = new HashMap<>();
            if (srcValue != null) {
                for (int i = 0; i < srcValue.length(); i++) {
                    char c = srcValue.charAt(i);
                    if (c == '>') {
                        break;
                    }
                    if (c == '=') {
                        int j = i - 1;
                        while (j >= 0) {
                            char v = srcValue.charAt(j);
                            if (isVisibleChar(v)) {
                                break;
                            }
                            j--;
                        }
                        StringBuilder name = new StringBuilder();
                        while (j >= 0) {
                            char v = srcValue.charAt(j);
                            if (!isVisibleChar(v)) {
                                break;
                            }
                            j--;
                            name.append(v);
                        }
                        j = i + 1;
                        while (j < srcValue.length()) {
                            char v = srcValue.charAt(j);
                            if (isVisibleChar(v)) {
                                break;
                            }
                            j++;
                        }
                        StringBuilder value = new StringBuilder();
                        while (j < srcValue.length()) {
                            char v = srcValue.charAt(j);
                            if (!isVisibleChar(v)) {
                                break;
                            }
                            j++;
                            value.append(v);
                        }
                        i = j;
                        keyValues.put(name.reverse().toString(), value.toString());
                    }
                }
                int start = srcValue.indexOf(">");
                int end = srcValue.lastIndexOf("</");
                if (start >= 0 && end > 0 && start < end) {
                    data = srcValue.substring(start+1, end);
                }
            }
        }
    }

    public List<Node> xmlParsing() {
        Stack<Tag> stack = new Stack<>();
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < xmlData.length(); i++) {
            char c = xmlData.charAt(i);
            if (c == '<') {
                //过滤掉<?xml
                if (i < (xmlData.length() - 5) && xmlData.charAt(i + 1) == '?') {
                    i += 2;
                    while (i < xmlData.length() - 1) {
                        if (xmlData.charAt(i) == '?' && xmlData.charAt(i + 1) == '>') {
                            break;
                        }
                        i++;
                    }
                    continue;
                }
                if (i < (xmlData.length() - 1) && xmlData.charAt(i + 1) == '!') {
                    i += 3;
                    while (i < xmlData.length() - 3) {
                        if (xmlData.charAt(i) == '-' && xmlData.charAt(i + 1) == '-' && xmlData.charAt(i + 2) == '>') {
                            break;
                        }
                        i++;
                    }
                    continue;
                }

                int j = i;
                StringBuilder sb = new StringBuilder();
                c = xmlData.charAt(++j);
                if (c == '/') {
                    continue;
                }
                while (c != ' ' && c != '\n' && c != '\t' && c != '>') {
                    sb.append(c);
                    if (j == xmlData.length() - 1) {
                        System.out.println("");
                    }
                    c = xmlData.charAt(++j);
                }
                Tag tag = new Tag(sb.toString(), i);
                stack.push(tag);
            } else if (i > 0 && c == '/' && ((xmlData.charAt(i - 1) == '<')
                    || (i < xmlData.length() - 1 && xmlData.charAt(i + 1) == '>'))) {
                int j = i;
                StringBuilder sb = new StringBuilder();
                while (j < xmlData.length() && xmlData.charAt(j) != '>') {
                    sb.append(xmlData.charAt(j++));
                }
                Tag tag = new Tag(sb.toString(), j + 1);
                Tag startTag = stack.pop();
                String data = xmlData.substring(startTag.position, tag.position);
                if (startTag.position >= tag.position) {
                    System.out.println("");
                }
                nodes.add(new Node(startTag, startTag, data));
            }
        }
        return nodes;
    }
}