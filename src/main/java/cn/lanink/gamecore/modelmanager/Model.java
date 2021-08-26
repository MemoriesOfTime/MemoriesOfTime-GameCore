package cn.lanink.gamecore.modelmanager;

import cn.nukkit.entity.data.Skin;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentMap;

/**
 * @author iGxnon
 * @date 2021/08/26
 */
@SuppressWarnings("unused")
public class Model implements IModel {

    @Override
    public Skin getModel(String key) {
        return null;
    }

    @Override
    public Skin getModel(Path dir, String... children) {
        return null;
    }

    @Override
    public Skin getModel(File dir, String... children) {
        return null;
    }

    @Override
    public Skin getModel(File json, File image) {
        return null;
    }

    @Override
    public Skin getAndRegisterModel(String key, File json, File image) {
        return null;
    }

    @Override
    public Skin getAndRegisterModel(String key, File dir) {
        return null;
    }

    @Override
    public boolean register(String key, Skin skin) {
        return false;
    }

    @Override
    public boolean register(String key, File dir) {
        return false;
    }

    @Override
    public ConcurrentMap<String, Skin> getModels() {
        return null;
    }

}
