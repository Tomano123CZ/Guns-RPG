package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class NoDamageGivenConditionProvider extends AbstractQuestConditionProvider implements IQuestCondition {

    private final ITextComponent descriptor;

    public NoDamageGivenConditionProvider(QuestConditionProviderType<? extends NoDamageGivenConditionProvider> type) {
        super(type);
        this.descriptor = new TranslationTextComponent(this.getLocalizationString());
    }

    @Override
    public ITextComponent getDescriptor() {
        return descriptor;
    }

    @Override
    public boolean isValid(PlayerEntity player, IPropertyReader reader) {
        return false;
    }

    @Override
    public IQuestCondition getCondition() {
        return this;
    }
}
