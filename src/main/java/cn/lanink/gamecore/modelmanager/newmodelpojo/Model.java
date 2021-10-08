package cn.lanink.gamecore.modelmanager.newmodelpojo;


import cn.lanink.gamecore.modelmanager.IManager;

import java.util.List;

public class Model implements IManager.IModel {

    public Description description;
    public List<Bone> bones;

    public Model(Description description, List<Bone> bones) {
        this.description = description;
        this.bones = bones;
    }

    public Description getDescription() {
        return description;
    }

    public List<Bone> getBones() {
        return bones;
    }

}
