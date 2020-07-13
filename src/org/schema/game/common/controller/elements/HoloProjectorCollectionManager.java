package org.schema.game.common.controller.elements;

import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementCollectionMesh;
import org.schema.game.common.data.element.ScaledElementCollectionMesh;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class HoloProjectorCollectionManager extends ElementCollectionManager<HoloProjectorUnit, HoloProjectorCollectionManager, VoidElementManager<HoloProjectorUnit, HoloProjectorCollectionManager>>  implements BlockActivationListenerInterface {

    private float projectionScale;
    private ScaledElementCollectionMesh collectionMesh;

    public HoloProjectorCollectionManager(short i, SegmentController segmentController, VoidElementManager<HoloProjectorUnit, HoloProjectorCollectionManager> holoProjectorUnitHoloProjectorCollectionManagerVoidElementManager) {
        super(i, segmentController, holoProjectorUnitHoloProjectorCollectionManagerVoidElementManager);
        projectionScale = 0.05f;
    }

    @Override
    public int onActivate(SegmentPiece segmentPiece, boolean b, boolean b1) {
        toggleProjection();
        return 0;
    }

    @Override
    public void updateActivationTypes(ShortOpenHashSet shortOpenHashSet) {

    }

    @Override
    public boolean isHandlingActivationForType(short i) {
        return false;
    }

    @Override
    public int getMargin() {
        return 0;
    }

    @Override
    protected Class<HoloProjectorUnit> getType() {
        return HoloProjectorUnit.class;
    }

    @Override
    public boolean needsUpdate() {
        return false;
    }

    @Override
    public HoloProjectorUnit getInstance() {
        return null;
    }

    @Override
    protected void onChangedCollection() {

    }

    @Override
    public GUIKeyValueEntry[] getGUICollectionStats() {
        return new GUIKeyValueEntry[0];
    }

    @Override
    public String getModuleName() {
        return null;
    }

    public void setProjectionColor(Vector4f projectionColor) {
        if(collectionMesh != null) {
            collectionMesh.setColor(projectionColor);
        }
    }

    public void setProjectionScale(float projectionScale) {
        this.projectionScale = projectionScale;
    }

    public void setTarget(SegmentController targetEntity, ElementCollection<?, ?, ?> targetElements) {
        ElementCollectionMesh rawMesh = targetElements.getMesh();
        rawMesh.setFirstVertex(new Vector3f(getElementCollections().get(0).getProjectorPos().getAbsolutePosX(), getElementCollections().get(0).getProjectorPos().getAbsolutePosY(), getElementCollections().get(0).getProjectorPos().getAbsolutePosZ()));
        
    }

    public void toggleProjection() {
        if(collectionMesh != null) {
            collectionMesh.draw();
        }
    }
}
