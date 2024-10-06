package kn.jktech.kpaint.mixins;

import kn.jktech.kpaint.Painting;
import kn.jktech.kpaint.clipaint;
import net.minecraft.src.game.entity.EnumArt;
import net.minecraft.src.game.entity.other.EntityPainting;
import net.minecraft.src.game.item.ItemHangingPainting;
import net.minecraft.src.game.item.ItemStack;
import net.minecraft.src.game.level.World;
import net.minecraft.src.game.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(ItemHangingPainting.class)
public class ItemHangingPaintingMixin {
@Inject(method = "spawnedEntity",at = @At("HEAD"),cancellable = true)
    public void spawnedEntity(ItemStack itemstack, World world, int x, int y, int z, int direction, CallbackInfoReturnable ci) throws NoSuchFieldException, IllegalAccessException {
    EntityPainting painting= new EntityPainting(world, x, y, z, direction,EnumArt.Kebab);
    if (itemstack.nbtTagCompound!=null){
    String paint=itemstack.nbtTagCompound.getString("painting");
if (paint!=""){
    painting.art=null;
    Painting p=clipaint.paintings.get(paint);
    painting.getClass().getField("painting").set(painting,p);
    painting.setDirection(direction);
    ci.setReturnValue(painting);
    ci.cancel();
    return;
}}
        ArrayList<EnumArt> artArr = new ArrayList<EnumArt>();

        for (EnumArt p : EnumArt.values()) {
            painting.art = p;
            painting.setDirection(direction);
            if (painting.onValidSurface()) {
                artArr.add(p);
            }
        }

        if (artArr.size() > 0) {
            painting.art = artArr.get(world.rand.nextInt(artArr.size()));
        }

        ArrayList<Painting> pArr=new ArrayList<>();
        for (Painting p: clipaint.paintings.values()){
            painting.getClass().getField("painting").set(painting,p);

            painting.setDirection(direction);
            if (painting.onValidSurface()) {
                pArr.add(p);
            }
        }


        if (pArr.size()>0){
            float arti= artArr.size();
            float painti= pArr.size();
            float chance=arti==painti?.5f:arti>painti?painti/arti:1.f-(arti/painti);
            float r=world.rand.nextFloat();
            if (r<chance){
                painting.art=null;
                painting.getClass().getField("painting").set(painting,pArr.get(world.rand.nextInt(pArr.size())));
            }
        else {
            painting.getClass().getField("painting").set(painting,null);
        }}
    painting.setDirection(direction);
        ci.setReturnValue(painting);
        ci.cancel();
    }
}
