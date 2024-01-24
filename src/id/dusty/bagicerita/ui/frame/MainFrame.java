package id.dusty.bagicerita.ui.frame;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import id.dusty.bagicerita.entity.Post;
import id.dusty.bagicerita.entity.User;
import id.dusty.bagicerita.service.PostService;
import id.dusty.bagicerita.service.UserService;
import id.dusty.bagicerita.ui.components.PostListPanel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.Instant;
import java.util.List;
import java.util.regex.*;

public class MainFrame extends JFrame {
    private JButton btnAdd;
    private JButton btnAksi;
    private JScrollPane postScrollPane;
    private PostListPanel postListPanel;
    private JLabel lblUsername;
    private String username;

    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Bagi Cerita");
        setMinimumSize(new Dimension(1280, 720));
        setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(null);
        loginHandler();
    }

    private void loginHandler(){
        loginPanel();
        if (this.username != null){
            initComponents();
            initData();
        }
    }

    private void initData() {
        List<Post> postList = PostService.getAll();
        for (Post post: postList) {
            postListPanel.addPost(post);
        }
    }

    private void initComponents() {
        lblUsername = new JLabel("Welcome, " + this.username + "!"); 
        btnAdd = new JButton("Tambahkan utas");
        btnAksi = new JButton("Aksi");
        postListPanel = new PostListPanel();
        postScrollPane = new JScrollPane(postListPanel);

        JPanel navbarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Gunakan FlowLayout
        navbarPanel.add(lblUsername);
        navbarPanel.add(btnAdd);
        navbarPanel.add(btnAksi);
        JLabel beranda = new JLabel("Beranda");
        Border border = beranda.getBorder();
        Border margin = new EmptyBorder(10,10,10,10);
        beranda.setBorder(new CompoundBorder(border, margin));

        JSplitPane navbarSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, beranda, navbarPanel);
        navbarSplit.setDividerSize(0);
        add(navbarSplit, BorderLayout.NORTH); // Tambahkan panel navbar ke bagian atas

        // Buat JSplitPane untuk memisahkan panel atas dan bawah
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, navbarSplit, postScrollPane);

        // Nonaktifkan fungsionalitas pemisahan dan pengaturan lebar otomatis
        splitPane.setDividerSize(0);
        add(splitPane, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> inputPanel(0, "", "", 1));
        btnAksi.addActionListener(e -> profilePanel());
    }

    private void inputPanel(int id, String dj, String dk, int act){
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField _judul = new JTextField();
        JTextArea _konten = new JTextArea(5, 20);
        _judul.setText(dj);
        _konten.setText(dk);
        panel.add(new JLabel("Judul: "));
        panel.add(_judul);

        panel.add(new JLabel("Konten/Isi: "));
        panel.add(new JScrollPane(_konten));

        int res = JOptionPane.showOptionDialog(
            null,
            panel,
            "Isi judul dan konten",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null, // icon
            null, // options
            JOptionPane.UNINITIALIZED_VALUE // initial value
    );

    if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
        // Tombol silang atau Cancel ditekan
        return;
    }
        if (res == JOptionPane.OK_CANCEL_OPTION){
            JOptionPane.showMessageDialog(
                this,
                "Silakan isi judul dan isi konten.",
                "Peringatan",
                JOptionPane.OK_OPTION
            );
            return;
        }
        String judul = _judul.getText();
        String konten = _konten.getText();
        Post post = new Post();
        if (id != 0){
            post = PostService.get(id);
        }
        post.setPenulis(username);
        post.setJudul(judul);
        post.setKonten(konten);
        if (judul.trim().isEmpty() && konten.trim().isEmpty()) {
            errBox("Judul dan konten tidak boleh kosong.");
        } else if (judul.trim().isEmpty()) {
            errBox("Judul tidak boleh kosong.");
        } else if (konten.trim().isEmpty()) {
            errBox("Konten tidak boleh kosong.");
        } else {
            if (act == 1){
                post.setTimestamp(Instant.now().toEpochMilli());
                PostService.create(post);
                postListPanel.addPost(post);
            } else if (act == 2){
                post.setEditTimestamp(Instant.now().toEpochMilli());
                PostService.update(post);
                postListPanel.updatePostList();
                initData();
                errBox("Utas berhasil diperbarui.");
            } else {
                errBox("Gagal memposting cerita.");
            }
        }
    }
    
    private void profilePanel(){
        boolean cek = false;
        while (!cek){
            JPanel panel = new JPanel(new GridLayout(2, 1));
            JTextField idPost = new JTextField();
            panel.add(new JLabel("ID Post: "));
            panel.add(idPost);
            
            int res = JOptionPane.showOptionDialog(
                this,
                panel,
                "Masukkan post id",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Edit", "Delete"}, 
                (Object) "Delete"
                );
            
            if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
                return;
            }
            
            String id = idPost.getText();
            if (id.trim().isEmpty()) {
                errBox("ID wajib diisi sesuai utas yang kamu buat.");
            } else {
                Post cekerPost = PostService.get(Integer.parseInt(id));
                if (cekerPost == null){
                    errBox("Utas tidak ditemukan");
                }else if (cekerPost.getPenulis().equals(this.username)){
                    if (res == 0){
                        inputPanel(cekerPost.getId(), cekerPost.getJudul(), cekerPost.getKonten(), 2);
                    } else if (res == 1){
                        int resi = JOptionPane.showConfirmDialog(
                            this,
                            "Anda yakin ingin menghapus utas ini?",
                            "Peringatan",
                            JOptionPane.OK_CANCEL_OPTION
                        );
                        if (resi == 0){
                            PostService.delete(cekerPost);
                            postListPanel.updatePostList();
                            initData();
                            errBox("Utas berhasil di hapus.");
                        } else {
                            return;
                        }
                    }
                } else {
                    errBox("Utas tersebut bukan kamu yang buat!");
                }
            }
        }

    }

    private void loginPanel() {
        boolean cek = false;
        JDialog loginDialog = new JDialog(this, "Login ke Bagi Cerita", true);

        loginDialog.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        loginDialog.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                handleLoginPanelClose(loginDialog);
            }

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        while (!cek){
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JTextField _u = new JTextField(1);
            JPasswordField _p = new JPasswordField(1);
            JButton daftarBtn = new JButton("disini.");
    
            panel.add(new JLabel("Username: "));
            panel.add(_u);
    
            panel.add(new JLabel("Password: "));
            panel.add(_p);
            panel.add(createSeparator());
            panel.add(createSeparator());
            panel.add(new JLabel("Belum mempunyai akun? daftar "));
            panel.add(daftarBtn);
    
            daftarBtn.addActionListener(e -> daftarPanel());
    
            int res = JOptionPane.showConfirmDialog(
                loginDialog,
                panel,
                "Login ke Bagi Cerita",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
                handleLoginPanelClose(loginDialog);
                return;
            }
            String usernames = (String) _u.getText();
            String password = new String(_p.getPassword());
            User user = new User(usernames, password);
            if (usernames.trim().isEmpty() && password.trim().isEmpty()) {
                    errBox("Username dan password tidak boleh kosong.");
            } else if (usernames.trim().isEmpty()) {
                errBox("Username tidak boleh kosong.");
            } else if (password.trim().isEmpty()) {
                errBox("Password tidak boleh kosong.");
            } else {
                int log = UserService.login(user);
                if (log == 1){
                    errBox("Login berhasil");
                    this.username = usernames;
                    cek = true;
                } else if (log == 2){
                    errBox("Password salah");
                } else if (log == 0){
                    errBox("Username tidak ditemukan");
                } else {
                    errBox("Gagal login.");
                }
            }
        }
    }
    
    private void errBox(String teks){
        JOptionPane.showMessageDialog(
            this,
            teks,
            "Peringatan",
            JOptionPane.OK_OPTION
        );
    }

    private Component createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        return separator;
    }

    private void daftarPanel(){
        boolean cek = false;
        while (!cek){
            JPanel panel = new JPanel(new GridLayout(4, 2));
            JTextField _u = new JTextField(1);
            JPasswordField _p = new JPasswordField(1);
    
            panel.add(new JLabel("Username: "));
            panel.add(_u);
    
            panel.add(new JLabel("Password: "));
            panel.add(_p);
            panel.add(createSeparator());
            panel.add(createSeparator());
            panel.add(new JLabel("Sudah mempunyai akun? silahkan tutup window ini."));
    
            int res = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Daftar ke Bagi Cerita",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            
            if (res == JOptionPane.CLOSED_OPTION || res == JOptionPane.CANCEL_OPTION) {
                // Tombol silang atau Cancel ditekan
                return;
            }
    
            String username = (String) _u.getText();
            if (username.length() < 4) {
                errBox("Panjang username tidak boleh kurang dari 4 karakter!");
                continue;
            }
            if (!usernameHandler(username)){
                errBox("Username tidak valid!");
                continue;
            }
            String password = new String(_p.getPassword());
            User user = new User(username, password);
            if (username.trim().isEmpty() && password.trim().isEmpty()) {
                    errBox("Username dan password tidak boleh kosong.");
            } else if (username.trim().isEmpty()) {
                errBox("Username tidak boleh kosong.");
            } else if (password.trim().isEmpty()) {
                errBox("Password tidak boleh kosong.");
            } else {
                int sign = UserService.create(user);
                if (sign == 1){
                    errBox("Pendaftaran berhasil!");
                    cek = true;
                } else if (sign == 2){
                    errBox("Username \"" + username +"\" sudah digunakan.");
                } else {
                    errBox("Pendaftaran gagal!");
                }
            }
        }
    }

    private void handleLoginPanelClose(JDialog loginDialog) {
        int result = JOptionPane.showConfirmDialog(loginDialog, "Apa kamu yakin ingin keluar?",
                "Konfirmasi Keluar", JOptionPane.YES_NO_OPTION);
    
        if (result == JOptionPane.YES_OPTION) {    
            loginDialog.dispose(); // Tutup dialog login
            System.exit(0); // Contoh: Menutup aplikasi (sesuaikan dengan kebutuhan Anda)
        }
    }

    private boolean usernameHandler(String u){
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{4,20}$");
        Matcher matcher = pattern.matcher(u);
        return matcher.matches();
    }

}
