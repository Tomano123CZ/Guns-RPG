package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.DeathCrateContainer;
import dev.toma.gunsrpg.common.tileentity.DeathCrateTileEntity;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DeathCrateBlock extends BaseBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.death_crate");
    private static final VoxelShape SHAPE = VoxelShapes.box(0.0, 0.0, 0.0, 1.0, 0.4, 1.0);

    public DeathCrateBlock(String name) {
        super(name, Properties.of(Material.WOOD).strength(1.2F).randomTicks().noOcclusion());
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        TileEntity te = world.getBlockEntity(pos);
        if (te instanceof DeathCrateTileEntity) {
            DeathCrateTileEntity deathCrate = (DeathCrateTileEntity) te;
            if (deathCrate.isEmpty()) {
                world.destroyBlock(pos, false);
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider(
                (windowID, playerInventory, player) -> new DeathCrateContainer(windowID, playerInventory, (DeathCrateTileEntity) world.getBlockEntity(pos)),
                TITLE
        );
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (world.isClientSide)
            return ActionResultType.SUCCESS;
        else {
            NetworkHooks.openGui((ServerPlayerEntity) player, getMenuProvider(state, world, pos), pos);
            return ActionResultType.CONSUME;
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader reader) {
        return new DeathCrateTileEntity();
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            ModUtils.dropInventoryItems(world, pos);
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        return Collections.emptyList();
    }
}
