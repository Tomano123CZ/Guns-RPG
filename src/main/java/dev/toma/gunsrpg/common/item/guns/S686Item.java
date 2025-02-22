package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.render.RenderConfigs;
import dev.toma.gunsrpg.client.render.item.S686Renderer;
import dev.toma.gunsrpg.common.IShootProps;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponBuilder;
import dev.toma.gunsrpg.common.item.guns.setup.WeaponCategory;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.util.SkillUtil;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static dev.toma.gunsrpg.util.properties.Properties.LOOT_LEVEL;

public class S686Item extends AbstractShotgun {

    private static final ResourceLocation AIM = GunsRPG.makeResource("s686/aim");
    private static final ResourceLocation RELOAD = GunsRPG.makeResource("s686/reload_both");
    private static final ResourceLocation RELOAD_SINGLE = GunsRPG.makeResource("s686/reload_single");
    private static final ResourceLocation UNJAM = GunsRPG.makeResource("s686/unjam");

    public S686Item(String name) {
        super(name, new Properties().setISTER(() -> S686Renderer::new).durability(230));
    }

    @Override
    public int getPelletCount(LivingEntity shooter, ItemStack stack) {
        return 8;
    }

    @Override
    public void initializeWeapon(WeaponBuilder builder) {
        builder
                .category(WeaponCategory.SG)
                .config(ModConfig.weaponConfig.s686)
                .firemodeSelector(this::switchFiremode)
                .ammo()
                    .define(AmmoMaterials.WOOD, 0)
                    .define(AmmoMaterials.STONE, 1)
                    .define(AmmoMaterials.IRON, 2)
                    .define(AmmoMaterials.LAPIS, 1)
                    .define(AmmoMaterials.GOLD, 3)
                    .define(AmmoMaterials.REDSTONE, 2)
                    .define(AmmoMaterials.EMERALD, 5)
                    .define(AmmoMaterials.QUARTZ, 4)
                    .define(AmmoMaterials.DIAMOND, 6)
                    .define(AmmoMaterials.AMETHYST, 8)
                    .define(AmmoMaterials.NETHERITE, 10)
                .build();
    }

    @Override
    protected SoundEvent getShootSound(PlayerEntity entity) {
        return ModSounds.GUN_S686;
    }

    @Override
    public int getReloadTime(IAttributeProvider provider, ItemStack stack) {
        return Attribs.S686_RELOAD.intValue(provider);
    }

    @Override
    public int getFirerate(IAttributeProvider provider) {
        return 8;
    }

    @Override
    public int getMaxAmmo(IAttributeProvider provider) {
        return 2;
    }

    @Override
    public int getUnjamTime(ItemStack stack) {
        return 70;
    }

    @Override
    protected void prepareForShooting(AbstractProjectile projectile, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            if (PlayerData.hasActiveSkill(player, Skills.S686_HUNTER)) {
                projectile.setProperty(LOOT_LEVEL, SkillUtil.HUNTER_LOOTING_LEVEL);
            }
        }
    }

    @Override
    protected float getInaccuracy(IShootProps props, LivingEntity entity) {
        float spread = super.getInaccuracy(props, entity);
        if (entity instanceof PlayerEntity) {
            if (PlayerData.hasActiveSkill((PlayerEntity) entity, Skills.S686_CHOKE)) {
                spread *= SkillUtil.CHOKE_SPREAD;
            }
        }
        return spread;
    }

    @Override
    protected float getInitialVelocity(IWeaponConfig config, LivingEntity shooter) {
        float velocity = config.getVelocity();
        if (shooter instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) shooter;
            if (PlayerData.hasActiveSkill(player, Skills.S686_EXTENDED_BARREL)) {
                return velocity * SkillUtil.EXTENDED_BARREL_VELOCITY;
            }
        }
        return velocity;
    }

    @Override
    public void onHitEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.S686_BLAZING_PELLETS)) {
            victim.setRemainingFireTicks(120);
        }
    }

    @Override
    public void onKillEntity(AbstractProjectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {
        if (shooter instanceof PlayerEntity && PlayerData.hasActiveSkill((PlayerEntity) shooter, Skills.S686_HUNTER) && victim instanceof MonsterEntity) {
            shooter.heal(2.0F);
        }
    }

    @Override
    protected float getModifiedDamageChance(float damageChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.S686_RELIABLE)) {
            return 0.85F * damageChance;
        }
        return damageChance;
    }

    @Override
    protected float getModifiedJamChance(float jamChance, IPlayerData data) {
        if (data.getSkillProvider().hasSkill(Skills.S686_RELIABLE)) {
            return 0.8F * jamChance;
        }
        return jamChance;
    }

    @Override
    public float getVerticalRecoil(IAttributeProvider provider) {
        return 3.4F * super.getVerticalRecoil(provider);
    }

    @Override
    public float getHorizontalRecoil(IAttributeProvider provider) {
        return 2.0F * super.getHorizontalRecoil(provider);
    }

    @Override
    public SkillType<?> getRequiredSkill() {
        return Skills.S686_ASSEMBLY;
    }

    @Override
    public ResourceLocation getReloadAnimation(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        int count = getAmmoCount(stack);
        return count == 0 ? RELOAD : RELOAD_SINGLE;
    }

    @Override
    public ResourceLocation getUnjamAnimationPath() {
        return UNJAM;
    }

    @Override
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return AIM;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onShoot(PlayerEntity player, ItemStack stack) {
    }

    @Override
    public IRenderConfig left() {
        return RenderConfigs.S686_LEFT;
    }

    @Override
    public IRenderConfig right() {
        return RenderConfigs.S686_RIGHT;
    }

    private Firemode switchFiremode(PlayerEntity player, Firemode firemode) {
        boolean canSwitch = firemode == Firemode.DOUBLE || PlayerData.hasActiveSkill(player, Skills.S686_CANNON_BLAST);
        return canSwitch ? firemode == Firemode.DOUBLE ? Firemode.SINGLE : Firemode.DOUBLE : firemode;
    }
}
