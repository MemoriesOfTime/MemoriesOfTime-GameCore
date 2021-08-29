package cn.lanink.gamecore.modelmanager.oldmodelpojo;

import java.util.List;

public class Cube {

    public List<Double> origin;
    public List<Double> size;
    public List<Integer> uv;

    public Cube(List<Double> origin, List<Double> size, List<Integer> uv) {
        this.origin = origin;
        this.size = size;
        this.uv = uv;
    }

}
