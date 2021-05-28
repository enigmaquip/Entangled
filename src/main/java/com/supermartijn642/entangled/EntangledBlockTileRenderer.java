package com.supermartijn642.entangled;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;

/**
 * Created 3/16/2020 by SuperMartijn642
 */
public class EntangledBlockTileRenderer extends TileEntityRenderer<EntangledBlockTile> {

    private static int depth = 0;

    public EntangledBlockTileRenderer(TileEntityRendererDispatcher dispatcher){
        super(dispatcher);
    }

    @Override
    public void render(EntangledBlockTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn){
        if(!tileEntityIn.isBound())
            return;

        Block boundBlock = tileEntityIn.getWorld().func_234923_W_() == tileEntityIn.getBoundDimension() ? tileEntityIn.getWorld().getBlockState(tileEntityIn.getBoundBlockPos()).getBlock() : null;
        TileEntity boundTile = tileEntityIn.getWorld().func_234923_W_() == tileEntityIn.getBoundDimension() ? tileEntityIn.getWorld().getTileEntity(tileEntityIn.getBoundBlockPos()) : null;
        BlockState state = tileEntityIn.getBoundBlockState();

        matrixStackIn.push();

        matrixStackIn.translate(0.5, 0.5, 0.5);
        float angleX = System.currentTimeMillis() % 10000 / 10000f * 360f;
        float angleY = System.currentTimeMillis() % 11000 / 11000f * 360f;
        float angleZ = System.currentTimeMillis() % 12000 / 12000f * 360f;
        matrixStackIn.rotate(new Quaternion(angleX, angleY, angleZ, true));

        matrixStackIn.scale(0.55f, 0.55f, 0.55f);
        matrixStackIn.translate(-0.5, -0.5, -0.5);

        if(boundBlock != null && boundTile != null && canRenderTileEntity(boundBlock.getRegistryName())){
            if(!(boundTile instanceof EntangledBlockTile) || depth < 10){
                depth++;
                TileEntityRendererDispatcher.instance.renderTileEntity(boundTile, partialTicks, matrixStackIn, bufferIn);
                depth--;
            }
        }
        if(state != null && state.getRenderType() == BlockRenderType.MODEL && canRenderBlock(state.getBlock().getRegistryName()))
            Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(state, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);

        matrixStackIn.pop();
    }

    private static boolean canRenderBlock(ResourceLocation block){
        return !Entangled.RENDER_BLACKLISTED_MODS.contains(block.getNamespace()) && !Entangled.RENDER_BLACKLISTED_BLOCKS.contains(block);
    }

    private static boolean canRenderTileEntity(ResourceLocation tile){
        return !Entangled.RENDER_BLACKLISTED_MODS.contains(tile.getNamespace()) && !Entangled.RENDER_BLACKLISTED_TILE_ENTITIES.contains(tile);
    }
}
