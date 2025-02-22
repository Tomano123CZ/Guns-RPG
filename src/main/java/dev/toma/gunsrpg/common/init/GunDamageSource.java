package dev.toma.gunsrpg.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class GunDamageSource extends EntityDamageSource {

    @Nullable
    private final Entity src;
    @Nullable
    private final Entity directSource;
    private final ItemStack stacc;

    protected GunDamageSource(Entity src, Entity directSource, ItemStack stacc) {
        super("mob", src);
        this.src = src;
        this.directSource = directSource;
        this.stacc = stacc;
    }

    @Nullable
    @Override
    public Entity getDirectEntity() {
        return directSource;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return null;
    }

    public Entity getSrc() {
        return src;
    }

    public ItemStack getStacc() {
        return stacc;
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity entityLivingBaseIn) {
        ItemStack itemstack = this.src instanceof LivingEntity ? ((LivingEntity) this.src).getMainHandItem() : ItemStack.EMPTY;
        String s = "death.attack." + this.msgId;
        String s1 = s + ".item";
        return !itemstack.isEmpty() ? new TranslationTextComponent(s1, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName(), itemstack.getDisplayName()) : new TranslationTextComponent(s, entityLivingBaseIn.getDisplayName(), this.src.getDisplayName());
    }
}
