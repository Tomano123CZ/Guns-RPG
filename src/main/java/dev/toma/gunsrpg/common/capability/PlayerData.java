package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.ReloadInfo;
import dev.toma.gunsrpg.common.capability.object.SkillData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<NBTTagCompound> {

    SkillData getSkillData();

    DebuffData getDebuffData();

    NBTTagCompound writePermanentData();

    AimInfo getAimInfo();

    ReloadInfo getReloadInfo();

    void readPermanentData(NBTTagCompound nbt);

    void tick();

    void sync();

    void syncCloneData();

    void handleLogin();
}
