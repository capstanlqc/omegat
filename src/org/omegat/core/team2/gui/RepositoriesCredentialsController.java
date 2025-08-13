package org.omegat.core.team2.gui;

import java.awt.Dimension;
import java.awt.Window;
import java.net.URL;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.omegat.core.team2.TeamSettings;
import org.omegat.gui.preferences.BasePreferencesController;
import org.omegat.util.OStrings;

public class RepositoriesCredentialsController extends BasePreferencesController {

    private static final int MAX_ROW_COUNT = 10;
    private RepositoriesCredentialsPanel dialog;

    // Stage list until OK
    private List<String> stagedRepositories;

    @Override
    public JComponent getGui() {
        if (dialog == null) {
            initGui();
            initFromPrefs();
        }
        return dialog;
    }

    @Override
    public String toString() {
        return OStrings.getString("TEAM_REPOSITORIES_DIALOG");
    }

    private void initGui() {
        dialog = new RepositoriesCredentialsPanel();

        dialog.btnRemove.addActionListener(e -> removeAction());
        dialog.btnExport.addActionListener(e -> exportAction());
        dialog.btnImport.addActionListener(e -> importAction());
        dialog.btnEdit.addActionListener(e -> editAction());
        dialog.btnAdd.addActionListener(e -> addAction());

        Dimension tableSize = dialog.list.getPreferredSize();
        dialog.list.setPreferredScrollableViewportSize(
                new Dimension(tableSize.width, dialog.list.getRowHeight() * MAX_ROW_COUNT));
    }

    @Override
    public void initFromPrefs() {
        stagedRepositories = loadReposFromTeamSettings();
        dialog.setRepositories(new ArrayList<>(stagedRepositories));
    }

    @Override
    public void restoreDefaults() {
        stagedRepositories = new ArrayList<>();
        dialog.setRepositories(stagedRepositories);
    }

    @Override
    public void undoChanges() {
        initFromPrefs();
    }

    private List<String> loadReposFromTeamSettings() {
        Set<String> urls = new TreeSet<>();
        for (Object o : TeamSettings.listKeys()) {
            String key = String.valueOf(o);
            int p = key.lastIndexOf('!');
            if (p > 0) {
                urls.add(key.substring(0, p));
            }
        }
        return new ArrayList<>(urls);
    }

    private List<String> getTargetsForAction() {
        List<String> checked = dialog.getCheckedRepositories();
        if (!checked.isEmpty()) {
            return checked;
        }
        String caret = dialog.getHighlightedRepo();
        return caret != null ? List.of(caret) : List.of();
    }

    private Window getOwnerWindow() {
        return SwingUtilities.getWindowAncestor(dialog);
    }

    private boolean validateURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void removeRepoFromSettings(String repo) {
        for (Object o : TeamSettings.listKeys()) {
            String key = o.toString();
            if (key.startsWith(repo + "!")) {
                TeamSettings.set(key, null);
            }
        }
    }

    private void removeAction() {
        List<String> toRemove = getTargetsForAction();
        if (toRemove.isEmpty()) return;
        stagedRepositories.removeAll(toRemove);
        dialog.removeRepositories(toRemove);
    }

    private void exportAction() {
        List<String> toExport = getTargetsForAction();
        RepoCredentialsIO.exportRepositories(dialog, toExport);
    }

    private void importAction() {
        int importedCount = RepoCredentialsIO.importRepositories(dialog);
        if (importedCount > 0) {
            initFromPrefs();
            javax.swing.JOptionPane.showMessageDialog(dialog,
                    OStrings.getString("PREFS_REPO_CREDS_IMPORT_DONE", importedCount),
                    OStrings.getString("PREFS_REPO_CREDS_IMPORT_TITLE"),
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editAction() {
        String originalRepo = dialog.getHighlightedRepo();
        if (originalRepo == null) return;
        RepoCredentialsEditDialog dlg = new RepoCredentialsEditDialog(getOwnerWindow(),
                OStrings.getString("PREFS_REPO_CREDS_EDIT_TITLE"), originalRepo);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            String newUrl = dlg.getUrl();
            if (!validateURL(newUrl)) {
                javax.swing.JOptionPane.showMessageDialog(dialog,
                        OStrings.getString("PREFS_REPO_CREDS_INVALID_URL"),
                        OStrings.getString("ERROR_TITLE"),
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!originalRepo.equals(newUrl)) {
                removeRepoFromSettings(originalRepo);
            }
            stagedRepositories = loadReposFromTeamSettings();
            dialog.setRepositories(new ArrayList<>(stagedRepositories));
        }
    }

    private void addAction() {
        RepoCredentialsEditDialog dlg = new RepoCredentialsEditDialog(getOwnerWindow(),
                OStrings.getString("PREFS_REPO_CREDS_ADD_TITLE"), null);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
            String newUrl = dlg.getUrl();
            if (!validateURL(newUrl)) {
                javax.swing.JOptionPane.showMessageDialog(dialog,
                        OStrings.getString("PREFS_REPO_CREDS_INVALID_URL"),
                        OStrings.getString("ERROR_TITLE"),
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            stagedRepositories = loadReposFromTeamSettings();
            dialog.setRepositories(new ArrayList<>(stagedRepositories));
        }
    }

    @Override
    public void persist() {
        Set<String> currentKeys = new HashSet<>();
        for (Object o : TeamSettings.listKeys()) {
            currentKeys.add(o.toString());
        }
        for (String key : currentKeys) {
            int p = key.lastIndexOf('!');
            if (p > 0) {
                String repo = key.substring(0, p);
                if (!stagedRepositories.contains(repo)) {
                    TeamSettings.set(key, null);
                }
            }
        }
    }
}
