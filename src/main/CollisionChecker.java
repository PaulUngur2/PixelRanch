package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel panel;

    public CollisionChecker(GamePanel panel) {
        this.panel = panel;
    }

    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.x + entity.hitBox.x;
        int entityRightWorldX = entity.x + entity.hitBox.x + entity.hitBox.width;
        int entityTopWorldY = entity.y + entity.hitBox.y;
        int entityBottomWorldY = entity.y + entity.hitBox.y + entity.hitBox.height;

        int entityLeftColumn = entityLeftWorldX / panel.tilesSize;
        int entityRightColumn = entityRightWorldX / panel.tilesSize;
        int entityTopRow = entityTopWorldY / panel.tilesSize;
        int entityBottomRow = entityBottomWorldY / panel.tilesSize;

        int tileNumber1, tileNumber2;

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
