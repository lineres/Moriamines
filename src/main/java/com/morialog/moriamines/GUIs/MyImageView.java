package com.morialog.moriamines.GUIs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class MyImageView extends ImageView implements Serializable {

    public MyImageView( Image image ) {
        super(image);
    }

}