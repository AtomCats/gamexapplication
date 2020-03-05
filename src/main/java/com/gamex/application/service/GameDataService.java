package com.gamex.application.service;

import com.gamex.application.model.UserGameData;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;

@Repository
@NoArgsConstructor
public class GameDataService {
    @Autowired
    private SessionFactory sessionFactory;

    public synchronized UserGameData getGameData(String userId) throws NoResultException {
        Session session = sessionFactory.openSession();
        String sql = "from UserGameData where userId = :userId";
        Query<UserGameData> query = session.createQuery(sql, UserGameData.class);
        query.setParameter("userId", userId);
        final UserGameData result = query.getSingleResult();
        System.out.println(result.getUserId());
        session.close();
        return result;
    }

    @Transactional
    public synchronized void updateGameData(String userId, UserGameData gameData) {
        try{
            final UserGameData userGameData = getGameData(userId);
            userGameData.setMoney(gameData.getMoney());
            sessionFactory.getCurrentSession().update(userGameData);
        } catch (NoResultException ex) {
            sessionFactory.getCurrentSession().save(gameData);
        }
    }
}
