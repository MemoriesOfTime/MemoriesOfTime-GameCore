package cn.lanink.gamecore.utils;

import java.io.File;

public class Joke {

    public static void main(String[] args) {
        //彩蛋功能
        System.out.print("Start downloading Nukkit...\nplease wait\n");
        Download.download(
                "https://ci.opencollab.dev/job/NukkitX/job/Nukkit/job/master/lastSuccessfulBuild/artifact/target/nukkit-1.0-SNAPSHOT.jar",
                new File(System.getProperty("user.dir") + "/nukkit.jar"),
                file -> {
                    System.out.print("Nukkit download is complete, use method: java -jar nukkit.jar\n");
                }
        );
    }

}
