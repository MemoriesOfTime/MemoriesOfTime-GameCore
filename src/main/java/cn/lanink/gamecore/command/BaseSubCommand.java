package cn.lanink.gamecore.command;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;


/**
 * @author SmallasWater
 */
public abstract class BaseSubCommand {

    private final String name;

    public BaseSubCommand(String name) {
        this.name = name;
    }

    /**
     * @param sender CommandSender
     * @return boolean
     */
    public abstract boolean canUser(CommandSender sender);

    public String getDescription(){
        return "";
    }

    /**
     * 获取名称
     * @return string
     */
    public String getName(){
        return this.name;
    }

    /**
     * 获取别名
     * @return string[]
     */
    public abstract String[] getAliases();

    /**
     * 命令响应
     * @param sender the sender      - CommandSender
     * @param args   The arrugements      - String[]
     * @param label  label..
     * @return true if true
     */
    public abstract boolean execute(CommandSender sender, String label, String[] args);

    /**
     * 指令参数.
     * @return  提示参数
     * */
    public abstract CommandParameter[] getParameters();

}
