package org.robertwojcik.emailclient;

import javafx.scene.control.TreeItem;
import org.robertwojcik.emailclient.controller.services.FetchFoldersService;
import org.robertwojcik.emailclient.model.EmailAccount;
import org.robertwojcik.emailclient.model.EmailTreeItem;

public class EmailManager {

    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<>("");

    public TreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount) {
        EmailTreeItem<String> treeItem = new EmailTreeItem<>(emailAccount.getAddress());
        FetchFoldersService fetchFoldersService =
                new FetchFoldersService(emailAccount.getStore(), treeItem);
        fetchFoldersService.start();
        foldersRoot.getChildren().add(treeItem);
    }
}
