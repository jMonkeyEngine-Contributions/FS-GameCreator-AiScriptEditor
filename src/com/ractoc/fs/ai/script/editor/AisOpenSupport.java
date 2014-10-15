/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ractoc.fs.ai.script.editor;

import com.jme3.gde.core.assets.AssetData;
import com.ractoc.fs.ai.script.editor.filetype.AisDataObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.OpenSupport;
import org.openide.util.Lookup;
import org.openide.windows.CloneableTopComponent;

public class AisOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    private static final Logger logger = Logger.getLogger(AisOpenSupport.class.getName());
    private AisDataObject dataObject;
    private AssetData assetData;
    private AisTopComponent topComponent;

    public AisOpenSupport(AisDataObject ais) {
        super(ais.getPrimaryEntry());
        dataObject = ais;
        logger.info("AisOpenSupport: looking up asset");
        Lookup lookup = dataObject.getLookup();
        logger.log(Level.INFO, "AisOpenSupport: looked up asset {0}", lookup);
        assetData = lookup.lookup(AssetData.class);
    }

    @Override
    public void open() {
        super.open();
        logger.log(Level.INFO, "AisOpenSupport: opening asset {0}", assetData);
        assetData.loadAsset();
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        if (topComponent == null) {
            topComponent = new AisTopComponent();
            topComponent.setAisDataObject(dataObject);
        }
        return topComponent;
    }
}
