package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel panel;
    //Constructor
    public CollisionChecker(GamePanel panel) {
        this.panel = panel;
    }
    //This method calculates the coordinates of the hitbox of an entity in the game world,
    //and then it calculates the rows and columns of tiles that the hitbox is in by dividing the coordinates by the size of the tiles.
    public void checkTile(Entity entity){
        // Calculate the  x and y coordinate of the entity's hitbox in the game world
        int entityLeftWorldX = entity.x + entity.hitBox.x;
        int entityRightWorldX = entity.x + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.y + entity.hitBox.y;
        int entityBottomWorldY = entity.y + entity.hitBox.y + entity.hitBox.height;
        // Calculate the row and column of the tile that the entity's hitbox is in
        int entityLeftColumn = entityLeftWorldX / panel.tilesSize;
        int entityRightColumn = entityRightWorldX / panel.tilesSize;
        int entityTopRow = entityTopWorldY / panel.tilesSize;
        int entityBottomRow = entityBottomWorldY / panel.tilesSize;

        int tileNumber1, tileNumber2;
        //In this method, the direction of an entity is being checked and based on the direction, the code calculates
        //the new row of tiles that the entity would be in if it moved in that direction. Then, it retrieves the tile numbers
        //of the tiles that the entity's hitbox would be in if it moved in that direction. After that, it checks if either of those
        //tiles have collision enabled and if they do, it sets the entity's collision property to true.
        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / panel.tilesSize;
                tileNumber1 = panel.tileManager.mapTileNumber[entityLeftColumn][entityTopRow];
                tileNumber2 = panel.tileManager.mapTileNumber[entityRightColumn][entityTopRow];
                if (panel.tileManager.tiles[tileNumber1].collision || panel.tileManager.tiles[tileNumber2].collision) {
                    entity.collision = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / panel.tilesSize;
                tileNumber1 = panel.tileManager.mapTileNumber[entityLeftColumn][entityBottomRow];
                tileNumber2 = panel.tileManager.mapTileNumber[entityRightColumn][entityBottomRow];
                if (panel.tileManager.tiles[tileNumber1].collision || panel.tileManager.tiles[tileNumber2].collision) {
                    entity.collision = true;
                }
            }
            case "left" -> {
                entityLeftColumn = (entityLeftWorldX - entity.speed) / panel.tilesSize;
                tileNumber1 = panel.tileManager.mapTileNumber[entityLeftColumn][entityTopRow];
                tileNumber2 = panel.tileManager.mapTileNumber[entityLeftColumn][entityBottomRow];
                if (panel.tileManager.tiles[tileNumber1].collision || panel.tileManager.tiles[tileNumber2].collision) {
                    entity.collision = true;
                }
            }
            case "right" -> {
                entityRightColumn = (entityRightWorldX + entity.speed) / panel.tilesSize;
                tileNumber1 = panel.tileManager.mapTileNumber[entityRightColumn][entityTopRow];
                tileNumber2 = panel.tileManager.mapTileNumber[entityRightColumn][entityBottomRow];
                if (panel.tileManager.tiles[tileNumber1].collision || panel.tileManager.tiles[tileNumber2].collision) {
                    entity.collision = true;
                }
            }
        }
    }
    //This method loops through all the objects in the game, updating the coordinates of the hitbox of each object and the entity.
    //Then it checks the direction of the entity and moves its hitbox accordingly. It then checks if the hitbox of the entity intersects
    //with the hitbox of the object and if it does, it sets the collision property of the entity to true, it also sets the index variable
    //to the index of the object that the entity collided with.
    public int checkObject(Entity entity, boolean player) {

        int index = 999;
        for (int i = 0; i < panel.objects.length; i++) {
            if (panel.objects[i] != null) {
                entity.hitBox.x = entity.x + entity.hitBox.x;
                entity.hitBox.y = entity.y + entity.hitBox.y;

                panel.objects[i].hitBox.x = panel.objects[i].x + panel.objects[i].hitBox.x;
                panel.objects[i].hitBox.y = panel.objects[i].y + panel.objects[i].hitBox.y;

                switch (entity.direction) {
                    case "up" -> {
                        entity.hitBox.y -= entity.speed;
                        if (entity.hitBox.intersects(panel.objects[i].hitBox)) {
                            if (panel.objects[i].collision) {
                                entity.collision = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                    }
                    case "down" -> {
                        entity.hitBox.y += entity.speed;
                        if (entity.hitBox.intersects(panel.objects[i].hitBox)) {
                            if (panel.objects[i].collision) {
                                entity.collision = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                    }
                    case "left" -> {
                        entity.hitBox.x -= entity.speed;
                        if (entity.hitBox.intersects(panel.objects[i].hitBox)) {
                            if (panel.objects[i].collision) {
                                entity.collision = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                    }
                    case "right" -> {
                        entity.hitBox.x += entity.speed;
                        if (entity.hitBox.intersects(panel.objects[i].hitBox)) {
                            if (panel.objects[i].collision) {
                                entity.collision = true;
                            }
                            if (player) {
                                index = i;
                            }
                        }
                    }
                }
                entity.hitBox.x = entity.hitBoxDefaultX;
                entity.hitBox.y = entity.hitBoxDefaultY;
                panel.objects[i].hitBox.x = panel.objects[i].hitBoxDefaultX;
                panel.objects[i].hitBox.y = panel.objects[i].hitBoxDefaultY;
            }
        }

        return index;
    }
}
