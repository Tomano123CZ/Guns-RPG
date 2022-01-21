package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlockEntities {
    private static final DeferredRegister<TileEntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, GunsRPG.MODID);

    public static final RegistryObject<TileEntityType<AirdropTileEntity>> AIRDROP = register("airdrop", AirdropTileEntity::new, () -> new Block[] {ModBlocks.AIRDROP});
    public static final RegistryObject<TileEntityType<BlastFurnaceTileEntity>> BLAST_FURNACE = register("blast_furnace", BlastFurnaceTileEntity::new, () -> new Block[] {ModBlocks.BLAST_FURNACE});
    public static final RegistryObject<TileEntityType<DeathCrateTileEntity>> DEATH_CRATE = register("death_crate", DeathCrateTileEntity::new, () -> new Block[] {ModBlocks.DEATH_CRATE});
    public static final RegistryObject<TileEntityType<SmithingTableTileEntity>> SMITHING_TABLE = register("smithing_table", SmithingTableTileEntity::new, () -> new Block[] {ModBlocks.SMITHING_TABLE});
    public static final RegistryObject<TileEntityType<CookerTileEntity>> COOKER = register("cooker", CookerTileEntity::new, () -> new Block[] { ModBlocks.COOKER });

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> supplier, Supplier<Block[]> blockSupplier) {
        return TYPES.register(name, () -> TileEntityType.Builder.of(supplier, blockSupplier.get()).build(null));
    }

    public static void subscribe(IEventBus bus) {
        TYPES.register(bus);
    }
}
