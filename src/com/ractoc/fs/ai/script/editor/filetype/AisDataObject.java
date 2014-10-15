package com.ractoc.fs.ai.script.editor.filetype;

import com.jme3.asset.AssetKey;
import com.jme3.export.Savable;
import com.jme3.gde.core.assets.AssetDataObject;
import com.jme3.gde.core.assets.ProjectAssetManager;
import com.ractoc.fs.ai.AiScript;
import com.ractoc.fs.parsers.ai.AiScriptKey;
import com.ractoc.fs.ai.script.editor.AisOpenSupport;
import com.ractoc.fs.parsers.ai.AiScriptWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Children;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_AisDataObject_LOADER=Files of AisDataObject"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_AisDataObject_LOADER",
        mimeType = "application/ais",
        extension = {"ais", "AIS"})
@DataObject.Registration(
        mimeType = "application/ais",
        iconBase = "com/ractoc/fs/ai/script/editor/meta/icon.gif",
        displayName = "#LBL_AisDataObject_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/ais/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class AisDataObject extends AssetDataObject {

    private ProjectAssetManager manager;
    private Project project;
    private String fileName;
    private AiScript script;
    private static final Logger LOG = Logger.getLogger(AisDataObject.class.getName());

    public AisDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        System.setProperty(AisDataObject.class.getName() + ".level", "100");

        setupCookies();
    }

    private void setupCookies() {
        CookieSet cookies = getCookieSet();
        cookies.add((Node.Cookie) new AisOpenSupport(this));
        cookies.assign(OpenCookie.class, new AisOpenSupport(this));
        cookies.assign(CloseCookie.class, new AisOpenSupport(this));
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    public synchronized Savable loadAsset() {
        setupManager();
        setupProject();
        extractFileName();
        return loadScriptFile();
    }

    private void setupManager() {
        manager = getLookup().lookup(ProjectAssetManager.class);
        if (manager == null) {
            DialogDisplayer.getDefault().notifyLater(new NotifyDescriptor.Message("File is not part of a project!\nCannot load without ProjectAssetManager."));
        }
    }

    private void setupProject() {
        project = manager.getProject();
    }

    private void extractFileName() {
        fileName = getPrimaryFile().getPath();
    }

    private Savable loadScriptFile() {
        LOG.log(Level.INFO, "loading script file {0}", fileName);
        script = manager.loadAsset(getAssetKey());
        return script;
    }

    @Override
    public synchronized AssetKey<AiScript> getAssetKey() {
        if (assetKeyHasInCorrectType()) {
            assetKey = new AiScriptKey(super.getAssetKey().getName());
        }
        return (AiScriptKey) assetKey;
    }

    private boolean assetKeyHasInCorrectType() {
        return !(super.getAssetKey() instanceof AiScriptKey);
    }

    @Override
    protected Node createNodeDelegate() {
        return new AiScriptNode(
                this,
                Children.create(new AisChildFactory(this), true),
                getLookup());
    }

    public AiScript getScript() {
        if (script == null) {
            loadAsset();
        }
        return script;
    }

    @Override
    public synchronized void saveAsset() throws IOException {
        File outputFile = new File(fileName);
        saveAssetToFile(outputFile);
    }

    private void saveAssetToFile(File outputFile) {
        AiScriptWriter writer = new AiScriptWriter();
        writer.write(script, outputFile);
        this.setModified(false);
    }
}
