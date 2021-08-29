package cn.lanink.gamecore.modelmanager.oldmodelpojo;

import java.util.List;

public class Bone {

    public String name;
    public List<Double> pivot;
    public boolean mirror;
    public boolean neverRender;
    public String parent;
    public List<Integer> rotation;
    public List<Cube> cubes;

    public Bone(String name, List<Double> pivot, boolean mirror, boolean neverRender, String parent, List<Integer> rotation, List<Cube> cubes) {
        this.name = name;
        this.pivot = pivot;
        this.mirror = mirror;
        this.neverRender = neverRender;
        this.parent = parent;
        this.rotation = rotation;
        this.cubes = cubes;
    }

}
