/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author TienDuc
 */
public class LeaderBoard {
    private int id;
    private int hacker_id;
    private int challenge_id;
    private double score;
    private int hacker_index;

    public LeaderBoard() {
    }

    public LeaderBoard(int hacker_id, int challenge_id, double score,  int hacker_index) {
        this.hacker_id = hacker_id;
        this.challenge_id = challenge_id;
        this.score = score;
        this.hacker_index = hacker_index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHacker_id() {
        return hacker_id;
    }

    public void setHacker_id(int hacker_id) {
        this.hacker_id = hacker_id;
    }

    public int getChallenge_id() {
        return challenge_id;
    }

    public void setChallenge_id(int challenge_id) {
        this.challenge_id = challenge_id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getHacker_index() {
        return hacker_index;
    }

    public void setHacker_index(int hacker_index) {
        this.hacker_index = hacker_index;
    }
    
}
