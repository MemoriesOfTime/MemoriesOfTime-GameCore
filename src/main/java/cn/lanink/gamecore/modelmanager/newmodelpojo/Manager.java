package cn.lanink.gamecore.modelmanager.newmodelpojo;

import cn.lanink.gamecore.modelmanager.IManager;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author iGxnon
 * @date 2021/08/27
 */
public class Manager implements IManager {

    public String format_version = "1.12.0";

    @SerializedName("minecraft:geometry")
    public List<Model> models;

    public Manager(List<Model> models) {
        this.models = models;
    }

    @Override
    public String getMainIdentifier() {
        return ((Model) getMainModel()).getDescription().identifier;
    }

    @Override
    public IModel getMainModel() {
        return models.get(0);
    }

    @Override
    public IModel getModelFromIndex(int index) {
        return models.get(index);
    }
}
