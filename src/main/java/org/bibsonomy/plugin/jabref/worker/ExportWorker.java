package org.bibsonomy.plugin.jabref.worker;

import java.util.Collections;
import java.util.List;

import net.sf.jabref.gui.JabRefFrame;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.FieldName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bibsonomy.common.enums.PostUpdateOperation;
import org.bibsonomy.model.BibTex;
import org.bibsonomy.model.Post;
import org.bibsonomy.model.Resource;
import org.bibsonomy.model.User;
import org.bibsonomy.plugin.jabref.BibsonomyProperties;
import org.bibsonomy.plugin.jabref.action.ShowSettingsDialogAction;
import org.bibsonomy.plugin.jabref.util.JabRefModelConverter;
import org.bibsonomy.plugin.jabref.util.WorkerUtil;
import org.bibsonomy.rest.exceptions.AuthenticationException;

/**
 * Export an entry to service
 *
 * @author Waldemar Biller <biller@cs.uni-kassel.de>
 */
public class ExportWorker extends AbstractPluginWorker {

    private static final Log LOG = LogFactory.getLog(ExportWorker.class);

    private List<BibEntry> entries;

    public void run() {
        try {
            for (BibEntry entry : entries) {
                String intrahash = entry.getField("intrahash").get();
                jabRefFrame.output("Exporting post " + entry.getCiteKey());

                // add private or public if groups is empty
                if (entry.getField("groups") == null || "".equals(entry.getField(FieldName.GROUPS))) {
                    entry.setField("groups", BibsonomyProperties.getDefaultVisibilty());
                }

                entry.setField("username", BibsonomyProperties.getUsername());
                String owner = entry.getField(FieldName.OWNER).get();
                entry.clearField(FieldName.OWNER);

                Post<BibTex> post = JabRefModelConverter.convertEntry(entry);
                if (post.getUser() == null) {
                    post.setUser(new User(BibsonomyProperties.getUsername()));
                }

                if (intrahash != null && !"".equals(intrahash)) {
                    changePost(post);
                } else {
                    createPost(post);
                }
                entry.setField("intrahash", post.getResource().getIntraHash());
                entry.setField(FieldName.OWNER, owner);

                String files = entry.getField(FieldName.FILE).get();
                if (files != null && !"".equals(files)) {
                    WorkerUtil.performAsynchronously(new UploadDocumentsWorker(jabRefFrame, entry.getField("intrahash").get(), files));
                }
            }
            jabRefFrame.output("Done.");
            return;
        } catch (AuthenticationException ex) {
            (new ShowSettingsDialogAction(jabRefFrame)).actionPerformed(null);
        } catch (Exception ex) {
            LOG.error("Failed to export post ", ex);
        } catch (Throwable ex) {
            LOG.error("Failed to export post ", ex);
        }
        jabRefFrame.output("Failed.");
    }

    private void changePost(Post<? extends Resource> post) throws Exception {
        final List<String> hashes = getLogic().updatePosts(Collections.<Post<? extends Resource>>singletonList(post), PostUpdateOperation.UPDATE_ALL);
        if (hashes.size() != 1) {
            throw new IllegalStateException("changePosts returned " + hashes.size() + " hashes");
        }
        post.getResource().setIntraHash(hashes.get(0));
    }

    private void createPost(Post<? extends Resource> post) throws Exception {
        final List<String> hashes = getLogic().createPosts(Collections.<Post<? extends Resource>>singletonList(post));
        if (hashes.size() != 1) {
            throw new IllegalStateException("createPosts returned " + hashes.size() + " hashes");
        }
        post.getResource().setIntraHash(hashes.get(0));
    }

    public ExportWorker(JabRefFrame jabRefFrame, List<BibEntry> entries) {
        super(jabRefFrame);
        this.entries = entries;
    }
}