package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.CookerContainer;
import dev.toma.gunsrpg.common.tileentity.CookerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class CookerBlock extends BaseBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.cooker");

    public CookerBlock(String name) {
        super(name, Properties.of(Material.STONE).strength(2.2F, 16.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1));
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.LIT, false));
    }

    @Override
    public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.getValue(BlockStateProperties.LIT)) {
            double y = pos.getY() + 1;
            if (random.nextDouble() <= 0.2) {
                world.playSound(null, pos.getX(), y, pos.getZ(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            double randomX = (random.nextDouble() - random.nextDouble()) * 0.4;
            double randomZ = (random.nextDouble() - random.nextDouble()) * 0.4;
            world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5 + randomX, y + 0.05, pos.getZ() + 0.5 + randomZ, 0.0, 0.0, 0.0);
        }
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? 12 : 0;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (!state.is(oldState.getBlock())) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof CookerTileEntity) {
                CookerTileEntity tileEntity = (CookerTileEntity) tile;
                InventoryHelper.dropContents(world, pos, tileEntity);
                tileEntity.getRecipesForAwardsWithExp(world, Vector3d.atCenterOf(pos));
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, oldState, p_196243_5_);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, World level, BlockPos pos) {
        return Container.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CookerTileEntity();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(BlockStateProperties.LIT);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        NetworkHooks.openGui((ServerPlayerEntity) player, this.getMenuProvider(state, world, pos), pos);
        return ActionResultType.CONSUME;
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inv, player) -> new CookerContainer(id, inv, (CookerTileEntity) world.getBlockEntity(pos)), TITLE);
    }
}
