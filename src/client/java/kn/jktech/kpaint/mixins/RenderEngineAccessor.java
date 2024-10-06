package kn.jktech.kpaint.mixins;

import net.minecraft.src.client.renderer.RenderEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.awt.image.BufferedImage;
import java.util.HashMap;

@Mixin(RenderEngine.class)
public interface RenderEngineAccessor {
    @Accessor
    HashMap<String, Object> getTextureMap();
    @Accessor
    BufferedImage getMissingTextureImage();
}
