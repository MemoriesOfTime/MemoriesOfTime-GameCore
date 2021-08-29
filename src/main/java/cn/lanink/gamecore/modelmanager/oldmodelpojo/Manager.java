package cn.lanink.gamecore.modelmanager.oldmodelpojo;

import cn.lanink.gamecore.modelmanager.IManager;

import java.util.Map;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
public class Manager implements IManager {

    public Map<String, Model> modelsMap;

    @Override
    public String getMainIdentifier() {
        return modelsMap.keySet().toArray(new String[0])[0];
    }

    @Override
    public IModel getMainModel() {
        return modelsMap.values().toArray(new Model[0])[0];
    }

    @Override
    public IModel getModelFromIndex(int index) {
        String key = modelsMap.keySet().toArray(new String[0])[index];
        if(key != null) {
            return modelsMap.get(key);
        }
        return null;
    }


}
