package kn.jktech.kpaint.mixins;

import net.minecraft.src.game.item.ItemHangingPainting;
import net.minecraft.src.game.item.ItemStack;
import net.minecraft.src.game.item.description.ItemDescPainting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemDescPainting.class)
public class ItemDescPaintingMixin {
    @Inject(method = "runDesc",at = @At("HEAD"),cancellable = true)
    public void runDesc(List<String> desc, ItemStack item, CallbackInfo ci) {
        if ((item.getItem() instanceof ItemHangingPainting)) {
            if (item.nbtTagCompound!=null && item.getItemDamage()==999){
                desc.add("\u00a77" +item.nbtTagCompound.getString("painting"));
                ci.cancel();
            }
        }
    }
}
