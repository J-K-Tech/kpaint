package kn.jktech.kpaint.mixins;

import kn.jktech.kpaint.Painting;
import net.minecraft.src.game.entity.Entity;
import net.minecraft.src.game.entity.other.EntityPainting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Random;

@Mixin(Entity.class)
public interface EntityPaintingAccessor {
    @Accessor
    public Random getRand();

}
