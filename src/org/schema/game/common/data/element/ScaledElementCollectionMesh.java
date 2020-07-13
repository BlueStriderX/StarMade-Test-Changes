package org.schema.game.common.data.element;

import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.graphicsengine.forms.BoundingBox;
import javax.vecmath.Vector3f;

public class ScaledElementCollectionMesh extends ElementCollectionMesh {

    private float scale;
    private SegmentPiece segmentPiece;
    private BoundingBox boundingBox;
    private ElementCollection<?, ?, ?> elementCollection;
    private Vector3f offset;

    public ScaledElementCollectionMesh(float scale, SegmentPiece segmentPiece, Vector3f offset, ElementCollection<?, ?, ?> elementCollection) {
        this.scale = scale;
        this.segmentPiece = segmentPiece;
        this.elementCollection = elementCollection;
        this.offset = offset;
        boundingBox = new BoundingBox();
        scaleMesh();
    }

    public float getScale() {
        return scale;
    }

    public Vector3f getOffset() {
        return offset;
    }

    public SegmentPiece getSegmentPiece() {
        return segmentPiece;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void scaleMesh() {
        //Get max and min
        Vector3i collectionMinInt = new Vector3i();
        elementCollection.getMin(collectionMinInt);
        Vector3i collectionMaxInt = new Vector3i();
        elementCollection.getMax(collectionMaxInt);
        Vector3f collectionMin = collectionMinInt.toVector3f();
        Vector3f collectionMax = collectionMaxInt.toVector3f();

        //Scale Collection
        collectionMin.scale(scale);
        collectionMax.scale(scale);

        //Move to SegmentPiece
        Vector3f newMin = new Vector3f();
        Vector3f newMax = new Vector3f();
        collectionMin.add(new Vector3f(collectionMin.x / 2, collectionMin.y / 2, collectionMin.z / 2));
        collectionMax.add(new Vector3f(collectionMax.x / 2, collectionMax.y / 2, collectionMax.z / 2));

        //Offset positions

    }
}