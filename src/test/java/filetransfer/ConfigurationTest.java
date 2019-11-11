package filetransfer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Optional;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

/**
 * Unit test for simple ConfigurationTest.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest {

    @Test
    public void _1_shouldReadDefaultConfiguration() {
        try {
            String configurationPath = ConfigurationTest.class.getResource("../configuration.yaml").getPath();
            ConfigurationReader configurationReader = new ConfigurationReader(configurationPath);
            Optional<Configuration> configuration = configurationReader.readYamlConfiguration();

            if (configuration.isPresent()) {
                Integer serverPort = Integer.parseInt(configuration.get().getServer().get("port"));
                String storagePath = configuration.get().getServer().get("storage-path");
                int bufferSize = Integer.parseInt(configuration.get().getNetwork().get("buffer-size"));
                int timeout = Integer.parseInt(configuration.get().getClient().get("timeout"));

                assertEquals(9991, serverPort.intValue());
                assertEquals("../", storagePath);
                assertEquals(16384, bufferSize);
                assertEquals(2000, timeout);
            }

         } catch (Exception e) {
            assertTrue(false);
        }
    }
}