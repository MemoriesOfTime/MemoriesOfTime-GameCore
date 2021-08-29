package cn.lanink.gamecore.modelmanager.newmodelpojo;

import java.util.Arrays;
import java.util.List;

public class Cube {

    public List<Double> origin;
    public List<Double> size;
    public List<Double> pivot;
    public List<Double> rotation;
    public List<Integer> uv;
    public boolean mirror;

    public Cube(List<Double> origin, List<Double> size, List<Double> pivot, List<Double> rotation, List<Integer> uv, boolean mirror) {
        this.origin = origin;
        this.size = size;
        this.pivot = pivot;
        this.rotation = rotation;
        this.uv = uv;
        this.mirror = mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public void setUv(int x, int y) {
        this.uv = Arrays.asList(x, y);
    }

    public void setRotation(double x, double y, double z) {
        this.rotation = Arrays.asList(x, y, z);
    }

    public void setPivot(double x, double y, double z) {
        this.pivot = Arrays.asList(x, y, z);
    }

    public void setOrigin(double x, double y, double z) {
        this.origin = Arrays.asList(x, y, z);
    }

    public void setSize(double x, double y, double z) {
        this.size = Arrays.asList(x, y, z);
    }

}
