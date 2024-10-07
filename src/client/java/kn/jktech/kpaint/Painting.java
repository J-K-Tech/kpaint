package kn.jktech.kpaint;

import java.awt.image.BufferedImage;

public class Painting {
    public final boolean animated;
    public String name;
    public int x,z;
    public int scaledx, scaledy;
    public int keyframe=0;
    public final int keys;
    public final int keyrate;
    public int loop=0;


    public final boolean customimage=false;
    public BufferedImage img=null;
    public void tick(){
        loop++;
        if (loop>=keyrate){loop=0;
        keyframe++;
        if (keyframe>=keys){
            keyframe=0;
        }
        }
    }


    public Painting(String n,int SizeX,int Sizez){
        this(n,SizeX,Sizez,SizeX,Sizez,0,0,false);
    }
    public Painting(String n,int SizeX,int Sizez,int scalex, int scalez){
        this(n,SizeX,Sizez,scalex,scalez,0,0,false);
    }
    public Painting(String n,int SizeX,int Sizez,int scalex, int scalez,int keyss,int rate){
        this(n,SizeX,Sizez,scalex,scalez,keyss,rate,true);
    }
    public Painting(String n,int SizeX,int Sizez,int keyss,float rate){
        this(n,SizeX,Sizez,SizeX,Sizez,keyss, (int) rate,true);
    }

    private Painting(String n,int SizeX,int Sizez,int scalex, int scalez,int keyss,int rate,boolean anim){
        this.animated=anim;
        this.keys=keyss;
        this.keyframe=0;
        this.keyrate=rate;
        this.x=SizeX;
        this.scaledx=scalex;
        this.scaledy =scalez;
        this.z=Sizez;
        this.name=n;
    }


}
