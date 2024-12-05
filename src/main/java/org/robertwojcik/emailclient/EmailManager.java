package org.robertwojcik.emailclient;

import javafx.scene.control.TreeItem;
import org.robertwojcik.emailclient.controller.services.FetchFoldersService;
import org.robertwojcik.emailclient.controller.services.FolderUpdaterService;
import org.robertwojcik.emailclient.model.EmailAccount;
import org.robertwojcik.emailclient.model.EmailMessage;
import org.robertwojcik.emailclient.model.EmailTreeItem;

import javax.mail.Flags;
import javax.mail.Folder;
import java.util.ArrayList;
import java.util.List;

public class EmailManager {

    private List<Folder> folderList = new ArrayList<>();

    private EmailMessage selectedMessage;

    private EmailTreeItem<String> selectedFolder;

    private FolderUpdaterService folderUpdaterService;

    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    public EmailManager() {
        this.folderUpdaterService = new FolderUpdaterService(folderList);
        folderUpdaterService.start();
    }

    public EmailMessage getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(EmailMessage selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public EmailTreeItem<String> getSelectedFolder() {
        return selectedFolder;
    }

    public void setSelectedFolder(EmailTreeItem<String> selectedFolder) {
        this.selectedFolder = selectedFolder;
    }

    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService =
                new FetchFoldersService(emailAccount.getStore(), treeItem, folderList);
        fetchFoldersService.start();
        foldersRoot.getChildren().add(treeItem);
    }

    public void setRead() {
        try {
            selectedMessage.setRead(true);
            selectedMessage.getMessage().setFlag(Flags.Flag.SEEN, true);
            selectedFolder.decrementUnreadMessagesCount();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
