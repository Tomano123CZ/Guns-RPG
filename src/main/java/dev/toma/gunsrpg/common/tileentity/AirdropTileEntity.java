package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.resource.airdrop.AirdropLootManager;
import dev.toma.gunsrpg.resource.airdrop.IAirdropContentProvider;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;
import java.util.function.Supplier;

public class AirdropTileEntity extends InventoryTileEntity {

    public AirdropTileEntity() {
        this(ModBlockEntities.AIRDROP.get());
    }

    protected AirdropTileEntity(TileEntityType<? extends AirdropTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }

    public void generateLoot() {
        Lifecycle lifecycle = GunsRPG.getModLifecycle();
        AirdropLootManager lootManager = lifecycle.getAirdropLootManager();
        IAirdropContentProvider contentProvider = lootManager.getGeneratedContent();
        ItemStack[] itemStacks = contentProvider.getItems();
        for (int i = 0; i < itemStacks.length; i++) {
            setItem(i, itemStacks[i]);
        }
    }
}
