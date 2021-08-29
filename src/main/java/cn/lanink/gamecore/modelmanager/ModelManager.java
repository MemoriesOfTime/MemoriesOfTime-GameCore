package cn.lanink.gamecore.modelmanager;

import cn.lanink.gamecore.GameCore;
import cn.nukkit.entity.data.Skin;
import com.google.gson.Gson;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * @author iGxnon
 * @date 2021/08/26
 */
@SuppressWarnings("unused")
public class ModelManager implements IModelManager {

    private static final ConcurrentMap<String, Skin> MODEL_LIST = new ConcurrentHashMap<>();

    private final Gson GSON = new Gson();

    @Override
    public Skin getModel(String key) {
        return MODEL_LIST.get(key);
    }

    @Override
    public Skin getModel(Path dir, String... children) {
        children = (children == null) ? new String[0] : children;
        File parent = Paths.get(dir.toString(), children).toFile();
        return getModel(parent);
    }

    @Override
    public Skin getModel(File dir, String... children) {
        children = (children == null) ? new String[0] : children;
        File parent = Paths.get(dir.toString(), children).toFile();
        File[] data = new File[2];
        // 准备加入动画支持 [model.json picture.image animation.json]
        if(parent.listFiles().length > 3) {
            return null;
        }
        Stream.of(parent.listFiles())
                .filter(file -> file.getName().endsWith(".json") || file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))
                .forEach(file -> {
                    if(file.getName().endsWith(".json")) {
                        data[0] = file;
                    }else if(file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                        data[1] = file;
                    }
                });
        if(data[0] == null || data[1] == null) {
            return null;
        }else {
            return getModel(data[0], data[1]);
        }
    }

    @Override
    public Skin getModel(File json, File image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            Skin skin = new Skin();
            skin.setTrusted(true);
            String jsonStr = new String(Files.readAllBytes(json.toPath()));
            skin.setGeometryData(jsonStr);
            skin.setSkinId(UUID.randomUUID().toString());
            skin.setSkinData(bufferedImage);
            skin.setGeometryName(getManager(json).getMainIdentifier());
            return skin;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Skin getAndRegisterModel(String key, File json, File image) {
        Skin skin = getModel(json, image);
        if(register(key, skin)) {
            return skin;
        }
        return null;
    }

    @Override
    public Skin getAndRegisterModel(String key, File dir) {
        Skin skin = getModel(dir);
        if (register(key, skin)) {
            return skin;
        }
        return null;
    }

    @Override
    public boolean register(String key, Skin skin) {
        if(MODEL_LIST.containsKey(key)) {
            return false;
        }else {
            MODEL_LIST.put(key, skin);
            return true;
        }
    }

    @Override
    public boolean register(String key, File dir) {
        Skin skin = getModel(dir);
        return register(key, skin);
    }

    @Override
    public ConcurrentMap<String, Skin> getModels() {
        return MODEL_LIST;
    }

    @Override
    public IManager getManager(Path path) {
        return getManager(path.toFile());
    }

    @Override
    public IManager getManager(File file) {
        try {
            return getManagerFromJsonStr(new String(Files.readAllBytes(file.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
            GameCore.getInstance().getLogger().warning("IO错误导致模型管理器获取失败");
            return null;
        }
    }

    @Override
    public IManager getManagerFromJsonStr(String json) {
        if (json.contains("1.12.0")) {
            return GSON.fromJson(json, cn.lanink.gamecore.modelmanager.newmodelpojo.Manager.class);
        }
        json = "{\"modelsMap\": " + json + "}";
        return GSON.fromJson(json, cn.lanink.gamecore.modelmanager.oldmodelpojo.Manager.class);
    }

    @Override
    public String generateJsonStr(IModelManager modelManager) {
        if(modelManager instanceof cn.lanink.gamecore.modelmanager.newmodelpojo.Manager) {
            return GSON.toJson(modelManager);
        }
        String json = GSON.toJson(modelManager);
        // 裁剪去添加的头尾
        json = json.substring(13);
        json = json.substring(0, json.length()-1);
        return json;
    }

}
