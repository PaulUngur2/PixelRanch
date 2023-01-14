package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel panel;
    public Tile [] tiles;
    public int[][] mapTileNumber; // 2D array of tile numbers

    public TileManager(GamePanel panel) {
        this.panel = panel;
        tiles = new Tile[16];
        mapTileNumber = new int[panel.maxScreenCol][panel.maxScreenRow];
        getTileImage();
        loadMap("/map/map1");

    }

    private void getTileImage() {
            setUp(0, "blank", false);
            setUp(1, "gravel", false);
            setUp(2, "grass", false);
            setUp(3, "field", true);
            setUp(4, "farm_fence", true);
            setUp(5, "house_1", true);
            setUp(6, "house_2", true);
            setUp(7, "fence", true);
            setUp(8, "fence_field", true);
            setUp(9, "farm_end", true);
            setUp(10, "farm", true);
            setUp(11, "field_end", true);
            setUp(12,"house1", true);
            setUp(13,"house2", true);
    }

    public void setUp(int index, String name, boolean collision){
        UtilityTool tool = new UtilityTool();

        try {
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/tile/" + name + ".png"));
            tiles[index].image = tool.scaleImage(tiles[index].image, panel.tilesSize, panel.tilesSize);
            tiles[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadMap(String path) {

        try {
            InputStream in = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            int col = 0;
            int row = 0;

            while (col < panel.maxScreenCol && row < panel.maxScreenRow) {
                String line = br.readLine();
                while (col < panel.maxScreenCol) {

                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNumber[col][row] = num;
                    col++;
                }

                if (col == panel.maxScreenCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
        int col = 0;
        int row = 0;
        int x = 0;
        int y = 0;
        while(col < panel.maxScreenCol && row < panel.maxScreenRow) {

            int tileNumber = mapTileNumber[col][row];

            g2d.drawImage(tiles[tileNumber].image, x, y, null);
            col++;
            x += panel.tilesSize;
            if(col == panel.maxScreenCol) {
                col = 0;
                row++;
                x = 0;
                y += panel.tilesSize;
            }
        }
    }

}
