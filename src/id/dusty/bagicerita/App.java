package id.dusty.bagicerita;

import com.formdev.flatlaf.FlatIntelliJLaf;

import id.dusty.bagicerita.ui.frame.MainFrame;

public class App {
    public static void main(String[] args) throws Exception {
        FlatIntelliJLaf.setup();

        new MainFrame().setVisible(true);
    }
}
