package id.dusty.bagicerita.ui.components;

import java.awt.Dimension;

import javax.swing.JLabel;

import id.dusty.bagicerita.entity.Post;

public class showPost extends JLabel {

    public showPost(Post post) {
        String edit = "";
        String pets = post.getEditTimestamp();
        if (pets != ""){
            edit = "<br>Terakhir diedit pada: " + pets;
        }
        setText(
            "<html>" +
            "<h2>" + post.getId() + " - " + post.getJudul() + "</h2>"  +
            post.getKonten().replaceAll("\n", "<br>") +
            "<br><br><h4>Diposting oleh: " + post.getPenulis() +
            "<br>Diposting pada: " + post.getTimestamp() +
            edit +
            "</h4><hr><html>"
        );
        setPreferredSize(new Dimension(400, 600));
    }

}
