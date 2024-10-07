package kn.jktech.kpaint.mixins;

import kn.jktech.kpaint.Painting;
import net.minecraft.client.Minecraft;
import net.minecraft.src.client.renderer.GLAllocation;
import net.minecraft.src.client.renderer.RenderEngine;
import net.minecraft.src.client.renderer.Tessellator;
import net.minecraft.src.client.renderer.entity.Render;
import net.minecraft.src.client.renderer.entity.RenderPainting;
import net.minecraft.src.game.entity.Entity;
import net.minecraft.src.game.entity.EnumArt;
import net.minecraft.src.game.entity.other.EntityPainting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static kn.jktech.kpaint.clipaint.PAINTINGS;

@Mixin(RenderPainting.class)
public abstract class RenderArtMixin extends Render {
    @Shadow
    private void renderPainting(EntityPainting painting, int sizeX, int sizeY){}
    @Shadow
    protected abstract void renderLivingLabel(Entity entity, String label, double x, double y, double z, int distMax);
@Shadow
    private void getDirection(EntityPainting painting, float scaleX, float scaleY){}
    @Overwrite
    public void renderPainting(EntityPainting painting, double x, double y, double z, float rotation, float deltaTicks) throws NoSuchFieldException, IllegalAccessException {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        EnumArt artData = painting.art;
        Painting p= (Painting) painting.getClass().getField("painting").get(painting);

        float scale = 0.0625F;
        GL11.glScalef(scale, scale, scale);
        if (p==null) {
            this.loadTexture("/art/" + artData.title + ".png");
            this.renderPainting(painting, artData.sizeX, artData.sizeY);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            if (Minecraft.isDebugInfoEnabled()) {
                this.renderLivingLabel(painting, artData.title, x, y, z, 64);
            }
        } else {
            if (p.animated){p.tick();}
            this.loadTexture(p);
            this.renderPainting(painting, p.scaledx/2, p.scaledy,p);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            if (Minecraft.isDebugInfoEnabled()) {
                this.renderLivingLabel(painting, p.name, x, y, z, 64);
            }
        }

    }
    protected void loadTexture(Painting p){
        RenderEngine re = this.renderManager.renderEngine;
        if (p.customimage){
            int id = GLAllocation.generateTextureNames();
            if (p.img!=null){

            re.setupTextureExt(p.img, id, false, false);
            ((RenderEngineAccessor)re).getTextureMap().put(p.name, id);
            re.bindTexture(id);
            }
            else {
                re.setupTextureExt(((RenderEngineAccessor)re).getMissingTextureImage(), id, false, false);
                ((RenderEngineAccessor)re).getTextureMap().put(p.name, id);
                re.bindTexture(id);
            }
            return;
        }
        File f=new File(PAINTINGS,
                p.name+(p.animated?"/"+p.keyframe:"")+".png");
        if (f.exists()){
            int id = GLAllocation.generateTextureNames();
            BufferedImage img= null;
            try {
                img = ImageIO.read(f);
            } catch (Exception e) {
                e.printStackTrace();
                re.setupTextureExt(((RenderEngineAccessor)re).getMissingTextureImage(), id, false, false);
                ((RenderEngineAccessor)re).getTextureMap().put(p.name, id);
                re.bindTexture(id);

            }

            re.setupTextureExt(img, id, false, false);

            ((RenderEngineAccessor)re).getTextureMap().put(p.name, id);
            re.bindTexture(id);}}


