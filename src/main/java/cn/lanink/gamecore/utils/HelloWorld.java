package cn.lanink.gamecore.utils;

import java.io.File;

public class HelloWorld {

    public static void main(String[] args) {
        //彩蛋功能
        System.out.print("Hello! I think I need to remind you that MemoriesOfTime - GameCore is a plugin written for the nukkit server core and he can't be used alone!\n" +
                "I can help you download a nukkit server core.\n\n\n" +
                "Start downloading Nukkit...\n" +
                "please wait...\n");
        Download.download(
                "https://ci.opencollab.dev/job/NukkitX/job/Nukkit/job/master/lastSuccessfulBuild/artifact/target/nukkit-1.0-SNAPSHOT.jar",
                new File(System.getProperty("user.dir") + "/nukkit.jar"),
                file -> System.out.print("\nNukkit download is complete, You can start the server with the command 'java -jar nukkit.jar'\n")
        );
    }

}
