package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModRecipeTypes;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SynchBlockEntityPacket;
import dev.toma.gunsrpg.resource.crafting.SmithingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Optional;

public class SmithingTableTileEntity extends VanillaInventoryTileEntity implements ISkilledCrafting {

    private IGridChanged gridChanged;

    public SmithingTableTileEntity() {
        this(ModBlockEntities.SMITHING_TABLE.get());
    }

    protected SmithingTableTileEntity(TileEntityType<? extends SmithingTableTileEntity> type) {
        super(type);
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(9);
    }

    @Override
    public IRecipeType<?> getRecipeType() {
        return ModRecipeTypes.SMITHING_RECIPE_TYPE;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        onSynch();
    }

    /**
     * Should be called only client-side for UI controls
     * @param callback The callback
     */
    @Override
    public void attachCallback(IGridChanged callback) {
        gridChanged = callback;
        onSynch(); // immediately updates listener
    }

    /**
     * Deletes callback reference
     */
    @Override
    public void detachCallback() {
        attachCallback(null);
    }

    @Override
    public void onSynch() {
        if (level.isClientSide) {
            if (gridChanged != null) {
                RecipeManager manager = level.getRecipeManager();
                Optional<SmithingRecipe> optional = manager.getRecipeFor(ModRecipeTypes.SMITHING_RECIPE_TYPE, this, level);
                gridChanged.onChange(optional.orElse(null));
            }
        } else {
            NetworkManager.sendWorldPacket(level, new S2C_SynchBlockEntityPacket(worldPosition));
        }
    }

}
