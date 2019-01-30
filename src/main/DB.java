/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

    public Connection conn = null;

    public DB() {
        try {
            String url = "jdbc:sqlite:E:/case_study_teko/db/crawler.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("error connecting to sqlite");
        }
    }

    public ResultSet executeSelect(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public void insertChallenge(int id, String name, double difficulty, int max_score, String content) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO challenge(name,difficulty,max_score,content,id) values(?,?,?,?,?)");
            ps.setString(1, name);
            ps.setDouble(2, difficulty);
            ps.setInt(3, max_score);
            ps.setString(4, content);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error inserting challenge: " + e.getMessage());
        }
    }

    public void updateChallenge(int id, String name, double difficulty, int max_score, String content) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE challenge SET name=?, difficulty=?, max_score=?, content=? where id=?");
            ps.setString(1, name);
            ps.setDouble(2, difficulty);
            ps.setInt(3, max_score);
            ps.setString(4, content);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error updating challenge: " + e.getMessage());
        }
    }

    public void insertHacker(int id, String name, String country) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO hacker(id, name,country) values(?,?,?)");
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, country);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error inserting hacker: " + e.getMessage());
        }
    }

    public void updateHacker(int id, String name, String country) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE hacker set name=?, country=? where id=?");
            ps.setString(1, name);
            ps.setString(2, country);
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error editing hacker: " + e.getMessage());
        }

    }

    public void insertLeaderBoard(int challenge_id, int hacker_id, double score) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO leaderboard(challenge_id,hacker_id,score) values(?,?,?)");
            ps.setInt(1, challenge_id);
            ps.setInt(2, hacker_id);
            ps.setDouble(3, score);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error saving leaderboard: " + e.getMessage());
        }
    }

    public void updateLeaderBoard(int challenge_id, int hacker_id, double score) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE leaderboard set score=? where challenge_id=? and hacker_id=?");
            ps.setDouble(1, score);
            ps.setInt(2, challenge_id);
            ps.setInt(3, hacker_id);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error updating leaderboard: " + e.getMessage());
        }
    }

    public Double getScore(int challenge_id, int hacker_id) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT score from leaderboard where challenge_id=? and hacker_id=?");
            ps.setInt(1, challenge_id);
            ps.setInt(2, hacker_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("score");
            }
        } catch (Exception e) {
            System.out.println("error getting score: " + e.getMessage());
        }
        return null;
    }

}
