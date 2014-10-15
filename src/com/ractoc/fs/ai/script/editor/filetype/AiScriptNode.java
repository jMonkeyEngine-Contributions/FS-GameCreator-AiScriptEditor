/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ractoc.fs.ai.script.editor.filetype;

import com.ractoc.fs.ai.AiScript;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.nodes.PropertySupport.ReadOnly;
import org.openide.nodes.Sheet;
import org.openide.util.Lookup;

/**
 *
 * @author racto_000
 */
class AiScriptNode extends DataNode {

    public AiScriptNode(AisDataObject aThis, Children kids, Lookup lookup) {
        super(aThis, kids, lookup);
    }

    @Override
    protected Sheet createSheet() {
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        sheet.put(set);
        set.put(new EntryComponentProperty(this));
        return sheet;
    }

    private class EntryComponentProperty extends ReadOnly<String> {

        private final AiScriptNode node;

        public EntryComponentProperty(AiScriptNode node) {
            super("entryComponent", String.class, "Entry Component", "Component which gets called when the script is started");
            this.node = node;
        }

        @Override
        public String getValue() {
            AisDataObject aisDataObject = (AisDataObject) node.getDataObject();
            AiScript script = aisDataObject.getScript();
            return script.getEntry();
        }
    }
}
