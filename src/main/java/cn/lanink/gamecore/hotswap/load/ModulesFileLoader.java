package cn.lanink.gamecore.hotswap.load;

import lombok.Getter;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@ToString(exclude = "moduleFile")
public class ModulesFileLoader {

    private final File moduleFile;

    private String contents;

    private String repo;

    private final Map<String, String> dependencies = new HashMap<>();
    private final Map<String, String> httpUrls = new HashMap<>();

    public ModulesFileLoader(File moduleFile) {
        this.moduleFile = moduleFile;
        try {
            this.loadContents();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContents() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(moduleFile));
        StringBuilder sb = new StringBuilder();
        reader.lines()
                .filter(str -> !str.startsWith("#") && !"".equals(str))
                .filter(str -> str.matches("repo(.+)") || str.matches("implementation(.+)") || str.matches("http_url(.+)"))
                .forEach(str -> {
                    sb.append(str).append(",");
                    String value = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
                    if (str.startsWith("repo")) {
                        this.repo = value;
                    }else if (str.startsWith("implementation")) {
                        String[] s = value.split(":");
                        this.dependencies.put(s[1] + "-" + s[2], value);
                        this.httpUrls.put(s[1] + "-" + s[2], repo + "/" + s[0].replace(".", "/") + "/" + s[1] + "/" + s[2] + "/" + s[1] + "-" + s[2] + ".jar");
                    }else if (str.startsWith("http_url")) {
                        String[] s = value.split(":");
                        this.httpUrls.put(s[0], s[1]+":"+s[2]);
                    }
                });
        this.contents = sb.toString();
        reader.close();
    }
}
