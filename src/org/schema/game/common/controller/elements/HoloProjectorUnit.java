package org.schema.game.common.controller.elements;

import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;

public class HoloProjectorUnit extends ElementCollection<HoloProjectorUnit, HoloProjectorCollectionManager, VoidElementManager<HoloProjectorUnit, HoloProjectorCollectionManager>> {

    private SegmentPiece projectorPos;


    public HoloProjectorUnit() {
        projectorPos = getElementCollectionId();
    }

    @Override
    public ControllerManagerGUI createUnitGUI(GameClientState gameClientState, ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager1) {
        return null;
    }

    public SegmentPiece getProjectorPos() {
        return projectorPos;
    }
}