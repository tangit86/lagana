package my.than.lagana.web;

import my.than.lagana.core.common.LaganaConfiguration;
import my.than.lagana.core.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class WebAppController {

    final static Logger logger = LoggerFactory.getLogger(WebAppController.class);

    @Autowired
    LaganaConfiguration laganaConfiguration;

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "index.html";
    }

//    @GetMapping("/{path}")
//    @ResponseBody
//    public String point(@PathVariable("path") String path) {
//        StringBuilder content = new StringBuilder();
//        FileReader fileReader = null;
//        String webRoot = getWebRoot();
//        try {
//            fileReader = new FileReader(webRoot + path);
//        } catch (FileNotFoundException e) {
//            try {
//                fileReader = new FileReader(webRoot + index);
//            } catch (FileNotFoundException fileNotFoundException) {
//                return "index.html is missing, what on earth did you do with that?";
//            }
//        }
//
//        try {
//            BufferedReader reader = new BufferedReader(fileReader);
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content.append(line);
//            }
//        } catch (IOException e) {
//            logger.error(StringUtils.format("Failed to find/read web resource {}", path), e);
//        }
//        return content.toString();
//    }
//
//    private String getWebRoot() {
//        String root = laganaConfiguration.getProperty(LaganaConfiguration.ConfKeys.ROOT);
//        String webRoot = laganaConfiguration.getProperty(LaganaConfiguration.ConfKeys.WEB_ROOT);
//        return root+webRoot;
//    }
}
