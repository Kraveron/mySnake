package com.example.asusmr.snake.engine;

import com.example.asusmr.snake.classes.Coordinate;
import com.example.asusmr.snake.enums.Direction;
import com.example.asusmr.snake.enums.Status;
import com.example.asusmr.snake.enums.Tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by AsusMR on 02/01/2018.
 */

public class GameEngine {
    public static final int borderWidth = 16;
    public static final int borderHeight = 26;

    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snake = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();

    private int score = 0;
    private boolean increaseTail = false;
    private Random random = new Random(); // used for apple placement
    private Direction currentDirection = Direction.East;  // default direction for start

    private Status currentGameStatus = Status.Ready;


    public void loadGame(){
        addWalls();
        addSnake();
        addApple();
        addApple();
    }
    public void runGame(){
        currentGameStatus = Status.Run;
    }

    public void restartGame(){
        currentGameStatus = Status.Ready;
        clear();
        currentDirection = Direction.East;
        loadGame();
    }

    private void clear() {
        score = 0;
        walls.clear();
        apples.clear();
        snake.clear();
    }


    // Changing Direction
    public void updateDirection(Direction newDirection){
        if(Math.abs(newDirection.ordinal() - currentDirection.ordinal()) % 2 == 1){
            currentDirection = newDirection;
        }
    }

    // Basic functionality of the game
    public void updateGame(){
        switch (currentDirection){

            case North:
                updateSnake(0,-1);
                break;
            case East:
                updateSnake(1,0);
                break;
            case South:
                updateSnake(0,1);
                break;
            case West:
                updateSnake(-1,0);
                break;
        }

        // Wall collision
        for (Coordinate w : walls){
            if (snake.get(0).equals(w)){
                currentGameStatus = Status.GameOver;
            }
        }

        // Snake collision
        for (int i = 1; i < snake.size(); i++){
            if (getSnakeHead().equals(snake.get(i))){
                currentGameStatus = Status.GameOver;
                return;
            }
        }

        // Apple Eaten
        Coordinate removeApple = null;
        for(Coordinate a : apples){
            if (getSnakeHead().equals(a)){
                removeApple = a;
            }
        }
        if(removeApple != null){
            apples.remove(removeApple);
            addApple();
            increaseTail = true;
            score++;
        }
    }

    // Snake in-game functionality
    private void updateSnake(int x, int y){

        int newX = snake.get(snake.size() -1).getX();
        int newY = snake.get(snake.size() -1).getY();

        for (int i = snake.size()-1; i > 0 ; i--) {

            snake.get(i).setX(snake.get(i-1).getX());
            snake.get(i).setY(snake.get(i-1).getY());
        }

        if(increaseTail){
            snake.add(new Coordinate(newX,newY)); // adding a snake part
            increaseTail = false;
        }

        snake.get(0).setX(snake.get(0).getX()+ x);
        snake.get(0).setY(snake.get(0).getY()+ y);
    }


    // Map that defines all elements for the view
    public Tiles[][] getMap(){
        Tiles[][] map = new Tiles[borderWidth][borderHeight];

        for (int x = 0; x < borderWidth; x++){
            for (int y = 0; y < borderHeight; y++){
                map[x][y] = Tiles.Empty;
            }
        }

        for (Coordinate s : snake){
            map[s.getX()][s.getY()] = Tiles.Tail;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] = Tiles.Snakehead;

        for (Coordinate w: walls){
            map[w.getX()][w.getY()] = Tiles.Wall;
        }

        for (Coordinate a : apples){
            map[a.getX()][a.getY()] = Tiles.Apple;
        }

        return map;
    }

    // Creating the Snake
    private void addSnake() {
        snake.clear();

        snake.add(new Coordinate(7,7));
        snake.add(new Coordinate(6,7));
        snake.add(new Coordinate(5,7));
        snake.add(new Coordinate(4,7));
    }

    //Creating the Walls
    private void addWalls() {

        for (int x = 0; x < borderWidth; x++){
            walls.add(new Coordinate(x,0));
            walls.add(new Coordinate(x,borderHeight-1));
        }

        for (int y = 1; y < borderHeight; y++){
            walls.add(new Coordinate(0,y));
            walls.add(new Coordinate(borderWidth-1,y));
        }

    }

    // Creating an Apple
    private void addApple() {
        Coordinate coordinate = null;

        boolean added = false;

        while (!added){
            int x = 1 + random.nextInt(borderWidth -2);
            int y = 1 + random.nextInt(borderHeight -2);  // will not add apple on walls

            coordinate = new Coordinate(x,y);
            boolean collision = false;
            for (Coordinate s : snake){
                if(s.equals(coordinate)){ // will not add an apple on snake
                    collision = true;
                }
            }
            for (Coordinate a : apples){
                if(a.equals(coordinate)){ // will not add apple on apple
                    collision = true;
                }
            }
            added = !collision;
        }
        apples.add(coordinate);
    }

    public Status getCurrentStatus() {
        return currentGameStatus;
    }

    public int getScore(){
        return score;
    }

    private Coordinate getSnakeHead(){
        return snake.get(0);
    }
}

