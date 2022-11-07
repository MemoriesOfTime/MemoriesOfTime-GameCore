package cn.lanink.gamecore.utils;

import cn.lanink.gamecore.GameCore;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LT_Name
 */
@SuppressWarnings("unused")
public class ConfigUtils {

    private static final String KEY_HEADER = "header";
    private static final String KEY_FOOTER = "footer";

    public static void addDescription(File file, File description) {
        addDescription(file, new Config(description));
    }

    public static void addDescription(Config config, Config description) {
        try {
            Field field = config.getClass().getDeclaredField("file");
            field.setAccessible(true);
            addDescription((File) field.get(config), description);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addDescription(File file, Config description) {
        if (!file.exists()) {
            return;
        }

        StringBuilder result = new StringBuilder();

        //添加头部
        if (description.exists(KEY_HEADER) && !description.getString(KEY_HEADER).trim().isEmpty()) {
            for (String header : description.getString(KEY_HEADER).trim().split("\n")) {
                result.append("# ").append(header).append(System.lineSeparator());
            }
            result.append(System.lineSeparator());
        }

        //添加内容
        StringBuilder keyBuilder = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {
            String line;
            LinkedList<String[]> path = new LinkedList<>();
            Pattern pattern = Pattern.compile("^( *)([a-zA-Z0-9]+):");
            int lastIdent = 0;
            String[] last = null;
            //逐行读取并添加介绍
            while ((line = in.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (!matcher.find()) {
                    result.append(line).append(System.lineSeparator());
                    continue;
                }

                String current = matcher.group(2);
                String ident = matcher.group(1);
                int newIdent = ident.length();

                //返回上一级
                if (newIdent < lastIdent) {
                    int reduced = lastIdent - newIdent; //需要移除的空格数量
                    int i = 0;
                    while (i < reduced && !path.isEmpty()) {
                        if (path.pollLast()[1].length() == newIdent) { //返回到同级后 就不需要再移除上一级了
                            break;
                        }
                        i++;
                    }
                    lastIdent = lastIdent - reduced;
                }
                //进入下一级
                if (newIdent > lastIdent) {
                    path.add(last);
                    lastIdent = newIdent;
                }
                last = new String[]{current, ident};

                keyBuilder.setLength(0);
                for (String[] part : path) {
                    keyBuilder.append('.').append(part[0]);
                }
                keyBuilder.append('.').append(current);
                String key = keyBuilder.substring(1); //忽略最开始的 . 号
                if (description.exists(key) && !description.getString(key).trim().isEmpty()) {
                    String[] comments = description.getString(key).trim().split("\n");
                    for (String comment : comments) {
                        result.append(ident).append("# ").append(comment).append(System.lineSeparator());
                    }
                }

                result.append(line).append(System.lineSeparator());
            }

            //添加尾部
            if (description.exists(KEY_FOOTER) && !description.getString(KEY_FOOTER).trim().isEmpty()) {
                result.append(System.lineSeparator()).append(System.lineSeparator()); //空两行
                for (String footer : description.getString(KEY_FOOTER).trim().split("\n")) {
                    result.append("# ").append(footer).append(System.lineSeparator());
                }
            }

            Utils.writeFile(file, result.toString());
        } catch (Exception e) {
            GameCore.getInstance().getLogger().error(e.getMessage(), e);
        }
    }

}
