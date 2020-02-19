package com.tema1.main;
import com.tema1.players.Players;
import  com.tema1.players.FinalScore;
import  com.tema1.players.BasicPlayer;
import com.tema1.players.BribedPlayer;
import com.tema1.players.GreedyPlayer;
import com.tema1.players.PlayerStrategy;
import  com.tema1.players.PlayerComparator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import com.tema1.common.Constants;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        int numberOfPlayers = gameInput.getPlayerNames().size();
        int numberOfRounds = gameInput.getRounds();
        ArrayList<Players> players = new ArrayList<Players>(numberOfPlayers);
        //parcurg lista de nume si adaug jucatorii in fucntie de strategie
        for (int i = 0; i < numberOfPlayers; i++) {
            if (gameInput.getPlayerNames().get(i).equals("basic")) {
                players.add(new BasicPlayer(i, PlayerStrategy.basic, Constants.startCoins));
            }
            if (gameInput.getPlayerNames().get(i).equals("greedy")) {
                players.add(new GreedyPlayer(i, PlayerStrategy.greedy, Constants.startCoins));
            }
            if (gameInput.getPlayerNames().get(i).equals("bribed")) {
                players.add(new BribedPlayer(i, PlayerStrategy.bribed, Constants.startCoins));
            }
        }
        int numberofCards = 0;
        //daca jucatorul e serif, doar ceilalti jucatori vor primi carti
       for (int r = 1; r <= numberOfRounds; r++) {
            //pentru fiecare subrunda
            for (int l = 0; l < numberOfPlayers; l++) {
                //impart cartile tuturor in afara de serif
                for (int j = 0; j < numberOfPlayers; j++) {
                    if (j != l) {
                        int i = 0;
                        for (int k = numberofCards; k < numberofCards + Constants.ten; k++) {
                            players.get(j).getCards().add(i, gameInput.getAssetIds().get(k));
                            i++;
                        }
                        //fiecare jucator isi creeaza sacul
                        if (players.get(j).getStrategy().equals(PlayerStrategy.basic)) {
                            ((BasicPlayer) players.get(j)).createTheBag(players.get(j).getCards());
                        }
                        if (players.get(j).getStrategy().equals(PlayerStrategy.greedy)) {
                            ((GreedyPlayer) players.get(j)).createBag(players.get(j).getCards(), r);
                        }
                        if (players.get(j).getStrategy().equals(PlayerStrategy.bribed)) {
                            ((BribedPlayer) players.get(j)).
                                    createBribeBag(players.get(j).getCards());
                        }

                        //acesta va fi controlat de catre serif
                        if (players.get(l).getStrategy().equals(PlayerStrategy.basic)) {
                            ((BasicPlayer) players.get(l)).sheriffTime(players.get(j), r);
                        }
                        if (players.get(l).getStrategy().equals(PlayerStrategy.greedy)) {
                            ((GreedyPlayer) players.get(l)).sherrif(players.get(j), r);
                        }
                        if (players.get(l).getStrategy().equals(PlayerStrategy.bribed)) {
                            ((BribedPlayer) players.get(l)).
                                    bribeSheriff(players.get(j), r, numberOfPlayers);
                        }
                        players.get(j).getCards().clear();
                        numberofCards += Constants.ten;
                    }
                }
            }
        }
        FinalScore score = new FinalScore();
        PlayerComparator playerComparator = new PlayerComparator();
        List<Players> scoreBoard = new LinkedList<>();
        score.addIllegalBonus(players);
        score.addKingQueen(players);
        for (int i = 0; i < numberOfPlayers; i++) {
            players.get(i).addCoins(score.addProfit(players.get(i).getGoodsOnStand()));
        }
        for (Players p : players) {
            scoreBoard.add(p);
        }
        Collections.sort(scoreBoard, playerComparator);
        for (Players player : scoreBoard) {
            System.out.println(player.getId()
                    + " " + player.getStrategy().toString().toUpperCase()
                    + " " + player.getCoins());
        }
    }
}
