package cn.lanink.gamecore.modelmanager.oldmodelpojo;

import cn.lanink.gamecore.modelmanager.IManager;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model implements IManager.IModel {

    @SerializedName("texturewidth")
    public int textureWidth;

    @SerializedName("textureheight")
    public int textureHeight;

    public int visible_bounds_width;

    public int visible_bounds_height;

    public List<Integer> visible_bounds_offset;

    public List<Bone> bones;

    public Bone getBoneFromName(String name) {
        for (Bone bones : this.bones){
            if(bones.name.equals(name)){
                return bones;
            }
        }
        return null;
    }

    public Model(int textureWidth, int textureHeight, int visible_bounds_width, int visible_bounds_height, List<Integer> visible_bounds_offset, List<Bone> bones) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.visible_bounds_width = visible_bounds_width;
        this.visible_bounds_height = visible_bounds_height;
        this.visible_bounds_offset = visible_bounds_offset;
        this.bones = bones;
    }

}
