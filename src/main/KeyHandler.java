package main;

import game.Animals;
import game.Crops;
import game.Database;
import game.Game;
import game.interfaces.GameLogic;
import game.interfaces.GameLogicAnimals;
import game.interfaces.GameLogicCrops;
import game.interfaces.GameLogicPlayer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.SQLException;

public class KeyHandler implements KeyListener{
    GameLogicPlayer player;
    GameLogicAnimals animals;
    GameLogicCrops crops;
    GameLogic game;
    GamePanel panel;
    Database database;
    public KeyHandler(GamePanel panel, GameLogicPlayer player, GameLogicCrops crops, GameLogicAnimals animals, GameLogic game, Database database) {
        this.panel = panel;
        this.player = player;
        this.crops = crops;
        this.animals = animals;
        this.game = game;
        this.database = database;
    }
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = e.getKeyChar();
        int code = e.getKeyCode();
        if (panel.gameState == panel.titleScreen){
            if(panel.ui.titleScreenState == 0) {
                titleScreen(code);
            } else if (panel.ui.titleScreenState == 1){
                characterScreen(code, key);
            }
        } else if (panel.gameState == panel.playState) {
            switch (code) {
                case KeyEvent.VK_W -> upPressed = true;
                case KeyEvent.VK_S -> downPressed = true;
                case KeyEvent.VK_A -> leftPressed = true;
                case KeyEvent.VK_D -> rightPressed = true;
                case KeyEvent.VK_ESCAPE -> {
                    panel.gameState = panel.pauseState;
                    panel.stopMusic();
                }
                case KeyEvent.VK_ENTER -> enterPressed = true;
                case KeyEvent.VK_1 -> {
                    try {
                        game.day();
                    } catch (Game.GameSystemException | Crops.FieldsException ex) {
                        panel.ui.addMessage(ex.getMessage());
                    }
                    panel.player.setDefaultValues();
                    panel.ui.addMessage("Day " + player.getDays());
                }
            }
        } else if (panel.gameState == panel.menuState) {
            switch (panel.ui.interaction) {
                case 1 -> {
                    if (panel.ui.buyScreenState == 0 && panel.ui.sellScreenState == 0) {
                        shopScreen(code);
                    } else if (panel.ui.buyScreenState == 1) {
                        buyScreen(code);
                    } else if (panel.ui.sellScreenState == 1) {
                        sellScreen(code);
                    }
                }
                case 2 -> {
                    switch (panel.ui.fieldScreenState) {
                        case 0 -> fieldScreen(code);
                        case 1 -> {
                            crops.setOption(1);
                            plantScreen(code);
                        }
                        case 2 -> {
                            crops.setOption(2);
                            menuFieldSwitch(code);
                        }
                        case 3 -> {
                            crops.setOption(3);
                            menuFieldSwitch(code);
                        }
                        case 4 -> {
                            crops.setOption(1);
                            menuFieldSwitch(code);
                        }
                    }
                }
                case 3 -> {
                    switch (panel.ui.fieldFarmScreenState) {
                        case 0 -> farmScreen(code);
                        case 1 -> {
                            animals.setOption(1);
                            buyAnimalScreen(code);
                        }
                        case 2 -> {
                            animals.setOption(2);
                            menuFarmSwitch(code);
                        }
                        case 3 -> {
                            animals.setOption(3);
                            menuFarmSwitch(code);
                        }
                        case 4 -> {
                            animals.setOption(4);
                            menuFarmSwitch(code);
                        }
                        case 5 -> {
                            animals.setOption(1);
                            menuFarmSwitch(code);
                        }
                    }
                }
            }
        } else if (panel.gameState == panel.pauseState) {
            switch (code) {
                case KeyEvent.VK_ESCAPE -> {
                    panel.gameState = panel.playState;
                    panel.playMusic(0);
                }
                case KeyEvent.VK_W -> {
                    panel.ui.pauseCommandNum--;
                    if (panel.ui.pauseCommandNum < 0) {
                        panel.ui.pauseCommandNum = 2;
                    }
                }
                case KeyEvent.VK_S -> {
                    panel.ui.pauseCommandNum++;
                    if (panel.ui.pauseCommandNum > 2) {
                        panel.ui.pauseCommandNum = 0;
                    }
                }
                case KeyEvent.VK_ENTER -> {
                    switch (panel.ui.pauseCommandNum) {
                        case 0 -> {
                            panel.gameState = panel.playState;
                            panel.playMusic(0);
                        }
                        case 1 -> {
                            game.restart();
                            panel.player.setDefaultValues();
                        }
                        case 2 -> {
                            game.shutdownHook(database, player.getName());
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }

    private void titleScreen(int code) {
        switch (code) {
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 1;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 1) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_A -> {
                panel.ui.difficulty--;
                if (panel.ui.difficulty < 0) {
                    panel.ui.difficulty = 1;
                }
            }
            case KeyEvent.VK_D -> {
                panel.ui.difficulty++;
                if (panel.ui.difficulty > 1) {
                    panel.ui.difficulty = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (panel.ui.commandNum == 0) {
                    try{
                        if (panel.ui.difficulty == 0) {
                            player.setConfigDifficulty("normal");
                        } else if (panel.ui.difficulty == 1) {
                            player.setConfigDifficulty("hard");
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    panel.ui.titleScreenState = 1;
                } else if (panel.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        }
    }

    private void characterScreen(int code, char key) {
        if(code == KeyEvent.VK_BACK_SPACE) {
            if(panel.ui.name.length() > 0) {
                panel.ui.name = panel.ui.name.substring(0, panel.ui.name.length() - 1);
            }
        } else if(code >= KeyEvent.VK_A && code <= KeyEvent.VK_Z) {
            if (panel.ui.name.length() < 5) {
                panel.ui.name += String.valueOf(key).toUpperCase();
            }
        } else if (code == KeyEvent.VK_ENTER) {
            if (panel.ui.name.length() > 0) {
                panel.gameState = panel.playState;
                panel.ui.titleScreenState = 0;
                panel.ui.commandNum = 0;
                player.setName(panel.ui.name);
                try {
                    database.updateBalance(panel.ui.name);
                } catch (SQLException e) {
                    e.printStackTrace();
                    panel.ui.addMessage("PROBLEMS WITH DATABASE");
                }
                panel.ui.name = "";
                player.configDifficulty();
                panel.playMusic(0);
            }
        }
    }

    private void shopScreen(int code) {

        switch (code) {
            case KeyEvent.VK_ESCAPE -> panel.gameState = panel.playState;
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 1;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 1) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                if (panel.ui.commandNum == 0) {
                    panel.ui.buyScreenState = 1;
                } else if (panel.ui.commandNum == 1) {
                    panel.ui.sellScreenState = 1;
                    panel.ui.commandNum = 0;
                }
            }
        }
    }

    private void fieldScreen(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> panel.gameState = panel.playState;
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 2;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 2) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (panel.ui.commandNum) {
                    case 0 -> panel.ui.fieldScreenState = 1;
                    case 1 -> {
                        panel.ui.commandNum = 0;
                        panel.ui.fieldScreenState = 2;
                    }
                    case 2 -> {
                        panel.ui.commandNum = 0;
                        panel.ui.fieldScreenState = 3;
                    }
                }
            }
        }
    }

    private void farmScreen(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.gameState = panel.playState;
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 3;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 3) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (panel.ui.commandNum) {
                    case 0 -> panel.ui.fieldFarmScreenState = 1;
                    case 1 -> {
                        panel.ui.commandNum = 0;
                        panel.ui.fieldFarmScreenState = 2;
                    }
                    case 2 -> {
                        panel.ui.commandNum = 0;
                        panel.ui.fieldFarmScreenState = 3;
                    }
                    case 3 -> {
                        panel.ui.commandNum = 0;
                        panel.ui.fieldFarmScreenState = 4;
                    }
                }
            }
        }
    }

