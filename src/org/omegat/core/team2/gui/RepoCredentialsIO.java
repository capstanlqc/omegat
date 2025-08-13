package org.omegat.core.team2.gui;

import org.omegat.core.team2.TeamSettings;
import org.omegat.util.OStrings;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class RepoCredentialsIO {

    public static void exportRepositories(Component parent, List<String> repos) {
        if (repos.isEmpty()) return;
        JFileChooser saveChooser = new JFileChooser(System.getProperty("user.home"));
        saveChooser.setDialogTitle(OStrings.getString("PREFS_REPO_CREDS_EXPORT_TITLE"));
        saveChooser.setSelectedFile(new File("export_credentials.properties"));
        saveChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                OStrings.getString("PREFS_REPO_CREDS_FILE_FILTER"), "properties"));

        if (saveChooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return;

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(saveChooser.getSelectedFile()), StandardCharsets.UTF_8))) {
            Properties exportProps = new Properties();
            for (String repo : repos) {
                String username = TeamSettings.get(repo + "!username");
                String password = TeamSettings.get(repo + "!password");
                if (username != null) exportProps.setProperty(repo + "!username", username);
                if (password != null) exportProps.setProperty(repo + "!password", password);
            }
            exportProps.store(writer, null);
            JOptionPane.showMessageDialog(parent,
                    OStrings.getString("PREFS_REPO_CREDS_EXPORT_DONE", repos.size()),
                    OStrings.getString("PREFS_REPO_CREDS_EXPORT_TITLE"),
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                    OStrings.getString("PREFS_REPO_CREDS_EXPORT_ERROR", e.getMessage()),
                    OStrings.getString("ERROR_TITLE"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static int importRepositories(Component parent) {
        JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                OStrings.getString("PREFS_REPO_CREDS_FILE_FILTER"), "properties"));
        chooser.setDialogTitle(OStrings.getString("PREFS_REPO_CREDS_IMPORT_TITLE"));

        if (chooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION) return 0;

        Properties props = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(chooser.getSelectedFile()), StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent,
                    OStrings.getString("PREFS_REPO_CREDS_IMPORT_ERROR", e.getMessage()),
                    OStrings.getString("ERROR_TITLE"),
                    JOptionPane.ERROR_MESSAGE);
            return 0;
        }

        Set<String> importedUrls = new HashSet<>();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            TeamSettings.set(key, value);
            int p = key.lastIndexOf('!');
            if (p > 0) {
                importedUrls.add(key.substring(0, p));
            }
        }
        return importedUrls.size();
    }
}
