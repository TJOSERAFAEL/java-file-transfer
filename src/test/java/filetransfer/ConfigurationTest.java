package filetransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

/**
 * Unit test for simple ConfigurationTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest {
    
    private Optional<Configuration> readYamlConfiguration(String path) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            Configuration configuration = mapper.readValue(new File(path), Configuration.class);
            return Optional.of(configuration);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void _1_shouldReadDefaultConfiguration() {
        try {
            String configurationPath = ConfigurationTest.class.getResource("../configuration.yaml").getPath();
            Optional<Configuration> configuration = readYamlConfiguration(configurationPath);

            if (configuration.isPresent()) {
                Integer serverPort = Integer.parseInt(configuration.get().getServer().get("port"));
                String storagePath = configuration.get().getServer().get("storage-path");
                int bufferSize = Integer.parseInt(configuration.get().getNetwork().get("buffer-size"));
                int timeout = Integer.parseInt(configuration.get().getClient().get("timeout"));

                assertEquals(9991, serverPort.intValue());
                assertEquals("C:\\file-transfer\\", storagePath);
                assertEquals(16384, bufferSize);
                assertEquals(2000, timeout);
            }

         } catch (Exception e) {
            assertTrue(false);
        }
    }
}