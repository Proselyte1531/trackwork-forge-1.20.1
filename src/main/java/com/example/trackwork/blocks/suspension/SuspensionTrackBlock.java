package com.example.trackwork.blocks.suspension;

import com.example.trackwork.TrackworkBlockEntityTypes;
import com.example.trackwork.blocks.TrackBaseBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SuspensionTrackBlock extends TrackBaseBlock<SuspensionTrackBlockEntity> {
   public static final Property<TrackVariant> WHEEL_VARIANT = EnumProperty.create("variant", TrackVariant.class);

   public SuspensionTrackBlock(Properties properties) {
      super(properties);
      this.registerDefaultState(this.getStateDefinition().any().setValue(PART, TrackBaseBlock.TrackPart.NONE).setValue(WHEEL_VARIANT, TrackVariant.WHEEL));
   }

   @Override
   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
      super.createBlockStateDefinition(builder.add(WHEEL_VARIANT));
   }

   @Override
   public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
      ItemStack heldItem = player.getItemInHand(hand);
      if (AllItems.WRENCH.isIn(heldItem)) {
         TrackVariant old = state.getValue(WHEEL_VARIANT);
         if (old == TrackVariant.WHEEL) {
            level.setBlockAndUpdate(pos, state.setValue(WHEEL_VARIANT, TrackVariant.BLANK));
         } else {
            level.setBlockAndUpdate(pos, state.setValue(WHEEL_VARIANT, TrackVariant.WHEEL));
         }
         return InteractionResult.SUCCESS;
      } else {
         return super.use(state, level, pos, player, hand, hit);
      }
   }

   @Override
   public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
      return false;
   }

   public Class<SuspensionTrackBlockEntity> getBlockEntityClass() {
      return SuspensionTrackBlockEntity.class;
   }

   @Override
   public RenderShape getRenderShape(@NotNull BlockState pState) {
       return RenderShape.MODEL;
   }

   @Override
   public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return Shapes.empty();
   }

   @Override
   public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
      return Shapes.block();
   }

   public BlockEntityType<? extends SuspensionTrackBlockEntity> getBlockEntityType() {
      return TrackworkBlockEntityTypes.SUSPENSION_TRACK.get();
   }

   public enum TrackVariant implements StringRepresentable {
      WHEEL,
      BLANK;
      @Override
      public @NotNull String getSerializedName() {
         return Lang.asId(this.name());
      }
   }
}
