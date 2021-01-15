/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.application.authentication.framework.dao.impl;

import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.identity.application.authentication.framework.dao.UserSessionDAO;
import org.wso2.carbon.identity.application.authentication.framework.exception.session.mgt
        .SessionManagementServerException;
import org.wso2.carbon.identity.application.authentication.framework.model.Application;
import org.wso2.carbon.identity.application.authentication.framework.model.UserSession;
import org.wso2.carbon.identity.application.authentication.framework.store.SQLQueries;
import org.wso2.carbon.identity.application.authentication.framework.util.JdbcUtils;
import org.wso2.carbon.identity.application.authentication.framework.util.SessionMgtConstants;
import org.wso2.carbon.identity.core.util.IdentityDatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants.AnalyticsAttributes.SESSION_ID;
import static org.wso2.carbon.identity.application.mgt.ApplicationConstants.IDP_NAME;

/**
 * Default implementation of {@link UserSessionDAO}. This handles {@link UserSession} related DB operations.
 */
public class UserSessionDAOImpl implements UserSessionDAO {

    public UserSessionDAOImpl() {
    }

    public UserSession getSession(String sessionId) throws SessionManagementServerException {

        List<Application> applicationList;
        HashMap<String, String> propertiesMap = new HashMap<>();
        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();

        try {
            applicationList = jdbcTemplate.executeQuery(SQLQueries.SQL_GET_APPLICATION, (resultSet, rowNumber) ->
                            new Application(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4)),
                    preparedStatement -> preparedStatement.setString(1, sessionId));

            jdbcTemplate.executeQuery(SQLQueries.SQL_GET_PROPERTIES_FROM_SESSION_META_DATA, ((resultSet, rowNumber)
                    -> propertiesMap.put(resultSet.getString(1), resultSet.getString(2))), preparedStatement ->
                    preparedStatement.setString(1, sessionId));

            UserSession userSession = new UserSession();
            userSession.setSessionId(sessionId);

            propertiesMap.forEach((key, value) -> {
                switch (key) {
                    case SessionMgtConstants.USER_AGENT:
                        userSession.setUserAgent(value);
                        break;
                    case SessionMgtConstants.IP_ADDRESS:
                        userSession.setIp(value);
                        break;
                    case SessionMgtConstants.LAST_ACCESS_TIME:
                        userSession.setLastAccessTime(value);
                        break;
                    case SessionMgtConstants.LOGIN_TIME:
                        userSession.setLoginTime(value);
                        break;
                }
            });

            if (!applicationList.isEmpty()) {
                userSession.setApplications(applicationList);
                return userSession;
            }
        } catch (DataAccessException e) {
            throw new SessionManagementServerException(
                    SessionMgtConstants.ErrorMessages.ERROR_CODE_UNABLE_TO_GET_SESSION,
                    SessionMgtConstants.ErrorMessages.ERROR_CODE_UNABLE_TO_GET_SESSION.getDescription(), e);
        }
        return null;
    }

    /**
     *Get federated authentication session mapping info during the federated idp init logout
     *
     * @param fedIdpSessionId
     * @return A map containing federated authentication session details
     * @throws SessionManagementServerException
     */
    public Map<String, String> getSessionDetails(String fedIdpSessionId) throws SessionManagementServerException {

        try (Connection connection = IdentityDatabaseUtil.getDBConnection(false);
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQLQueries.SQL_GET_FEDERATED_AUTH_SESSION_INFO_BY_SESSION_ID)) {
            preparedStatement.setString(1, fedIdpSessionId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Map<String, String> sessionDetails = new HashMap<>();
                if (resultSet.next()) {
                    sessionDetails.put(SESSION_ID, resultSet.getString("SESSION_ID"));
                    sessionDetails.put(IDP_NAME, resultSet.getString("IDP_NAME"));
                }
                return sessionDetails;
            }
        } catch (SQLException e) {
            throw new SessionManagementServerException(
                    SessionMgtConstants.ErrorMessages.ERROR_CODE_UNABLE_TO_GET_SESSION,
                    SessionMgtConstants.ErrorMessages.ERROR_CODE_UNABLE_TO_GET_SESSION.getDescription(), e);
        }
    }
}
