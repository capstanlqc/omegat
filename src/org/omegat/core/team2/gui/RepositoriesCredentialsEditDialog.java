package org.omegat.core.team2.gui;

import org.omegat.core.team2.TeamSettings;
import org.omegat.util.OStrings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;

public class RepositoriesCredentialsEditDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JTextField urlField;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JCheckBox stripSpacesCheck;
    private final JLabel statusLabel;

    private boolean confirmed = false;
    private final String originalRepo;

    private static final Set<String> ALLOWED_SCHEMES = Set.of(
            "http", "https", "git", "file", "svn", "svn+ssh", "ftp", "ftps"
    );

    public RepositoriesCredentialsEditDialog(Window owner, String title, String repoUrl) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.originalRepo = repoUrl;

        setLayout(new BorderLayout(10, 10));
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- Fields panel ---
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // URL
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_URL")), gbc);

        urlField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(urlField, gbc);

        // Username
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_USERNAME")), gbc);

        usernameField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        fieldsPanel.add(new JLabel(OStrings.getString("PREFS_REPO_CREDS_PASSWORD")), gbc);

        JPanel passPanel = new JPanel(new BorderLayout(5, 0));
        passwordField = new JPasswordField();
        passPanel.add(passwordField, BorderLayout.CENTER);

        JButton toggleButton = new JButton();
        org.openide.awt.Mnemonics.setLocalizedText(toggleButton, OStrings.getString("PREFS_REPO_CREDS_SHOW"));
        toggleButton.addActionListener(ev -> {
            if (passwordField.getEchoChar() == 0) {
                passwordField.setEchoChar((Character) UIManager.getDefaults().get("PasswordField.echoChar"));
                org.openide.awt.Mnemonics.setLocalizedText(toggleButton, OStrings.getString("PREFS_REPO_CREDS_SHOW"));
            } else {
                passwordField.setEchoChar((char) 0);
                org.openide.awt.Mnemonics.setLocalizedText(toggleButton, OStrings.getString("PREFS_REPO_CREDS_HIDE"));
            }
        });
        passPanel.add(toggleButton, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(passPanel, gbc);

        // Checkbox
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        stripSpacesCheck = new JCheckBox();
        org.openide.awt.Mnemonics.setLocalizedText(stripSpacesCheck, OStrings.getString("PREFS_REPO_CREDS_STRIP_SPACES"));
        stripSpacesCheck.setSelected(true);
        fieldsPanel.add(stripSpacesCheck, gbc);

        // Glue for expansion
        gbc.gridy = 4; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        fieldsPanel.add(Box.createVerticalGlue(), gbc);

        contentPanel.add(fieldsPanel, BorderLayout.CENTER);

        // --- Bottom: status + OK/Cancel ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusPanel.add(statusLabel);
        bottomPanel.add(statusPanel);

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
        add(contentPanel);

        // --- Prefill if editing ---
        if (repoUrl != null) {
            urlField.setText(repoUrl);
            usernameField.setText(TeamSettings.get(repoUrl + "!username"));
            String storedPwd = TeamSettings.get(repoUrl + "!password");
            if (storedPwd != null) {
                try {
                    // Attempt Base64 decode
                    String decoded = new String(Base64.getDecoder().decode(storedPwd), StandardCharsets.UTF_8);
                    passwordField.setText(decoded);
                } catch (IllegalArgumentException ex) {
                    // Not Base64? Show as-is
                    passwordField.setText(storedPwd);
                }
            }
        }

        // Sizing
        pack();
        Dimension pref = getPreferredSize();
        setMinimumSize(pref);
        setSize(new Dimension(pref.width + 250, pref.height));
        setLocationRelativeTo(owner);
        setResizable(true);
    }

    private boolean isValidURL(String s) {
        try {
            URL u = new URL(s);
            return ALLOWED_SCHEMES.contains(u.getProtocol().toLowerCase());
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
        // Encode password as Base64 before storing
        String encodedPw = Base64.getEncoder().encodeToString(pw.getBytes(StandardCharsets.UTF_8));
        TeamSettings.set(url + "!username", user);
        TeamSettings.set(url + "!password", encodedPw);
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
