package kn.jktech.kpaint;

import com.fox2code.foxloader.launcher.FoxLauncher;
import com.fox2code.foxloader.loader.ClientMod;
import com.fox2code.foxloader.loader.Mod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class clipaint extends Mod implements ClientMod {

    public static final File PAINTINGS = new File(FoxLauncher.getGameDir(), "paintings");
public static Map<String,Painting> paintings=new HashMap<>();
    @Override
    public void onInit() {
        if (!PAINTINGS.exists()){PAINTINGS.mkdir();}

        for (File painting: PAINTINGS.listFiles()){
            if (painting.toString().charAt(0)!='.'){
                if (!painting.isDirectory()&&!painting.toString().contains(".xy")) {
                    try {
                        BufferedImage img =ImageIO.read(painting);
                        int w=img.getWidth();
                        int h=img.getHeight();
                        String name=painting.getName().replaceFirst("[.][^.]+$", "");
                        File cfg=new File(PAINTINGS,name+".xy");
                        if (cfg.exists()){
                            BufferedReader br=new BufferedReader(new FileReader(cfg));
                            int ws=Integer.parseInt(br.readLine())*32;
                            int hs=Integer.parseInt(br.readLine())*16;
                            addPainting(new Painting(name,w,h,ws,hs));
                        }
                        else {
                            addPainting(new Painting(name,w,h));}
                    } catch (IOException e) {
                        System.err.println("FAILED TO READ IMAGE "+painting.toString());
                    }
                }

            }
        }
    }
    public static void addPainting(Painting painting){

        System.out.println("loading painting "+painting.name);

        if (paintings.get(painting.name)==null)
        paintings.put(painting.name,painting);
        else
        paintings.replace(painting.name,painting);
    }
}
