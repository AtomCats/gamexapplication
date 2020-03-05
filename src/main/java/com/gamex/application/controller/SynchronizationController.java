package com.gamex.application.controller;

import com.gamex.application.model.GameStatistics;
import com.gamex.application.model.StatusResponse;
import com.gamex.application.model.UserGameData;
import com.gamex.application.service.GameDataService;
import com.gamex.application.service.GameStatisticsService;
import com.google.gson.Gson;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
public class SynchronizationController {

    @Autowired
    GameStatisticsService gameStatisticsService;

    @Autowired
    GameDataService gameDataService;

    public void initRoutes() {
        path("/synchronization/:userId", () -> {
            get("/user-game-data", (request, response) -> {
                UserGameData gameData = gameDataService.getGameData(request.params("userId"));
                return new Gson().toJson(gameData);
            });
            post("/user-game-data", (request, response) -> {
                try{
                    final UserGameData userGameData = new Gson().fromJson(request.body(), UserGameData.class);
                    gameDataService.updateGameData(request.params("userId"), userGameData);
                    response.status(200);
                    response.body(StatusResponse.SUCCESS.toJsonView());
                } catch (HibernateException e) {
                    response.status(500);
                    response.body(StatusResponse.ERROR.toJsonView());
                }
                return response.body();
            });
            post("/game-statistics", (request, response) -> {
                try{
                    final GameStatistics gameStatistics = new Gson().fromJson(request.body(), GameStatistics.class);
                    gameStatisticsService.updateGameSatatistics(request.params("userId"), gameStatistics);
                    response.status(200);
                    response.body(StatusResponse.SUCCESS.toJsonView());
                } catch (HibernateException e) {
                    response.status(500);
                    response.body(StatusResponse.ERROR.toJsonView());
                }
                return response.body();
            });
        });
    }
}
