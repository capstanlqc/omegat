package org.omegat.core.team2.gui;

import org.omegat.core.team2.TeamSettings;
import org.omegat.util.OStrings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Set;

public class RepoCredentialsEditDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTextField urlField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JCheckBox stripSpacesCheck;
    private final JLabel statusLabel;

    private boolean confirmed = false;
    private final String originalRepo;

    // Allowed URL schemes
    private static final Set<String> ALLOWED_SCHEMES = Set.of(
            "http", "https", "git", "file", "svn", "svn+ssh", "ftp", "ftps"
    );

    public RepoCredentialsEditDialog(Window owner, String title, String repoUrl) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.originalRepo = repoUrl;

        setLayout(new BorderLayout(10, 10));
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ==== Top panel with inputs ====
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // URL field
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_URL")), gbc);
        urlField = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(urlField, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_USERNAME")), gbc);
        usernameField = new JTextField();
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(usernameField, gbc);

        // Password + toggle
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_PASSWORD")), gbc);
        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passwordField = new JPasswordField();
        passPanel.add(passwordField, BorderLayout.CENTER);
        JButton toggleButton = new JButton();
        org.openide.awt.Mnemonics.setLocalizedText(toggleButton,
                OStrings.getString("PREFS_REPO_CREDS_SHOW"));
        toggleButton.addActionListener(ev -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar((Character) UIManager.getDefaults().get("PasswordField.echoChar"));
                org.openide.awt.Mnemonics.setLocalizedText(toggleButton,
                        OStrings.getString("PREFS_REPO_CREDS_SHOW"));
            } else {
                passwordField.setEchoChar((char) 0);
                org.openide.awt.Mnemonics.setLocalizedText(toggleButton,
                        OStrings.getString("PREFS_REPO_CREDS_HIDE"));
            }
        });
        passPanel.add(toggleButton, BorderLayout.EAST);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(passPanel, gbc);

        // Checkbox
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 1;
        stripSpacesCheck = new JCheckBox();
        org.openide.awt.Mnemonics.setLocalizedText(stripSpacesCheck,
                OStrings.getString("PREFS_REPO_CREDS_STRIP_SPACES"));
        stripSpacesCheck.setSelected(true);
        fieldsPanel.add(stripSpacesCheck, gbc);

        // Make fields panel expand vertically when resized
        gbc.gridx = 0; gbc.gridy = 4; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        fieldsPanel.add(Box.createVerticalGlue(), gbc);

        contentPanel.add(fieldsPanel, BorderLayout.CENTER);

        // ==== Bottom: Status + buttons (anchored) ====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel statusHolder = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusHolder.add(statusLabel);
        bottomPanel.add(statusHolder);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton();
        org.openide.awt.Mnemonics.setLocalizedText(ok, OStrings.getString("BUTTON_OK"));
        ok.addActionListener(this::onOk);
        JButton cancel = new JButton();
        org.openide.awt.Mnemonics.setLocalizedText(cancel, OStrings.getString("BUTTON_CANCEL"));
        cancel.addActionListener(e -> dispose());
        buttonsPanel.add(ok);
        buttonsPanel.add(cancel);
        bottomPanel.add(buttonsPanel);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        getContentPane().add(contentPanel);

        // Populate if editing
        if (repoUrl != null) {
            urlField.setText(repoUrl);
            usernameField.setText(TeamSettings.get(repoUrl + "!username"));
            passwordField.setText(TeamSettings.get(repoUrl + "!password"));
        }

        pack();
        Dimension pref = getPreferredSize();
        Dimension startSize = new Dimension(pref.width + 250, pref.height);
        setMinimumSize(pref);
        setSize(startSize);
        setLocationRelativeTo(owner);
        setResizable(true);
    }

    private boolean isValidURL(String s) {
        try {
            URL u = new URL(s);
            String proto = u.getProtocol().toLowerCase();
            return ALLOWED_SCHEMES.contains(proto);
        } catch (Exception e) {
            return false;
        }
    }

    private void onOk(ActionEvent e) {
        String url = urlField.getText();
        String user = usernameField.getText();
        String pw = new String(passwordField.getPassword());
        if (stripSpacesCheck.isSelected()) {
            url = url.strip();
            user = user.strip();
            pw = pw.strip();
        }
        if (url.isEmpty() || user.isEmpty() || pw.isEmpty()) {
            statusLabel.setText(OStrings.getString("PREFS_REPO_CREDS_STATUS_EMPTY"));
            return;
        }
        if (!isValidURL(url)) {
            statusLabel.setText(OStrings.getString("PREFS_REPO_CREDS_STATUS_INVALID_URL"));
            return;
        }
        if (originalRepo != null && !originalRepo.equals(url)) {
            for (Object o : TeamSettings.listKeys()) {
                String key = o.toString();
                if (key.startsWith(originalRepo + "!")) {
                    TeamSettings.set(key, null);
                }
            }
        }
        TeamSettings.set(url + "!username", user);
        TeamSettings.set(url + "!password", pw);
        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getUrl() {
        return urlField.getText();
    }
}
