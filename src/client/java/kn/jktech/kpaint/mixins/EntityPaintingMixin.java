package kn.jktech.kpaint.mixins;

import kn.jktech.kpaint.Painting;
import kn.jktech.kpaint.clipaint;
import net.minecraft.src.game.entity.EnumArt;
import net.minecraft.src.game.entity.other.EntityHanging;
import net.minecraft.src.game.entity.other.EntityPainting;
import net.minecraft.src.game.level.World;
import net.minecraft.src.game.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Random;

@Mixin(EntityPainting.class)
public abstract class EntityPaintingMixin extends EntityHanging {
    @Shadow
    public EnumArt art;
    @Unique
    public Painting painting=null;

    public EntityPaintingMixin(World world) {
        super(world);
    }

    public EntityPaintingMixin(World world, int x, int y, int z, int direction) {
        super(world, x, y, z, direction);
    }
    @Overwrite
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setString("painting", this.art!=null?this.art.title:"");
        tagCompound.setString("painting", this.painting!=null?this.painting.name:"");

    }
    @Overwrite
    public void readEntityFromNBT(NBTTagCompound tagCompound){
        String artTitle = tagCompound.getString("Motive");

        for (EnumArt painting : EnumArt.values()) {
            if (painting.title.equals(artTitle)) {
                this.art = painting;
            }
        }

        if (this.art == null) {
            this.art = EnumArt.Kebab;
        }
        this.painting=clipaint.paintings.get(tagCompound.getString("painting"));
        if (painting!=null)this.art=null;

        super.readEntityFromNBT(tagCompound);



    }
    @Inject(method = "scaleXZ",at = @At("HEAD"),cancellable = true)
    public void scaleXZ(CallbackInfoReturnable ci) {
        if (painting!=null){
            ci.setReturnValue(painting.scaledx/2);
            ci.cancel();
        }
    }

    @Inject(method = "scaleY",at = @At("HEAD"),cancellable = true)
    public void scaleY(CallbackInfoReturnable ci) {
        if (painting!=null){
            ci.setReturnValue(painting.scaledy);
            ci.cancel();
        }
    }

}
