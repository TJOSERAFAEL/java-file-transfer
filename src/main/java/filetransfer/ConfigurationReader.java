package filetransfer;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class ConfigurationReader {

    private String path;

    public ConfigurationReader(String path) {
        this.path = path;
    }

    public Optional<Configuration> readYamlConfiguration() throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Configuration configuration = mapper.readValue(new File(this.path), Configuration.class);
            return Optional.of(configuration);
        } catch (Exception e) {
            throw e;
        }
    }
}
