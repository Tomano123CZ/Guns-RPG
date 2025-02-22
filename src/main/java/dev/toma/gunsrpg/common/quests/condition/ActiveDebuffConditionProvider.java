package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ActiveDebuffConditionProvider extends AbstractQuestConditionProvider<ActiveDebuffConditionProvider> implements IQuestCondition {

    private final IDebuffType<?> debuff;
    private final ITextComponent descriptor;

    public ActiveDebuffConditionProvider(QuestConditionProviderType<?> type, IDebuffType<?> debuff) {
        super(type);
        this.debuff = debuff;
        this.descriptor = new TranslationTextComponent(this.getLocalizationString(), new TranslationTextComponent("debuff." + ModUtils.convertToLocalization(debuff.getRegistryName())));
    }

    public static ActiveDebuffConditionProvider fromNbt(QuestConditionProviderType<ActiveDebuffConditionProvider> type, CompoundNBT nbt) {
        ResourceLocation debuffId = new ResourceLocation(nbt.getString("debuff"));
        DebuffType<?> debuffType = ModRegistries.DEBUFFS.getValue(debuffId);
        return new ActiveDebuffConditionProvider(type, debuffType);
    }

    @Override
    public ActiveDebuffConditionProvider makeConditionInstance() {
        return this;
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public IQuestConditionProvider<?> getProviderType() {
        return this;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IDebuffs debuffs = data.getDebuffControl();
        return debuffs.hasDebuff(debuff);
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putString("debuff", debuff.getRegistryName().toString());
    }

    public static class Serializer implements IQuestConditionProviderSerializer<ActiveDebuffConditionProvider> {

        @Override
        public ActiveDebuffConditionProvider deserialize(QuestConditionProviderType<ActiveDebuffConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "debuff"));
            IDebuffType<?> type = ModRegistries.DEBUFFS.getValue(id);
            if (type == null) throw new JsonSyntaxException("Unknown debuff: " + id);
            return new ActiveDebuffConditionProvider(conditionType, type);
        }
    }
}
