/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.JSONObject;
import dao.DAO;
import model.*;

public class Main {

    public static DAO dao = new DAO();
    public static ArrayList<Integer> challenges;
    public static ArrayList<Integer> hackers;

    public static void main(String[] args) throws Exception {
        challenges = dao.getListChallengeId();
        hackers = dao.getListHackerId();
        crawlListChallenge();
    }

    public static void crawlListChallenge() throws Exception {
        Challenge challenge = new Challenge();
        int index = 0;
        while (index < 100) {
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

                        challenge.setId(challengeObj.getInt("id"));
                        challenge.setName(challengeObj.getString("name"));
                        challenge.setDifficulty(challengeObj.getDouble("difficulty"));
                        challenge.setMax_score(challengeObj.getInt("max_score"));
                        challenge.setContent(content.html());

                        if (challenges.contains(challenge.getId())) {
                            dao.updateChallenge(challenge);
                        } else {
                            dao.insertChallenge(challenge);
                        }
                        crawlHacker(url, challenge.getId());
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

    public static void crawlHacker(String url, int challenge_id) throws Exception {
        Hacker hacker = new Hacker();
        LeaderBoard leaderBoard = new LeaderBoard();
        int index = 0;
        while (index < 100) {
            String leaderBoardJson = Jsoup.connect(url + "/leaderboard?offset=" + index).ignoreContentType(true).execute().body();
            try {
                JSONObject obj = new JSONObject(leaderBoardJson);
                JSONObject hackerObj;
                if (obj.getJSONArray("models").length() > 0) {
                    for (int j = 0; j < obj.getJSONArray("models").length(); j++) {
                        hackerObj = obj.getJSONArray("models").getJSONObject(j);
                        hacker.setId(hackerObj.getInt("hacker_id"));
                        hacker.setName(hackerObj.getString("hacker"));
                        hacker.setCountry(hackerObj.get("country").toString());
                        hacker.setSchool(hackerObj.get("school").toString());
                        hacker.setCompany(hackerObj.get("company").toString());
                        hacker.setLanguage(hackerObj.getString("language"));

                        double score = hackerObj.getDouble("score");
                        leaderBoard.setChallenge_id(challenge_id);
                        leaderBoard.setHacker_id(hacker.getId());
                        leaderBoard.setScore(score);
                        leaderBoard.setHacker_index(hackerObj.getInt("index"));

                        if (hackers.contains(hacker.getId())) {
                            if (dao.getScore(challenge_id, hacker.getId()) == null) {
                                dao.insertLeaderBoard(leaderBoard);
                            } else {
                                dao.updateLeaderBoard(leaderBoard);
                            }
                        } else {
                            hackers.add(hacker.getId());
                            dao.insertHacker(hacker);
                            dao.insertLeaderBoard(leaderBoard);
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
