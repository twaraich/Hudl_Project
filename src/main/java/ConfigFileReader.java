import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Read the config properties file to securely use the valid username/password from it
 */
public class ConfigFileReader {

    private final Properties properties;

    public ConfigFileReader() {
        BufferedReader reader;
        String propertyFilePath = "config//configuration.properties";
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try {
                properties.load(reader);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("config file not found at " + propertyFilePath);
        }
    }

    public String getUserName() {
        String username = properties.getProperty("username");
        if (username != null) return username;
        else throw new RuntimeException("username not found in properties file.");
    }

    public String getPassword() {
        String password = properties.getProperty("password");
        if (password != null) return password;
        else throw new RuntimeException("password not found in properties file.");
    }

}