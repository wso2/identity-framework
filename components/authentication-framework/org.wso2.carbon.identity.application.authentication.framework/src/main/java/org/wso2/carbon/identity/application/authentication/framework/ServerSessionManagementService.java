/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.carbon.identity.application.authentication.framework;

import org.wso2.carbon.identity.application.authentication.framework.context.SessionContext;

/**
 * Defines the session management service operations.
 */
public interface ServerSessionManagementService {

    /**
     * Terminate session related to the sessionID
     *
     * @param sessionId
     * @return true if the termination is success
     */
    boolean removeSession(String sessionId);

    /**
     * Terminate session of the current user
     *
     * @param sessionId
     * @return true if the termination is success
     */
    boolean removeMySession(String username, String tenantDomain, String sessionId);

    /**
     * @param sessionContext
     * @param sessionId
     */
    void terminateSession(SessionContext sessionContext, String sessionId);

}
