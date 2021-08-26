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
public interface IModel {

    /**
     * 从模型列表获取模型
     * @param key 模型 identifier
     * @return Skin 模型
     */
    Skin getModel(String key);

    /**
     * 从指定模型文件夹获取模型
     * @param dir Path 目录
     * @param children 子目录
     * @return Skin 模型
     * 注: 最终目录一定要是个文件夹
     */
    Skin getModel(Path dir, String... children);

    /**
     * 从指定模型文件夹获取模型
     * @param dir File 目录
     * @param children 子目录
     * @return Skin 模型
     * 注: 最终目录一定要是个文件夹
     */
    Skin getModel(File dir, String... children);

    /**
     * 从指定模型文件读取模型
     * @param json Json文件
     * @param image 图像文件
     * @return Skin 模型
     */
    Skin getModel(File json, File image);

    /**
     * 获取模型并以 key为 identifier注册进模型列表
     * @param key 模型 identifier
     * @param json Json文件
     * @param image 图像文件
     * @return Skin 模型
     */
    Skin getAndRegisterModel(String key, File json, File image);

    /**
     * 获取模型并以 key为 identifier注册进模型列表
     * @param key 模型 identifier
     * @param dir 模型目录
     * @return Skin 模型
     */
    Skin getAndRegisterModel(String key, File dir);

    /**
     * 以 key为 identifier注册进模型列表
     * @param key 模型 identifier
     * @param skin Skin 模型
     * @return 是否注册成功
     */
    boolean register(String key, Skin skin);

    /**
     * 读取指定目录并以 key为 identifier注册进模型列表
     * @param key 模型 identifier
     * @param dir 模型目录
     * @return 是否注册成功
     */
    boolean register(String key, File dir);

    /**
     * 获取模型列表
     * @return 模型列表
     */
    ConcurrentMap<String, Skin> getModels();

}
