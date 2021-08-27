package cn.lanink.gamecore.modelmanager.newmodelpojo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class Description {

    public String identifier;

    @SerializedName("texture_width")
    public int textureWidth = 128;

    @SerializedName("texture_height")
    public int textureHeight = 128;

    @SerializedName("visible_bounds_width")
    public double visibleBoundsWidth;

    @SerializedName("visible_bounds_height")
    public double visibleBoundsHeight;

    @SerializedName("visible_bounds_offset")
    public List<Double> visibleBoundsOffset;



    public Description(String identifier, int textureWidth, int textureHeight, double visibleBoundsWidth, double visibleBoundsHeight, List<Double> visibleBoundsOffset) {
        this.identifier = identifier;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.visibleBoundsWidth = visibleBoundsWidth;
        this.visibleBoundsHeight = visibleBoundsHeight;
        this.visibleBoundsOffset = visibleBoundsOffset;
    }

    public static Description generateDefault() {
        return new Description("geometry.test", 64, 64, 3D, 3D, Arrays.asList(0D,0.75D,0D));
    }

}
