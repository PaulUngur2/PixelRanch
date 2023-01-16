package main;
import objects.*;
import objects.ObjectsField1;
import objects.ObjectsField2;
import objects.ObjectsShop1;
import objects.ObjectsShop2;

public class ObjectSetter {
    GamePanel panel;
    //Constructor
    public ObjectSetter(GamePanel panel) {
        this.panel = panel;
    }
    //Sets up the objects
    public void setObject() {

        panel.objects[0] = new ObjectsShop1(panel);
        panel.objects[0].x = panel.tilesSize * 7;
        panel.objects[0].y = 0;

        panel.objects[1] = new ObjectsShop2(panel);
        panel.objects[1].x = panel.tilesSize * 8;
        panel.objects[1].y = 0;

        panel.objects[2] = new ObjectsField1(panel);
        panel.objects[2].x = panel.tilesSize * 15;
        panel.objects[2].y = panel.tilesSize * 5;

        panel.objects[3] = new ObjectsField2(panel);
        panel.objects[3].x = panel.tilesSize * 15;
        panel.objects[3].y = panel.tilesSize * 6;

        panel.objects[4] = new ObjectsFarm1(panel);
        panel.objects[4].x = 0;
        panel.objects[4].y = panel.tilesSize * 5;

        panel.objects[5] = new ObjectsFarm2(panel);
        panel.objects[5].x = 0;
        panel.objects[5].y = panel.tilesSize * 6;
    }

}
