package com.gamex.application.service;

import com.gamex.application.model.GameStatistics;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class GameStatisticsService {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public synchronized void updateGameSatatistics(String userId, GameStatistics gameStatistics) {
        gameStatistics.setUserId(userId);
        sessionFactory.getCurrentSession().save(gameStatistics);
    }
}
