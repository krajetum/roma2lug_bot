package krajetum.LTB.configs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Lorenzo on 15/11/2016.
 */
public class BotConfig {

    public static String BOT_TOKEN;
    public static String BOT_USERNAME;
    public static long BOT_LUG_GROUP_TEST_ID;
    public static long BOT_LUG_GROUP_ID;

    static{
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(System.getProperty("user.dir")+"/config/properties.properties")));
            BOT_TOKEN = properties.getProperty("TOKEN");
            BOT_LUG_GROUP_TEST_ID = Long.parseLong(properties.getProperty("LUG_TEST_ID"));
            BOT_LUG_GROUP_ID = Long.parseLong(properties.getProperty("LUG_ID"));
            BOT_USERNAME = properties.getProperty("USERNAME");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
