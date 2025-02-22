package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.RepairStationContainer;
import dev.toma.gunsrpg.common.tileentity.RepairStationTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class RepairStationBlock extends BaseHorizontalBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("container.repair_station");

    public RepairStationBlock(String name) {
        super(name, Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).strength(3.2F).noOcclusion());
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player, getMenuProvider(state, world, pos), pos);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        RepairStationTileEntity tile = (RepairStationTileEntity) world.getBlockEntity(pos);
        return new SimpleNamedContainerProvider((id, inv, ent) -> new RepairStationContainer(id, inv, tile), TITLE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new RepairStationTileEntity();
    }
}
