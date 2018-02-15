package com.report;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * @author song.j
 * @create 2017-08-10 09:09:12
 **/
public class Config {

    private static Config current = null;

    public static Map<String, String> propertiesKey = new HashMap<>();

    public static final String USER_DIR = System.getProperty("user.dir");

    private Config() {
        System.out.println();
        String confFile = System.getProperty("user.dir") + "/conf/email.properties";
        try {
            System.out.println(confFile);
            loadProperties(confFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config get() {

        if (current == null) {
            synchronized (Config.class) {
                current = new Config();
                return current;
            }
        }
        return current;
    }

    public void loadProperties(String confFile) throws IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(confFile);
            Properties properties = new Properties();
            properties.load(is);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                propertiesKey.put((String) entry.getKey(), (String) entry.getValue());
            }
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
