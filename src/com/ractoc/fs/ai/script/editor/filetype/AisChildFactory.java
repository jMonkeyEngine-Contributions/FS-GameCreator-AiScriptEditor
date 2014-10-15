/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ractoc.fs.ai.script.editor.filetype;

import com.ractoc.fs.ai.AiComponent;
import com.ractoc.fs.ai.AiScript;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.openide.filesystems.FileObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author racto_000
 */
public class AisChildFactory extends ChildFactory<AiComponent> {

    private final AisDataObject aisDataObject;
    private AiScript script;

    public AisChildFactory(AisDataObject aisDataObject) {
        this.aisDataObject = aisDataObject;
    }

    @Override
    protected boolean createKeys(List<AiComponent> list) {
        script = aisDataObject.getScript();
        Collection<AiComponent> components = script.getComponents();
        list.addAll(components);
        return true;
    }

    @Override
    protected Node createNodeForKey(AiComponent component) {
        Node childNode = new AiComponentNode(Children.LEAF);
        childNode.setDisplayName(component.getId());
        childNode.setValue("component", component);
        childNode.setValue("script", script);
        return childNode;
    }
}
