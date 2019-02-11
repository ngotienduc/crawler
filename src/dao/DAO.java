/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

public class DAO {

    public Connection conn = null;

    public DAO() {
        try {
            String url = "jdbc:sqlite:E:/case_study_teko/db/crawler2.db";
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("error connecting to sqlite");
        }
    }

    public ResultSet executeSelect(String sql) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        return ps.executeQuery();
    }

    public ArrayList<Integer> getListChallengeId(){
        ArrayList<Integer> challenges = new ArrayList<>();
        try {
            String sql = "select id from challenge";
            ResultSet rs = executeSelect(sql);
            while (rs.next()) {
                challenges.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            System.out.println("error getting list challenge id");
        }
        return challenges;
    }
    
    public ArrayList<Integer> getListHackerId(){
        ArrayList<Integer> hackers = new ArrayList<>();
        try {
            String sql = "select id from hacker";
            ResultSet rs = executeSelect(sql);
            while (rs.next()) {
                hackers.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            System.out.println("error getting list hacker id");
        }
        return hackers;
    }
    
    public void insertChallenge(Challenge challenge) {
        try { 
            PreparedStatement ps = conn.prepareStatement("INSERT INTO challenge(name,difficulty,max_score,content,id) values(?,?,?,?,?)");
            ps.setString(1, challenge.getName());
            ps.setDouble(2, challenge.getDifficulty());
            ps.setInt(3, challenge.getMax_score());
            ps.setString(4, challenge.getContent());
            ps.setInt(5, challenge.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error inserting challenge: " + e.getMessage());
        }
    }

    public void updateChallenge(Challenge challenge) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE challenge SET name=?, difficulty=?, max_score=?, content=? where id=?");
            ps.setString(1, challenge.getName());
            ps.setDouble(2, challenge.getDifficulty());
            ps.setInt(3, challenge.getMax_score());
            ps.setString(4, challenge.getContent());
            ps.setInt(5, challenge.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error updating challenge: " + e.getMessage());
        }
    }

    public void insertHacker(Hacker hacker) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO hacker(id, name,country, school, company, language) values(?,?,?,?,?,?)");
            ps.setInt(1, hacker.getId());
            ps.setString(2, hacker.getName());
            ps.setString(3, hacker.getCountry());
            ps.setString(4, hacker.getSchool());
            ps.setString(5, hacker.getCompany());
            ps.setString(6, hacker.getLanguage());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error inserting hacker: " + e.getMessage());
        }
    }

    public void updateHacker(Hacker hacker) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE hacker set name=?, country=?, school=?, company=?, language=? where id=?");
            ps.setInt(7, hacker.getId());
            ps.setString(1, hacker.getName());
            ps.setString(2, hacker.getCountry());
            ps.setString(3, hacker.getSchool());
            ps.setString(4, hacker.getCompany());
            ps.setString(5, hacker.getLanguage());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error editing hacker: " + e.getMessage());
        }

    }

    public void insertLeaderBoard(LeaderBoard leaderBoard) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO leaderboard(challenge_id,hacker_id,score,hacker_index) values(?,?,?,?)");
            ps.setInt(1, leaderBoard.getChallenge_id());
            ps.setInt(2, leaderBoard.getHacker_id());
            ps.setDouble(3, leaderBoard.getScore());
            ps.setInt(4, leaderBoard.getHacker_index());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("error saving leaderboard: " + e.getMessage());
        }
    }

    public void updateLeaderBoard(LeaderBoard leaderBoard) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE leaderboard set score=?, hacker_index=? where challenge_id=? and hacker_id=?");
            ps.setDouble(1, leaderBoard.getScore());
            ps.setInt(2, leaderBoard.getHacker_index());
            ps.setInt(3, leaderBoard.getChallenge_id());
            ps.setInt(4, leaderBoard.getHacker_id());
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
