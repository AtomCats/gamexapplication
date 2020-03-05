package com.gamex.application;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import com.gamex.application.controller.SynchronizationController;
import com.gamex.application.model.UserGameData;
import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import spark.servlet.SparkApplication;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ControllerTest {
    private static final String TEST_UUID = "237c6f38-3556-4320-9364-4c7ebcb19226";

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    SessionFactory sessionFactory;

    @Before
    public void beforeTest(){
        MockitoAnnotations.initMocks(this);
    }

    public static class WebAppTestSparkApp implements SparkApplication {
        public void init() {
            new SynchronizationController().initRoutes();
        }
    }

    @ClassRule
    public static SparkServer<WebAppTestSparkApp> testServer = new SparkServer<>(ControllerTest.WebAppTestSparkApp.class, 4567);

    @Test
    public void getGameDataSuccessTest() throws HttpClientException {
        final UserGameData gameData = new UserGameData(TEST_UUID, 100, "RU");
        gameData.setId(0);
        Session session = Mockito.mock(Session.class);
        Query query = Mockito.mock(Query.class);
        doReturn(session).when(sessionFactory).openSession();
        doReturn(query).when(session).createQuery(anyString(), eq(UserGameData.class));
        doReturn(gameData).when(query).getSingleResult();
        GetMethod request = testServer.get("/synchronization/" + TEST_UUID + "/user-game-data", false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(gameData, new Gson().fromJson(new String(httpResponse.body()), UserGameData.class));
    }

    @Test
    public void getGameDataErrorTest() throws HttpClientException {
        GetMethod request = testServer.get("/synchronization/" + TEST_UUID + "/user-game-data", false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(500, httpResponse.code());
    }

    @Test
    public void updateGameDataTest() throws HttpClientException {
        final UserGameData gameData = new UserGameData(TEST_UUID, 100, "RU");
        Session session = Mockito.mock(Session.class);
        Query query = Mockito.mock(Query.class);
        doReturn(session).when(sessionFactory).openSession();
        doReturn(query).when(session).createQuery(anyString(), eq(UserGameData.class));
        doReturn(gameData).when(query).getSingleResult();
//        doReturn(void).when(sessionFactory).getCurrentSession().update(any(UserGameData.class));
        PostMethod request = testServer.post("/synchronization/" + TEST_UUID + "/user-game-data", new Gson().toJson(gameData), false);
        HttpResponse httpResponse = testServer.execute(request);
        assertEquals(200, httpResponse.code());
    }
}