    private void renderPainting(EntityPainting painting, int sizeX, int sizeY,Painting p) {
        float x = (float)(-sizeX) / 2.0F;
        float y = (float)(-sizeY) / 2.0F;
        float negZ = -0.5F;
        float posZ = 0.5F;

        for (int alignX = 0; alignX < sizeX / 16; alignX++) {
            for (int alignY = 0; alignY < sizeY / 16; alignY++) {
                float minXScale = x + (float)(alignX * 16);
                float maxXScale = x + (float)((alignX + 1) * 16);
                float minYScale = y + (float)(alignY * 16);
                float maxYScale = y + (float)((alignY + 1) * 16);
                this.getDirection(painting, (maxXScale + minXScale) / 2.0F, (maxYScale + minYScale) / 2.0F);
                float maxXFrame = (float)(sizeX + alignX * 16) / (float)(p.x )*((float) p.x /p.scaledx);
                float minXFrame = (float)(sizeX + (alignX + 1) * 16) / (float)(p.x )*((float) p.x /p.scaledx);
                float minYFrame = (float)(sizeY - alignY * 16) / (float)p.z*((float) p.z /p.scaledy);
                float maxYFrame = (float)(sizeY - (alignY + 1) * 16) / (float)p.z*((float) p.z /p.scaledy);
                float minX = (float)(sizeX - alignX * 16) / (float)(p.x )*((float) p.x /p.scaledx);
                float maxX = (float)(sizeX - (alignX + 1) * 16) / (float)(p.x)*((float) p.x /p.scaledx);
                float minY = (float)(sizeY - alignY * 16) / (float)p.z*((float) p.z /p.scaledy);
                float maxY = (float)(sizeY - (alignY + 1) * 16) / (float)p.z*((float) p.z /p.scaledy);
                Tessellator instance = Tessellator.instance;
                instance.startDrawingQuads();
                instance.setNormal(0.0F, 0.0F, -1.0F);
                instance.addVertexWithUV((double)maxXScale, (double)minYScale, (double)negZ, (double)maxX, (double)minY);
                instance.addVertexWithUV((double)minXScale, (double)minYScale, (double)negZ, (double)minX, (double)minY);
                instance.addVertexWithUV((double)minXScale, (double)maxYScale, (double)negZ, (double)minX, (double)maxY);
                instance.addVertexWithUV((double)maxXScale, (double)maxYScale, (double)negZ, (double)maxX, (double)maxY);
                instance.setNormal(0.0F, 0.0F, 1.0F);
                instance.addVertexWithUV(
                        (double)maxXScale, (double)maxYScale, (double)posZ, (double)minXFrame, (double)maxYFrame
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)maxYScale, (double)posZ, (double)maxXFrame, (double)maxYFrame
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)minYScale, (double)posZ, (double)maxXFrame, (double)minYFrame
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)minYScale, (double)posZ, (double)minXFrame, (double)minYFrame
                );
                instance.setNormal(0.0F, 1.0F, 0.0F);
                instance.addVertexWithUV(
                        (double)maxXScale, (double)maxYScale, (double)negZ, (double)minX, (double)minY / 16.0
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)maxYScale, (double)negZ, (double)maxX, (double)minY / 16.0
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)maxYScale, (double)posZ, (double)maxX, (double)maxY / 16.0
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)maxYScale, (double)posZ, (double)minX, (double)maxY / 16.0
                );
                instance.setNormal(0.0F, -1.0F, 0.0F);
                instance.addVertexWithUV(
                        (double)maxXScale, (double)minYScale, (double)posZ, (double)minX, (double)minY / 16.0
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)minYScale, (double)posZ, (double)maxX, (double)minY / 16.0
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)minYScale, (double)negZ, (double)maxX, (double)maxY / 16.0
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)minYScale, (double)negZ, (double)minX, (double)maxY / 16.0
                );
                instance.setNormal(-1.0F, 0.0F, 0.0F);
                instance.addVertexWithUV(
                        (double)maxXScale, (double)maxYScale, (double)posZ, (double)maxX / 256.0, (double)maxY
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)minYScale, (double)posZ, (double)maxX / 256.0, (double)minY
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)minYScale, (double)negZ, (double)minX / 256.0, (double)minY
                );
                instance.addVertexWithUV(
                        (double)maxXScale, (double)maxYScale, (double)negZ, (double)minX / 256.0, (double)maxY
                );
                instance.setNormal(1.0F, 0.0F, 0.0F);
                instance.addVertexWithUV(
                        (double)minXScale, (double)maxYScale, (double)negZ, (double)maxX / 256.0, (double)maxY
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)minYScale, (double)negZ, (double)maxX / 256.0, (double)minY
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)minYScale, (double)posZ, (double)minX / 256.0, (double)minY
                );
                instance.addVertexWithUV(
                        (double)minXScale, (double)maxYScale, (double)posZ, (double)minX / 256.0, (double)maxY
                );
                instance.draw();
            }
        }
    }
}