/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;

public class Main {

    public static DB db = new DB();
    public static ArrayList<Integer> challenges = new ArrayList<>();
    public static ArrayList<Integer> hackers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        crawlListChallenge();
    }

    public static void crawlListChallenge() throws Exception {
        String sql = "select * from challenge";
        ResultSet rs = db.executeSelect(sql);
        while (rs.next()) {
            challenges.add(rs.getInt("id"));
        }

        String sql2 = "select * from hacker";
        ResultSet rs2 = db.executeSelect(sql2);
        while (rs2.next()) {
            hackers.add(rs2.getInt("id"));
        }

        int index = 0;
        while (true) {
            String algorithmJson = Jsoup.connect("https://www.hackerrank.com/rest/contests/master/tracks/algorithms/challenges?offset=" + index).ignoreContentType(true).execute().body();
            try {
                JSONObject obj = new JSONObject(algorithmJson);
                JSONObject challengeObj;
                if (obj.getJSONArray("models").length() > 0) {
                    for (int j = 0; j < obj.getJSONArray("models").length(); j++) {
                        challengeObj = obj.getJSONArray("models").getJSONObject(j);
                        String problemLink = "https://www.hackerrank.com/challenges/" + challengeObj.getString("slug");
                        String url = "https://www.hackerrank.com/rest/contests/master/challenges/" + challengeObj.getString("slug");
                        Document doc = Jsoup.connect(problemLink).get();
                        Element content = doc.select("div.challenge-body-html").first();
                        content.select("style").remove();
                        if (challenges.contains(challengeObj.getInt("id"))) {
                            db.updateChallenge(challengeObj.getInt("id"), challengeObj.getString("name"), challengeObj.getDouble("difficulty"), challengeObj.getInt("max_score"), content.html());                          
                        } else {
                            db.insertChallenge(challengeObj.getInt("id"), challengeObj.getString("name"), challengeObj.getDouble("difficulty"), challengeObj.getInt("max_score"), content.html());
                        }
                        crawlHacker(url, challengeObj.getInt("id"));
                    }
                    index += obj.getJSONArray("models").length();
                } else {
                    break;
                }

            } catch (Exception ex) {
                System.out.println("Error crawling challenge");
                System.out.println(ex.getMessage());
            }
        }

    }

    public static void crawlHacker(String url, int challenge_id) throws Exception{
        int index = 0;
        while (true) {
            String leaderBoardJson = Jsoup.connect(url+"/leaderboard?offset=" + index).ignoreContentType(true).execute().body();
            try {
                JSONObject obj = new JSONObject(leaderBoardJson);
                JSONObject hackerObj;
                if (obj.getJSONArray("models").length() > 0) {
                    for (int j = 0; j < obj.getJSONArray("models").length(); j++) {
                        hackerObj = obj.getJSONArray("models").getJSONObject(j);
                        int hacker_id = hackerObj.getInt("hacker_id");
                        String hacker_name = hackerObj.getString("hacker");
                        String hacker_country = hackerObj.get("country").toString();
                        double score = hackerObj.getDouble("score");
                        if(hackers.contains(hacker_id)){
                            if(db.getScore(challenge_id, hacker_id)==null)
                                db.insertLeaderBoard(challenge_id, hacker_id, score);
                            else
                                db.updateLeaderBoard(challenge_id, hacker_id, score);
                        }
                        else {
                            hackers.add(hacker_id);
                            db.insertHacker(hacker_id, hacker_name, hacker_country);
                            db.insertLeaderBoard(challenge_id, hacker_id, score);
                        }
                    }
                    index += obj.getJSONArray("models").length();
                } else {
                    break;
                }

            } catch (Exception ex) {
                System.out.println("Error crawling hacker");
                System.out.println(ex.getMessage());
            }
        }
    }
    
}
