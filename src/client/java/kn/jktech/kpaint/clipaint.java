package kn.jktech.kpaint;

import com.fox2code.foxloader.launcher.FoxLauncher;
import com.fox2code.foxloader.loader.ClientMod;
import com.fox2code.foxloader.loader.Mod;
import net.minecraft.src.game.item.Item;
import net.minecraft.src.game.item.ItemStack;
import net.minecraft.src.game.nbt.NBTTagCompound;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.fox2code.foxloader.client.CreativeItems.addToCreativeInventory;

public class clipaint extends Mod implements ClientMod {

    public static final File PAINTINGS = new File(FoxLauncher.getGameDir(), "paintings");
public static Map<String,Painting> paintings=new HashMap<>();
    @Override
    public void onInit() {
        if (!PAINTINGS.exists()){PAINTINGS.mkdir();}

        for (File painting: PAINTINGS.listFiles()){
            if (painting.toString().charAt(0)!='.'){
                if (!painting.isDirectory()&&painting.toString().contains(".png")) {
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
                        NBTTagCompound nbt=new NBTTagCompound();
                        nbt.setString("painting",name);
                        addToCreativeInventory(new ItemStack(Item.painting, 1,999,nbt));
                    } catch (IOException e) {
                        System.err.println("FAILED TO READ IMAGE "+painting.toString());
                    }
                } else if (painting.isDirectory()) {
                    try {

                        BufferedImage img  = ImageIO.read(new File(painting,"0.png"));
                        int w=img.getWidth();
                        int h=img.getHeight();
                        String name=painting.getName();
                        File cfg=new File(painting,"xy");
                        File keyrate=new File(painting,"keyrate");
                        int rate=1;
                        int keys= Objects.requireNonNull(painting.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {

                                return name.toLowerCase().endsWith(".png");
                            }
                        })).length;

                        if (keyrate.exists()){
                            BufferedReader br=new BufferedReader(new FileReader(keyrate));
                            rate=Integer.parseInt(br.readLine());

                        }

                        if (cfg.exists()){
                            BufferedReader br=new BufferedReader(new FileReader(cfg));
                            int ws=Integer.parseInt(br.readLine())*32;
                            int hs=Integer.parseInt(br.readLine())*16;
                            addPainting(new Painting(name,w,h,ws,hs,keys,rate));
                        }
                        else {
                            addPainting(new Painting(name,w,h,keys,(float) rate));}
                        NBTTagCompound nbt=new NBTTagCompound();
                        nbt.setString("painting",name);
                        addToCreativeInventory(new ItemStack(Item.painting, 1,999,nbt));


                    } catch (IOException e) {
                        System.err.println("FAILED TO READ ANIMATION "+painting.toString());
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
