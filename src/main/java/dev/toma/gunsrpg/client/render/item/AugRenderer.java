package dev.toma.gunsrpg.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

/*
//Hand render config
//right
RenderConfig.newDef().withPosition(0.150, -0.340, -0.500).withScale(1.000F, 1.000F, 1.000F).withRotation(new Quaternion(0.077F, -0.383F, 0.000F, 0.924F)).finish();
//left
RenderConfig.newDef().withPosition(0.100, -0.500, -0.100).withScale(0.700F, 0.700F, 1.000F).withRotation(new Quaternion(0.086F, -0.574F, 0.000F, 0.819F)).finish();
 */
public class AugRenderer extends AbstractWeaponRenderer {

    @Override
    public AbstractWeaponModel getWeaponModel() {
        return WeaponModels.AUG;
    }

    @Override
    protected void positionModel(MatrixStack stack, ItemCameraTransforms.TransformType transform) {
        switch (transform) {
            case THIRD_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.15, 0.8);
                break;
            case FIRST_PERSON_RIGHT_HAND:
                stack.translate(-0.2, 0.2, 0.85);
                break;
        }
    }

    @Override
    protected void scaleModel(MatrixStack matrixStack, ItemCameraTransforms.TransformType transform) {
        super.scaleModel(matrixStack, transform);
    }

    @Override
    protected float scaleForTransform(ItemCameraTransforms.TransformType transform) {
        return transform.firstPerson() ? 0.4F : 0.25F;
    }

    @Override
    protected void transformUI(MatrixStack matrix) {
        matrix.translate(0.0, 0.5, 0.0);
    }
}
