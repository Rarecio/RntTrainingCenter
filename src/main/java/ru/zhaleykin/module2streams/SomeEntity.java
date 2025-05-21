package ru.zhaleykin.module2streams;

import java.util.HashMap;
import java.util.Map;

public class SomeEntity {
    private final int n;
    private final String str;
    private final boolean flag;
    private final Map<Property, Boolean> properties;

    public SomeEntity(int n, String str, boolean flag) {
        this(n, str, flag, new HashMap<>());
    }

    public SomeEntity(int n, String str, boolean flag, Map<Property, Boolean> properties) {
        this.n = n;
        this.str = str;
        this.flag = flag;
        this.properties = properties;
    }

    int getIntInfo(){
        int res = n + str.length();
        if (flag) return res * 2;
        else return res + 10;
    }

    String getInfo(){
        return n +" "+ str +" "+ flag;
    }

    public Map<Property, Boolean> properties() {
        return properties;
    }
}
