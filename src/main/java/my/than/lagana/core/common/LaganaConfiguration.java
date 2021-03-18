package my.than.lagana.core.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.than.lagana.core.bake.filters.OperatorEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaganaConfiguration {
    final Logger logger = LoggerFactory.getLogger(getClass());

    ObjectMapper mapper = new ObjectMapper();

    public final String APP_CONF;

    public enum ConfKeys {
        ROOT("ROOT"),
        WEB_ROOT("WEB.ROOT"),
        REAP_SCHEMA("REAP.SCHEMA"),
        BAKE_SCHEMA("BAKE.SCHEMA"),
        DELI_SCHEMA("DELI.SCHEMA");

        ConfKeys(String key) {

        }
    }


    public String getProperty(ConfKeys key) {
        return this.confMap.get(key);
    }

    protected Map<ConfKeys, String> confMap = new HashMap<>();

    protected String schemaPath = "";

    public LaganaConfiguration(String appConfPath) throws IOException {
        this.APP_CONF = appConfPath;
        init();
    }


    private void init() throws IOException {
        FileReader fileReader = new FileReader(this.APP_CONF);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            populate(line, ConfKeys.ROOT);
            populate(line, ConfKeys.WEB_ROOT);
            populate(line, ConfKeys.REAP_SCHEMA);
            populate(line, ConfKeys.BAKE_SCHEMA);
            populate(line, ConfKeys.DELI_SCHEMA);
        }

    }

    private void populate(String line, ConfKeys key) {
        if (line == null || line.isEmpty() || confMap.containsKey(key)) {
            return;
        }
        String[] parts = line.split("=");
        if (parts[0].equals(key.name())) {
            confMap.put(key, parts[1]);
        }
    }

    Object getSchema() throws IOException {
        InputStream is = new FileInputStream(schemaPath);
        return mapper.readValue(is, Object.class);
    }

    void persistSchema(Object schema) throws IOException {
        OutputStream os = new FileOutputStream(schemaPath);
        mapper.writeValue(os, schema);
    }
}
