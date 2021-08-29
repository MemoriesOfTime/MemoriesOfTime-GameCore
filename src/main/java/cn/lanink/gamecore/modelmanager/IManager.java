package cn.lanink.gamecore.modelmanager;

@SuppressWarnings("unused")
public interface IManager {

    /**
     * @return 返回第一个模型的 identifier
     */
    String getMainIdentifier();

    /**
     * @return 返回第一个模型
     */
    IModel getMainModel();

    /**
     * @param index 目录
     * @return 返回指定角标的模型
     */
    IModel getModelFromIndex(int index);

    interface IModel {

    }
}
