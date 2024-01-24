package id.dusty.bagicerita.ui.components;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import id.dusty.bagicerita.entity.Post;


public class PostListPanel extends JPanel {

    public PostListPanel() {
        setLayout(new BoxLayout(
            this, 
            BoxLayout.Y_AXIS
        ));
        setAlignmentX(1);
        setAlignmentY(TOP_ALIGNMENT);
    }

    public void addPost(Post post) {
        add(new showPost(post));
        repaint();
        revalidate();
    }

    public void updatePostList() {
        removeAll();
        repaint();
        revalidate();
    }
}