    private void buyScreen(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.buyScreenState = 0;
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 4;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 4) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_SPACE -> {
                panel.ui.numberOf++;
                if(panel.ui.numberOf > 5) {
                    panel.ui.numberOf = 1;
                }
            }
            case KeyEvent.VK_ENTER -> {
                int numberOfItems = panel.ui.numberOf;
                try {
                    switch (panel.ui.commandNum) {
                        case 0 -> crops.buy(numberOfItems, "Wheat");
                        case 1 -> crops.buy(numberOfItems, "Corn");
                        case 2 -> crops.buy(numberOfItems, "Potatoes");
                        case 3 -> crops.buy(numberOfItems, "Carrots");
                        case 4 -> crops.buy(numberOfItems, "Hay");
                    }
                } catch (Crops.NotEnoughMoneyException e) {
                    panel.ui.addMessage(e.getMessage());
                }
            }
        }
    }

    private void sellScreen(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.sellScreenState = 0;
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 9;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 9) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_SPACE -> {
                panel.ui.numberOf++;
                if(panel.ui.numberOf > 5) {
                    panel.ui.numberOf = 1;
                }
            }
            case KeyEvent.VK_ENTER -> {
                int numberOfItems = panel.ui.numberOf;
                try {
                    switch (panel.ui.commandNum) {
                        case 0 -> animals.sell(numberOfItems, "Pork");
                        case 1 -> animals.sell(numberOfItems, "Chicken_Meat");
                        case 2 -> animals.sell(numberOfItems, "Rabbit_Meat");
                        case 3 -> animals.sell(numberOfItems, "Beef");
                        case 4 -> animals.sell(numberOfItems, "Mutton");
                        case 5 -> animals.sell(numberOfItems, "Eggs");
                        case 6 -> animals.sell(numberOfItems, "Milk");
                        case 7 -> animals.sell(numberOfItems, "Wool");
                        case 8 -> animals.sell(numberOfItems, "Hide");
                    }
                } catch (Animals.NotEnoughProduceException e) {
                    panel.ui.addMessage(e.getMessage());
                }
            }
        }
    }

    private void buyAnimalScreen(int code){
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.fieldFarmScreenState = 0;
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 5;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 5) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {

                switch (panel.ui.commandNum) {
                    case 0 -> {
                        animals.setType("Pig");
                        panel.ui.fieldFarmScreenState = 5;
                    }
                    case 1 -> {
                        panel.ui.commandNum = 0;
                        animals.setType("Chicken");
                        panel.ui.fieldFarmScreenState = 5;
                    }
                    case 2 -> {
                        panel.ui.commandNum = 0;
                        animals.setType("Rabbit");
                        panel.ui.fieldFarmScreenState = 5;
                    }
                    case 3 -> {
                        panel.ui.commandNum = 0;
                        animals.setType("Cow");
                        panel.ui.fieldFarmScreenState = 5;
                    }
                    case 4 -> {
                        panel.ui.commandNum = 0;
                        animals.setType("Sheep");
                        panel.ui.fieldFarmScreenState = 5;
                    }
                    case 5 -> {

                        try {
                            animals.buyField();
                        } catch (Animals.FieldsException | Animals.NotEnoughMoneyException e) {
                            panel.ui.addMessage(e.getMessage());
                        }

                    }
                }

            }
        }
    }

    private void plantScreen(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.fieldScreenState = 0;

            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = 5;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > 5) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (panel.ui.commandNum) {
                    case 0 -> {
                        crops.setType("Wheat");
                        panel.ui.fieldScreenState = 4;
                    }
                    case 1 -> {
                        panel.ui.commandNum = 0;
                        crops.setType("Corn");
                        panel.ui.fieldScreenState = 4;
                    }
                    case 2 -> {
                        panel.ui.commandNum = 0;
                        crops.setType("Potatoes");
                        panel.ui.fieldScreenState = 4;
                    }
                    case 3 -> {
                        panel.ui.commandNum = 0;
                        crops.setType("Carrots");
                        panel.ui.fieldScreenState = 4;
                    }
                    case 4 -> {
                        panel.ui.commandNum = 0;
                        crops.setType("Hay");
                        panel.ui.fieldScreenState = 4;
                    }
                    case 5 -> {
                        try {
                            crops.buyField();
                        } catch (Crops.FieldsException | Crops.NotEnoughMoneyException e) {
                            panel.ui.addMessage(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void menuFieldSwitch(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.fieldScreenState = 0;
                crops.setOption(0);
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = crops.getField().size() - 1;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > crops.getField().size() - 1) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (crops.getOption()) {
                    case 1 -> {
                        try {
                            switch (panel.ui.commandNum) {
                                case 0 -> crops.plant(crops.getType(), "Field 1");
                                case 1 -> crops.plant(crops.getType(), "Field 2");
                                case 2 -> crops.plant(crops.getType(), "Field 3");
                                case 3 -> crops.plant(crops.getType(), "Field 4");
                                case 4 -> crops.plant(crops.getType(), "Field 5");
                                case 5 -> crops.plant(crops.getType(), "Field 6");
                            }
                        } catch (Crops.CropsException e){
                            panel.ui.addMessage(e.getMessage());
                        }
                    }
                    case 2 -> {
                        try{
                            switch (panel.ui.commandNum) {
                                case 0 -> crops.collect("Field 1");
                                case 1 -> crops.collect("Field 2");
                                case 2 -> crops.collect("Field 3");
                                case 3 -> crops.collect("Field 4");
                                case 4 -> crops.collect("Field 5");
                                case 5 -> crops.collect("Field 6");
                            }
                        } catch (Crops.CropsException | Crops.FieldsException e) {
                            panel.ui.addMessage(e.getMessage());
                        }
                    }
                    case 3 -> {
                        try {
                            switch (panel.ui.commandNum) {
                                case 0 -> crops.water("Field 1");
                                case 1 -> crops.water("Field 2");
                                case 2 -> crops.water("Field 3");
                                case 3 -> crops.water("Field 4");
                                case 4 -> crops.water("Field 5");
                                case 5 -> crops.water("Field 6");
                            }
                        } catch (Crops.CropsException | Crops.FieldsException e) {
                            panel.ui.addMessage(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void menuFarmSwitch(int code) {
        switch (code) {
            case KeyEvent.VK_ESCAPE -> {
                panel.ui.commandNum = 0;
                panel.ui.fieldFarmScreenState = 0;
                animals.setOption(0);
            }
            case KeyEvent.VK_W -> {
                panel.ui.commandNum--;
                if (panel.ui.commandNum < 0) {
                    panel.ui.commandNum = animals.getField().size() - 1;
                }
            }
            case KeyEvent.VK_S -> {
                panel.ui.commandNum++;
                if (panel.ui.commandNum > animals.getField().size() - 1) {
                    panel.ui.commandNum = 0;
                }
            }
            case KeyEvent.VK_ENTER -> {
                switch (animals.getOption()) {
                   case 1 -> {
                       try {
                           switch (panel.ui.commandNum) {
                               case 0 -> animals.buy(animals.getType(), "Field 1");
                               case 1 -> animals.buy(animals.getType(), "Field 2");
                               case 2 -> animals.buy(animals.getType(), "Field 3");
                               case 3 -> animals.buy(animals.getType(), "Field 4");
                               case 4 -> animals.buy(animals.getType(), "Field 5");
                               case 5 -> animals.buy(animals.getType(), "Field 6");
                           }
                       }catch (Animals.FieldsException | Animals.NotEnoughMoneyException e) {
                           panel.ui.addMessage(e.getMessage());
                       }
                   }
                   case 2 -> {
                       try{
                           switch (panel.ui.commandNum) {
                               case 0 -> animals.kill("Field 1");
                               case 1 -> animals.kill("Field 2");
                               case 2 -> animals.kill("Field 3");
                               case 3 -> animals.kill("Field 4");
                               case 4 -> animals.kill("Field 5");
                               case 5 -> animals.kill("Field 6");
                           }
                       }catch (Animals.FieldsException e) {
                           panel.ui.addMessage(e.getMessage());
                       }
                   }
                   case 3 -> {
                       try {

                           switch (panel.ui.commandNum) {
                               case 0 -> animals.collectProduce("Field 1");
                               case 1 -> animals.collectProduce("Field 2");
                               case 2 -> animals.collectProduce("Field 3");
                               case 3 -> animals.collectProduce("Field 4");
                               case 4 -> animals.collectProduce("Field 5");
                               case 5 -> animals.collectProduce("Field 6");
                           }
                       }catch (Animals.FieldsException e) {
                           panel.ui.addMessage(e.getMessage());
                       }
                   }
                   case 4 -> {
                       try {
                           switch (panel.ui.commandNum) {
                               case 0 -> game.feed("Field 1");
                               case 1 -> game.feed("Field 2");
                               case 2 -> game.feed("Field 3");
                               case 3 -> game.feed("Field 4");
                               case 4 -> game.feed("Field 5");
                               case 5 -> game.feed("Field 6");
                           }
                       }catch (Game.GameSystemException e) {
                           panel.ui.addMessage(e.getMessage());
                       }
                   }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
        }
    }
}
